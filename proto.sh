rm -rf src/generated
rm -rf android/app/src/generated/java
rm -rf ios/generated

npx buf generate proto/math.proto

# mkdir -p src/generated
# mkdir -p android/app/src/generated/java
# mkdir -p ios/generated

# protoc -I=proto --java_out=android/app/src/generated/java --swift_out=ios/generated --grpc-swift_out=ios/generated math.proto
# npx protoc --ts_out src/generated --ts_opt server_generic --proto_path proto proto/math.proto

# Rust generate swift using uniffi
# cargo run -p uniffi-bindgen-cli generate ../rust_mod/src/math.udl --language swift --out-dir ./generated