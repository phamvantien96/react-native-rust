use math::{AddRequest, AddResponse, MathServiceServer, MathService};
// use std::{result::Result, ops::Add};
use std::{collections::HashMap, sync::Mutex};
use lazy_static::lazy_static;

pub mod math {
    include!(concat!(env!("OUT_DIR"), "/com.reactnativerust.rs"));
}

lazy_static! {
    static ref SERVICE_MAP: HashMap<String, Box<dyn prost_rpc::Service + Sync>> = {
        let mut map: HashMap<String, Box<dyn prost_rpc::Service + Sync>> = HashMap::new();
        let math_service = MathServiceServer::new(MathServiceImpl::new());
        map.insert(math_service.get_proto_name(), Box::new(math_service));
        map
    };
}

struct MathServiceImpl {}

impl MathServiceImpl {
    fn new() -> MathServiceImpl {
        Self {}
    }
}

impl MathService for MathServiceImpl {
    fn add(&self,input:AddRequest) -> Result<AddResponse, String> {
        Ok(AddResponse { result: input.first_number + input.second_number })
    }

    fn minus(&self, input: AddRequest) -> Result<AddResponse, String> {
        Ok(AddResponse { result: input.first_number - input.second_number })
    }
}

/// This function handle unary call
pub fn unary(request: String, service: String, method: String) -> String {
    let service = SERVICE_MAP.get(&service).unwrap();
    match service.as_ref().call(method, request) {
        Ok(s) => s,
        Err(s) => s,
    }
}

uniffi::include_scaffolding!("rust_mod");