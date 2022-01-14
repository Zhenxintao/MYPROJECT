package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("能耗聚合查询类")
@Data
public class QueryEsBucketDto {
	private List<TimeRange> timeRanges;
	private Integer[] heatSystemId;
	private String[] includeFields;//包含列
	private String[] excludeFields;//排除列
	private HistoryDocument document; //访问数据类型 实时数据、小时整点、小时平均数据、天数据
	private HistorySourceType sourceType;//一次、二次、室温、能耗
	private Integer index = 1;//是否根据systemId分组 1:分组 根据系统分组汇聚    2:不分组 获取所有系统累计成单条数据
}
