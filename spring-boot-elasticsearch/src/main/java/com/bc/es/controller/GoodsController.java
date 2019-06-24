package com.bc.es.controller;

import com.bc.es.entity.Goods;
import com.bc.es.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商品
 *
 * @author zhou
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    /**
     * 创建商品
     *
     * @param goodsName           商品名
     * @param goodsSeoKeyWord     SEO关键字
     * @param goodsSeoDescription SEO描述
     * @return ResponseEntity
     */
    @ApiOperation(value = "创建商品", notes = "创建商品")
    @PostMapping(value = "")
    public String saveGoods(@RequestParam String goodsName,
                            @RequestParam String goodsSeoKeyWord,
                            @RequestParam String goodsSeoDescription) {
        Goods goods = new Goods(goodsName, goodsSeoKeyWord, goodsSeoDescription);
        goodsService.save(goods);
        return "ok";
    }

    /**
     * 搜索商品
     *
     * @param searchKey 搜索关键字
     * @param page      页数(默认第1页)
     * @param pageSize  分页大小(默认单页10条)
     * @return 搜索结果
     */
    @ApiOperation(value = "搜索商品", notes = "搜索商品")
    @GetMapping(value = "")
    public Page<Goods> search(@RequestParam String searchKey,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(searchKey)) {
            // must
            // 根据关键字匹配若干字段,商品名称、SEO关键字及SEO描述
            MultiMatchQueryBuilder keywordMmqb = QueryBuilders.multiMatchQuery(searchKey,
                    "name", "seoKeyWords", "seoDescription");
            boolQuery = boolQuery.must(keywordMmqb);
        }
        page = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Goods> resultPage = goodsService.search(boolQuery, pageable);
        return resultPage;
    }
}
