package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.ListUtil;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStationTree;
import com.bmts.heating.commons.db.service.HeatOrganizationService;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatOrganizationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "热源组织架构")
@RestController
@RequestMapping("/heatOrganizationService")
@Slf4j
public class HeatOrganizationJoggle {

    @Autowired
    private HeatOrganizationService heatOrganizationService;
    @Autowired
    private AuthrityService authrityService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody HeatOrganization heatOrganization) {
        heatOrganization.setCreateTime(LocalDateTime.now());
        heatOrganizationService.save(heatOrganization);
        return Response.success(heatOrganizationService.insert(heatOrganization));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id) {
        HeatOrganization heatOrganization = heatOrganizationService.getById(id);
        String code = heatOrganization.getCode();
        QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("code", code);
        return heatOrganizationService.remove(queryWrapper) ? Response.success() : Response.fail();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batch")
    public Response deleteBatch(@RequestBody List<Integer> ids) {
        return heatOrganizationService.deleteByIds(ids) >= ids.size() ? Response.success() : Response.fail();
    }

    @ApiOperation("更新")
    @PutMapping
    public Response update(@RequestBody HeatOrganization heatOrganization) {
        heatOrganization.setUpdateTime(LocalDateTime.now());
        return heatOrganizationService.updateById(heatOrganization) ? Response.success() : Response.fail();
    }

    @ApiOperation("单体查询")
    @GetMapping("/{id}")
    public Response query(@PathVariable int id) {
        return Response.success(heatOrganizationService.getById(id));
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Response query(@RequestBody HeatOrganizationDto dto) {
        Response response = Response.fail();
        try {
            Page<HeatOrganization> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            if (StringUtils.isNotBlank(dto.getKeyWord()))
                queryWrapper.like("name", dto.getKeyWord());
            if (StringUtils.isNotBlank(String.valueOf(dto.getPid())))
                queryWrapper.eq("pid", dto.getPid());
            queryWrapper.eq("deleteFlag", false);
            return Response.success(heatOrganizationService.page(page, queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }

    @ApiOperation("查询全部")
    @GetMapping("/all/{userId}")
    public Response queryAll(@PathVariable Integer userId) {
        if (userId < 0) return Response.success(heatOrganizationService.queryAll());
        List<UserDataPerms> userDataPerms = authrityService.queryOrgs(userId);
        List<HashSet<HeatOrganization>> hashSets = userDataPerms.stream().map(UserDataPerms::getOrgs).collect(Collectors.toList());
        List<HeatOrganization> orgs = new ArrayList<>();
        hashSets.forEach(orgs::addAll);
        return Response.success(orgs);
    }

    @ApiOperation("base-根据父节点获取子所有节点")
    @PostMapping("/orgIds")
    public Set<Integer> queryByParentOrgIds(@RequestBody List<Integer> ids) {
        QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.eq("deleteFlag", false);
        List<HeatOrganization> list = heatOrganizationService.list(queryWrapper);
        Map<Boolean, List<HeatOrganization>> firstCollect = list.stream().collect(Collectors.groupingBy(HeatOrganization::getIsEnd));
        Set<String> heatOrganizations = new HashSet<>();
        if (ListUtil.isValid(firstCollect.get(false))) {
            heatOrganizations = firstCollect.get(false).stream().map(HeatOrganization::getCode).collect(Collectors.toSet());
        }
        Set<HeatOrganization> heatOrganizationList = new HashSet<>();
        heatOrganizations.forEach(e -> {
            QueryWrapper<HeatOrganization> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.like("code", e);
            queryWrapper1.eq("deleteFlag", false);
            heatOrganizationList.addAll(heatOrganizationService.list(queryWrapper1));
        });
        Set<Integer> collect1 = heatOrganizationList.stream().map(HeatOrganization::getId).collect(Collectors.toSet());
        Set<Integer> collect = new HashSet<>();
        if (ListUtil.isValid(firstCollect.get(true)))
            collect = firstCollect.get(true).stream().map(HeatOrganization::getId).collect(Collectors.toSet());
        collect.addAll(collect1);
        return collect;
    }

    @Autowired
    private HeatTransferStationService transferStationService;

    //查询系统
    @ApiOperation(value = "组织架构带系统方法")
    @PostMapping("/queryTree2")
    public Response queryTree2(@RequestBody QueryTreeDto dto){
        List<HeatOrganization> list = heatOrganizationService.list();
        //集团和公司
        List<HeatOrganization> listCompany = list.stream().filter(x->x.getLevel()!=3).collect(Collectors.toList());
        List<HeatOrganization> listNew = new ArrayList<>();
        //用于根据组织结构的id查询 站和系统
        List<Integer> companyIds =  list.stream().map(HeatOrganization::getId).collect(Collectors.toList());
        List<CommonTree> commonTrees = null;
        HashSet<Integer> stationPerms = null;
        if (dto.getUserId()!=-1){
            UserDataPerms userDataPerms = authrityService.queryStations(dto.getUserId());
            stationPerms = userDataPerms.getStations();
            //站所属于的组织的id
            if (!CollectionUtils.isEmpty(stationPerms)){
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.in("id",stationPerms);
                List<HeatTransferStation> list1 = transferStationService.list(wrapper);//查询出有权限的站对应的所 或者公司
                for (HeatOrganization heatOrganization : list) {
                    boolean b1 = list1.stream().anyMatch(x -> x.getHeatOrganizationId().equals(heatOrganization.getId()));
                    if (b1){
                        listNew.add(heatOrganization);
                        for (HeatOrganization organization : listCompany) {
                            if (organization.getLevel() ==1&&!listNew.contains(organization)){
                                listNew.add(organization);//如果是集团直接添加
                            }
                            if (!listNew.contains(organization)&&heatOrganization.getPid().equals(organization.getId())){
                                listNew.add(organization);
                            }
                        }
                    }
                }
            }
        }
        if (dto.getFlag() == 1){
            //带系统
            List<OrgAndStationTree> systemAndStation = heatOrganizationService.findStationAndSystem(companyIds);
            if (dto.getUserId()!=-1){
                List<OrgAndStationTree> systemAndStationNew = new ArrayList<>();
                if (!CollectionUtils.isEmpty(stationPerms)) {
                    for (OrgAndStationTree orgAndStationTree : systemAndStation) {
                        for (Integer station : stationPerms) {
                            if (station.equals(orgAndStationTree.getStationId())) {
                                systemAndStationNew.add(orgAndStationTree);
                            }
                        }
                    }
                }
                commonTrees = this.setTreeContainSystem(listNew, systemAndStationNew, null,dto);
            }else {
                commonTrees = this.setTreeContainSystem(list, systemAndStation, null,dto);
            }
            return Response.success(commonTrees);
        }else{
            //不带系统
            List<HeatTransferStation> stations = transferStationService.list(new QueryWrapper<HeatTransferStation>().ne("status",0).in("heatOrganizationId", companyIds));
            if (dto.getUserId()!=-1){
                List<HeatTransferStation> stations1 = new ArrayList<>();
                if (!CollectionUtils.isEmpty(stationPerms)) {
                    for (HeatTransferStation orgAndStationTree : stations) {
                        for (Integer station : stationPerms) {
                            if (station.equals(orgAndStationTree.getId())) {
                                stations1.add(orgAndStationTree);
                            }
                        }
                    }
                }
                commonTrees = this.setTreeContainSystem(listNew,null ,stations1, dto);
            }else {
                commonTrees = this.setTreeContainSystem(list,null ,stations, dto);
            }
        }
        return Response.success(commonTrees);
    }

    private List<CommonTree> setTreeContainSystem(List<HeatOrganization> list,List<OrgAndStationTree> systemAndStation,List<HeatTransferStation> stations,QueryTreeDto dto){
        List<CommonTree> commonTrees = new ArrayList<>();
        List<Integer> companyId = new ArrayList<>();
        for (HeatOrganization heatOrganization : list) {
            if (heatOrganization.getLevel() == 2){
                //公司id
                companyId.add(heatOrganization.getId());
            }
        }
        for (HeatOrganization heatOrganization : list) {
            CommonTree commonTree = new CommonTree();
            commonTree.setId(heatOrganization.getId().toString());
            commonTree.setPid(heatOrganization.getPid().toString());
            commonTree.setName(heatOrganization.getName());
            commonTree.setLevel(heatOrganization.getLevel());
            if (dto.getType() == 1){
                //单层级
                if (commonTree.getLevel()==dto.getLevel()){
                    commonTrees.add(commonTree);
                }
            }
            if(dto.getType() == 2){
                //多层级
                if (commonTree.getLevel()<=dto.getLevel()){
                    commonTrees.add(commonTree);
                }
            }
            //带站带系统
            if (!CollectionUtils.isEmpty(systemAndStation)&& dto.getFlag()==1) {
                for (OrgAndStationTree orgAndStationTree : systemAndStation) {
                    //公司下直接挂站
                    if (commonTree.getLevel() == 3&&dto.getLevel() == 2&&heatOrganization.getId().equals(orgAndStationTree.getHeatOrganizationId())){
                        CommonTree commonTreeStation = new CommonTree();
                        CommonTree commonTreeSystem = new CommonTree();
                        commonTreeStation.setId(String.valueOf(orgAndStationTree.getStationId()));
                        commonTreeStation.setName(orgAndStationTree.getStationName());
                        commonTreeStation.setLevel(4);
                        for (Integer integer : companyId) {
                            //比对所得pid与公司得id对应
                            if (integer.equals(heatOrganization.getPid())){
                                commonTreeStation.setPid(integer.toString());
                            }
                        }
                        commonTreeStation.setProperties("station");
                        if (!commonTrees.contains(commonTreeStation)){
                            commonTrees.add(commonTreeStation);
                        }
                        commonTreeSystem.setId(String.valueOf(orgAndStationTree.getSystemId()));
                        commonTreeSystem.setName(orgAndStationTree.getSystemName());
                        commonTreeSystem.setProperties("system");
                        commonTreeSystem.setLevel(5);
                        commonTreeSystem.setPid(String.valueOf(orgAndStationTree.getStationId()));
                        if (!commonTrees.contains(commonTreeSystem)){
                            commonTrees.add(commonTreeSystem);
                        }
                    }
                    if (dto.getLevel().equals(commonTree.getLevel())||(dto.getLevel()==3&&commonTree.getLevel()==2)){
                        //所下挂站
                        if (Integer.valueOf(commonTree.getId()).equals(orgAndStationTree.getHeatOrganizationId())){
                            CommonTree commonTreeStation = new CommonTree();
                            CommonTree commonTreeSystem = new CommonTree();
                            commonTreeStation.setId(String.valueOf(orgAndStationTree.getStationId()));
                            commonTreeStation.setName(orgAndStationTree.getStationName());
                            commonTreeStation.setLevel(4);
                            commonTreeStation.setPid(commonTree.getId());//
                            commonTreeStation.setProperties("station");
                            if (!commonTrees.contains(commonTreeStation)){
                                commonTrees.add(commonTreeStation);
                            }
                            commonTreeSystem.setId(String.valueOf(orgAndStationTree.getSystemId()));
                            commonTreeSystem.setName(orgAndStationTree.getSystemName());
                            commonTreeSystem.setProperties("system");
                            commonTreeSystem.setLevel(5);
                            commonTreeSystem.setPid(String.valueOf(orgAndStationTree.getStationId()));
                            if (!commonTrees.contains(commonTreeSystem)){
                                commonTrees.add(commonTreeSystem);
                            }
                        }
                    }
                 }
                //带站 带系统
            }
            //带站 不带系统
            if (!CollectionUtils.isEmpty(stations)&& dto.getFlag()==2){
                for (HeatTransferStation station : stations) {
                    //当公司下直接挂站时
                    if (heatOrganization.getLevel() == 3&&dto.getLevel() == 2&&heatOrganization.getId().equals(station.getHeatOrganizationId())){
                        CommonTree commonTree1 = new CommonTree();
                        commonTree1.setId(station.getId().toString());
                        commonTree1.setLevel(4);
                        for (Integer integer : companyId) {
                            if (integer.equals(heatOrganization.getPid())){
                                commonTree1.setPid(integer.toString());
                            }
                        }
                        commonTree1.setProperties("station");
                        commonTree1.setName(station.getName());
                        if (!commonTrees.contains(commonTree1)){
                            commonTrees.add(commonTree1);
                        }
                    }
                }
                //所下挂站 或者把公司下直接带站的也查出来
                if (dto.getLevel().equals(commonTree.getLevel())||(dto.getLevel()==3&&commonTree.getLevel()==2)){
                    for (HeatTransferStation station : stations) {
                        if (station.getHeatOrganizationId().equals(Integer.valueOf(commonTree.getId()))){
                            CommonTree commonTree1 = new CommonTree();
                            commonTree1.setId(station.getId().toString());
                            commonTree1.setLevel(4);
                            commonTree1.setPid(commonTree.getId());
                            commonTree1.setProperties("station");
                            commonTree1.setName(station.getName());
                            if (!commonTrees.contains(commonTree1)){
                                commonTrees.add(commonTree1);
                            }
                        }
                    }
                }
            }
        }
        return commonTrees;
    }

    //没有查询系统
    @ApiOperation(value = "组织架构通用方法")
    @PostMapping("/queryTree")
    public Response queryTree(@RequestBody QueryTreeDto dto){
        if (dto.getType() == 1){
            //单层级
            List<HeatOrganization> list = heatOrganizationService.list(new QueryWrapper<HeatOrganization>().eq("level",dto.getLevel()));
            if (!CollectionUtils.isEmpty(list)){
                List<CommonTree> commonTrees = new ArrayList<>();
                for (HeatOrganization heatOrganization : list) {
                    CommonTree commonTree = new CommonTree();
                    commonTree.setId(heatOrganization.getId().toString());
                    commonTree.setLevel(heatOrganization.getLevel());
                    commonTree.setName(heatOrganization.getName());
                    commonTree.setPid(heatOrganization.getPid().toString());
                    commonTrees.add(commonTree);
                }
                return Response.success(commonTrees);
            }
        }
        if(dto.getType() == 2){
            //2 多层级 level <= 2
            List<HeatOrganization> list = heatOrganizationService.list(new QueryWrapper<HeatOrganization>().le("level", dto.getLevel()));
            if (!CollectionUtils.isEmpty(list)){
                List<CommonTree> commonTrees = new ArrayList<>();
                for (HeatOrganization heatOrganization : list) {
                    CommonTree commonTree = new CommonTree();
                    commonTree.setId(heatOrganization.getId().toString());
                    commonTree.setLevel(heatOrganization.getLevel());
                    commonTree.setName(heatOrganization.getName());
                    commonTree.setPid(heatOrganization.getPid().toString());
                    commonTrees.add(commonTree);
                }
                return Response.success(commonTrees);
            }
        }
        if(dto.getType() == 3){
            //3 单层级带站
            List<OrgAndStationTree> list = heatOrganizationService.queryOrganizationTreeAndStation(dto.getLevel());
            List<CommonTree> commonTrees = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list)){
                for (OrgAndStationTree orgAndStation : list) {
                    CommonTree commonTree = new CommonTree();
                    CommonTree commonTree2 = new CommonTree();
                    commonTree.setId(orgAndStation.getId());
                    commonTree.setPid(orgAndStation.getPid());
                    commonTree.setName(orgAndStation.getName());
                    commonTree.setLevel(orgAndStation.getLevel());
                    if (!commonTrees.contains(commonTree)){
                        commonTrees.add(commonTree);
                    }
                    commonTree2.setId(String.valueOf(orgAndStation.getStationId()));
                    commonTree2.setPid(orgAndStation.getId());
                    commonTree2.setName(orgAndStation.getStationName());
                    commonTrees.add(commonTree2);
                }
                return Response.success(commonTrees);
            }
        }
        if(dto.getType() == 4){
            //4 多层级带站 先查询出多层级结构树
            List<HeatOrganization> list = heatOrganizationService.list(new QueryWrapper<HeatOrganization>().le("level",dto.getLevel()));
            List<OrgAndStationTree> orgAndStations  = heatOrganizationService.queryOrganizationTreeAndStation(dto.getLevel());
            List<CommonTree> commonTrees = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(orgAndStations)){
                for (HeatOrganization heatOrganization : list) {
                    if (heatOrganization.getLevel() == 1){
                        CommonTree commonTree = new CommonTree();
                        commonTree.setLevel(heatOrganization.getLevel());
                        commonTree.setName(heatOrganization.getName());
                        commonTree.setId(heatOrganization.getId().toString());
                        commonTree.setPid(heatOrganization.getPid().toString());
                        commonTrees.add(commonTree);
                    }
                    if (dto.getLevel() == 3){//当level 为所时 将公司添加进list
                        if (heatOrganization.getLevel() == 2){
                            CommonTree commonTree = new CommonTree();
                            commonTree.setPid(heatOrganization.getPid().toString());
                            commonTree.setId(heatOrganization.getId().toString());
                            commonTree.setLevel(heatOrganization.getLevel());
                            commonTree.setName(heatOrganization.getName());
                            commonTrees.add(commonTree);
                        }
                    }
                }
                for (OrgAndStationTree orgAndStation : orgAndStations) {
                    CommonTree commonTree = new CommonTree();
                    CommonTree commonTree2 = new CommonTree();
                    commonTree.setId(String.valueOf(orgAndStation.getId()));
                    commonTree.setName(orgAndStation.getName());
                    commonTree.setPid(orgAndStation.getPid());
                    commonTree.setLevel(orgAndStation.getLevel());
                    if (!commonTrees.contains(commonTree)){
                        commonTrees.add(commonTree);
                    }
                    commonTree2.setId(String.valueOf(orgAndStation.getStationId()));
                    commonTree2.setName(orgAndStation.getStationName());
                    commonTree2.setPid(orgAndStation.getId());
                    commonTrees.add(commonTree2);
                }
                return Response.success(commonTrees);
            }
        }
        return Response.success();
    }

}
