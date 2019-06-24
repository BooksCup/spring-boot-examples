package com.bc.es.service.impl;

import com.bc.es.entity.Goods;
import com.bc.es.repository.GoodsRepository;
import com.bc.es.service.GoodsService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 商品业务类实现
 *
 * @author zhou
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 保存商品文档
     *
     * @param goods 商品
     */
    @Override
    public void save(Goods goods) {
        goodsRepository.save(goods);
    }

    /**
     * 搜索商品
     *
     * @param queryBuilder 请求参数
     * @param pageable     分页参数
     * @return 搜索结果
     */
    @Override
    public Page<Goods> search(QueryBuilder queryBuilder, Pageable pageable) {
        Page<Goods> resultPage = goodsRepository.search(queryBuilder, pageable);
        return resultPage;
    }

}
