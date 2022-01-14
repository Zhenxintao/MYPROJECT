package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryEsDto extends PageDto {

    private long start;
    private long end;
    private Integer[] heatSystemId;
    private String[] includeFields;//包含列
    private String[] excludeFields;//排除列
    private HistoryDocument document; //访问数据类型 实时数据、小时整点、小时平均数据、天数据
    private HistorySourceType sourceType;//一次、二次、室温、能耗
    private Boolean includeTotal;//是否包含数据总条数
    private Integer index = 1;//查询方式 1

    public void routeFirst(){
        this.sourceType = HistorySourceType.FIRST;
    }

    public void routeSecond(){
        this.sourceType = HistorySourceType.SECOND;
    }

    public void routeIndoorTemp(){
        this.sourceType = HistorySourceType.INDOOR_TEMP;
    }
    public void routeEnergy(){
        this.sourceType = HistorySourceType.ENERGY_CONVERGE;
    }


    public void realData(){
        this.document = HistoryDocument.REAL_DATA;
    }

    public void hourAvg(){
        this.document = HistoryDocument.HOUR_AVG;
    }

    public void hour(){
        this.document = HistoryDocument.HOUR;
    }

    public void day(){
        this.document = HistoryDocument.DAY;
    }


}
