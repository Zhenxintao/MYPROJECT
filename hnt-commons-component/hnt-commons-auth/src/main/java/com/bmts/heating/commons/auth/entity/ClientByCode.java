package com.bmts.heating.commons.auth.entity;

import lombok.Data;

@Data
public class ClientByCode extends RegisterClient {
    private String code;
}
