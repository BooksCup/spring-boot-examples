package com.bc.es.service;

import com.bc.es.entity.Goods;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsService {
    void save(Goods goods);

    Page<Goods> search(QueryBuilder queryBuilder, Pageable pageable);
}
