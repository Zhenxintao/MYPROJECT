package com.bmts.heating.commons.entiy.balance.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetPvssNetBalanceDto {
    /**
     * 请求PVSS热网Id参数
     * */
    @JsonProperty("HeatNetID")
    private String HeatNetID;
}
