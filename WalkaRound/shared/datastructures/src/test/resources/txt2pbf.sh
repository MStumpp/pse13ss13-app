#!/bin/sh
# Encoding in folder "src/test/resources"
protoc --encode=SaveGeometryData --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto > geometryData.pbf < geometryData.txt
protoc --encode=SaveGraphData    --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto > graphData.pbf    < graphData.txt
protoc --encode=SaveLocationData --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto > locationData.pbf < locationData.txt
