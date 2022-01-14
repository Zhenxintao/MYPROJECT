package com.bmts.heating.commons.grpc.lib.services.tdengine;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.1)",
    comments = "Source: tdengine/meta.proto")
public final class MetaGrpc {

  private MetaGrpc() {}

  public static final String SERVICE_NAME = "Meta";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "insert",
      requestType = com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;
    if ((getInsertMethod = MetaGrpc.getInsertMethod) == null) {
      synchronized (MetaGrpc.class) {
        if ((getInsertMethod = MetaGrpc.getInsertMethod) == null) {
          MetaGrpc.getInsertMethod = getInsertMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Meta", "insert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new MetaMethodDescriptorSupplier("insert"))
                  .build();
          }
        }
     }
     return getInsertMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MetaStub newStub(io.grpc.Channel channel) {
    return new MetaStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MetaBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MetaBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MetaFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MetaFutureStub(channel);
  }

  /**
   */
  public static abstract class MetaImplBase implements io.grpc.BindableService {

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getInsertMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInsertMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_INSERT)))
          .build();
    }
  }

  /**
   */
  public static final class MetaStub extends io.grpc.stub.AbstractStub<MetaStub> {
    private MetaStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MetaStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetaStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MetaStub(channel, callOptions);
    }

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInsertMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MetaBlockingStub extends io.grpc.stub.AbstractStub<MetaBlockingStub> {
    private MetaBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MetaBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetaBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MetaBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult insert(com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request request) {
      return blockingUnaryCall(
          getChannel(), getInsertMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MetaFutureStub extends io.grpc.stub.AbstractStub<MetaFutureStub> {
    private MetaFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MetaFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetaFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MetaFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> insert(
        com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request request) {
      return futureUnaryCall(
          getChannel().newCall(getInsertMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INSERT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MetaImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MetaImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT:
          serviceImpl.insert((com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.Request) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MetaBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.tdengine.MetaOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Meta");
    }
  }

  private static final class MetaFileDescriptorSupplier
      extends MetaBaseDescriptorSupplier {
    MetaFileDescriptorSupplier() {}
  }

  private static final class MetaMethodDescriptorSupplier
      extends MetaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MetaMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MetaGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MetaFileDescriptorSupplier())
              .addMethod(getInsertMethod())
              .build();
        }
      }
    }
    return result;
  }
}
