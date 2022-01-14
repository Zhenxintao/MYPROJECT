// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: method/executeMethod.proto

package com.bmts.heating.commons.grpc.lib.services.method;

public final class ExecuteMethodOuterClass {
  private ExecuteMethodOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MethodNameOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MethodName)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string name = 1;</code>
     */
    java.lang.String getName();
    /**
     * <code>string name = 1;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();
  }
  /**
   * Protobuf type {@code MethodName}
   */
  public  static final class MethodName extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:MethodName)
      MethodNameOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MethodName.newBuilder() to construct.
    private MethodName(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MethodName() {
      name_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private MethodName(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              java.lang.String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.internal_static_MethodName_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.internal_static_MethodName_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.class, com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.Builder.class);
    }

    public static final int NAME_FIELD_NUMBER = 1;
    private volatile java.lang.Object name_;
    /**
     * <code>string name = 1;</code>
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 1;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName)) {
        return super.equals(obj);
      }
      com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName other = (com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName) obj;

      boolean result = true;
      result = result && getName()
          .equals(other.getName());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code MethodName}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MethodName)
        com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodNameOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.internal_static_MethodName_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.internal_static_MethodName_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.class, com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.Builder.class);
      }

      // Construct using com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        name_ = "";

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.internal_static_MethodName_descriptor;
      }

      @java.lang.Override
      public com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName getDefaultInstanceForType() {
        return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.getDefaultInstance();
      }

      @java.lang.Override
      public com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName build() {
        com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName buildPartial() {
        com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName result = new com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName(this);
        result.name_ = name_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName) {
          return mergeFrom((com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName other) {
        if (other == com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.getDefaultInstance()) return this;
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object name_ = "";
      /**
       * <code>string name = 1;</code>
       */
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string name = 1;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 1;</code>
       */
      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 1;</code>
       */
      public Builder clearName() {
        
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <code>string name = 1;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        name_ = value;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:MethodName)
    }

    // @@protoc_insertion_point(class_scope:MethodName)
    private static final com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName();
    }

    public static com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MethodName>
        PARSER = new com.google.protobuf.AbstractParser<MethodName>() {
      @java.lang.Override
      public MethodName parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MethodName(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MethodName> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MethodName> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MethodName_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MethodName_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\032method/executeMethod.proto\032\023common/com" +
      "mon.proto\"\032\n\nMethodName\022\014\n\004name\030\001 \001(\t2=\n" +
      "\rExecuteMethod\022,\n\016execute_method\022\013.Metho" +
      "dName\032\013.BoolResult\"\000B3\n1com.bmts.heating" +
      ".commons.grpc.lib.services.methodb\006proto" +
      "3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.bmts.heating.commons.grpc.lib.services.common.Common.getDescriptor(),
        }, assigner);
    internal_static_MethodName_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_MethodName_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MethodName_descriptor,
        new java.lang.String[] { "Name", });
    com.bmts.heating.commons.grpc.lib.services.common.Common.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
