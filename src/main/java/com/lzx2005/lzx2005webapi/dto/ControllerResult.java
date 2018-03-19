package com.lzx2005.lzx2005webapi.dto;

/**
 * Created by Lizhengxian on 2018/3/19.
 */
public class ControllerResult {
    private int code;
    private String msg;
    private Object data;

    public static ControllerResult ok(Object object){
        ControllerResult result = new ControllerResult();
        result.setCode(200);
        result.setMsg("请求成功");
        result.setData(object);
        return result;
    }

    public int getCode() {
        return code;
    }

    public ControllerResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ControllerResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ControllerResult setData(Object data) {
        this.data = data;
        return this;
    }
}
