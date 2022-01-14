package com.bmts.heating.web.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.auth.utils.PhoneFormatCheckUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.mapper.HeatOrganizationMapper;
import com.bmts.heating.commons.db.mapper.auth.SysUserMapper;
import com.bmts.heating.commons.db.service.HeatOrganizationService;
import com.bmts.heating.commons.db.service.auth.SysRoleService;
import com.bmts.heating.commons.db.service.auth.SysUserOrganizationService;
import com.bmts.heating.commons.db.service.auth.SysUserRoleService;
import com.bmts.heating.commons.db.service.auth.SysUserService;
import com.bmts.heating.commons.jwt.config.IgnoreConfig;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.auth.base.dto.UserInfoDto;
import com.bmts.heating.web.auth.base.dto.UserInfoImport;
import com.bmts.heating.web.auth.base.dto.UserRoleDto;
import com.bmts.heating.web.auth.base.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "用户管理")
@RestController
@RequestMapping("users")
@Slf4j
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserOrganizationService sysUserOrganizationService;
    @Autowired
    HeatOrganizationMapper heatOrganizationMapper;
    @Autowired
    IgnoreConfig ignoreConfig;

    @PostMapping("insertWithRoles")
    @ApiOperation("新增用户")
    public Response insert(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) {
        Response response = new Response();
        if (PhoneFormatCheckUtils.isChinaPhoneLegal(sysUser.getPhone())) {
            List<SysUser> list = sysUserService.list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername,
                    sysUser.getUsername()).or().eq(SysUser::getPhone, sysUser.getPhone()));
            if (!CollectionUtils.isEmpty(list)) {
                return Response.fail("用户名已经存在或者电话号已经存在");
            }
            sysUser.setPassword(encoder.encode(sysUser.getPassword()));
            sysUser.setCreate_user(JwtUtils.getUserId(request));
            if (sysUserService.save(sysUser)) {
//                if (!setUserClient(sysUser.getId(), sysUser.getClients())) {
//                    return response.notOk(StatusCode.CODE_Error);
//                }
                if (!setUserOrgs(sysUser.getId(), sysUser.getOrgs())) {
                    return Response.fail("登陆失败");
                }
                if (!setUserRoles(sysUser.getId(), sysUser.getRoles()))
                    return Response.fail("登陆失败");
            } else
                return Response.fail("登陆失败");

            return response;
        } else {
            return Response.fail("电话号格式不正确");
        }
    }

    public static void main(String[] args) {
        String pwd = new BCryptPasswordEncoder(10).encode("admin123A");
        System.out.printf("密码:{}", pwd);
    }

    private boolean setUserRoles(int userId, List<Integer> roles) {
        try {
            sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId,
                    userId));

            List<SysUserRole> userRoleList = new ArrayList<>();
            roles.forEach(x -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(x);
                userRoleList.add(sysUserRole);
            });
            if (userRoleList.stream().count() > 0 && !sysUserRoleService.saveBatch(userRoleList))
                return false;


        } catch (Exception e) {
            e.printStackTrace();
            log.error("set user roles cause exception {}", e);
            return false;
        }
        return true;
    }

    private boolean setUserOrgs(int userId, List<Integer> orgs) {
        try {
            sysUserOrganizationService.remove(Wrappers.<SysUserOrganization>lambdaQuery().eq(SysUserOrganization::getUserId,
                    userId));
            List<SysUserOrganization> userOrganizations = new ArrayList<>();
            orgs.forEach(x -> {
                SysUserOrganization sysUserOrganization = new SysUserOrganization();
                sysUserOrganization.setUserId(userId);
                sysUserOrganization.setHeatOrganizationId(x);
                userOrganizations.add(sysUserOrganization);
            });
            if (CollectionUtils.isNotEmpty(userOrganizations) && !sysUserOrganizationService.saveBatch(userOrganizations))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("set user organization cause exception {}", e);
            return false;
        }
        return true;
    }

    @PostMapping("insert")
    @ApiOperation("新增用户")
    public Response add(@Valid @RequestBody SysUser sysUser, HttpServletRequest request) throws Exception {

        Response response = new Response();
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(sysUser.getPhone()))
            return Response.fail("电话号格式不正确");
        SysUser userDb = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername,
                sysUser.getUsername()).or().eq(SysUser::getPhone, sysUser.getPhone()));
        if (userDb != null) {
            return Response.fail("用户名已经存在或者电话号已经存在");
        }
        sysUser.setPassword(encoder.encode(sysUser.getPassword()));
        sysUser.setCreate_user(JwtUtils.getUserId(request));
        int id = sysUserMapper.insert(sysUser);

        if (id <= 0)
            return Response.fail();
        return response;
    }

    @DeleteMapping
    @ApiOperation("删除用户")
    public Response delete(@RequestParam int id) {
        Response response = new Response();
        if (!sysUserService.removeById(id))
            return Response.fail();
        sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id));
        return response;
    }

    @PostMapping("query")
    @ApiOperation("查询用户")
    public Response queryUser(@RequestBody UserInfoDto userInfoDto, HttpServletRequest request) {
        Response response = new Response();
        IPage<SysUser> page = new Page<>(userInfoDto.getCurrentPage(), userInfoDto.getPagesize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        int userId = JwtUtils.getUserId(request);
        if (userId > 0)
            queryWrapper.eq("create_user", userId);
        if (StringUtils.isNotBlank(userInfoDto.getUserName())) {
            queryWrapper.and(x->x.like("username", userInfoDto.getUserName()).or().like("nickname", userInfoDto.getUserName()));
        }
        IPage<SysUser> datas = sysUserService.page(page, queryWrapper);
        response.setData(datas);
        return response;
    }

    @PostMapping("setUserRole")
    @ApiOperation("设置用户角色")
    public Response setUserRole(@Valid @RequestBody UserRoleDto userRoleDto) {
        Response response = new Response();
        try {
            sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId,
                    userRoleDto.getUserId()));
            List<SysUserRole> userRoleList = new ArrayList<>();
            userRoleDto.getRoleId().forEach(x -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userRoleDto.getUserId());
                sysUserRole.setRoleId(x);
                userRoleList.add(sysUserRole);
            });
            if (userRoleList.stream().count() > 0 && !sysUserRoleService.saveBatch(userRoleList))
                return Response.fail();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
        return response;
    }

    @GetMapping("loadUserRoles")
    @ApiOperation("加载所有角色及用户角色")
    public Response loadUserRoles(@RequestParam("id") Integer userId) {
        Response response = new Response();
        Map resMap = new HashMap<String, Object>();
        resMap.put("allRoles", sysRoleService.list());
        resMap.put("roles",
                sysUserMapper.queryRoleByUser(userId).stream().map(SysRole::getId).collect(Collectors.toList()));
        response.setData(resMap);
        return response;
    }

    @GetMapping("loadRoles")
    @ApiOperation("加载用户角色")
    public Response loadRoles(@RequestParam("id") Integer userId) {
        Response response = new Response();
        response.setData(sysUserMapper.queryRoleByUser(userId).stream().map(SysRole::getId).collect(Collectors.toList()));
        return response;
    }

    @PostMapping("loadRoleUsers")
    @ApiOperation("加载角色用户")
    public Response loadRoleUsers(@RequestParam("id") Integer roleId) {
        Response response = new Response();
        response.setData(sysUserMapper.queryUsersByRole(roleId));
        return response;
    }

