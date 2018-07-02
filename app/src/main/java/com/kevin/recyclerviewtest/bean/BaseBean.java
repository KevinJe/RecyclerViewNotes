package com.kevin.recyclerviewtest.bean;

/**
 * Created by Kevin Jern on 2018/6/29 13:11.
 */
public class BaseBean<T> {
    // 服务器返回的错误码
    public int errorCode;
    // 服务器返回的错误信息
    public String errorMsg;
    // 服务器返回的信息
    public T data;

    public BaseBean(int errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
