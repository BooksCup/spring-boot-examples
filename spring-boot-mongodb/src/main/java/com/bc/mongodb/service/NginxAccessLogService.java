package com.bc.mongodb.service;

import com.bc.mongodb.entity.NginxAccessLog;
import org.springframework.data.domain.PageImpl;

/**
 * Nginx访问日志业务类接口
 *
 * @author zhou
 */
public interface NginxAccessLogService {
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
    PageImpl<NginxAccessLog> getNginxAccessLogPageByStatus(
            String uri, Integer status, Integer page, Integer pageSize, String sortField, String sortDirection);
}
