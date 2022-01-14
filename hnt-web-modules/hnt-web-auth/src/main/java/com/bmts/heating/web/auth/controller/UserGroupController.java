//package com.bmts.heating.web.auth.controller;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.bmts.heating.commons.auth.entity.Response;
//import com.bmts.heating.commons.basement.model.db.entity.SysUser;
//import com.bmts.heating.commons.basement.model.db.entity.SysUserGroup;
//import com.bmts.heating.commons.db.service.auth.SysUserGroupService;
//import com.bmts.heating.commons.db.service.auth.SysUserService;
//import com.bmts.heating.web.auth.base.dto.SysUserGroupPojo;
//import com.bmts.heating.web.auth.base.dto.UserGroupPojo;
//import com.bmts.heating.web.auth.base.dto.UserInfoDto;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@Api(tags = "用户组管理")
//@RequestMapping("group")
//public class UserGroupController {
//    @Autowired
//    SysUserGroupService sysUserGroupService;
//    @Autowired
//    SysUserService sysUserService;
//
//    @ApiOperation("新增")
//    @PostMapping
//    public Response insert(@RequestBody SysUserGroupPojo pojo) {
//        Response response = new Response();
//        pojo.setCreateTime(LocalDateTime.now());
//        sysUserGroupService.save(pojo);
//        if (pojo.getId() > 0) {
//            //批量设置用户所属用户组
//            return response;
//        } else
//            return response.notOk();
//    }
//
//    @PutMapping
//    @ApiOperation("修改")
//    public Response update(@RequestBody SysUserGroupPojo sysUserGroupPojo) {
//        Response response = new Response();
//        sysUserGroupService.saveOrUpdate(sysUserGroupPojo);
//        if (sysUserGroupPojo.getId() > 0) {
//            List<SysUser> list =
//                    sysUserService.list(
//                            Wrappers.<SysUser>lambdaQuery()
//                                    .eq(SysUser::getGroupId, sysUserGroupPojo.getId())
//                    );
//            if (CollectionUtils.isNotEmpty(list)) {
//                List<Integer> collectUsers = list.stream().map(x -> x.getGroupId()).collect(Collectors.toList());
//                List<Integer> delGroupUserIds = collectUsers.stream().filter(x -> !sysUserGroupPojo.getUserIds().contains(x)).collect(Collectors.toList());
//                setUserGroup(delGroupUserIds, null);
//                List<Integer> setGroupIds = sysUserGroupPojo.getUserIds().stream().filter(x -> !collectUsers.contains(x)).collect(Collectors.toList());
//                setUserGroup(setGroupIds, sysUserGroupPojo.getId());
//            }
//            return response;
//        } else {
//            return response.notOk();
//        }
//
//    }
//
//
//    @DeleteMapping
//    @ApiOperation("删除")
//    public Response delete(@RequestParam int id) {
//        Response response = new Response();
//        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("groupId", id);
//        updateWrapper.set("groupId", null);
//        if (sysUserService.update(updateWrapper) && sysUserGroupService.removeById(id))
//            return response;
//        else return response.notOk();
//    }
//
//    @PostMapping("/query")
//    @ApiOperation("查询")
//    public Response query(@RequestBody UserGroupPojo userGroupPojo)
//    {
//        Response response = new Response();
//        IPage<SysUserGroup> page = new Page<>(userGroupPojo.getCurrentPage(), userGroupPojo.getPagesize());
//        QueryWrapper<SysUserGroup> queryWrapper = new QueryWrapper<>();
//
//        if (StringUtils.isNotBlank(userGroupPojo.getName())) {
//            queryWrapper.like("name", userGroupPojo.getName());
//        }
//        response.setData(sysUserGroupService.page(page, queryWrapper));
//        return response;
//    }
//    private void setUserGroup(List<Integer> users, Object obj) {
//        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.in("id", users);
//        updateWrapper.set("groupId", obj);
//        sysUserService.update(updateWrapper);
//    }
//
//
//
//}
