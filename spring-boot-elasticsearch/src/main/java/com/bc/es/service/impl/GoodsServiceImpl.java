package com.bc.es.service.impl;

import com.bc.es.entity.Goods;
import com.bc.es.repository.GoodsRepository;
import com.bc.es.service.GoodsService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public void save(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public Page<Goods> search(QueryBuilder queryBuilder, Pageable pageable) {
        Page<Goods> resultPage = goodsRepository.search(queryBuilder, pageable);
        return resultPage;
    }

}
