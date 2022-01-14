package com.bmts.heating.commons.grpc.lib.services.points;

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
    comments = "Source: point/point.proto")
public final class PointGrpc {

  private PointGrpc() {}

  public static final String SERVICE_NAME = "Point";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> getGetEntityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetEntity",
      requestType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> getGetEntityMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> getGetEntityMethod;
    if ((getGetEntityMethod = PointGrpc.getGetEntityMethod) == null) {
      synchronized (PointGrpc.class) {
        if ((getGetEntityMethod = PointGrpc.getGetEntityMethod) == null) {
          PointGrpc.getGetEntityMethod = getGetEntityMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Point", "GetEntity"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus.getDefaultInstance()))
                  .setSchemaDescriptor(new PointMethodDescriptorSupplier("GetEntity"))
                  .build();
          }
        }
     }
     return getGetEntityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMessageIssueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MessageIssue",
      requestType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMessageIssueMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMessageIssueMethod;
    if ((getMessageIssueMethod = PointGrpc.getMessageIssueMethod) == null) {
      synchronized (PointGrpc.class) {
        if ((getMessageIssueMethod = PointGrpc.getMessageIssueMethod) == null) {
          PointGrpc.getMessageIssueMethod = getMessageIssueMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Point", "MessageIssue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new PointMethodDescriptorSupplier("MessageIssue"))
                  .build();
          }
        }
     }
     return getMessageIssueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMonitorIssueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MonitorIssue",
      requestType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMonitorIssueMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> getMonitorIssueMethod;
    if ((getMonitorIssueMethod = PointGrpc.getMonitorIssueMethod) == null) {
      synchronized (PointGrpc.class) {
        if ((getMonitorIssueMethod = PointGrpc.getMonitorIssueMethod) == null) {
          PointGrpc.getMonitorIssueMethod = getMonitorIssueMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Point", "MonitorIssue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new PointMethodDescriptorSupplier("MonitorIssue"))
                  .build();
          }
        }
     }
     return getMonitorIssueMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PointStub newStub(io.grpc.Channel channel) {
    return new PointStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PointBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PointBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PointFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PointFutureStub(channel);
  }

  /**
   */
  public static abstract class PointImplBase implements io.grpc.BindableService {

    /**
     */
    public void getEntity(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> responseObserver) {
      asyncUnimplementedUnaryCall(getGetEntityMethod(), responseObserver);
    }

    /**
     */
    public void messageIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getMessageIssueMethod(), responseObserver);
    }

    /**
     */
    public void monitorIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getMonitorIssueMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetEntityMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus>(
                  this, METHODID_GET_ENTITY)))
          .addMethod(
            getMessageIssueMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>(
                  this, METHODID_MESSAGE_ISSUE)))
          .addMethod(
            getMonitorIssueMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>(
                  this, METHODID_MONITOR_ISSUE)))
          .build();
    }
  }

  /**
   */
  public static final class PointStub extends io.grpc.stub.AbstractStub<PointStub> {
    private PointStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PointStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PointStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PointStub(channel, callOptions);
    }

    /**
     */
    public void getEntity(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetEntityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void messageIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMessageIssueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void monitorIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMonitorIssueMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PointBlockingStub extends io.grpc.stub.AbstractStub<PointBlockingStub> {
    private PointBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PointBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PointBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PointBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus getEntity(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList request) {
      return blockingUnaryCall(
          getChannel(), getGetEntityMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result messageIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request) {
      return blockingUnaryCall(
          getChannel(), getMessageIssueMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result monitorIssue(com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request) {
      return blockingUnaryCall(
          getChannel(), getMonitorIssueMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PointFutureStub extends io.grpc.stub.AbstractStub<PointFutureStub> {
    private PointFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PointFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PointFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PointFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus> getEntity(
        com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList request) {
      return futureUnaryCall(
          getChannel().newCall(getGetEntityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> messageIssue(
        com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request) {
      return futureUnaryCall(
          getChannel().newCall(getMessageIssueMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result> monitorIssue(
        com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList request) {
      return futureUnaryCall(
          getChannel().newCall(getMonitorIssueMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ENTITY = 0;
  private static final int METHODID_MESSAGE_ISSUE = 1;
  private static final int METHODID_MONITOR_ISSUE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PointImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PointImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ENTITY:
          serviceImpl.getEntity((com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.ResStatus>) responseObserver);
          break;
        case METHODID_MESSAGE_ISSUE:
          serviceImpl.messageIssue((com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>) responseObserver);
          break;
        case METHODID_MONITOR_ISSUE:
          serviceImpl.monitorIssue((com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointIssueList) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.Result>) responseObserver);
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

  private static abstract class PointBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PointBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Point");
    }
  }

  private static final class PointFileDescriptorSupplier
      extends PointBaseDescriptorSupplier {
    PointFileDescriptorSupplier() {}
  }

  private static final class PointMethodDescriptorSupplier
      extends PointBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PointMethodDescriptorSupplier(String methodName) {
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
      synchronized (PointGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PointFileDescriptorSupplier())
              .addMethod(getGetEntityMethod())
              .addMethod(getMessageIssueMethod())
              .addMethod(getMonitorIssueMethod())
              .build();
        }
      }
    }
    return result;
  }
}
