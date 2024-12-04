[![GitHub Actions](https://github.com/raccoons-co/abstract-protobuf-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/raccoons-co/jru-telegrambot/actions)
[![Codecov](https://codecov.io/gh/raccoons-co/abstract-protobuf-plugin/graph/badge.svg?token=y9xaNeJ4Lz)](https://codecov.io/gh/raccoons-co/abstract-protobuf-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=raccoons-co_abstract-protobuf-plugin&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=raccoons-co_abstract-protobuf-plugin)

# Abstract Protobuf Plugin

### Developing Protobuf Compiler Plugin in Java

---

Let's simplify creation of Protoc Plugin with using the
`AbstractProtocPlugin`. This class has skeletal implementation to handle
plugins' standard input and output streams.

To introduce a concrete plugin the programmer must extend `AbstractProtocPlugin`
class and implement the method `response()` which returns an instance of 
`CodeGeneratorResponse`. This response will be implicitly written to plugins'
stdout on calling `integrate()` method.

The protected method `request()` returns the instance of the 
`CodeGeneratorRequest` which has been read from plugins' standard input.
The request should be used for the further processing by your code generator.

``` Java
public static void main(String[]args) {
    new AbstractProtocPlugin() {
        @Override
        protected CodeGeneratorResponse response() {
            var request = request();
            var generator = new ExtraMessageInterface();
            var files = generator.process(request);
            return CodeGeneratorResponse.newBuilder()
                    .addAllFile(files)
                    .build();
        }
    }.integrate();
}
```

### Applying plugin executable

---

The plugin should be named `protoc-gen-$NAME`, and will then be used when the
flag `--${NAME}_out` is passed to protoc. The example of usage in the
`Makefile` is given below

~~~ Makefile
proto: clean $(GENERATED_OUT_DIR)
proto: PROTO_PATH = ./src/main/proto
proto:
    protoc \
        --plugin=protoc-gen-example=./plugin.sh \
        --example_out=$(GENERATED_OUT_DIR) \
        --java_out=$(GENERATED_OUT_DIR) \
        --proto_path=$(PROTO_PATH) \
        $(wildcard $(PROTO_PATH)/*.proto)
~~~

### References

1. [plugin.proto](https://github.com/protocolbuffers/protobuf/blob/main/src/google/protobuf/compiler/plugin.proto)
2. Protobuf compiler [api-docs](https://protobuf.dev/reference/java/api-docs/com/google/protobuf/compiler/package-summary.html)
3. Extending Protobuf: [custom options](https://giorgio.azzinna.ro/2017/07/extending-protobuf-custom-options/)
