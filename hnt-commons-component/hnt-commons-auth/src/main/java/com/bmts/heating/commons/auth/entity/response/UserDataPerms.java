package com.bmts.heating.commons.auth.entity.response;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import lombok.Data;

import java.util.HashSet;

@Data
public class UserDataPerms {
    private int userId;
    //站点id
    private HashSet<Integer> stations;
    //组织机构id
    private HashSet<HeatOrganization> orgs;
}
