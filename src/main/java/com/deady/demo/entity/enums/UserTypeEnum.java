package com.deady.demo.entity.enums;

/**
 * Created by wzwuw on 2018/1/31.
 */
public enum UserTypeEnum {
    ADMIN("1", "管理员"), TEACHER("2", "教师"), STUDENT("3", "学生");

    private String type;
    private String desc;

    private UserTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
