#!/bin/sh
# Decoding in folder "src/test/resources"
protoc --decode=SaveGeometryData --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto < geometryData.pbf > geometryData.txt
protoc --decode=SaveGraphData    --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto < graphData.pbf    > graphData.txt
protoc --decode=SaveLocationData --proto_path=../../main/protobuf/ ../../main/protobuf/data-io.proto < locationData.pbf > locationData.txt
