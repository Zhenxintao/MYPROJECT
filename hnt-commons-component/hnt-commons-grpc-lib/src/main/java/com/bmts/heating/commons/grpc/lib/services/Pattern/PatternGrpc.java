package com.bmts.heating.commons.grpc.lib.services.Pattern;

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
    comments = "Source: pattern/pattern.proto")
public final class PatternGrpc {

  private PatternGrpc() {}

  public static final String SERVICE_NAME = "Pattern";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle,
      com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> getListFeaturesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListFeatures",
      requestType = com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle,
      com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> getListFeaturesMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle, com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> getListFeaturesMethod;
    if ((getListFeaturesMethod = PatternGrpc.getListFeaturesMethod) == null) {
      synchronized (PatternGrpc.class) {
        if ((getListFeaturesMethod = PatternGrpc.getListFeaturesMethod) == null) {
          PatternGrpc.getListFeaturesMethod = getListFeaturesMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle, com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "Pattern", "ListFeatures"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature.getDefaultInstance()))
                  .setSchemaDescriptor(new PatternMethodDescriptorSupplier("ListFeatures"))
                  .build();
          }
        }
     }
     return getListFeaturesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PatternStub newStub(io.grpc.Channel channel) {
    return new PatternStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PatternBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PatternBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PatternFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PatternFutureStub(channel);
  }

  /**
   */
  public static abstract class PatternImplBase implements io.grpc.BindableService {

    /**
     */
    public void listFeatures(com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> responseObserver) {
      asyncUnimplementedUnaryCall(getListFeaturesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getListFeaturesMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle,
                com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature>(
                  this, METHODID_LIST_FEATURES)))
          .build();
    }
  }

  /**
   */
  public static final class PatternStub extends io.grpc.stub.AbstractStub<PatternStub> {
    private PatternStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PatternStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PatternStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PatternStub(channel, callOptions);
    }

    /**
     */
    public void listFeatures(com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getListFeaturesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PatternBlockingStub extends io.grpc.stub.AbstractStub<PatternBlockingStub> {
    private PatternBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PatternBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PatternBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PatternBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature> listFeatures(
        com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle request) {
      return blockingServerStreamingCall(
          getChannel(), getListFeaturesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PatternFutureStub extends io.grpc.stub.AbstractStub<PatternFutureStub> {
    private PatternFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PatternFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PatternFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PatternFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_LIST_FEATURES = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PatternImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PatternImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_FEATURES:
          serviceImpl.listFeatures((com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Rectangle) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.Feature>) responseObserver);
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

  private static abstract class PatternBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PatternBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Pattern");
    }
  }

  private static final class PatternFileDescriptorSupplier
      extends PatternBaseDescriptorSupplier {
    PatternFileDescriptorSupplier() {}
  }

  private static final class PatternMethodDescriptorSupplier
      extends PatternBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PatternMethodDescriptorSupplier(String methodName) {
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
      synchronized (PatternGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PatternFileDescriptorSupplier())
              .addMethod(getListFeaturesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
