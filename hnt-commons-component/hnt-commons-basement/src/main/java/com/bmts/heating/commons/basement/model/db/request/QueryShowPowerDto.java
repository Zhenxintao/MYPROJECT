package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 吉林大屏展示--热源或换热站实时数据接口请求参数值
 * */
@Data
public class QueryShowPowerDto {
  @ApiModelProperty("采集点信息")
  //采集点信息
  private List<String> tagNames;
  @ApiModelProperty("请求数据分类，-1为全部换热站，-2为全部热源,-3为全部热源及换热站,1为换热站,2为热源")
  //参数-1为全部换热站，-2为全部热源,-3为全部热源及换热站,1为换热站,2为热源。
  private Integer type;
  @ApiModelProperty("查询具体换热站或热源信息（type为1或2时传递有效）")
  //查询具体换热站或热源信息（type为1或2时传递有效）
  private List<Integer> relevanceIds;
}
