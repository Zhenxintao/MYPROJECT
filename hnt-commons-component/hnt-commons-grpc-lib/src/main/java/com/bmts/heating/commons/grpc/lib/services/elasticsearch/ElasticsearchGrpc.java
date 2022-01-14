package com.bmts.heating.commons.grpc.lib.services.elasticsearch;

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
    comments = "Source: elsaticsearch/elasticsearch.proto")
public final class ElasticsearchGrpc {

  private ElasticsearchGrpc() {}

  public static final String SERVICE_NAME = "Elasticsearch";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest,
      com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getFindByHeatSystemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findByHeatSystem",
      requestType = com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest,
      com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getFindByHeatSystemMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest, com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getFindByHeatSystemMethod;
    if ((getFindByHeatSystemMethod = ElasticsearchGrpc.getFindByHeatSystemMethod) == null) {
      synchronized (ElasticsearchGrpc.class) {
        if ((getFindByHeatSystemMethod = ElasticsearchGrpc.getFindByHeatSystemMethod) == null) {
          ElasticsearchGrpc.getFindByHeatSystemMethod = getFindByHeatSystemMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest, com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "Elasticsearch", "findByHeatSystem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ElasticsearchMethodDescriptorSupplier("findByHeatSystem"))
                  .build();
          }
        }
     }
     return getFindByHeatSystemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest,
      com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getBucketMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "bucket",
      requestType = com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest,
      com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getBucketMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest, com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> getBucketMethod;
    if ((getBucketMethod = ElasticsearchGrpc.getBucketMethod) == null) {
      synchronized (ElasticsearchGrpc.class) {
        if ((getBucketMethod = ElasticsearchGrpc.getBucketMethod) == null) {
          ElasticsearchGrpc.getBucketMethod = getBucketMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest, com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "Elasticsearch", "bucket"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ElasticsearchMethodDescriptorSupplier("bucket"))
                  .build();
          }
        }
     }
     return getBucketMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElasticsearchStub newStub(io.grpc.Channel channel) {
    return new ElasticsearchStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElasticsearchBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ElasticsearchBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElasticsearchFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ElasticsearchFutureStub(channel);
  }

  /**
   */
  public static abstract class ElasticsearchImplBase implements io.grpc.BindableService {

    /**
     */
    public void findByHeatSystem(com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFindByHeatSystemMethod(), responseObserver);
    }

    /**
     */
    public void bucket(com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBucketMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getFindByHeatSystemMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest,
                com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>(
                  this, METHODID_FIND_BY_HEAT_SYSTEM)))
          .addMethod(
            getBucketMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest,
                com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>(
                  this, METHODID_BUCKET)))
          .build();
    }
  }

  /**
   */
  public static final class ElasticsearchStub extends io.grpc.stub.AbstractStub<ElasticsearchStub> {
    private ElasticsearchStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElasticsearchStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElasticsearchStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElasticsearchStub(channel, callOptions);
    }

    /**
     */
    public void findByHeatSystem(com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFindByHeatSystemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bucket(com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getBucketMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ElasticsearchBlockingStub extends io.grpc.stub.AbstractStub<ElasticsearchBlockingStub> {
    private ElasticsearchBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElasticsearchBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElasticsearchBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElasticsearchBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> findByHeatSystem(
        com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getFindByHeatSystemMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse> bucket(
        com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getBucketMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ElasticsearchFutureStub extends io.grpc.stub.AbstractStub<ElasticsearchFutureStub> {
    private ElasticsearchFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElasticsearchFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElasticsearchFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElasticsearchFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_FIND_BY_HEAT_SYSTEM = 0;
  private static final int METHODID_BUCKET = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ElasticsearchImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ElasticsearchImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_BY_HEAT_SYSTEM:
          serviceImpl.findByHeatSystem((com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryRequest) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>) responseObserver);
          break;
        case METHODID_BUCKET:
          serviceImpl.bucket((com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.BucketRequest) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.HistoryResponse>) responseObserver);
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

  private static abstract class ElasticsearchBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ElasticsearchBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Elasticsearch");
    }
  }

  private static final class ElasticsearchFileDescriptorSupplier
      extends ElasticsearchBaseDescriptorSupplier {
    ElasticsearchFileDescriptorSupplier() {}
  }

  private static final class ElasticsearchMethodDescriptorSupplier
      extends ElasticsearchBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ElasticsearchMethodDescriptorSupplier(String methodName) {
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
      synchronized (ElasticsearchGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElasticsearchFileDescriptorSupplier())
              .addMethod(getFindByHeatSystemMethod())
              .addMethod(getBucketMethod())
              .build();
        }
      }
    }
    return result;
  }
}
