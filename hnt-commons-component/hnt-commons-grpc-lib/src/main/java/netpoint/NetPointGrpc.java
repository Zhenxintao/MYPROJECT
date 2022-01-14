package netpoint;

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
 * <pre>
 * The greeting service definition.}
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.1)",
    comments = "Source: net/netPoint.proto")
public final class NetPointGrpc {

  private NetPointGrpc() {}

  public static final String SERVICE_NAME = "netpoint.NetPoint";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<netpoint.NetPointOuterClass.NetPointL,
      netpoint.NetPointOuterClass.Result> getPushMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Push",
      requestType = netpoint.NetPointOuterClass.NetPointL.class,
      responseType = netpoint.NetPointOuterClass.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<netpoint.NetPointOuterClass.NetPointL,
      netpoint.NetPointOuterClass.Result> getPushMethod() {
    io.grpc.MethodDescriptor<netpoint.NetPointOuterClass.NetPointL, netpoint.NetPointOuterClass.Result> getPushMethod;
    if ((getPushMethod = NetPointGrpc.getPushMethod) == null) {
      synchronized (NetPointGrpc.class) {
        if ((getPushMethod = NetPointGrpc.getPushMethod) == null) {
          NetPointGrpc.getPushMethod = getPushMethod = 
              io.grpc.MethodDescriptor.<netpoint.NetPointOuterClass.NetPointL, netpoint.NetPointOuterClass.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "netpoint.NetPoint", "Push"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  netpoint.NetPointOuterClass.NetPointL.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  netpoint.NetPointOuterClass.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new NetPointMethodDescriptorSupplier("Push"))
                  .build();
          }
        }
     }
     return getPushMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NetPointStub newStub(io.grpc.Channel channel) {
    return new NetPointStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NetPointBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NetPointBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NetPointFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NetPointFutureStub(channel);
  }

  /**
   * <pre>
   * The greeting service definition.}
   * </pre>
   */
  public static abstract class NetPointImplBase implements io.grpc.BindableService {

    /**
     */
    public void push(netpoint.NetPointOuterClass.NetPointL request,
        io.grpc.stub.StreamObserver<netpoint.NetPointOuterClass.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getPushMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPushMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                netpoint.NetPointOuterClass.NetPointL,
                netpoint.NetPointOuterClass.Result>(
                  this, METHODID_PUSH)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.}
   * </pre>
   */
  public static final class NetPointStub extends io.grpc.stub.AbstractStub<NetPointStub> {
    private NetPointStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NetPointStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetPointStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NetPointStub(channel, callOptions);
    }

    /**
     */
    public void push(netpoint.NetPointOuterClass.NetPointL request,
        io.grpc.stub.StreamObserver<netpoint.NetPointOuterClass.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPushMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.}
   * </pre>
   */
  public static final class NetPointBlockingStub extends io.grpc.stub.AbstractStub<NetPointBlockingStub> {
    private NetPointBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NetPointBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetPointBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NetPointBlockingStub(channel, callOptions);
    }

    /**
     */
    public netpoint.NetPointOuterClass.Result push(netpoint.NetPointOuterClass.NetPointL request) {
      return blockingUnaryCall(
          getChannel(), getPushMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.}
   * </pre>
   */
  public static final class NetPointFutureStub extends io.grpc.stub.AbstractStub<NetPointFutureStub> {
    private NetPointFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NetPointFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetPointFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NetPointFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<netpoint.NetPointOuterClass.Result> push(
        netpoint.NetPointOuterClass.NetPointL request) {
      return futureUnaryCall(
          getChannel().newCall(getPushMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PUSH = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NetPointImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NetPointImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUSH:
          serviceImpl.push((netpoint.NetPointOuterClass.NetPointL) request,
              (io.grpc.stub.StreamObserver<netpoint.NetPointOuterClass.Result>) responseObserver);
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

  private static abstract class NetPointBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NetPointBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return netpoint.NetPointOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NetPoint");
    }
  }

  private static final class NetPointFileDescriptorSupplier
      extends NetPointBaseDescriptorSupplier {
    NetPointFileDescriptorSupplier() {}
  }

  private static final class NetPointMethodDescriptorSupplier
      extends NetPointBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NetPointMethodDescriptorSupplier(String methodName) {
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
      synchronized (NetPointGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NetPointFileDescriptorSupplier())
              .addMethod(getPushMethod())
              .build();
        }
      }
    }
    return result;
  }
}
