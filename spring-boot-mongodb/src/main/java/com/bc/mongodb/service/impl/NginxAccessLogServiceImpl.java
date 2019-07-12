package com.bc.mongodb.service.impl;

import com.bc.mongodb.cons.Constants;
import com.bc.mongodb.entity.NginxAccessLog;
import com.bc.mongodb.service.NginxAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Nginx访问日志业务类实现
 *
 * @author zhou
 */
@Service("nginxAccessLogService")
public class NginxAccessLogServiceImpl implements NginxAccessLogService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 分页查询Nginx访问日志
     *
     * @param uri           uri
     * @param status        HTTP状态码
     * @param page          页码
     * @param pageSize      每页记录的条数
     * @param sortField     排序域
     * @param sortDirection 排序方式 "asc":"升序"  "desc":"降序"
     * @return 包含Nginx访问日志列表的分页信息
     */
    @Override
    public PageImpl<NginxAccessLog> getNginxAccessLogPageByStatus(
            String uri, Integer status, Integer page, Integer pageSize, String sortField, String sortDirection) {
        Query query = new Query();
        if (!StringUtils.isEmpty(uri)) {
            query.addCriteria(Criteria.where("uri").is(uri));
        }
        if (null != status) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        page = page - 1 < 0 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        query.with(pageable);
        if (!StringUtils.isEmpty(sortField)) {
            // 默认倒序
            if (Constants.SORT_DIRECTION_ASC.equalsIgnoreCase(sortDirection)) {
                query.with(new Sort(Sort.Direction.ASC, sortField));
            } else {
                query.with(new Sort(Sort.Direction.DESC, sortField));
            }
        }

        // 查询总数
        int count = (int) mongoTemplate.count(query, NginxAccessLog.class, Constants.COLLECTION_NGINX_ACCESS_LOG);
        List<NginxAccessLog> nginxAccessLogList = mongoTemplate.find(query,
                NginxAccessLog.class, Constants.COLLECTION_NGINX_ACCESS_LOG);
        return (PageImpl<NginxAccessLog>) PageableExecutionUtils.getPage(nginxAccessLogList, pageable, () -> count);
    }

}
