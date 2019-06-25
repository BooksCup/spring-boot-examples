package com.bc.es.controller;

import com.bc.es.cons.Constants;
import com.bc.es.entity.Goods;
import com.bc.es.enums.ResponseMsg;
import com.bc.es.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
    @ApiOperation(value = "搜索商品(版本号:v1)", notes = "搜索商品(版本号:v1)")
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
        Page<Goods> resultPage = goodsService.search(boolQuery, pageable);
        return resultPage;
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
    @ApiOperation(value = "搜索商品(版本号:v2)", notes = "搜索商品(版本号:v2)")
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
        Page<Goods> resultPage = goodsService.search(searchQuery);
        return resultPage;
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
