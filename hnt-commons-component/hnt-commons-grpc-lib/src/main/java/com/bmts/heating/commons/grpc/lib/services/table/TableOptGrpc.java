package com.bmts.heating.commons.grpc.lib.services.table;

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
    comments = "Source: table/tableService.proto")
public final class TableOptGrpc {

  private TableOptGrpc() {}

  public static final String SERVICE_NAME = "TableOpt";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getBuildSuperMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "buildSuper",
      requestType = com.bmts.heating.commons.grpc.lib.services.table.TableService.Table.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getBuildSuperMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getBuildSuperMethod;
    if ((getBuildSuperMethod = TableOptGrpc.getBuildSuperMethod) == null) {
      synchronized (TableOptGrpc.class) {
        if ((getBuildSuperMethod = TableOptGrpc.getBuildSuperMethod) == null) {
          TableOptGrpc.getBuildSuperMethod = getBuildSuperMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TableOpt", "buildSuper"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.table.TableService.Table.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new TableOptMethodDescriptorSupplier("buildSuper"))
                  .build();
          }
        }
     }
     return getBuildSuperMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getAddColumnMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "addColumn",
      requestType = com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getAddColumnMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getAddColumnMethod;
    if ((getAddColumnMethod = TableOptGrpc.getAddColumnMethod) == null) {
      synchronized (TableOptGrpc.class) {
        if ((getAddColumnMethod = TableOptGrpc.getAddColumnMethod) == null) {
          TableOptGrpc.getAddColumnMethod = getAddColumnMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TableOpt", "addColumn"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new TableOptMethodDescriptorSupplier("addColumn"))
                  .build();
          }
        }
     }
     return getAddColumnMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelColumnMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "delColumn",
      requestType = com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelColumnMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelColumnMethod;
    if ((getDelColumnMethod = TableOptGrpc.getDelColumnMethod) == null) {
      synchronized (TableOptGrpc.class) {
        if ((getDelColumnMethod = TableOptGrpc.getDelColumnMethod) == null) {
          TableOptGrpc.getDelColumnMethod = getDelColumnMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TableOpt", "delColumn"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new TableOptMethodDescriptorSupplier("delColumn"))
                  .build();
          }
        }
     }
     return getDelColumnMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "delTable",
      requestType = com.bmts.heating.commons.grpc.lib.services.table.TableService.Table.class,
      responseType = com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
      com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelTableMethod() {
    io.grpc.MethodDescriptor<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> getDelTableMethod;
    if ((getDelTableMethod = TableOptGrpc.getDelTableMethod) == null) {
      synchronized (TableOptGrpc.class) {
        if ((getDelTableMethod = TableOptGrpc.getDelTableMethod) == null) {
          TableOptGrpc.getDelTableMethod = getDelTableMethod = 
              io.grpc.MethodDescriptor.<com.bmts.heating.commons.grpc.lib.services.table.TableService.Table, com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "TableOpt", "delTable"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.table.TableService.Table.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult.getDefaultInstance()))
                  .setSchemaDescriptor(new TableOptMethodDescriptorSupplier("delTable"))
                  .build();
          }
        }
     }
     return getDelTableMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TableOptStub newStub(io.grpc.Channel channel) {
    return new TableOptStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TableOptBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TableOptBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TableOptFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TableOptFutureStub(channel);
  }

  /**
   */
  public static abstract class TableOptImplBase implements io.grpc.BindableService {

    /**
     */
    public void buildSuper(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getBuildSuperMethod(), responseObserver);
    }

    /**
     */
    public void addColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getAddColumnMethod(), responseObserver);
    }

    /**
     */
    public void delColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getDelColumnMethod(), responseObserver);
    }

    /**
     */
    public void delTable(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnimplementedUnaryCall(getDelTableMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getBuildSuperMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_BUILD_SUPER)))
          .addMethod(
            getAddColumnMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_ADD_COLUMN)))
          .addMethod(
            getDelColumnMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_DEL_COLUMN)))
          .addMethod(
            getDelTableMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bmts.heating.commons.grpc.lib.services.table.TableService.Table,
                com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>(
                  this, METHODID_DEL_TABLE)))
          .build();
    }
  }

  /**
   */
  public static final class TableOptStub extends io.grpc.stub.AbstractStub<TableOptStub> {
    private TableOptStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TableOptStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TableOptStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TableOptStub(channel, callOptions);
    }

    /**
     */
    public void buildSuper(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBuildSuperMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddColumnMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDelColumnMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delTable(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request,
        io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDelTableMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TableOptBlockingStub extends io.grpc.stub.AbstractStub<TableOptBlockingStub> {
    private TableOptBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TableOptBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TableOptBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TableOptBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult buildSuper(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request) {
      return blockingUnaryCall(
          getChannel(), getBuildSuperMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult addColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn request) {
      return blockingUnaryCall(
          getChannel(), getAddColumnMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult delColumn(com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn request) {
      return blockingUnaryCall(
          getChannel(), getDelColumnMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult delTable(com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request) {
      return blockingUnaryCall(
          getChannel(), getDelTableMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TableOptFutureStub extends io.grpc.stub.AbstractStub<TableOptFutureStub> {
    private TableOptFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TableOptFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TableOptFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TableOptFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> buildSuper(
        com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request) {
      return futureUnaryCall(
          getChannel().newCall(getBuildSuperMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> addColumn(
        com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn request) {
      return futureUnaryCall(
          getChannel().newCall(getAddColumnMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> delColumn(
        com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn request) {
      return futureUnaryCall(
          getChannel().newCall(getDelColumnMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult> delTable(
        com.bmts.heating.commons.grpc.lib.services.table.TableService.Table request) {
      return futureUnaryCall(
          getChannel().newCall(getDelTableMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_BUILD_SUPER = 0;
  private static final int METHODID_ADD_COLUMN = 1;
  private static final int METHODID_DEL_COLUMN = 2;
  private static final int METHODID_DEL_TABLE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TableOptImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TableOptImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BUILD_SUPER:
          serviceImpl.buildSuper((com.bmts.heating.commons.grpc.lib.services.table.TableService.Table) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>) responseObserver);
          break;
        case METHODID_ADD_COLUMN:
          serviceImpl.addColumn((com.bmts.heating.commons.grpc.lib.services.table.TableService.NewColumn) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>) responseObserver);
          break;
        case METHODID_DEL_COLUMN:
          serviceImpl.delColumn((com.bmts.heating.commons.grpc.lib.services.table.TableService.TableColumn) request,
              (io.grpc.stub.StreamObserver<com.bmts.heating.commons.grpc.lib.services.common.Common.BoolResult>) responseObserver);
          break;
        case METHODID_DEL_TABLE:
          serviceImpl.delTable((com.bmts.heating.commons.grpc.lib.services.table.TableService.Table) request,
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

  private static abstract class TableOptBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TableOptBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bmts.heating.commons.grpc.lib.services.table.TableService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TableOpt");
    }
  }

  private static final class TableOptFileDescriptorSupplier
      extends TableOptBaseDescriptorSupplier {
    TableOptFileDescriptorSupplier() {}
  }

  private static final class TableOptMethodDescriptorSupplier
      extends TableOptBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TableOptMethodDescriptorSupplier(String methodName) {
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
      synchronized (TableOptGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TableOptFileDescriptorSupplier())
              .addMethod(getBuildSuperMethod())
              .addMethod(getAddColumnMethod())
              .addMethod(getDelColumnMethod())
              .addMethod(getDelTableMethod())
              .build();
        }
      }
    }
    return result;
  }
}
