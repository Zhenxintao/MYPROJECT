
package com.bmts.heating.web.auth.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import com.bmts.heating.commons.db.service.auth.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class UserControllerSyn {

	@Autowired
	private SysUserService sysUserService;

	private static Logger logger = LoggerFactory.getLogger(UserControllerSyn.class);

	@JmsListener(destination = "user.add")
	public void userAdd(String user){
		try{
			SysUser sysUser = JSON.parseObject(user, SysUser.class);
			SysUser sysUser1 = new SysUser();
			SysUser center = sysUserService.getOne(new QueryWrapper<SysUser>().eq("center_id", sysUser.getId()));
			if (sysUser.getId() != 0 && ObjectUtils.isEmpty(center)) {
				sysUser1.setCenterId(sysUser.getId());
				sysUser1.setUsername(sysUser.getUsername());
				sysUser1.setPassword("");
				sysUser1.setPhone(sysUser.getPhone());
				sysUser1.setEmail(sysUser.getEmail());
				sysUser1.setNickname(sysUser.getNickname());
				sysUserService.save(sysUser1);
			}
		}catch (Exception e){
			logger.error("save user error",e);
		}
	}

	@JmsListener(destination = "user.update")
	public void userUpdate(String user){
		try{
			SysUser sysUser = JSON.parseObject(user, SysUser.class);
			SysUser center = sysUserService.getOne(new QueryWrapper<SysUser>().eq("center_id",sysUser.getId()));
			if (sysUser.getId() != 0 && ObjectUtils.isNotEmpty(center)) {
				center.setCenterId(sysUser.getId());
				center.setUsername(sysUser.getUsername());
				center.setPhone(sysUser.getPhone());
				center.setEmail(sysUser.getEmail());
				center.setNickname(sysUser.getNickname());
				sysUserService.update(center, new QueryWrapper<SysUser>().eq("center_id",sysUser.getId()));
			}
		}catch (Exception e){
			logger.error("save user error",e);
		}


	}

	@JmsListener(destination = "user.delete")
	public void userDelete(String user){
		try{
			SysUser sysUser = JSON.parseObject(user, SysUser.class);
			if (sysUser.getId() != 0){
				sysUserService.remove(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getCenterId, sysUser.getId()));
			}
		}catch (Exception e){
			logger.error("save user error",e);
		}
	}
}

