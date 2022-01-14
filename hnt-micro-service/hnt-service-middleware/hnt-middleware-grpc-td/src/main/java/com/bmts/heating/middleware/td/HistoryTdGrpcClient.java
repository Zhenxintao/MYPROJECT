package com.bmts.heating.middleware.td;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
//import com.bmts.heating.commons.container.performance.config.GrpcClientPool;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.ColumnsDto;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.InsertEquipmentInfoDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.SecondNetDataMinuteDto;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.QueryBaseHistoryResponse;
import com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalGrpc;
import com.bmts.heating.commons.grpc.lib.services.abnormal.AbnormalOuterClass;
import com.bmts.heating.commons.grpc.lib.services.common.Common;
import com.bmts.heating.commons.grpc.lib.services.table.TableOptGrpc;
import com.bmts.heating.commons.grpc.lib.services.table.TableService;
import com.bmts.heating.commons.grpc.lib.services.tdengine.*;
import com.google.gson.JsonArray;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@Astrict(servicename = "td", servicetype = Astrict.EnumService.grpc)
public class HistoryTdGrpcClient extends SavantServices {

//    @Autowired
//    private GrpcClientPool grpcClientPool;

    public Boolean insertEquipmentPointInfo(InsertEquipmentInfoDto dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        TableOptGrpc.TableOptBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = TableOptGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult mls = stub.buildSuper(this.insertTable(dto));
        serverChannel.shutdown();
        return mls.getIsOk();
    }

