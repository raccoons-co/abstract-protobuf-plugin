GENERATED_OUT_DIR = ./generated

.PHONY: proto
proto: clean $(GENERATED_OUT_DIR)
proto: PROTO_PATH = ./src/main/proto
proto:
	protoc \
		--plugin=protoc-gen-example=./plugin.sh \
		--example_out=$(GENERATED_OUT_DIR) \
		--proto_path=$(PROTO_PATH) \
		$(wildcard $(PROTO_PATH)/*.proto)

# Removes generated directory entries
.PHONY: clean
clean:
	rm -rf $(GENERATED_OUT_DIR)

# Creates directory if not exist
$(GENERATED_OUT_DIR):
	[ -d $@ ] || mkdir -p $@
