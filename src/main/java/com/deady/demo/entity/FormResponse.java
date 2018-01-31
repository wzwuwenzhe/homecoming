package com.deady.demo.entity;

/**
 * Created by wzwuw on 2018/1/31.
 */
public class FormResponse {

    private boolean result;//结果
    private String msg;//消息
    private String data;

    public FormResponse(boolean result,String msg,String data ){
        this.result=result;
        this.msg=msg;
        this.data=data;
    }
}
