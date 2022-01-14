package com.bmts.heating.commons.history.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@ToString
@Description("存储点表初始化索引")
@Document(indexName = "#{ T(com.bmts.heating.commons.history.util.EsIndexEntry).getSuffix() }",shards = 3,createIndex = false)//索引信息,默认 replicas = 1
@TypeAlias("EsEnergy")//映射规则
public class EsEnergyData implements Serializable {
	@Id
	private String id;

	@Field(type = FieldType.Long)
	private Long timeStrap;  //时间戳

	@Field(type = FieldType.Integer)
	private Integer level;  //层级

	@Field(type = FieldType.Integer)
	private Integer heatingSystemId;  //所属系统Id
}
