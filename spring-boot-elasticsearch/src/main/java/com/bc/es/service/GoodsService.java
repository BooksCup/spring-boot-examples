package com.bc.es.service;

import com.bc.es.entity.Goods;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

public interface GoodsService {
    void save(Goods goods);
}
