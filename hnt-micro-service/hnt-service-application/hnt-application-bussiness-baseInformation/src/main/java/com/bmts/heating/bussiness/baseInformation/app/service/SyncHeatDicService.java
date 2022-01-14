package com.bmts.heating.bussiness.baseInformation.app.service;

import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.db.service.DicService;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.SyncHeatDicDetailDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.SyncHeatDicDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SynHeatDicService
 * @Description: 字典同步处理
 * @Author: zxt
 * @Date: 2021/7/12
 * @Version: 1.0
 */
@Service
public class SyncHeatDicService {
    @Autowired
    private DicService dicService;

    public Response saveHeatDic(List<SyncHeatDicDto> dto) {
        try{
        List<Dic> dicList = new ArrayList<>();
        for (SyncHeatDicDto syncHeatDicDto : dto) {
            Dic dic = new Dic();
            dic.setName(syncHeatDicDto.getMessage());
            dic.setCode(syncHeatDicDto.getCode());
            dic.setIsEnabled(true);
            dic.setPid(0);
            dic.setLevel(1);
            dic.setCreateUser("Pvss同步父级字典信息");
            dic.setCreateTime(LocalDateTime.now());
            dic.setDeleteFlag(false);
            dic.setSort(1);
            dicList.add(dic);
        }
        //添加父级数据库
        Boolean dicResult = dicService.saveBatch(dicList);
        if (dicResult) {
            List<Dic> detailDicList = new ArrayList<>();
            for (Dic dic : dicList) {
                SyncHeatDicDto syncHeatDicDto = dto.stream().filter(s -> StringUtils.equals(s.getCode(), dic.getCode())).findFirst().orElse(null);
                if (syncHeatDicDto.getData().stream().count() > 0) {
                    for (SyncHeatDicDetailDto syncHeatDicDetailDto : syncHeatDicDto.getData()) {
                        Dic detailDic = new Dic();
                        detailDic.setName(syncHeatDicDetailDto.getDetailName());
                        detailDic.setCode(syncHeatDicDetailDto.getIdentificationCode());
                        detailDic.setIsEnabled(true);
                        detailDic.setPid(dic.getId());
                        detailDic.setLevel(1);
                        detailDic.setCreateUser("Pvss同步子级字典信息");
                        detailDic.setCreateTime(LocalDateTime.now());
                        detailDic.setDeleteFlag(false);
                        detailDic.setSort(2);
                        detailDicList.add(detailDic);
                    }
                }
            }
            //添加子级数据库
            Boolean detailResult = dicService.saveBatch(detailDicList);
            if (detailResult)
                return Response.success();
            return Response.fail();
        }
        return Response.fail();
    }
    catch (Exception e)
    {
        return Response.fail();
    }
    }

}
