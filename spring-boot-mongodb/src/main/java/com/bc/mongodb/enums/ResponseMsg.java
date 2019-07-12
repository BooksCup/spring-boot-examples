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
    ,;
    private final String reasonPhrase;

    ResponseMsg(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return reasonPhrase;
    }
}
