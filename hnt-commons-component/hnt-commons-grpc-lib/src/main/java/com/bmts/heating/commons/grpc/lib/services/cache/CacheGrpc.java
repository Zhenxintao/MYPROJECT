package com.bmts.heating.commons.grpc.lib.services.cache;

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
    comments = "Source: cache/cache.proto")
public final class CacheGrpc {

  private CacheGrpc() {}

  public static final String SERVICE_NAME = "Cache";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryAllPointMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryAllPoint",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryAllPointMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryAllPointMethod;
    if ((getQueryAllPointMethod = CacheGrpc.getQueryAllPointMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryAllPointMethod = CacheGrpc.getQueryAllPointMethod) == null) {
          CacheGrpc.getQueryAllPointMethod = getQueryAllPointMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryAllPoint"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryAllPoint"))
                  .build();
          }
        }
     }
     return getQueryAllPointMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQueryFirstNetBaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryFirstNetBase",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQueryFirstNetBaseMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQueryFirstNetBaseMethod;
    if ((getQueryFirstNetBaseMethod = CacheGrpc.getQueryFirstNetBaseMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryFirstNetBaseMethod = CacheGrpc.getQueryFirstNetBaseMethod) == null) {
          CacheGrpc.getQueryFirstNetBaseMethod = getQueryFirstNetBaseMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryFirstNetBase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryFirstNetBase"))
                  .build();
          }
        }
     }
     return getQueryFirstNetBaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQuerySourceFirstNetBaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "querySourceFirstNetBase",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQuerySourceFirstNetBaseMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> getQuerySourceFirstNetBaseMethod;
    if ((getQuerySourceFirstNetBaseMethod = CacheGrpc.getQuerySourceFirstNetBaseMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQuerySourceFirstNetBaseMethod = CacheGrpc.getQuerySourceFirstNetBaseMethod) == null) {
          CacheGrpc.getQuerySourceFirstNetBaseMethod = getQuerySourceFirstNetBaseMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "querySourceFirstNetBase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("querySourceFirstNetBase"))
                  .build();
          }
        }
     }
     return getQuerySourceFirstNetBaseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryPointsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryPoints",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryPointsMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryPointsMethod;
    if ((getQueryPointsMethod = CacheGrpc.getQueryPointsMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryPointsMethod = CacheGrpc.getQueryPointsMethod) == null) {
          CacheGrpc.getQueryPointsMethod = getQueryPointsMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryPoints"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryPoints"))
                  .build();
          }
        }
     }
     return getQueryPointsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryComputePointsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryComputePoints",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryComputePointsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQueryComputePointsMethod;
    if ((getQueryComputePointsMethod = CacheGrpc.getQueryComputePointsMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryComputePointsMethod = CacheGrpc.getQueryComputePointsMethod) == null) {
          CacheGrpc.getQueryComputePointsMethod = getQueryComputePointsMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryComputePoints"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryComputePoints"))
                  .build();
          }
        }
     }
     return getQueryComputePointsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> getQueryRealDataBySystemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryRealDataBySystem",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> getQueryRealDataBySystemMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> getQueryRealDataBySystemMethod;
    if ((getQueryRealDataBySystemMethod = CacheGrpc.getQueryRealDataBySystemMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryRealDataBySystemMethod = CacheGrpc.getQueryRealDataBySystemMethod) == null) {
          CacheGrpc.getQueryRealDataBySystemMethod = getQueryRealDataBySystemMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryRealDataBySystem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryRealDataBySystem"))
                  .build();
          }
        }
     }
     return getQueryRealDataBySystemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankIntervalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryRankInterval",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankIntervalMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankIntervalMethod;
    if ((getQueryRankIntervalMethod = CacheGrpc.getQueryRankIntervalMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryRankIntervalMethod = CacheGrpc.getQueryRankIntervalMethod) == null) {
          CacheGrpc.getQueryRankIntervalMethod = getQueryRankIntervalMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryRankInterval"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryRankInterval"))
                  .build();
          }
        }
     }
     return getQueryRankIntervalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryRank",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> getQueryRankMethod;
    if ((getQueryRankMethod = CacheGrpc.getQueryRankMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryRankMethod = CacheGrpc.getQueryRankMethod) == null) {
          CacheGrpc.getQueryRankMethod = getQueryRankMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryRank"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryRank"))
                  .build();
          }
        }
     }
     return getQueryRankMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> getQueryPointsByIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "queryPointsByIds",
      requestType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto,
      com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> getQueryPointsByIdsMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> getQueryPointsByIdsMethod;
    if ((getQueryPointsByIdsMethod = CacheGrpc.getQueryPointsByIdsMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQueryPointsByIdsMethod = CacheGrpc.getQueryPointsByIdsMethod) == null) {
          CacheGrpc.getQueryPointsByIdsMethod = getQueryPointsByIdsMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto, com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "queryPointsByIds"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("queryPointsByIds"))
                  .build();
          }
        }
     }
     return getQueryPointsByIdsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQuerySourcePointsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "querySourcePoints",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQuerySourcePointsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> getQuerySourcePointsMethod;
    if ((getQuerySourcePointsMethod = CacheGrpc.getQuerySourcePointsMethod) == null) {
      synchronized (CacheGrpc.class) {
        if ((getQuerySourcePointsMethod = CacheGrpc.getQuerySourcePointsMethod) == null) {
          CacheGrpc.getQuerySourcePointsMethod = getQuerySourcePointsMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "Cache", "querySourcePoints"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList.getDefaultInstance()))
                  .setSchemaDescriptor(new CacheMethodDescriptorSupplier("querySourcePoints"))
                  .build();
          }
        }
     }
     return getQuerySourcePointsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CacheStub newStub(io.grpc.Channel channel) {
    return new CacheStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CacheBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CacheBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CacheFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CacheFutureStub(channel);
  }

  /**
   */
  public static abstract class CacheImplBase implements io.grpc.BindableService {

    /**
     */
    public void queryAllPoint(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryAllPointMethod(), responseObserver);
    }

    /**
     */
    public void queryFirstNetBase(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryFirstNetBaseMethod(), responseObserver);
    }

    /**
     */
    public void querySourceFirstNetBase(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> responseObserver) {
      asyncUnimplementedUnaryCall(getQuerySourceFirstNetBaseMethod(), responseObserver);
    }

    /**
     */
    public void queryPoints(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryPointsMethod(), responseObserver);
    }

    /**
     */
    public void queryComputePoints(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryComputePointsMethod(), responseObserver);
    }

    /**
     */
    public void queryRealDataBySystem(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryRealDataBySystemMethod(), responseObserver);
    }

    /**
     */
    public void queryRankInterval(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryRankIntervalMethod(), responseObserver);
    }

    /**
     */
    public void queryRank(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryRankMethod(), responseObserver);
    }

    /**
     */
    public void queryPointsByIds(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> responseObserver) {
      asyncUnimplementedUnaryCall(getQueryPointsByIdsMethod(), responseObserver);
    }

    /**
     */
    public void querySourcePoints(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnimplementedUnaryCall(getQuerySourcePointsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getQueryAllPointMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>(
                  this, METHODID_QUERY_ALL_POINT)))
          .addMethod(
            getQueryFirstNetBaseMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>(
                  this, METHODID_QUERY_FIRST_NET_BASE)))
          .addMethod(
            getQuerySourceFirstNetBaseMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>(
                  this, METHODID_QUERY_SOURCE_FIRST_NET_BASE)))
          .addMethod(
            getQueryPointsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>(
                  this, METHODID_QUERY_POINTS)))
          .addMethod(
            getQueryComputePointsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>(
                  this, METHODID_QUERY_COMPUTE_POINTS)))
          .addMethod(
            getQueryRealDataBySystemMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList>(
                  this, METHODID_QUERY_REAL_DATA_BY_SYSTEM)))
          .addMethod(
            getQueryRankIntervalMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>(
                  this, METHODID_QUERY_RANK_INTERVAL)))
          .addMethod(
            getQueryRankMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam,
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>(
                  this, METHODID_QUERY_RANK)))
          .addMethod(
            getQueryPointsByIdsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto,
                com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults>(
                  this, METHODID_QUERY_POINTS_BY_IDS)))
          .addMethod(
            getQuerySourcePointsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>(
                  this, METHODID_QUERY_SOURCE_POINTS)))
          .build();
    }
  }

  /**
   */
  public static final class CacheStub extends io.grpc.stub.AbstractStub<CacheStub> {
    private CacheStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheStub(channel, callOptions);
    }

    /**
     */
    public void queryAllPoint(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryAllPointMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryFirstNetBase(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryFirstNetBaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void querySourceFirstNetBase(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQuerySourceFirstNetBaseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryPoints(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getQueryPointsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryComputePoints(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryComputePointsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryRealDataBySystem(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryRealDataBySystemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryRankInterval(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryRankIntervalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryRank(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryRankMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void queryPointsByIds(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getQueryPointsByIdsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void querySourcePoints(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getQuerySourcePointsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CacheBlockingStub extends io.grpc.stub.AbstractStub<CacheBlockingStub> {
    private CacheBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList queryAllPoint(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam request) {
      return blockingUnaryCall(
          getChannel(), getQueryAllPointMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList queryFirstNetBase(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), getQueryFirstNetBaseMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList querySourceFirstNetBase(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), getQuerySourceFirstNetBaseMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> queryPoints(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam request) {
      return blockingServerStreamingCall(
          getChannel(), getQueryPointsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList queryComputePoints(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), getQueryComputePointsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList queryRealDataBySystem(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam request) {
      return blockingUnaryCall(
          getChannel(), getQueryRealDataBySystemMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList queryRankInterval(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request) {
      return blockingUnaryCall(
          getChannel(), getQueryRankIntervalMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList queryRank(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request) {
      return blockingUnaryCall(
          getChannel(), getQueryRankMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults queryPointsByIds(com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto request) {
      return blockingUnaryCall(
          getChannel(), getQueryPointsByIdsMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> querySourcePoints(
        com.google.protobuf.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), getQuerySourcePointsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CacheFutureStub extends io.grpc.stub.AbstractStub<CacheFutureStub> {
    private CacheFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CacheFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CacheFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CacheFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> queryAllPoint(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryAllPointMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> queryFirstNetBase(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryFirstNetBaseMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList> querySourceFirstNetBase(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getQuerySourceFirstNetBaseMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList> queryComputePoints(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryComputePointsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList> queryRealDataBySystem(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryRealDataBySystemMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> queryRankInterval(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryRankIntervalMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList> queryRank(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryRankMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults> queryPointsByIds(
        com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto request) {
      return futureUnaryCall(
          getChannel().newCall(getQueryPointsByIdsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_QUERY_ALL_POINT = 0;
  private static final int METHODID_QUERY_FIRST_NET_BASE = 1;
  private static final int METHODID_QUERY_SOURCE_FIRST_NET_BASE = 2;
  private static final int METHODID_QUERY_POINTS = 3;
  private static final int METHODID_QUERY_COMPUTE_POINTS = 4;
  private static final int METHODID_QUERY_REAL_DATA_BY_SYSTEM = 5;
  private static final int METHODID_QUERY_RANK_INTERVAL = 6;
  private static final int METHODID_QUERY_RANK = 7;
  private static final int METHODID_QUERY_POINTS_BY_IDS = 8;
  private static final int METHODID_QUERY_SOURCE_POINTS = 9;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CacheImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CacheImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_QUERY_ALL_POINT:
          serviceImpl.queryAllPoint((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsParam) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>) responseObserver);
          break;
        case METHODID_QUERY_FIRST_NET_BASE:
          serviceImpl.queryFirstNetBase((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>) responseObserver);
          break;
        case METHODID_QUERY_SOURCE_FIRST_NET_BASE:
          serviceImpl.querySourceFirstNetBase((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.FirstNetBaseList>) responseObserver);
          break;
        case METHODID_QUERY_POINTS:
          serviceImpl.queryPoints((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsParam) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>) responseObserver);
          break;
        case METHODID_QUERY_COMPUTE_POINTS:
          serviceImpl.queryComputePoints((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>) responseObserver);
          break;
        case METHODID_QUERY_REAL_DATA_BY_SYSTEM:
          serviceImpl.queryRealDataBySystem((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointParam) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointCacheList>) responseObserver);
          break;
        case METHODID_QUERY_RANK_INTERVAL:
          serviceImpl.queryRankInterval((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>) responseObserver);
          break;
        case METHODID_QUERY_RANK:
          serviceImpl.queryRank((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankIntervalParam) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.RankList>) responseObserver);
          break;
        case METHODID_QUERY_POINTS_BY_IDS:
          serviceImpl.queryPointsByIds((com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.queryPointsByIdsDto) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.PointsByIdsResults>) responseObserver);
          break;
        case METHODID_QUERY_SOURCE_POINTS:
          serviceImpl.querySourcePoints((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass.PointList>) responseObserver);
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

  private static abstract class CacheBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CacheBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Cache");
    }
  }

  private static final class CacheFileDescriptorSupplier
      extends CacheBaseDescriptorSupplier {
    CacheFileDescriptorSupplier() {}
  }

  private static final class CacheMethodDescriptorSupplier
      extends CacheBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CacheMethodDescriptorSupplier(String methodName) {
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
      synchronized (CacheGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CacheFileDescriptorSupplier())
              .addMethod(getQueryAllPointMethod())
              .addMethod(getQueryFirstNetBaseMethod())
              .addMethod(getQuerySourceFirstNetBaseMethod())
              .addMethod(getQueryPointsMethod())
              .addMethod(getQueryComputePointsMethod())
              .addMethod(getQueryRealDataBySystemMethod())
              .addMethod(getQueryRankIntervalMethod())
              .addMethod(getQueryRankMethod())
              .addMethod(getQueryPointsByIdsMethod())
              .addMethod(getQuerySourcePointsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
