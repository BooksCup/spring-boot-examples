package com.bc.es.controller;

import com.bc.es.entity.Goods;
import com.bc.es.enums.ResponseMsg;
import com.bc.es.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * @param goodsSeoKeyWord     SEO关键字
     * @param goodsSeoDescription SEO描述
     * @param goodsPrice          单价
     * @param goodsSalesVolume    销量
     * @return ResponseEntity
     */
    @ApiOperation(value = "创建商品", notes = "创建商品")
    @PostMapping(value = "")
    public ResponseEntity<String> saveGoods(@RequestParam String goodsName,
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

        Goods goods = new Goods(goodsName, goodsSeoKeyWord, goodsSeoDescription, price, salesVolume);
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
