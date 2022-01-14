package com.bmts.heating.commons.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.auth.entity.DataPerm;
import com.bmts.heating.commons.auth.entity.response.AuthResponse;
import com.bmts.heating.commons.auth.entity.response.PermsResponse;
import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.entity.SysDataRelevance;
import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStation;
import com.bmts.heating.commons.basement.model.db.response.SysDataPermResponse;
import com.bmts.heating.commons.basement.utils.MapperUtils;
import com.bmts.heating.commons.db.mapper.HeatOrganizationMapper;
import com.bmts.heating.commons.db.mapper.auth.SysDataRelevanceMapper;
import com.bmts.heating.commons.db.mapper.auth.SysPermissionMapper;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.auth.SysDataPermService;
import com.bmts.heating.commons.entiy.auth.response.SysPermissionRoleResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthrityService {
    @Autowired
    SysPermissionMapper sysPermissionMapper;
    @Autowired
    SysDataPermService sysDataPermService;
    @Autowired
    HeatOrganizationMapper heatOrganizationMapper;

    @Autowired
    HeatTransferStationService heatTransferStationService;

    @Autowired
    SysDataRelevanceMapper sysDataRelevanceMapper;
    @Autowired
    SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

    /**
     * 根据用户查询站对应操作权限
     *
     * @param userId
     * @return
     */
    public List<AuthResponse> queryStationsByUser(int userId) {
        if (userId <= 0) {
            List<AuthResponse> collect = heatTransferStationService.list().stream().map(x -> {
                AuthResponse authResponse = new AuthResponse();
                authResponse.setDataId(x.getId());
                return authResponse;
            }).collect(Collectors.toList());
            return collect;
        }
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("su.center_id", userId);
        List<SysPermissionRoleResponse> sysPermissionRoleResponses = sysPermissionMapper.selectPermAndMenuByUser(wrapper);
        Map<Integer, List<SysPermissionRoleResponse>> roleDataPerms = sysPermissionRoleResponses.stream().collect(Collectors.groupingBy(x -> x.getRoleId()));
        List<AuthResponse> result = new LinkedList<>();
        roleDataPerms.forEach((k, v) -> {
            SysPermissionRoleResponse sysPermissionRoleResponse = v.get(0);
            List<DataPerm> dataPerms = JSONObject.parseArray(sysPermissionRoleResponse.getChecked(), DataPerm.class);
            List<Integer> orgIds = idContainer(dataPerms, "org_");
            List<Integer> stations = idContainer(dataPerms, "station_");
            if (!CollectionUtils.isEmpty(orgIds)) {
                List<Integer> stationsByOrgs = queryOrgAndStation(orgIds);
                stations.addAll(stationsByOrgs.stream().filter(x -> !stations.contains(x)).collect(Collectors.toList()));
            }
            List<PermsResponse> sysPermissions = new ArrayList<>(v.size());
            Gson gson = new Gson();
            v.forEach(x -> {
                PermsResponse sysPermission = gson.fromJson(gson.toJson(x), PermsResponse.class);
                sysPermissions.add(sysPermission);
            });
            stations.forEach(x -> {
                result.add(AuthResponse.builder()
                        .dataId(x)
                        .perms(sysPermissions)
                        .build()
                );
            });
        });
        return result;
    }

    public boolean queryStationPerm(int userId, int stationId) {
        List<AuthResponse> authResponses = queryStationsByUser(userId);
        List<AuthResponse> collect = authResponses.stream().filter(x -> x.getDataId() == stationId).collect(Collectors.toList());
        for (AuthResponse authResponse : collect) {
            if (!CollectionUtils.isEmpty(authResponse.getPerms()) && authResponse.getPerms().stream().filter(x -> !StringUtils.isEmpty(x.getCode()) && x.getCode().equals("distributed-control")).findFirst().orElse(null) != null)
                return true;
        }
        return false;
    }

    public List<SysPermission> queryPerms(int userId) {
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        if (userId > 0)
            wrapper.eq("su.center_id", userId);
        List<SysPermission> sysPermissions = sysPermissionMapper.selectPermsByUser(wrapper);
//       sysPermissionMapper.selectPermAndMenuByUser(wrapper);
        return sysPermissions.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 查询所有用户站点权限
     *
     * @return
     */
    public List<UserDataPerms> queryStationsByAllUsers() {
        return queryStations(new QueryWrapper<SysDataPermResponse>());
    }

    /**
     * 根据用户id获取所有站权限
     *
     * @param userId
     * @return
     */
    public UserDataPerms queryStations(int userId) {

        List<Integer> ids = new ArrayList<>();
        ids.add(userId);
        List<UserDataPerms> userDataPerms = queryStationsByUsers(ids);
        if (CollectionUtils.isEmpty(userDataPerms))
            return new UserDataPerms();
        return userDataPerms.get(0);
    }

    /**
     * 根据用户id 获取组织机构权限
     *
     * @param userId
     * @return
     */
    public List<UserDataPerms> queryOrgs(int userId) {
        QueryWrapper<SysDataPermResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .gt("sdp.id", 0)
                .eq("su.center_id", userId);
        List<SysDataPermResponse> sysDataPermResponses = sysPermissionMapper.selectDataPerm(queryWrapper);
        Map<Integer, List<SysDataPermResponse>> dataPermsUsers = sysDataPermResponses.stream().collect(Collectors.groupingBy(x -> x.getUserId()));
        List<UserDataPerms> result = new LinkedList<>();
        dataPermsUsers.forEach((k, v) -> {
            UserDataPerms userDataPerms = new UserDataPerms();
            userDataPerms.setUserId(k);
            HashSet<HeatOrganization> results = new HashSet<>();
            for (SysDataPermResponse sysDataPermResponse : v) {
                List<DataPerm> dataPerms = JSONObject.parseArray(sysDataPermResponse.getChecked(), DataPerm.class);
                List<Integer> orgIds = idContainer(dataPerms, "org_");
                List<Integer> stations = idContainer(dataPerms, "station_");
                if (!CollectionUtils.isEmpty(orgIds)) {
                    List<HeatOrganization> stationsByOrgs = queryOrgAndStation(orgIds, stations);
                    results.addAll(stationsByOrgs);
                }
            }
            userDataPerms.setOrgs(results);
            result.add(userDataPerms);
        });
        return result;
    }

    /**
     * 根据用户id 查询具有的站数据权限
     *
     * @param userIds
     * @return
     */
    public List<UserDataPerms> queryStationsByUsers(List<Integer> userIds) {
        QueryWrapper<SysDataPermResponse> sysDataPermResponseQueryWrapper = new QueryWrapper<>();
        sysDataPermResponseQueryWrapper.in("su.center_id", userIds);
        return queryStations(sysDataPermResponseQueryWrapper);
    }


    private List<UserDataPerms> queryStations(QueryWrapper<SysDataPermResponse> queryWrapper) {
        queryWrapper.gt("sdp.id", 0);
        List<SysDataPermResponse> sysDataPermResponses = sysPermissionMapper.selectDataPerm(queryWrapper);
        Map<Integer, List<SysDataPermResponse>> dataPermsUsers = sysDataPermResponses.stream().collect(Collectors.groupingBy(x -> x.getCenterId()));
        List<UserDataPerms> result = new LinkedList<>();
        dataPermsUsers.forEach((k, v) -> {
            UserDataPerms userDataPerms = new UserDataPerms();
            userDataPerms.setUserId(k);
            HashSet<Integer> stationIds = new HashSet<>();
            for (SysDataPermResponse sysDataPermResponse : v) {
                List<DataPerm> dataPerms = JSONObject.parseArray(sysDataPermResponse.getChecked(), DataPerm.class);
                List<Integer> orgIds = idContainer(dataPerms, "org_");
                List<Integer> stations = idContainer(dataPerms, "station_");
                if (!CollectionUtils.isEmpty(orgIds)) {
                    List<Integer> stationsByOrgs = queryOrgAndStation(orgIds);
                    stations.addAll(stationsByOrgs.stream().filter(x -> !stations.contains(x)).collect(Collectors.toList()));
                }
                stationIds.addAll(stations);
            }
            userDataPerms.setStations(stationIds);
            result.add(userDataPerms);
        });
        return result;
    }


    private List<Integer> idContainer(List<DataPerm> dataPerms, String prefix) {
        return dataPerms.stream().filter(x -> x.getId().contains(prefix)).map(x -> Integer.valueOf(x.getId().split("_")[1])).collect(Collectors.toList());
    }

    private List<Integer> stationContainer(List<OrgAndStation> orgs) {
        return orgs.stream()
                .filter(x -> x.getStationId() != 0)
                .map(x -> x.getStationId())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 查询组织机构对应所有站
     *
     * @param orgs
     * @return
     */
    private List<Integer> queryOrgAndStation(List<Integer> orgs) {
        QueryWrapper<OrgAndStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("org.id", orgs);
        List<OrgAndStation> orgAndStations = heatOrganizationMapper.queryOrgAndStation(queryWrapper);
        return stationContainer(orgAndStations);
    }

    private List<HeatOrganization> queryOrgAndStation(List<Integer> orgs, List<Integer> stations) {
        QueryWrapper<OrgAndStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("org.id", orgs);
        if (!CollectionUtils.isEmpty(stations)) {
            queryWrapper.or().in("station.id", stations);
        }
        List<OrgAndStation> orgAndStations = heatOrganizationMapper.queryOrgAndStation(queryWrapper);
        return orgContainer(orgAndStations);
    }

    private List<HeatOrganization> orgContainer(List<OrgAndStation> orgs) {
        List<HeatOrganization> result = new ArrayList<>(orgs.size());
        for (OrgAndStation org : orgs) {
            HeatOrganization heatOrganization = new HeatOrganization();
            MapperUtils.copyProperties(org, heatOrganization);
            result.add(heatOrganization);
        }
        return result;
    }

    /**
     * 获取热源权限
     */
    public List<Integer> queryRelevanceList(Integer userId, Integer level) {
        QueryWrapper<SysDataRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s.center_id", userId).eq("r.level", level);
        List<SysDataRelevance> relevanceList = sysDataRelevanceMapper.querySourceRoleInfo(queryWrapper);
        List<Integer> ids = new ArrayList<>();
        if (relevanceList.size() > 0) {
            ids = JSONObject.parseArray(relevanceList.get(0).getRelevanceIds(), Integer.class);
        }
        return ids;
    }
}
