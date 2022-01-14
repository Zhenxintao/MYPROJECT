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
    comments = "Source: tdengine/queryPoints.proto")
public final class QueryPointsGrpc {

  private QueryPointsGrpc() {}

  public static final String SERVICE_NAME = "QueryPoints";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList,
      com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryOriginalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryOriginal",
      requestType = com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList,
      com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryOriginalMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList, com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryOriginalMethod;
    if ((getQueryOriginalMethod = QueryPointsGrpc.getQueryOriginalMethod) == null) {
      synchronized (QueryPointsGrpc.class) {
        if ((getQueryOriginalMethod = QueryPointsGrpc.getQueryOriginalMethod) == null) {
          QueryPointsGrpc.getQueryOriginalMethod = getQueryOriginalMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList, com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "QueryPoints", "queryOriginal"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new QueryPointsMethodDescriptorSupplier("queryOriginal"))
                  .build();
          }
        }
     }
     return getQueryOriginalMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static QueryPointsStub newStub(io.grpc.Channel channel) {
    return new QueryPointsStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static QueryPointsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new QueryPointsBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static QueryPointsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new QueryPointsFutureStub(channel);
  }

  /**
   */
  public static abstract class QueryPointsImplBase implements io.grpc.BindableService {

    /**
     */
    public void queryOriginal(com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryOriginalMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getQueryOriginalMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList,
                com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response>(
                  this, METHODID_QUERY_ORIGINAL)))
          .build();
    }
  }

  /**
   */
  public static final class QueryPointsStub extends io.grpc.stub.AbstractStub<QueryPointsStub> {
    private QueryPointsStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryPointsStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryPointsStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryPointsStub(channel, callOptions);
    }

    /**
     */
    public void queryOriginal(com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getQueryOriginalMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class QueryPointsBlockingStub extends io.grpc.stub.AbstractStub<QueryPointsBlockingStub> {
    private QueryPointsBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryPointsBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryPointsBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryPointsBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> queryOriginal(
        com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList request) {
      return blockingServerStreamingCall(
          getChannel(), getQueryOriginalMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class QueryPointsFutureStub extends io.grpc.stub.AbstractStub<QueryPointsFutureStub> {
    private QueryPointsFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryPointsFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryPointsFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryPointsFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_QUERY_ORIGINAL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final QueryPointsImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(QueryPointsImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_QUERY_ORIGINAL:
          serviceImpl.queryOriginal((com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.QueryList) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response>) responseObserver);
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

  private static abstract class QueryPointsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    QueryPointsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("QueryPoints");
    }
  }

  private static final class QueryPointsFileDescriptorSupplier
      extends QueryPointsBaseDescriptorSupplier {
    QueryPointsFileDescriptorSupplier() {}
  }

  private static final class QueryPointsMethodDescriptorSupplier
      extends QueryPointsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    QueryPointsMethodDescriptorSupplier(String methodName) {
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
      synchronized (QueryPointsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new QueryPointsFileDescriptorSupplier())
              .addMethod(getQueryOriginalMethod())
              .build();
        }
      }
    }
    return result;
  }
}
