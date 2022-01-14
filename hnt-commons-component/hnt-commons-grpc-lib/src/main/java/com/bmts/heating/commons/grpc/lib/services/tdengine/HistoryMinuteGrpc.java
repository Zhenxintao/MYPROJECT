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
    comments = "Source: tdengine/historyMinute.proto")
public final class HistoryMinuteGrpc {

  private HistoryMinuteGrpc() {}

  public static final String SERVICE_NAME = "HistoryMinute";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "insert",
      requestType = com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getInsertMethod;
    if ((getInsertMethod = HistoryMinuteGrpc.getInsertMethod) == null) {
      synchronized (HistoryMinuteGrpc.class) {
        if ((getInsertMethod = HistoryMinuteGrpc.getInsertMethod) == null) {
          HistoryMinuteGrpc.getInsertMethod = getInsertMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "HistoryMinute", "insert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new HistoryMinuteMethodDescriptorSupplier("insert"))
                  .build();
          }
        }
     }
     return getInsertMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HistoryMinuteStub newStub(io.grpc.Channel channel) {
    return new HistoryMinuteStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HistoryMinuteBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HistoryMinuteBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HistoryMinuteFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HistoryMinuteFutureStub(channel);
  }

  /**
   */
  public static abstract class HistoryMinuteImplBase implements io.grpc.BindableService {

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getInsertMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInsertMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_INSERT)))
          .build();
    }
  }

  /**
   */
  public static final class HistoryMinuteStub extends io.grpc.stub.AbstractStub<HistoryMinuteStub> {
    private HistoryMinuteStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HistoryMinuteStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HistoryMinuteStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HistoryMinuteStub(channel, callOptions);
    }

    /**
     */
    public void insert(com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInsertMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HistoryMinuteBlockingStub extends io.grpc.stub.AbstractStub<HistoryMinuteBlockingStub> {
    private HistoryMinuteBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HistoryMinuteBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HistoryMinuteBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HistoryMinuteBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult insert(com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body request) {
      return blockingUnaryCall(
          getChannel(), getInsertMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HistoryMinuteFutureStub extends io.grpc.stub.AbstractStub<HistoryMinuteFutureStub> {
    private HistoryMinuteFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HistoryMinuteFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HistoryMinuteFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HistoryMinuteFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> insert(
        com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body request) {
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
    private final HistoryMinuteImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HistoryMinuteImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT:
          serviceImpl.insert((com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.Body) request,
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

  private static abstract class HistoryMinuteBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HistoryMinuteBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.tdengine.HistoryMinuteOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HistoryMinute");
    }
  }

  private static final class HistoryMinuteFileDescriptorSupplier
      extends HistoryMinuteBaseDescriptorSupplier {
    HistoryMinuteFileDescriptorSupplier() {}
  }

  private static final class HistoryMinuteMethodDescriptorSupplier
      extends HistoryMinuteBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HistoryMinuteMethodDescriptorSupplier(String methodName) {
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
      synchronized (HistoryMinuteGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HistoryMinuteFileDescriptorSupplier())
              .addMethod(getInsertMethod())
              .build();
        }
      }
    }
    return result;
  }
}
