package com.bmts.heating.commons.basement.model.ldap.pojo.base;

import com.bmts.heating.commons.basement.model.ldap.pojo.model.*;
import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.Data;

import java.util.List;

/**
 * @Author: naming
 * @Description: dto
 * @Date: Create in 2020/9/24 11:27
 * @Modified by
 */
@Data
public class PointConstructionBO {
    private HeatingNetBO heatingNet;
    private HeatingSourceBO heatingSource;
    private GroupBO group;
    private ParentCompanyBO parentCompany;
    private BranchCompanyBO branchCompany;
    private StationBO station;
    private CommunityBO community;
    private FloorBO floor;
    private ControlHousingBO controlHousing;
    private StationSystemBO stationSystem;
    private StationSystemBranchBO stationSystemBranch;
    private UnitBO unit;
    private HouseHoldBO houseHold;
    private DeviceInfoBO deviceInfo;
    private List<PointL> pointL;
}
