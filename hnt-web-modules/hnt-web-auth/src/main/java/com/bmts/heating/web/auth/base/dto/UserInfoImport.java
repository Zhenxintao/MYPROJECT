package com.bmts.heating.web.auth.base.dto;


import com.bmts.heating.commons.basement.config.ExcelColumn;
import lombok.Data;

@Data
public class UserInfoImport {
    @ExcelColumn(value = "登录名",col = 1)
    public String userName;
    @ExcelColumn(value = "密码",col = 2)
    public String pwd;
    @ExcelColumn(value = "电话",col = 3)
    public String phone;
    @ExcelColumn(value = "邮箱",col = 4)
    public String email;
    @ExcelColumn(value = "用户姓名",col = 5)
    public String nickName;
}
