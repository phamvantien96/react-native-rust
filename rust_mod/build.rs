fn main() {
    uniffi::generate_scaffolding("./src/rust_mod.udl").unwrap();
    prost_build::Config::new()
    .type_attribute(".", "#[derive(serde::Serialize, serde::Deserialize)]")
    .type_attribute(".", "#[serde(rename_all = \"camelCase\")]")
    .service_generator(Box::new(prost_rpc::ServiceGenerator::new()))
    // .out_dir("src")
    .compile_well_known_types().compile_protos(
        &["../proto/math.proto"],
        &["../proto"],
    )
    .unwrap();
    // tonic_build::configure().build_client(false).build_transport(false).compile(&["../proto/math.proto"], &["../proto"]).unwrap();
}