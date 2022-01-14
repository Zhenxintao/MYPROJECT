package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: DefaultRealHeadersList
 * @Description: 默认表头
 * @Author: pxf
 * @Date: 2020/12/11 15:55
 * @Version: 1.0
 */

@Data
@ApiModel("默认表头")
public class DefaultRealHeadersList {

    @ApiModelProperty("1.参数汇总 2.全网平衡  3.单站监测历史  4.生产调度历史")
    private Integer type;

    @ApiModelProperty("表头集合")
    private List<DefaultRealHeaders> listHeader;


}
