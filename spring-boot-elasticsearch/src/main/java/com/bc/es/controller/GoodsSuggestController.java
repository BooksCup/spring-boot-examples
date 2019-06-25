package com.bc.es.controller;

import com.bc.es.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品搜索词补齐
 *
 * @author zhou
 */
@RestController
@RequestMapping("/goodsSuggests")
public class GoodsSuggestController {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GoodsSuggestController.class);

    @Resource
    private GoodsService goodsService;

    /**
     * 商品搜索词补齐
     *
     * @param prefix  搜索前缀
     * @param topSize 搜索结果数量
     * @return 搜索结果列表
     */
    @ApiOperation(value = "商品搜索词补齐", notes = "商品搜索词补齐")
    @GetMapping(value = "")
    public List<String> suggest(@RequestParam(required = false) String prefix,
                                @RequestParam(value = "topSize", required = false, defaultValue = "5") Integer topSize) {
        logger.info("suggest: " + prefix);
        if (StringUtils.isEmpty(prefix)) {
            return new ArrayList<>();
        }
        List<String> suggestList = goodsService.suggestSearch("seoKeyWords", prefix, topSize);

        return suggestList;
    }
}
