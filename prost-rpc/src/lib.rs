//! A library for generating service code to be used with `prost-build`.
//!

use std::fmt;
use heck::AsUpperCamelCase;
use std::fmt::Write;

/// The service generator to be used with `prost-build` to generate RPC implementations
///
/// See the crate-level documentation for more info.
#[allow(missing_copy_implementations)]
#[derive(Clone, Debug)]
pub struct ServiceGenerator {}

pub trait Service
{
    fn call(&self, method: String, request: String) -> Result<String, String>;
}

impl ServiceGenerator {
    /// Create a new `ServiceGenerator` instance with the default options set.
    pub fn new() -> ServiceGenerator {
        ServiceGenerator {}
    }
}

impl prost_build::ServiceGenerator for ServiceGenerator {
    fn generate(&mut self, service: prost_build::Service, mut buf: &mut String) {
        let server_name = format!("{}Server", service.name);

        let mut trait_methods = String::new();
        let mut match_handle_methods = String::new();

        for method in service.methods {
            ServiceGenerator::write_comments(&mut trait_methods, 4, &method.comments).unwrap();
            writeln!(
                trait_methods,
                r#"    fn {name}(&self, request: {input_type}) -> Result<{output_type}, String>;"#,
                name = method.name,
                input_type = method.input_type,
                output_type = method.output_type
            ).unwrap();

            let case = format!(
                "            \"{method_proto_name}\" => ",
                method_proto_name = method.proto_name
            );
            write!(
                match_handle_methods,
                r#"{} {{
                    let request = serde_json::from_str(&request).unwrap();
                    self.service.{method_name}(request)
                }}
"#,
                case,
                method_name = method.name
            ).unwrap();
        }

        ServiceGenerator::write_comments(&mut buf, 0, &service.comments).unwrap();
        write!(
            buf,
            r#"
pub trait {name} {{
{trait_methods}}}
/// A server for a `{name}`.
///
/// This implements the `Server` trait by handling requests and dispatch them to methods on the
/// supplied `{name}`.
#[derive(Debug)]
pub struct {server_name}<A> where A: {name} + Send + Sync + 'static
{{
    service: A
}}
impl<A> {server_name}<A> where A: {name} + Send + Sync + 'static
{{
    /// Creates a new server instance that dispatches all calls to the supplied service.
    pub fn new(service: A) -> {server_name}<A> {{
        {server_name} {{ service }}
    }}
    /// Get service name
    pub fn get_proto_name(&self) -> String {{
        "{proto_name}".to_string()
    }}
}}
impl<A> prost_rpc::Service for {server_name}<A> where A: {name} + Send + Sync + 'static {{
    fn call(&self, method: String, request: String)
        -> Result<String, String>
    {{
        let response = match method.as_str() {{
            {match_handle_methods}
            _ => {{
                Err("no method name".to_string())
            }}
        }}?;
        match serde_json::to_string(&response) {{
            Ok(r) => Ok(r),
            Err(e) => Err(e.to_string())
        }}
    }}
}}
"#,
            name = service.name,
            proto_name = service.package + "." + &service.proto_name,
            server_name = server_name,
            trait_methods = trait_methods,
            match_handle_methods = match_handle_methods
        ).unwrap();
    }
}

impl ServiceGenerator {
    fn write_comments<W>(
        mut write: W,
        indent: usize,
        comments: &prost_build::Comments,
    ) -> fmt::Result
    where
        W: Write,
    {
        for comment in &comments.leading {
            for line in comment.lines().filter(|s| !s.is_empty()) {
                writeln!(write, "{}///{}", " ".repeat(indent), line)?;
            }
        }
        Ok(())
    }
}