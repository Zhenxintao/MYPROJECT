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
    comments = "Source: tdengine/queryAggregate.proto")
public final class QueryAggregateGrpc {

  private QueryAggregateGrpc() {}

  public static final String SERVICE_NAME = "QueryAggregate";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery,
      com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "query",
      requestType = com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery,
      com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery, com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> getQueryMethod;
    if ((getQueryMethod = QueryAggregateGrpc.getQueryMethod) == null) {
      synchronized (QueryAggregateGrpc.class) {
        if ((getQueryMethod = QueryAggregateGrpc.getQueryMethod) == null) {
          QueryAggregateGrpc.getQueryMethod = getQueryMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery, com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "QueryAggregate", "query"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new QueryAggregateMethodDescriptorSupplier("query"))
                  .build();
          }
        }
     }
     return getQueryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static QueryAggregateStub newStub(io.grpc.Channel channel) {
    return new QueryAggregateStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static QueryAggregateBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new QueryAggregateBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static QueryAggregateFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new QueryAggregateFutureStub(channel);
  }

  /**
   */
  public static abstract class QueryAggregateImplBase implements io.grpc.BindableService {

    /**
     */
    public void query(com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getQueryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery,
                com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response>(
                  this, METHODID_QUERY)))
          .build();
    }
  }

  /**
   */
  public static final class QueryAggregateStub extends io.grpc.stub.AbstractStub<QueryAggregateStub> {
    private QueryAggregateStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryAggregateStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryAggregateStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryAggregateStub(channel, callOptions);
    }

    /**
     */
    public void query(com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class QueryAggregateBlockingStub extends io.grpc.stub.AbstractStub<QueryAggregateBlockingStub> {
    private QueryAggregateBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryAggregateBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryAggregateBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryAggregateBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response query(com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery request) {
      return blockingUnaryCall(
          getChannel(), getQueryMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class QueryAggregateFutureStub extends io.grpc.stub.AbstractStub<QueryAggregateFutureStub> {
    private QueryAggregateFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private QueryAggregateFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected QueryAggregateFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new QueryAggregateFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass.Response> query(
        com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_QUERY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final QueryAggregateImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(QueryAggregateImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_QUERY:
          serviceImpl.query((com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.aggregateQuery) request,
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

  private static abstract class QueryAggregateBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    QueryAggregateBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.tdengine.QueryAggregateOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("QueryAggregate");
    }
  }

  private static final class QueryAggregateFileDescriptorSupplier
      extends QueryAggregateBaseDescriptorSupplier {
    QueryAggregateFileDescriptorSupplier() {}
  }

  private static final class QueryAggregateMethodDescriptorSupplier
      extends QueryAggregateBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    QueryAggregateMethodDescriptorSupplier(String methodName) {
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
      synchronized (QueryAggregateGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new QueryAggregateFileDescriptorSupplier())
              .addMethod(getQueryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
