package com.bmts.heating.web.auth.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolePermDto {
    private String roleName;
    private int roleId;
    private List<Integer> perms;
}
