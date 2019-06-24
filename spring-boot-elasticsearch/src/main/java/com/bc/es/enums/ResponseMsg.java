package com.bc.es.enums;

/**
 * 响应消息
 *
 * @author zhou
 */
public enum ResponseMsg {
    /**
     * es模块接口返回信息
     */
    DOCUMENT_ADD_GOODS_PRICE_PARAMS_FORMAT_ERROR("参数goodsPrice格式错误"),
    DOCUMENT_ADD_GOODS_SALES_PARAMS_FORMAT_ERROR("参数goodsSalesVolume格式错误"),
    DOCUMENT_ADD_GOODS_SUCCESS("商品document保存成功"),
    DOCUMENT_ADD_GOODS_ERROR("商品document保存失败"),;
    private final String reasonPhrase;

    ResponseMsg(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return reasonPhrase;
    }
}