//    @PostMapping("setUserClient")
//    @ApiOperation("设置用户应用系统权限")
//    public Response setUserClient(@RequestBody UserClientDto userClientDto) {
//        Response response = new Response();
//        try {
//            sysUserClientService.remove(Wrappers.<SysUserClient>lambdaQuery().eq(SysUserClient::getSysUserId,
//                    userClientDto.getUserId()));
//            List<SysUserClient> sysUserClients = new ArrayList<>();
//            userClientDto.getClients().forEach(x -> {
//                SysUserClient sysUserClient = new SysUserClient();
//                sysUserClient.setSysUserId(userClientDto.getUserId());
//                sysUserClient.setClientDetailId(x);
//                sysUserClients.add(sysUserClient);
//            });
//            if (sysUserClients.stream().count() > 0 && !sysUserClientService.saveBatch(sysUserClients))
//                return response.notOk(StatusCode.CODE_Error);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return response.notOk(StatusCode.CODE_Error);
//        }
//        return response;
//    }


    @GetMapping
    @ApiOperation("详情")
    public Response detail(@RequestParam int id) {
        Response response = new Response();
        SysUser sysUser = sysUserService.getById(id);
        response.setData(sysUser);
        return response;
    }

    @PostMapping("setUserAdmin")
    @ApiOperation("设置为管理员")
    public Response setUserAdmin(@RequestBody SysUser sysUser) {
        Response response = new Response();
        SysUser user = sysUserService.getById(sysUser.getId());
        if (user == null)
            return Response.fail();
//        user.setClientId(sysUser.getClientId());
        if (!sysUserService.updateById(user))
            return Response.fail();
        return response;
    }

    @PutMapping
    @ApiOperation("修改")
    public Response upt(@RequestBody SysUser info) {
        Response response = new Response();
        SysUser sysUser = sysUserService.getById(info.getId());
        if (!PhoneFormatCheckUtils.isChinaPhoneLegal(info.getPhone()))
            return Response.fail("电话号格式不正确");
        if (sysUser == null)
            return Response.fail("不存在的用户");
        if (sysUser.getPhone().equals(info.getPhone()) && sysUser.getId() != info.getId())
            return Response.fail("电话号已经存在");
        sysUser.setUsername(info.getUsername());
        sysUser.setNickname(info.getNickname());
        sysUser.setPhone(info.getPhone());
        sysUser.setPosition(info.getPosition());
        sysUser.setCenterId(sysUser.getCenterId());
        if (!sysUserService.updateById(sysUser))
            return Response.fail();
//        if (info.getRoles().size() > 0) {
        if (!setUserRoles(sysUser.getId(), info.getRoles()))
            return Response.fail();
//        }
        if (!setUserOrgs(sysUser.getId(), info.getOrgs()))
            return Response.fail();
//        if (!setUserClient(info.getId(), info.getClients()))
//            return response.notOk();
        return response;
    }

    @GetMapping("info")
    public Response info(HttpServletRequest request) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("userName", JwtUtils.getUserName(request));
        map.put("isAdmin", JwtUtils.getUserId(request) <= 0);
        map.put("user", JwtUtils.getUserId(request));
        response.setData(map);
        return response;
    }

    @GetMapping("changeStatus")
    @ApiOperation("修改启用状态")
    public Response changeStatus(@RequestParam int id, @RequestParam Boolean status) {
        Response response = new Response();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser.equals(null))
            return Response.fail();
        sysUser.setStatus(status);
        sysUserService.updateById(sysUser);
        response.setData(sysUser);
        return response;
    }

    @GetMapping("batchInsert")
    @ApiOperation("修改启用状态")
    public Response batchImport(@RequestBody List<SysUser> users) {
        Response response = new Response();
        return response;
    }

    @PostMapping("userImport")
    public Response importUser(@RequestBody MultipartFile file) {
        Response response = new Response();
        List<UserInfoImport> list = ExcelUtil.readExcel("", UserInfoImport.class, file);
        Set<SysUser> set = new HashSet<>();
        List<SysUser> dbUsers = sysUserService.list();
        list.forEach(x -> {
                    if (StringUtils.isNotBlank(x.getUserName()) && StringUtils.isNotBlank(x.getPwd()) && StringUtils.isNotBlank(x.getPhone())) {
                        if (dbUsers.stream().anyMatch(s -> s.getUsername().equals(x.getUserName()) || s.getPhone().equals(x.getPhone()))) {
                            //db中存在
                            log.error("用户信息已经存在:用户名{},电话号{}", x.getUserName(), x.phone);
                            return;
                        }
                        if (set.stream().anyMatch(s -> s.getUsername().equals(x.getUserName()) || s.getPhone().equals(x.getPhone()))) {
                            log.error("数据重复:用户名{},电话号{}", x.getUserName(), x.phone);
                            return;
                        }
                        SysUser userinfo = new SysUser();
                        userinfo.setUsername(x.getUserName());
                        userinfo.setPhone(x.getPhone());
                        userinfo.setNickname(x.getNickName());
                        userinfo.setPassword(encoder.encode(x.pwd));
                        userinfo.setEmail(x.email);
                        userinfo.setStatus(true);
                        set.add(userinfo);
                    }
                }
        );

        if (!set.isEmpty()) {
            if (!sysUserService.saveBatch(set)) {
                return Response.fail();
            }
        }
        return response;
    }

    @Autowired
    HeatOrganizationService heatOrganizationService;

    @PostMapping("queryOrgTree")
    @ApiOperation("查询组织架构树形结构")

    public Response queryTree(HttpServletRequest request) {
        List<HeatOrganization> list = heatOrganizationService.list();
        int userId = JwtUtils.getUserId(request);
        if (userId < 1)
            return Response.success(list);
        QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("su.center_id", userId);
        List<HeatOrganization> heatOrganizations = heatOrganizationMapper.queryOrgByUser(queryWrapper);

        if (CollectionUtils.isNotEmpty(heatOrganizations)) {
            List<String> codes = heatOrganizations.stream().map(x -> x.getCode()).distinct().collect(Collectors.toList());
            List<HeatOrganization> result = list.stream().filter(x -> codes.stream().anyMatch(code -> x.getCode().contains(code))).distinct().collect(Collectors.toList());
            return Response.success(result);
        } else
            return Response.fail();
    }

    @GetMapping("queryOrgByUser")
    @ApiOperation("查询用户组织架构树形结构")
    public Response queryTreeByUserId(@RequestParam int id) {
        Response response = new Response();
        List<Integer> collect = sysUserOrganizationService.list(Wrappers.<SysUserOrganization>lambdaQuery().eq(SysUserOrganization::getUserId, id))
                .stream().map(x -> x.getHeatOrganizationId()).distinct()
                .collect(Collectors.toList());
        response.setData(collect);
        return response;
    }

}
