// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: protocol.proto

package rebryk.net.protobuf;

public final class Protocol {
  private Protocol() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface BenchmarkPacketOrBuilder extends
      // @@protoc_insertion_point(interface_extends:rebryk.net.protobuf.BenchmarkPacket)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional int32 count = 1;</code>
     */
    int getCount();

    /**
     * <code>repeated int32 array = 2;</code>
     */
    java.util.List<java.lang.Integer> getArrayList();
    /**
     * <code>repeated int32 array = 2;</code>
     */
    int getArrayCount();
    /**
     * <code>repeated int32 array = 2;</code>
     */
    int getArray(int index);
  }
  /**
   * Protobuf type {@code rebryk.net.protobuf.BenchmarkPacket}
   */
  public  static final class BenchmarkPacket extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:rebryk.net.protobuf.BenchmarkPacket)
      BenchmarkPacketOrBuilder {
    // Use BenchmarkPacket.newBuilder() to construct.
    private BenchmarkPacket(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private BenchmarkPacket() {
      count_ = 0;
      array_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private BenchmarkPacket(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              count_ = input.readInt32();
              break;
            }
            case 16: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                array_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              array_.add(input.readInt32());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                array_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              while (input.getBytesUntilLimit() > 0) {
                array_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw new RuntimeException(e.setUnfinishedMessage(this));
      } catch (java.io.IOException e) {
        throw new RuntimeException(
            new com.google.protobuf.InvalidProtocolBufferException(
                e.getMessage()).setUnfinishedMessage(this));
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          array_ = java.util.Collections.unmodifiableList(array_);
        }
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return rebryk.net.protobuf.Protocol.internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return rebryk.net.protobuf.Protocol.internal_static_rebryk_net_protobuf_BenchmarkPacket_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              rebryk.net.protobuf.Protocol.BenchmarkPacket.class, rebryk.net.protobuf.Protocol.BenchmarkPacket.Builder.class);
    }

    private int bitField0_;
    public static final int COUNT_FIELD_NUMBER = 1;
    private int count_;
    /**
     * <code>optional int32 count = 1;</code>
     */
    public int getCount() {
      return count_;
    }

    public static final int ARRAY_FIELD_NUMBER = 2;
    private java.util.List<java.lang.Integer> array_;
    /**
     * <code>repeated int32 array = 2;</code>
     */
    public java.util.List<java.lang.Integer>
        getArrayList() {
      return array_;
    }
    /**
     * <code>repeated int32 array = 2;</code>
     */
    public int getArrayCount() {
      return array_.size();
    }
    /**
     * <code>repeated int32 array = 2;</code>
     */
    public int getArray(int index) {
      return array_.get(index);
    }
    private int arrayMemoizedSerializedSize = -1;

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (count_ != 0) {
        output.writeInt32(1, count_);
      }
      if (getArrayList().size() > 0) {
        output.writeRawVarint32(18);
        output.writeRawVarint32(arrayMemoizedSerializedSize);
      }
      for (int i = 0; i < array_.size(); i++) {
        output.writeInt32NoTag(array_.get(i));
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (count_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, count_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < array_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(array_.get(i));
        }
        size += dataSize;
        if (!getArrayList().isEmpty()) {
          size += 1;
          size += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(dataSize);
        }
        arrayMemoizedSerializedSize = dataSize;
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static rebryk.net.protobuf.Protocol.BenchmarkPacket parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(rebryk.net.protobuf.Protocol.BenchmarkPacket prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code rebryk.net.protobuf.BenchmarkPacket}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:rebryk.net.protobuf.BenchmarkPacket)
        rebryk.net.protobuf.Protocol.BenchmarkPacketOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return rebryk.net.protobuf.Protocol.internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return rebryk.net.protobuf.Protocol.internal_static_rebryk_net_protobuf_BenchmarkPacket_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                rebryk.net.protobuf.Protocol.BenchmarkPacket.class, rebryk.net.protobuf.Protocol.BenchmarkPacket.Builder.class);
      }

      // Construct using rebryk.net.protobuf.Protocol.BenchmarkPacket.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        count_ = 0;

        array_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return rebryk.net.protobuf.Protocol.internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor;
      }

      public rebryk.net.protobuf.Protocol.BenchmarkPacket getDefaultInstanceForType() {
        return rebryk.net.protobuf.Protocol.BenchmarkPacket.getDefaultInstance();
      }

      public rebryk.net.protobuf.Protocol.BenchmarkPacket build() {
        rebryk.net.protobuf.Protocol.BenchmarkPacket result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public rebryk.net.protobuf.Protocol.BenchmarkPacket buildPartial() {
        rebryk.net.protobuf.Protocol.BenchmarkPacket result = new rebryk.net.protobuf.Protocol.BenchmarkPacket(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.count_ = count_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          array_ = java.util.Collections.unmodifiableList(array_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.array_ = array_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof rebryk.net.protobuf.Protocol.BenchmarkPacket) {
          return mergeFrom((rebryk.net.protobuf.Protocol.BenchmarkPacket)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(rebryk.net.protobuf.Protocol.BenchmarkPacket other) {
        if (other == rebryk.net.protobuf.Protocol.BenchmarkPacket.getDefaultInstance()) return this;
        if (other.getCount() != 0) {
          setCount(other.getCount());
        }
        if (!other.array_.isEmpty()) {
          if (array_.isEmpty()) {
            array_ = other.array_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureArrayIsMutable();
            array_.addAll(other.array_);
          }
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        rebryk.net.protobuf.Protocol.BenchmarkPacket parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (rebryk.net.protobuf.Protocol.BenchmarkPacket) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int count_ ;
      /**
       * <code>optional int32 count = 1;</code>
       */
      public int getCount() {
        return count_;
      }
      /**
       * <code>optional int32 count = 1;</code>
       */
      public Builder setCount(int value) {
        
        count_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 count = 1;</code>
       */
      public Builder clearCount() {
        
        count_ = 0;
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Integer> array_ = java.util.Collections.emptyList();
      private void ensureArrayIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          array_ = new java.util.ArrayList<java.lang.Integer>(array_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public java.util.List<java.lang.Integer>
          getArrayList() {
        return java.util.Collections.unmodifiableList(array_);
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public int getArrayCount() {
        return array_.size();
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public int getArray(int index) {
        return array_.get(index);
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public Builder setArray(
          int index, int value) {
        ensureArrayIsMutable();
        array_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public Builder addArray(int value) {
        ensureArrayIsMutable();
        array_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public Builder addAllArray(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        ensureArrayIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, array_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 array = 2;</code>
       */
      public Builder clearArray() {
        array_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:rebryk.net.protobuf.BenchmarkPacket)
    }

    // @@protoc_insertion_point(class_scope:rebryk.net.protobuf.BenchmarkPacket)
    private static final rebryk.net.protobuf.Protocol.BenchmarkPacket DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new rebryk.net.protobuf.Protocol.BenchmarkPacket();
    }

    public static rebryk.net.protobuf.Protocol.BenchmarkPacket getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<BenchmarkPacket>
        PARSER = new com.google.protobuf.AbstractParser<BenchmarkPacket>() {
      public BenchmarkPacket parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        try {
          return new BenchmarkPacket(input, extensionRegistry);
        } catch (RuntimeException e) {
          if (e.getCause() instanceof
              com.google.protobuf.InvalidProtocolBufferException) {
            throw (com.google.protobuf.InvalidProtocolBufferException)
                e.getCause();
          }
          throw e;
        }
      }
    };

    public static com.google.protobuf.Parser<BenchmarkPacket> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<BenchmarkPacket> getParserForType() {
      return PARSER;
    }

    public rebryk.net.protobuf.Protocol.BenchmarkPacket getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_rebryk_net_protobuf_BenchmarkPacket_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016protocol.proto\022\023rebryk.net.protobuf\"/\n" +
      "\017BenchmarkPacket\022\r\n\005count\030\001 \001(\005\022\r\n\005array" +
      "\030\002 \003(\005b\006proto3"
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
        }, assigner);
    internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_rebryk_net_protobuf_BenchmarkPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_rebryk_net_protobuf_BenchmarkPacket_descriptor,
        new java.lang.String[] { "Count", "Array", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}