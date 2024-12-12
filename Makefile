GENERATED_OUT_DIR = ./generated

.DEFAULT_GOAL := proto

.PHONY: proto
proto: clean
proto: $(GENERATED_OUT_DIR)
proto: COMMON_PROTO_PATH=./src/main/proto
proto: DEV_PROTO_PATH=./src/dev/proto
proto:
	protoc \
		--java_out=$(GENERATED_OUT_DIR) \
		--plugin=protoc-gen-example=./plugin.sh \
		--example_out=$(GENERATED_OUT_DIR) \
		--proto_path=$(COMMON_PROTO_PATH) \
		--proto_path=$(DEV_PROTO_PATH) \
		$(wildcard $(DEV_PROTO_PATH)/*.proto)

# Removes generated directory entries
.PHONY: clean
clean:
	rm -rf $(GENERATED_OUT_DIR)

# Creates directory if not exist
$(GENERATED_OUT_DIR):
	[ -d $@ ] || mkdir -p $@
