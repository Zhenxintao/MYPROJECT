package com.bmts.heating.web.balance.controller;

import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.entiy.balance.pojo.MembersQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.MembersVo;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.common.HttpIpUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.web.balance.base.BaseController;
import com.bmts.heating.web.balance.service.BalanceNewBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/2/2 15:53
 **/
@RestController
@RequestMapping("balance")
@Api(tags = "全网平衡组员管理")
public class MembersController extends BaseController {
    @Autowired
    private BalanceNewBaseService balanceNewBaseService;

    @ApiOperation(value = "查询列表",response = MembersVo.class)
    @PostMapping("/members/list")
    public Response loadMembers(@RequestBody MembersQueryDto membersDto) {
        Response response = template.doHttp("/netbalance/members/list", membersDto, netBalanceServer, Response.class, HttpMethod.POST);
//        response.getData()
//        FirstNetBase[] firstNetBases = template.doHttp("/common/firstNetBase", null, baseServer, FirstNetBase[].class, HttpMethod.GET);


//      Page<BalanceMembers> pages =JSONObject.parseObject( response.getData(),Page<BalanceMembers);
//        JSONObject.parseObject() response.getData()


        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping("/members/delete")
    public Response removeMembers(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        BalanceLogDto log = new BalanceLogDto();
        log.setIds(String.join(",",ids.toString()));
        log.setUserName(userName);
        log.setCreateTime(LocalDateTime.now());
        log.setIpAddress(HttpIpUtil.getRealIp(request));
        log.setButton("删除热网系统配置");
        log.setDescriptionHead("删除热网系统配置-");
        log.setModule("热网配置");
        log.setDescriptionTrail("-成功！");
        balanceNewBaseService.insertLogByIds(log);
        return template.doHttp("/netbalance/members/delete", ids, netBalanceServer, Response.class, HttpMethod.DELETE);
    }
    @ApiOperation("批量保存")
    @PostMapping("/members/save")
    public Response saveMemebers(@RequestBody List<BalanceMembers> balanceMembers) {
       //目前只考虑选择的是系统 由前端赋值 关联id 和level
        return template.doHttp("/netbalance/members/save", balanceMembers, netBalanceServer, Response.class, HttpMethod.POST);
    }
    @PutMapping("/members/update")
    public Response update(@RequestBody BalanceMembers entity)
    {
        return template.doHttp("/netbalance/members/update", entity, netBalanceServer, Response.class, HttpMethod.PUT);
    }
}
