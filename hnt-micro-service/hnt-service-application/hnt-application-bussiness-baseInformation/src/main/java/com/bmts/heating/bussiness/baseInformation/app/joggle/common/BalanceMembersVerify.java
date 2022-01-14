package com.bmts.heating.bussiness.baseInformation.app.joggle.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.db.service.BalanceMembersService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BalanceMembersVerify {

	@Autowired
	private BalanceMembersService balanceMembersService;

//	public boolean verify(Integer id) {
//		BalanceMembers balanceMembers = balanceMembersService.getOne(Wrappers.<BalanceMembers>lambdaQuery().eq(BalanceMembers::getRelevanceId, id));
//		return balanceMembers.getLevel() == 1 && balanceMembers.getBalanceNetId() == null;
//
//	}

	public List<Integer> verify(List<Integer> ids) {
		List<Integer> deleteIds = new ArrayList<>();
		List<Integer> useIds = new ArrayList<>();
		List<BalanceMembers> balanceMembers = balanceMembersService.list(Wrappers.<BalanceMembers>lambdaQuery().in(BalanceMembers::getRelevanceId, ids));
		for (BalanceMembers balanceMember : balanceMembers) {
			//判断是系统Id
			if (balanceMember.getLevel() == TreeLevel.HeatSystem.level()){
				//将可以删除的Id加入到集合中
				if (balanceMember.getBalanceNetId() == null){
					deleteIds.add(balanceMember.getRelevanceId());
				}else{
					useIds.add(balanceMember.getRelevanceId());
				}
			}
		}
		if(balanceMembersService.remove(Wrappers.<BalanceMembers>lambdaQuery().in(BalanceMembers::getRelevanceId, deleteIds))){
			ids.removeAll(useIds);
		}
		return ids;
	}

}
