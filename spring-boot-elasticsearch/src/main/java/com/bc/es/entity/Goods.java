package com.bc.es.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

/**
 * 商品实体
 * 对应es中的商品文档
 * index: goods
 * type: goods
 *
 * @author zhou
 */
@Document(indexName = "goods", type = "goods", shards = 1, replicas = 0, refreshInterval = "-1")
public class Goods {
    @Id
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 库存数量
     */
    private int inventory;

    /**
     * SEO关键字
     */
    private String seoKeyWords;

    /**
     * SEO描述
     */
    private String seoDescription;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 销量
     */
    private Long salesVolume;

    public Goods() {

    }

    public Goods(String name, String seoKeyWords, String seoDescription, BigDecimal price, Long salesVolume) {
        this.name = name;
        this.seoKeyWords = seoKeyWords;
        this.seoDescription = seoDescription;
        this.price = price;
        this.salesVolume = salesVolume;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getSeoKeyWords() {
        return seoKeyWords;
    }

    public void setSeoKeyWords(String seoKeyWords) {
        this.seoKeyWords = seoKeyWords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Long salesVolume) {
        this.salesVolume = salesVolume;
    }
}
