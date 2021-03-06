package com.bc.es.repository;

import com.bc.es.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * 商品es组件
 *
 * @author zhou
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, String> {
}
