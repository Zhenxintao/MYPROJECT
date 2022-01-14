package com.bmts.heating.web.auth.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleDto {
    private Integer userId;
    private List<Integer> roleId;
}
