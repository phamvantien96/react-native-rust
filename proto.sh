protoc -I=proto --java_out=android/app/src/generated/java math.proto
npx protoc --ts_out src/generated --ts_opt server_generic --proto_path proto proto/math.proto