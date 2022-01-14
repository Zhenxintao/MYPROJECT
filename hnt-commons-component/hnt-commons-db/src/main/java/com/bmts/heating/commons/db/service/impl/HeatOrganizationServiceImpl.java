package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatNetSourceTree;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStationTree;
import com.bmts.heating.commons.db.mapper.HeatOrganizationMapper;
import com.bmts.heating.commons.db.service.HeatOrganizationService;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class HeatOrganizationServiceImpl extends ServiceImpl<HeatOrganizationMapper, HeatOrganization> implements HeatOrganizationService {

	@Autowired
	private HeatOrganizationMapper heatOrganizationMapper;

	public int insert(HeatOrganization hChild) {
		Integer id = hChild.getId();
		String code = "root:" + id;
		HeatOrganization hParent;
		if (0 != hChild.getPid()) {
			hParent = heatOrganizationMapper.selectById(hChild.getPid());
			hChild.setCode(hParent.getCode() + ":" + id);
			hChild.setLevel(hParent.getLevel() + 1);
			hParent.setIsEnd(false);
			return heatOrganizationMapper.updateById(hParent) == 1 ? heatOrganizationMapper.updateById(hChild) : 0;
		} else {
			hChild.setCode(code);
			hChild.setLevel(1);
			return heatOrganizationMapper.updateById(hChild);
		}
	}

	@Override
	public List<HeatOrganization> queryAll() {
		QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("deleteFlag", false);
		return heatOrganizationMapper.selectList(queryWrapper);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		AtomicInteger i = new AtomicInteger();
		QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", ids);
		List<HeatOrganization> heatOrganizations = heatOrganizationMapper.selectList(queryWrapper);
		heatOrganizations.forEach(e -> {
			QueryWrapper<HeatOrganization> updateWrapper = new QueryWrapper<>();
			updateWrapper.like("code", e.getCode());
			if(e.getPid() != 0)
				heatOrganizationMapper.updateById(heatOrganizationMapper.selectById(e.getPid()).setIsEnd(true));
			heatOrganizationMapper.delete(updateWrapper);
			i.incrementAndGet();
		});
		return i.intValue();
	}

	@Override
	public List<OrgAndStationTree> findStationAndSystem(List<Integer> ids) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.in("ht.heatOrganizationId",ids);
		queryWrapper.ne("hs.name","");
		queryWrapper.ne("ht.status",0);
		queryWrapper.ne("hs.number",0);
		return heatOrganizationMapper.findStationAndSystem(queryWrapper);
	}

	@Autowired
	private HeatTransferStationService heatTransferStationService;

	@Override
	public List<OrgAndStationTree> queryOrganizationTreeAndStation(Integer level) {
		if (level == 1){
			//集团下直接带站
			HeatOrganization heatOrganization = heatOrganizationMapper.selectOne(new QueryWrapper<HeatOrganization>().eq("level", level));
			List<HeatTransferStation> list = heatTransferStationService.list();
			List<OrgAndStationTree> orgAndStations = new ArrayList<>();
			OrgAndStationTree orgAndStation = new OrgAndStationTree();
			orgAndStation.setLevel(heatOrganization.getLevel());
			orgAndStation.setName(heatOrganization.getName());
			orgAndStation.setId(heatOrganization.getId().toString());
			orgAndStation.setPid(heatOrganization.getPid().toString());
			orgAndStations.add(orgAndStation);
			for (HeatTransferStation heatTransferStation : list) {
				OrgAndStationTree orgAndStation1 = new OrgAndStationTree();
				orgAndStation1.setStationName(heatTransferStation.getName());
				orgAndStation1.setPid(heatOrganization.getId().toString());
				orgAndStation1.setStationId(heatTransferStation.getId());
				orgAndStations.add(orgAndStation1);
			}
			return orgAndStations;
		}
		if (level == 2){
			// 带站 公司
			return heatOrganizationMapper.queryOrganizationTreeAndStation1(new QueryWrapper<>().eq("ho.level",level));
		}
		if (level == 3){
			// 带站 所
			return heatOrganizationMapper.queryOrganizationTreeAndStation2(new QueryWrapper<>().eq("ho.level",level));
		}
		return null;
	}

}
