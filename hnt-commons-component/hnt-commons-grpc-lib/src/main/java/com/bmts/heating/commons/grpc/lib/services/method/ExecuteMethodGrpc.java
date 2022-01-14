package com.bmts.heating.commons.grpc.lib.services.method;

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
    comments = "Source: method/executeMethod.proto")
public final class ExecuteMethodGrpc {

  private ExecuteMethodGrpc() {}

  public static final String SERVICE_NAME = "ExecuteMethod";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getExecuteMethodMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "execute_method",
      requestType = com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getExecuteMethodMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getExecuteMethodMethod;
    if ((getExecuteMethodMethod = ExecuteMethodGrpc.getExecuteMethodMethod) == null) {
      synchronized (ExecuteMethodGrpc.class) {
        if ((getExecuteMethodMethod = ExecuteMethodGrpc.getExecuteMethodMethod) == null) {
          ExecuteMethodGrpc.getExecuteMethodMethod = getExecuteMethodMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ExecuteMethod", "execute_method"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new ExecuteMethodMethodDescriptorSupplier("execute_method"))
                  .build();
          }
        }
     }
     return getExecuteMethodMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ExecuteMethodStub newStub(io.grpc.Channel channel) {
    return new ExecuteMethodStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ExecuteMethodBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ExecuteMethodBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ExecuteMethodFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ExecuteMethodFutureStub(channel);
  }

  /**
   */
  public static abstract class ExecuteMethodImplBase implements io.grpc.BindableService {

    /**
     */
    public void executeMethod(com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteMethodMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getExecuteMethodMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_EXECUTE_METHOD)))
          .build();
    }
  }

  /**
   */
  public static final class ExecuteMethodStub extends io.grpc.stub.AbstractStub<ExecuteMethodStub> {
    private ExecuteMethodStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExecuteMethodStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExecuteMethodStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExecuteMethodStub(channel, callOptions);
    }

    /**
     */
    public void executeMethod(com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExecuteMethodMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ExecuteMethodBlockingStub extends io.grpc.stub.AbstractStub<ExecuteMethodBlockingStub> {
    private ExecuteMethodBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExecuteMethodBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExecuteMethodBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExecuteMethodBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult executeMethod(com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName request) {
      return blockingUnaryCall(
          getChannel(), getExecuteMethodMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ExecuteMethodFutureStub extends io.grpc.stub.AbstractStub<ExecuteMethodFutureStub> {
    private ExecuteMethodFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExecuteMethodFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExecuteMethodFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExecuteMethodFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> executeMethod(
        com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName request) {
      return futureUnaryCall(
          getChannel().newCall(getExecuteMethodMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_EXECUTE_METHOD = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ExecuteMethodImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ExecuteMethodImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EXECUTE_METHOD:
          serviceImpl.executeMethod((com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.MethodName) request,
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

  private static abstract class ExecuteMethodBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ExecuteMethodBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ExecuteMethod");
    }
  }

  private static final class ExecuteMethodFileDescriptorSupplier
      extends ExecuteMethodBaseDescriptorSupplier {
    ExecuteMethodFileDescriptorSupplier() {}
  }

  private static final class ExecuteMethodMethodDescriptorSupplier
      extends ExecuteMethodBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ExecuteMethodMethodDescriptorSupplier(String methodName) {
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
      synchronized (ExecuteMethodGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ExecuteMethodFileDescriptorSupplier())
              .addMethod(getExecuteMethodMethod())
              .build();
        }
      }
    }
    return result;
  }
}
