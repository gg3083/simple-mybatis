package cn.gg.run.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class User {

    private Integer id;
    private String userName;
    private String userPwd;
    private Integer isDelete;
    private Date createTime;

}
