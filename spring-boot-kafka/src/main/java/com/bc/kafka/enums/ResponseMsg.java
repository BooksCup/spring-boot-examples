package com.bc.kafka.enums;

/**
 * 响应消息
 *
 * @author zhou
 */
public enum ResponseMsg {
    /**
     * mongodb模块接口返回信息
     */
    MESSAGE_SEND_SUCCESS("消息发送成功"),
    MESSAGE_SEND_ERROR("消息发送失败"),;
    private final String reasonPhrase;

    ResponseMsg(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return reasonPhrase;
    }
}