    public Boolean addColumn(ColumnsDto columnsDto,String tableName) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        TableOptGrpc.TableOptBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = TableOptGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult mls = stub.addColumn(this.insertNewColumn(columnsDto,tableName));
        serverChannel.shutdown();
        return mls.getIsOk();
    }

    private TableService.NewColumn insertNewColumn(ColumnsDto columnsDto, String tableName) {
        TableService.NewColumn.Builder newColumn = TableService.NewColumn.newBuilder();
        TableService.Columns.Builder column = TableService.Columns.newBuilder();
        column.setColumnName(columnsDto.getColumnName());
        this.columnType(columnsDto.getDataType(),column);
        newColumn.setColumn(column);
        newColumn.setTableName(tableName);
        return newColumn.build();
    }
    private TableService.TableColumn deleteTableColumn(ColumnsDto columnsDto,String tableName){
        TableService.TableColumn.Builder tableColumn = TableService.TableColumn.newBuilder();
        tableColumn.setColumnName(columnsDto.getColumnName());
        tableColumn.setTableName(tableName);
        return tableColumn.build();

    }
    public Boolean delColumn(ColumnsDto columnsDto,String tableName) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        TableOptGrpc.TableOptBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = TableOptGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult mls = stub.delColumn(this.deleteTableColumn(columnsDto,tableName));
        serverChannel.shutdown();
        return mls.getIsOk();
    }


    public Boolean insertHistoryMinuteToTd(List<InsertHistoryMinuteDto> dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        HistoryMinuteGrpc.HistoryMinuteBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = HistoryMinuteGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult mls = stub.insert(this.insertHistoryMinute(dto));
        serverChannel.shutdown();
        return mls.getIsOk();
    }

    public Boolean insertSecondHistoryMinuteToTd(List<SecondNetDataMinuteDto> dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        MetaGrpc.MetaBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = MetaGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult mls = stub.insert(this.insertSecondHistoryMinute(dto));
        serverChannel.shutdown();
        return mls.getIsOk();
    }


    public Boolean insertAbnormal(Abnormal dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        AbnormalGrpc.AbnormalBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = AbnormalGrpc.newBlockingStub(serverChannel);
            //.withMaxInboundMessageSize(Integer.MAX_VALUE);
            // server.withMaxOutboundMessageSize(Integer.MAX_VALUE);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        Common.BoolResult insert = stub.insert(this.abnormalInsert(dto));
        serverChannel.shutdown();
        return insert.getIsOk();
    }

    private AbnormalOuterClass.abnormal abnormalInsert(Abnormal dto) {
        AbnormalOuterClass.abnormal.Builder builder = AbnormalOuterClass.abnormal.newBuilder();
        if (dto.getGroupId() != null) {
            builder.setGroupid(dto.getGroupId());
        }
        if (dto.getLevel() != null) {
            builder.setLevel(dto.getLevel());
        }
        if (StringUtils.isNotBlank(dto.getMsg())) {
            builder.setMsg(dto.getMsg());
        }
        if (StringUtils.isNotBlank(dto.getPoint())) {
            builder.setPoint(dto.getPoint());
        }
        if (StringUtils.isNotBlank(dto.getValue())) {
            builder.setValue(dto.getValue());
        }
        builder.setTs(dto.getTs());
        if (dto.getType() != null) {
            builder.setType(dto.getType());
        }
        return builder.build();
    }


    private TableService.Table insertTable(InsertEquipmentInfoDto dto) {
        TableService.Table.Builder builder = TableService.Table.newBuilder();
        if (!dto.getEquipmentName().isEmpty()) {
            builder.setTableName(dto.getEquipmentName());
        }
        if (dto.getPoint().stream().count() > 0) {
            for (ColumnsDto c : dto.getPoint()) {
                TableService.Columns.Builder columnBuilder = TableService.Columns.newBuilder();
                columnBuilder.setColumnName(c.getColumnName());
                this.columnType(c.getDataType(), columnBuilder);
                builder.addPoint(columnBuilder);
            }
        }
        return builder.build();
    }

    private HistoryMinuteOuterClass.Body insertHistoryMinute(List<InsertHistoryMinuteDto> dto) {
        HistoryMinuteOuterClass.Body.Builder builder = HistoryMinuteOuterClass.Body.newBuilder();
        for (InsertHistoryMinuteDto info : dto) {
            HistoryMinuteOuterClass.Device.Builder deviceBuilder = HistoryMinuteOuterClass.Device.newBuilder();
            deviceBuilder.setDeviceCode(info.getDeviceCode());
            deviceBuilder.setGroupId(info.getGroupId());
            deviceBuilder.setLevel(info.getLevel());
            deviceBuilder.setTs(info.getTs());
            if (info.getId() != null) {
                deviceBuilder.setId(info.getId());
            }
            if (StringUtils.isNotBlank(info.getTableName())) {
                deviceBuilder.setTableName(info.getTableName());
            }
            for (PointInfo point : info.getPoints()) {
                HistoryMinuteOuterClass.PointInfo.Builder pointInfoBuilder = HistoryMinuteOuterClass.PointInfo.newBuilder();
                pointInfoBuilder.setPointName(point.getPointName());
                pointInfoBuilder.setPointValue(point.getValue());
                deviceBuilder.addPoints(pointInfoBuilder);
            }
            builder.addDevices(deviceBuilder);
        }
        return builder.build();
    }

    private MetaOuterClass.Request insertSecondHistoryMinute(List<SecondNetDataMinuteDto> dto) {
        MetaOuterClass.Request.Builder builder = MetaOuterClass.Request.newBuilder();
        for (SecondNetDataMinuteDto info : dto) {
            MetaOuterClass.TableInfo.Builder tableBuilder = MetaOuterClass.TableInfo.newBuilder();
            tableBuilder.setTs(info.getTs());
            if (StringUtils.isNotBlank(info.getStableName())) {
                tableBuilder.setStableName(info.getStableName());
            }
            if (StringUtils.isNotBlank(info.getTableName())) {
                tableBuilder.setTableName(info.getTableName());
            }
            for (PointInfo point : info.getPoints()) {
                MetaOuterClass.PointData.Builder pointInfoBuilder = MetaOuterClass.PointData.newBuilder();
                pointInfoBuilder.setPointName(point.getPointName());
                pointInfoBuilder.setPointValue(point.getValue());
                tableBuilder.addPoints(pointInfoBuilder);
            }
            for (PointInfo point : info.getTags()) {
                MetaOuterClass.PointData.Builder pointInfoBuilder = MetaOuterClass.PointData.newBuilder();
                pointInfoBuilder.setPointName(point.getPointName());
                pointInfoBuilder.setPointValue(point.getValue());
                tableBuilder.addTags(pointInfoBuilder);
            }
            builder.addDevices(tableBuilder);
        }
        return builder.build();
    }
    private void columnType(String dataType, TableService.Columns.Builder columnBuilder) {
        switch (dataType.toLowerCase()) {
            case "boolean":
                columnBuilder.setColumnType(TableService.ColumnType.BOOL);
                break;
            case "integer":
            case "uinteger":
            case "long":
            case "ulong":
                columnBuilder.setColumnType(TableService.ColumnType.INT);
                break;
            case "float":
                columnBuilder.setColumnType(TableService.ColumnType.FLOAT);
                break;
            case "double":
                columnBuilder.setColumnType(TableService.ColumnType.DOUBLE);
                break;
        }
    }

    //region 基础历史数据
    public QueryBaseHistoryResponse queryTdEngineData(QueryTdDto dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        QueryPointsGrpc.QueryPointsBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = QueryPointsGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        log.info("gRPC  function of queryTdEngineData ………… Starting post energy-Data");
        Iterator<QueryPointsOuterClass.Response> result = stub.queryOriginal(this.buildTdHistoryRequest(dto));
        List<QueryPointsOuterClass.Response> responseList = new ArrayList<>();
        JSONArray jsonElements = new JSONArray();
        while (result.hasNext()){
            QueryPointsOuterClass.Response response  = result.next();
            JSONObject jsonObject = JSON.parseObject(response.getJsonData());
            jsonElements.add(jsonObject);
            responseList.add(response);
        }
        QueryBaseHistoryResponse response = new QueryBaseHistoryResponse();
        response.setTotal(responseList.get(0).getTotal());
        response.setJsonData(jsonElements);
        Map map = new HashMap();
        map.put("managedChannel",serverChannel);
        response.setManagedChannel(map);
        log.info("gRPC  function of queryTdEngineData ………… Ending");
        return response;
    }

    /**
     * TD历史查询汇聚信息
     */
    public QueryPointsOuterClass.Response queryTdAggregate(QueryAggregateTdDto dto) {
        String serverName = "td";
        ConnectionToken cd = null;
        ManagedChannel serverChannel = null;
        QueryAggregateGrpc.QueryAggregateBlockingStub stub = null;
        try {
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = QueryAggregateGrpc.newBlockingStub(serverChannel);
        } catch (MicroException e) {
            e.printStackTrace();
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        log.info("gRPC  function of queryTdAggregate ………… Starting post energy-Data");
        QueryPointsOuterClass.Response result = stub.query(this.buildTdAggregateRequest(dto));
        log.info("gRPC  function of queryTdAggregate ………… Ending");
        serverChannel.shutdown();
        return result;
    }

    private QueryPointsOuterClass.QueryList buildTdHistoryRequest(QueryTdDto dto) {
        QueryPointsOuterClass.QueryList.Builder builder = QueryPointsOuterClass.QueryList.newBuilder();
        if (dto.getStartTime() <= dto.getEndTime()) {
            builder.setStartTime(dto.getStartTime())
                    .setEndTime(dto.getEndTime());
        }
        if (dto.getLevel() != null && dto.getLevel() != 0) {
            builder.setLevel(dto.getLevel());
        }
        if (dto.getLimit() != null) {
            builder.setLimit(dto.getLimit());
        }
        if (dto.getOffset() != null) {
            builder.setOffset(dto.getOffset());
        }
        if (dto.getPoints() != null && dto.getPoints().stream().count() > 0) {
            builder.addAllPoints(dto.getPoints());
        }
        if (dto.getGroupId() != null && dto.getGroupId().stream().count() > 0) {
            builder.addAllGroupid(dto.getGroupId());
        } else {
            log.error("无系统Id", dto.getGroupId().toString());
        }
        if (dto.getOriginalTd() != null) {
            tdEnumClientOriginalTd(dto.getOriginalTd(), builder);
        }
        tdEnumClientQueryTypeTd(dto.getQueryTypeTd(), builder);
        if (StringUtils.isNotBlank(dto.getTableName())) {
            builder.setTableName(dto.getTableName());
        }
        if (StringUtils.isNotBlank(dto.getOrder())) {
            builder.setOrder(dto.getOrder());
        }
        if (dto.getAbnormalType() != null) {
            builder.setAbnormalType(dto.getAbnormalType());
        }
        return builder.build();
    }

    private QueryAggregateOuterClass.aggregateQuery buildTdAggregateRequest(QueryAggregateTdDto dto) {
        QueryAggregateOuterClass.aggregateQuery.Builder builder = QueryAggregateOuterClass.aggregateQuery.newBuilder();
        if (dto.getStartTime() <= dto.getEndTime()) {
            builder.setStartTime(dto.getStartTime())
                    .setEndTime(dto.getEndTime());
        }
        if (dto.getLimit() != null) {
            builder.setLimit(dto.getLimit());
        }
        if (dto.getOffset() != null) {
            builder.setOffset(dto.getOffset());
        }
        if (dto.getPoints() != null && dto.getPoints().stream().count() > 0) {
            for (AggregatePoint aggregatePoint : dto.getPoints()) {
                QueryAggregateOuterClass.aggregatePoint.Builder pointBuilder = QueryAggregateOuterClass.aggregatePoint.newBuilder();
                pointBuilder.setPointName(aggregatePoint.getPointName());
                tdEnumClientAggregateType(aggregatePoint.getAggregateType(), pointBuilder);
                builder.addPoints(pointBuilder);
            }
        }
        if (dto.getGroupId() != null && dto.getGroupId().stream().count() > 0) {
            builder.addAllGroupid(dto.getGroupId());
        }
        if(dto.getOriginalTd()!=null) {
            tdEnumClientOriginalAggType(dto.getOriginalTd(), builder);
        }
        if (dto.getGroupType()!=null) {
            tdEnumClientAggregateGroupType(dto.getGroupType(), builder);
        }
        if (StringUtils.isNotBlank(dto.getTableName())) {
            builder.setTableName(dto.getTableName());
        }
        if (StringUtils.isNotBlank(dto.getOrder())) {
            builder.setOrder(dto.getOrder());
        }
        return builder.build();
    }

    private void tdEnumClientOriginalTd(OriginalTd originalTd, QueryPointsOuterClass.QueryList.Builder builder) {
        switch (originalTd) {
            case minute:
                builder.setOriginal(QueryPointsOuterClass.Original.minute);
                break;
            case hour:
                builder.setOriginal(QueryPointsOuterClass.Original.Hour);
                break;
            case day:
                builder.setOriginal(QueryPointsOuterClass.Original.Day);
                break;
        }
    }

    private void tdEnumClientQueryTypeTd(QueryTypeTd queryTypeTd, QueryPointsOuterClass.QueryList.Builder builder) {
        switch (queryTypeTd) {
            case history:
                builder.setQueryType(QueryPointsOuterClass.QueryType.history);
                break;
            case energy:
                builder.setQueryType(QueryPointsOuterClass.QueryType.energy);
                break;
            case abnormal:
                builder.setQueryType(QueryPointsOuterClass.QueryType.abnormal_query);
                break;
        }
    }

    private void tdEnumClientOriginalAggType(OriginalTd originalTd, QueryAggregateOuterClass.aggregateQuery.Builder aggregateBuilder) {
        switch (originalTd) {
            case minute:
                aggregateBuilder.setOriginal(QueryPointsOuterClass.Original.minute);
                break;
            case hour:
                aggregateBuilder.setOriginal(QueryPointsOuterClass.Original.Hour);
                break;
            case day:
                aggregateBuilder.setOriginal(QueryPointsOuterClass.Original.Day);
                break;
        }
    }

    private void tdEnumClientAggregateGroupType(AggregateGroupType aggregateGroupType, QueryAggregateOuterClass.aggregateQuery.Builder builder) {
        switch (aggregateGroupType) {
            case single:
                builder.setGroupType(QueryAggregateOuterClass.GroupType.singal);
                break;
            case group:
                builder.setGroupType(QueryAggregateOuterClass.GroupType.group);
                break;
        }
    }

    private void tdEnumClientAggregateType(AggregateType aggregateType, QueryAggregateOuterClass.aggregatePoint.Builder builder) {
        switch (aggregateType) {
            case avg:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.avg);
                break;
            case sum:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.sum);
                break;
            case max:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.max);
                break;
            case min:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.min);
                break;
            case TWA:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.TWA);
                break;
            case STDDEV:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.STDDEV);
                break;
            case diff:
                builder.setAggregateType(QueryAggregateOuterClass.aggregateType.diff);
                break;
        }
    }

}
