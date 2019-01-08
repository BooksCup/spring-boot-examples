package com.bc.es.controller;

import com.bc.es.cons.Constants;
import com.bc.es.entity.Goods;
import com.bc.es.enums.ResponseMsg;
import com.bc.es.service.GoodsService;
import com.bc.es.utils.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品
 *
 * @author zhou
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建商品
     *
     * @param goodsName           商品名
     * @param category            商品类别
     * @param goodsSeoKeyWord     SEO关键字
     * @param goodsSeoDescription SEO描述
     * @param goodsPrice          单价
     * @param goodsSalesVolume    销量
     * @return ResponseEntity
     */
    @ApiOperation(value = "创建商品", notes = "创建商品")
    @PostMapping(value = "")
    public ResponseEntity<String> saveGoods(@RequestParam String goodsName,
                                            @RequestParam String category,
                                            @RequestParam String goodsSeoKeyWord,
                                            @RequestParam String goodsSeoDescription,
                                            @RequestParam String goodsPrice,
                                            @RequestParam String goodsSalesVolume) {
        BigDecimal price;
        try {
            price = new BigDecimal(goodsPrice);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_ADD_GOODS_PRICE_PARAMS_FORMAT_ERROR.value(),
                    HttpStatus.BAD_REQUEST);
        }
        Long salesVolume;
        try {
            salesVolume = Long.valueOf(goodsSalesVolume);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_ADD_GOODS_SALES_PARAMS_FORMAT_ERROR.value(),
                    HttpStatus.BAD_REQUEST);
        }

        Goods goods = new Goods(goodsName, category, goodsSeoKeyWord, goodsSeoDescription, price, salesVolume);
        try {
            goodsService.save(goods);
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_ADD_GOODS_SUCCESS.value(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_ADD_GOODS_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 搜索商品
     * 版本号: v1
     * 直接通过Query查询
     *
     * @param searchKey     搜索关键字
     * @param page          页数(默认第1页)
     * @param pageSize      分页大小(默认单页10条)
     * @param sortField     排序字段
     * @param sortDirection ASC: "升序"  DESC:"降序"
     * @return 搜索结果
     */
    @ApiOperation(value = "搜索商品(版本号v1: 简单查询)", notes = "搜索商品(版本号v1: 简单查询)")
    @GetMapping(value = "/v1")
    public Page<Goods> searchV1(@RequestParam String searchKey,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "sortField", required = false) String sortField,
                                @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(searchKey)) {
            // must
            // 根据关键字匹配若干字段,商品名称、SEO关键字及SEO描述
            MultiMatchQueryBuilder keywordMmqb = QueryBuilders.multiMatchQuery(searchKey,
                    "name", "seoKeyWords", "seoDescription");
            boolQuery = boolQuery.must(keywordMmqb);
        }
        Pageable pageable = generatePageable(page, pageSize, sortField, sortDirection);
        return goodsService.search(boolQuery, pageable);
    }

    /**
     * 搜索商品
     * 版本号: v2
     * 构建复杂条件查询
     *
     * @param searchKey     搜索关键字
     * @param category      商品类别
     * @param page          页数(默认第1页)
     * @param pageSize      分页大小(默认单页10条)
     * @param sortField     排序字段
     * @param sortDirection ASC: "升序"  DESC:"降序"
     * @return 搜索结果
     */
    @ApiOperation(value = "搜索商品(版本号v2: 查询 + 过滤)", notes = "搜索商品(版本号v2: 查询 + 过滤)")
    @GetMapping(value = "/v2")
    public Page<Goods> searchV2(@RequestParam String searchKey,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "sortField", required = false) String sortField,
                                @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(searchKey)) {
            // must
            // 根据关键字匹配若干字段,商品名称、SEO关键字及SEO描述
            MultiMatchQueryBuilder keywordMmqb = QueryBuilders.multiMatchQuery(searchKey,
                    "name", "seoKeyWords", "seoDescription");
            boolQuery = boolQuery.must(keywordMmqb);
        }

        QueryBuilder postFilter = null;
        if (!StringUtils.isEmpty(category)) {
            // 类别不为空
            // 根据类别过滤
            postFilter = QueryBuilders.termQuery("category", category);
        }

        Pageable pageable = generatePageable(page, pageSize, sortField, sortDirection);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQuery).withPageable(pageable);
        if (null != postFilter) {
            nativeSearchQueryBuilder = nativeSearchQueryBuilder.withFilter(postFilter);
        }

        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        return goodsService.search(searchQuery);
    }


    /**
     * 搜索商品
     * 版本号: v3
     * 构建复杂条件查询
     * 高亮查询
     *
     * @param searchKey     搜索关键字
     * @param category      商品类别
     * @param page          页数(默认第1页)
     * @param pageSize      分页大小(默认单页10条)
     * @param sortField     排序字段
     * @param sortDirection ASC: "升序"  DESC:"降序"
     * @param highLightFlag 高亮FLAG开关 0:"关闭"  1:"开启" 默认开启
     * @return 搜索结果
     */
    @ApiOperation(value = "搜索商品(版本号v3: 高亮查询)", notes = "搜索商品(版本号v3: 高亮查询)")
    @GetMapping(value = "/v3")
    public Page<Goods> searchV3(@RequestParam String searchKey,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "sortField", required = false) String sortField,
                                @RequestParam(value = "sortDirection", required = false) String sortDirection,
                                @RequestParam(value = "highLightFlag", required = false, defaultValue = "1") String highLightFlag) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        List<String> highLightFieldList = new ArrayList<>();
        highLightFieldList.add("name");
        highLightFieldList.add("seoKeyWords");
        highLightFieldList.add("seoDescription");

        // 关闭高亮可以直接清除所有高亮域
        if (Constants.HIGHLIGHT_FLAG_CLOSE.equals(highLightFlag)) {
            highLightFieldList.clear();
        }

        if (!StringUtils.isEmpty(searchKey)) {
            // must
            // 根据关键字匹配若干字段,商品名称、SEO关键字及SEO描述
            MultiMatchQueryBuilder keywordMmqb = QueryBuilders.multiMatchQuery(searchKey,
                    "name", "seoKeyWords", "seoDescription");
            boolQuery = boolQuery.must(keywordMmqb);
        }

        QueryBuilder postFilter = null;
        if (!StringUtils.isEmpty(category)) {
            // 类别不为空
            // 根据类别过滤
            postFilter = QueryBuilders.termQuery("category", category);
        }

        HighlightBuilder.Field[] highLightFields = new HighlightBuilder.Field[highLightFieldList.size()];

        for (int i = 0; i < highLightFieldList.size(); i++) {
            highLightFields[i] = new HighlightBuilder.Field(highLightFieldList.get(i))
                    .preTags("<font color='red'>")
                    .postTags("</font>")
                    .fragmentSize(250);
        }


        Pageable pageable = generatePageable(page, pageSize, sortField, sortDirection);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQuery).withPageable(pageable);
        if (null != postFilter) {
            nativeSearchQueryBuilder = nativeSearchQueryBuilder.withFilter(postFilter);
        }
        nativeSearchQueryBuilder = nativeSearchQueryBuilder.withHighlightFields(highLightFields);

        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        return elasticsearchTemplate.queryForPage(searchQuery, Goods.class, new SearchResultMapper() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits searchHits = response.getHits();
                long totalHits = searchHits.getTotalHits();
                List<T> results = new ArrayList<>();
                for (SearchHit searchHit : searchHits) {
                    Map<String, Object> entityMap = searchHit.getSourceAsMap();
                    for (String highLightField : highLightFieldList) {
                        String highLightValue = searchHit.getHighlightFields().get(highLightField).fragments()[0].toString();
                        entityMap.put(highLightField, highLightValue);
                    }
                    results.add((T) CommonUtil.map2Object(entityMap, Goods.class));
                }
                return new AggregatedPageImpl<>(results, pageable, totalHits, response.getAggregations(), response.getScrollId());
            }
        });
    }

    /**
     * 删除商品
     *
     * @param goodsId 商品id
     * @return ResponseEntity
     */
    @ApiOperation(value = "删除商品", notes = "删除商品")
    @DeleteMapping(value = "/{goodsId}")
    public ResponseEntity<String> deleteGoodsById(@PathVariable String goodsId) {
        try {
            goodsService.deleteById(goodsId);
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_DELETE_GOODS_SUCCESS.value(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ResponseMsg.DOCUMENT_DELETE_GOODS_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 生成分页参数
     *
     * @param page          页数(默认第1页)
     * @param pageSize      分页大小(默认单页10条)
     * @param sortField     排序字段
     * @param sortDirection ASC: "升序"  DESC:"降序"
     * @return 分页参数
     */
    private Pageable generatePageable(Integer page, Integer pageSize, String sortField, String sortDirection) {
        page = page < 1 ? 0 : page - 1;
        Pageable pageable;
        if (!StringUtils.isEmpty(sortField)) {
            // 需要排序
            Sort.Direction direction;
            if (Constants.SORT_DIRECTION_ASC.equalsIgnoreCase(sortDirection)) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }
            Sort sort = new Sort(direction, sortField);
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(page, pageSize);
        }
        return pageable;
    }
}
