package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.SysDataPerm;
import lombok.Data;

@Data
public class SysDataPermResponse extends SysDataPerm {
    int userId;
    int centerId;
}
