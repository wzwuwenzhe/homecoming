package com.deady.demo.entity;

import java.io.Serializable;

/**
 * Created by wzwuw on 2018/1/31.
 */
public class Operator implements Serializable {
    private static final long serialVersionUID = 819537820575734140L;
    @BasicEntityField(length = 6)
    private String schoolId;// 学校Id
    @BasicEntityField(length = 32)
    private String id;// 操作员编号
    @BasicEntityField(length = 1)
    private String userType;// 用户类型
    @BasicEntityField(length = 20)
    private String loginName;// 登录名
    @BasicEntityField(length = 100)
    private String pwd;// 密码
    private String creationTime;// 创建时间
    private String modifyTime;// 修改时间
    private String msgCode;// 短信验证码

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
