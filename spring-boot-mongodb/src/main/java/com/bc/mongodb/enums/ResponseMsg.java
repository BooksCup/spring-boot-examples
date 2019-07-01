package com.bc.mongodb.enums;

/**
 * 响应消息
 *
 * @author zhou
 */
public enum ResponseMsg {
    /**
     * mongodb模块接口返回信息
     */
    ADD_USER_SUCCESS("保存用户成功"),
    ADD_USER_ERROR("保存用户失败"),

    UPDATE_USER_SUCCESS("修改用户成功"),
    UPDATE_USER_ERROR("修改用户失败"),

    DELETE_USER_SUCCESS("删除用户成功"),
    DELETE_USER_ERROR("删除用户失败"),;
    private final String reasonPhrase;

    ResponseMsg(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return reasonPhrase;
    }
}
