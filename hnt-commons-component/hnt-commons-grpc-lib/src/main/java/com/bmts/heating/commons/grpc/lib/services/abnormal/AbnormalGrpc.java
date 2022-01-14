package com.bmts.heating.commons.grpc.lib.services.abnormal;

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
    comments = "Source: abnormal/abnormal.proto")
public final class AbnormalGrpc {

  private AbnormalGrpc() {}

  public static final String SERVICE_NAME = "Abnormal";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "insert",
      requestType = com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;
    if ((getInsertMethod = AbnormalGrpc.getInsertMethod) == null) {
      synchronized (AbnormalGrpc.class) {
        if ((getInsertMethod = AbnormalGrpc.getInsertMethod) == null) {
          AbnormalGrpc.getInsertMethod = getInsertMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Abnormal", "insert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new AbnormalMethodDescriptorSupplier("insert"))
                  .build();
          }
        }
     }
     return getInsertMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AbnormalStub newStub(io.grpc.Channel channel) {
    return new AbnormalStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AbnormalBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AbnormalBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AbnormalFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AbnormalFutureStub(channel);
  }

  /**
   */
  public static abstract class AbnormalImplBase implements io.grpc.BindableService {

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getInsertMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInsertMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_INSERT)))
          .build();
    }
  }

  /**
   */
  public static final class AbnormalStub extends io.grpc.stub.AbstractStub<AbnormalStub> {
    private AbnormalStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AbnormalStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AbnormalStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AbnormalStub(channel, callOptions);
    }

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInsertMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AbnormalBlockingStub extends io.grpc.stub.AbstractStub<AbnormalBlockingStub> {
    private AbnormalBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AbnormalBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AbnormalBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AbnormalBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult insert(com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal request) {
      return blockingUnaryCall(
          getChannel(), getInsertMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AbnormalFutureStub extends io.grpc.stub.AbstractStub<AbnormalFutureStub> {
    private AbnormalFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AbnormalFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AbnormalFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AbnormalFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> insert(
        com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal request) {
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
    private final AbnormalImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AbnormalImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT:
          serviceImpl.insert((com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.abnormal) request,
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

  private static abstract class AbnormalBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AbnormalBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Abnormal");
    }
  }

  private static final class AbnormalFileDescriptorSupplier
      extends AbnormalBaseDescriptorSupplier {
    AbnormalFileDescriptorSupplier() {}
  }

  private static final class AbnormalMethodDescriptorSupplier
      extends AbnormalBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AbnormalMethodDescriptorSupplier(String methodName) {
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
      synchronized (AbnormalGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AbnormalFileDescriptorSupplier())
              .addMethod(getInsertMethod())
              .build();
        }
      }
    }
    return result;
  }
}
