package com.bc.es.service.impl;

import com.bc.es.entity.Goods;
import com.bc.es.repository.GoodsRepository;
import com.bc.es.service.GoodsService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品业务类实现
 *
 * @author zhou
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
        return goodsRepository.search(queryBuilder, pageable);
    }

    /**
     * 搜索商品
     *
     * @param searchQuery 查询参数
     * @return 搜索结果
     */
    @Override
    public Page<Goods> search(SearchQuery searchQuery) {
        return goodsRepository.search(searchQuery);
    }

    /**
     * 根据id删除商品文档
     *
     * @param goodsId 商品id
     */
    @Override
    public void deleteById(String goodsId) {
        goodsRepository.deleteById(goodsId);
    }

    /**
     * 补齐搜索
     *
     * @param field  补齐域
     * @param prefix 搜索前缀
     * @param size   搜索结果数量
     * @return 搜索结果列表
     */
    @Override
    public List<String> suggestSearch(String field, String prefix, Integer size) {
        CompletionSuggestionBuilder completionSuggestionBuilder = new CompletionSuggestionBuilder(
                field);
        completionSuggestionBuilder.text(prefix);
        completionSuggestionBuilder.size(size);

        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion(field,
                completionSuggestionBuilder);

        SearchResponse suggestResponse = elasticsearchTemplate.suggest(suggestBuilder, Goods.class);
        // 用来处理的接受结果
        List<String> resultList = new ArrayList<>();

        List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries = suggestResponse.getSuggest().getSuggestion(
                field).getEntries();
        // 处理结果
        for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> op : entries) {
            List<? extends Suggest.Suggestion.Entry.Option> options = op.getOptions();
            for (Suggest.Suggestion.Entry.Option option : options) {
                resultList.add(option.getText().toString());
            }
        }
        return resultList;
    }

}
