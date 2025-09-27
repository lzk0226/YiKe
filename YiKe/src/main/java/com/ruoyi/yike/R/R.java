package com.ruoyi.yike.R;


import com.ruoyi.yike.emun.ResultCodeEnum;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 8:10
 * @Author : SockLightDust
 */
public class R<T> {

    /** 响应码 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private Long timestamp;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public R(ResultCodeEnum resultCodeEnum) {
        this();
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public R(ResultCodeEnum resultCodeEnum, T data) {
        this(resultCodeEnum);
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> R<T> ok() {
        return new R<>(ResultCodeEnum.SUCCESS);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> R<T> ok(T data) {
        return new R<>(ResultCodeEnum.SUCCESS, data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> R<T> ok(String message) {
        return new R<>(ResultCodeEnum.SUCCESS.getCode(), message);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> R<T> ok(String message, T data) {
        return new R<>(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应（默认失败消息）
     */
    public static <T> R<T> fail() {
        return new R<>(ResultCodeEnum.FAIL);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> R<T> fail(String message) {
        return new R<>(ResultCodeEnum.FAIL.getCode(), message);
    }

    /**
     * 失败响应（指定错误码和消息）
     */
    public static <T> R<T> fail(Integer code, String message) {
        return new R<>(code, message);
    }

    /**
     * 失败响应（使用枚举）
     */
    public static <T> R<T> fail(ResultCodeEnum resultCodeEnum) {
        return new R<>(resultCodeEnum);
    }

    /**
     * 失败响应（使用枚举和数据）
     */
    public static <T> R<T> fail(ResultCodeEnum resultCodeEnum, T data) {
        return new R<>(resultCodeEnum, data);
    }

    /**
     * 根据布尔值返回成功或失败
     */
    public static <T> R<T> result(boolean flag) {
        return flag ? ok() : fail();
    }

    /**
     * 根据布尔值返回成功或失败（带消息）
     */
    public static <T> R<T> result(boolean flag, String successMessage, String failMessage) {
        return flag ? ok(successMessage) : fail(failMessage);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ResultCodeEnum.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 判断是否失败
     */
    public boolean isFail() {
        return !isSuccess();
    }

    // Getter 和 Setter 方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}