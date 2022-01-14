package com.bmts.heating.providers.es.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString
@Document(indexName = "energy",shards = 3)//索引信息,默认 replicas = 1
@TypeAlias("energy")//映射规则
public class EnergyBase {
	@Id
	private String id;

	@Field(type = FieldType.Long)
	private Long timeStrap;  //时间戳

	@Field(type = FieldType.Integer)
	private Integer heatCabinetId;  //控制柜Id

	@Field(type = FieldType.Integer)
	private Integer level;  //控制柜Id

	@Field(type = FieldType.Integer)
	private Integer heatingSystemId;  //所属系统Id

}
