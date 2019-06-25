package com.bc.es.service;

import com.bc.es.entity.Goods;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.List;

/**
 * 商品业务类接口
 *
 * @author zhou
 */
public interface GoodsService {

    /**
     * 保存商品文档
     *
     * @param goods 商品
     */
    void save(Goods goods);

    /**
     * 搜索商品
     *
     * @param queryBuilder 请求参数
     * @param pageable     分页参数
     * @return 搜索结果
     */
    Page<Goods> search(QueryBuilder queryBuilder, Pageable pageable);

    /**
     * 搜索商品
     *
     * @param searchQuery 查询参数
     * @return 搜索结果
     */
    Page<Goods> search(SearchQuery searchQuery);

    /**
     * 根据id删除商品文档
     *
     * @param goodsId 商品id
     */
    void deleteById(String goodsId);
}
