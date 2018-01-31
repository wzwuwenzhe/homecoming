package com.deady.demo.entity;


import com.google.gson.JsonObject;

/**
 * Created by wzwuw on 2018/1/31.
 */
public class FormResponse {



    private boolean result;//结果
    private String msg;//消息
    private String data;//数据

    public FormResponse(){}

    public FormResponse(boolean result,String msg,String data ){
        this.result=result;
        this.msg=msg;
        this.data=data;
    }

    public String toString(){
        JsonObject obj = new JsonObject();
        obj.addProperty("result",result);
        obj.addProperty("msg",msg);
        obj.addProperty("data",data);
        return obj.toString();
    }
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
