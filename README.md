[![GitHub Actions](https://github.com/raccoons-co/abstract-protobuf-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/raccoons-co/jru-telegrambot/actions)
[![Codecov](https://codecov.io/gh/raccoons-co/abstract-protobuf-plugin/graph/badge.svg?token=y9xaNeJ4Lz)](https://codecov.io/gh/raccoons-co/abstract-protobuf-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=raccoons-co_abstract-protobuf-plugin&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=raccoons-co_abstract-protobuf-plugin)

# Developing Protobuf Compiler Plugin in Java

### Abstract Protoc Plugin

---

Let's simplify creation of Protobuf Compiler Plugin with using the
`AbstractProtocPlugin`. This class has skeletal implementation to handle
plugins' standard input and output streams.

To introduce a concrete plugin the programmer must extend `AbstractProtocPlugin`
class and implement the method `response()` which returns an instance of 
`CodeGeneratorResponse`. This response will be implicitly written to plugins'
stdout on calling `integrate()` method.

The protected method `request()` returns the instance of the 
`CodeGeneratorRequest` which has been read from plugins' standard input.
The request should be used for the further processing by your code generator.

### Adding Custom Options

---

To define and use own options for Protocol Buffers the programmer must override 
method `registerCustomOptions(...)` and  implement corresponding *.proto* files 
as well.

``` Java
public static void main(String[] args) {
    new AbstractProtocPlugin() {
        @Override
        protected void registerCustomOptions(ExtensionRegistry registry) {
            OptionsProto.registerAllExtensions(registry);
        }

        @Override
        protected CodeGeneratorResponse response() {
            var request = request();
            var generator = CodeGenerator.newBuilder()
                    .addGenerator(new ExtraMessageInterface())
                    .build();
            var files = generator.process(request);
            return CodeGeneratorResponse.newBuilder()
                    .addAllFile(files)
                    .build();
        }
    }.integrate();
}
```
 
### Abstract Code Generator

---
There is another abstraction that can be used as a part of a further plugin 
development.

The skeletal implementation of `AbstractCodeGenerator` handles processing of
generating Java code extensions for any protocol message types.

To introduce a concrete generator that extends the output produced by another 
code generator, the programmer must extend `AbstractCodeGenerator`class and 
implement the method `generate(...)` which returns an instance of
`CodeGeneratorResponse.File`.

``` Java
final class ExtraMessageInterface extends AbstractCodeGenerator {

    @Override
    protected Predicate<ProtocolType> filter() {
        return super.filter()
                .and(ExtraMessageInterface::hasMessageType)
                .and(ExtraMessageInterface::hasExtraOption);
    }

    @Override
    protected File generate(ProtocolType protocolType) {
        var type = protocolType.getProtobufType();
        var insertionPoint = ProtocExtra.message_implements.newInsertionPoint(type);
        var content = content(protocolType);

        return File.newBuilder()
                .setName(insertionPoint.getFileName())
                .setInsertionPoint(insertionPoint.getIdentifier())
                .setContent(content)
                .build();
    }
    ...
}
```

### Applying Plugin Executable

---

The plugin should be named `protoc-gen-$NAME`, and will then be used when the
flag `--${NAME}_out` is passed to protoc.

The code generator that generates 
the initial file and the one which inserts into it must both run as part of 
a single invocation of protoc. Code generators are executed in the order in 
which they appear on the command line.

The example of usage in the `Makefile` is given below:

~~~ Makefile
proto: clean $(GENERATED_OUT_DIR)
proto: PROTO_PATH = ./src/main/proto
proto:
    protoc \
        --java_out=$(GENERATED_OUT_DIR) \
        --plugin=protoc-gen-example=./plugin.sh \
        --example_out=$(GENERATED_OUT_DIR) \
        --proto_path=$(PROTO_PATH) \
        $(wildcard $(PROTO_PATH)/*.proto)
~~~

### References

---

1. [plugin.proto](https://github.com/protocolbuffers/protobuf/blob/main/src/google/protobuf/compiler/plugin.proto)
2. Protobuf compiler [api-docs](https://protobuf.dev/reference/java/api-docs/com/google/protobuf/compiler/package-summary.html)
3. [descriptor.proto](https://github.com/protocolbuffers/protobuf/blob/main/src/google/protobuf/descriptor.proto)
4. Extending Protobuf: [custom options](https://giorgio.azzinna.ro/2017/07/extending-protobuf-custom-options/)
5. Programming-guides (proto3) [custom options](https://protobuf.dev/programming-guides/proto3/#customoptions)