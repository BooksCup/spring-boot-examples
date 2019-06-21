package com.bc.es.controller;

import com.bc.es.entity.Goods;
import com.bc.es.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @ApiOperation(value = "创建商品", notes = "创建商品")
    @PostMapping(value = "")
    public String saveGoods(@RequestParam String goodsName,
                            @RequestParam String goodsSeoKeyWord,
                            @RequestParam String goodsSeoDescription) {
        Goods goods = new Goods(goodsName, goodsSeoKeyWord, goodsSeoDescription);
        goodsService.save(goods);
        return "ok";
    }
}
