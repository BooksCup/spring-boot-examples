package com.bc.mongodb.controller;

import com.bc.mongodb.entity.NginxAccessLog;
import com.bc.mongodb.service.NginxAccessLogService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * nginx访问日志
 *
 * @author zhou
 */
@RestController
@RequestMapping("/nginxAccessLogs")
public class NginxAccessLogController {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(NginxAccessLogController.class);

    @Resource
    private NginxAccessLogService nginxAccessLogService;

    /**
     * 分页查询Nginx访问日志
     *
     * @param uri           uri
     * @param status        HTTP状态码
     * @param page          页码
     * @param pageSize      每页记录的条数
     * @param sortField     排序域
     * @param sortDirection 排序方式 "asc":"升序"  "desc":"降序"
     * @return ResponseEntity
     */
    @ApiOperation(value = "分页查询Nginx访问日志", notes = "分页查询Nginx访问日志")
    @GetMapping(value = "")
    public ResponseEntity<PageImpl<NginxAccessLog>> getNginxAccessLogPage(
            @RequestParam(value = "uri", required = false) String uri,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) String sortDirection) {

        try {
            PageImpl<NginxAccessLog> nginxAccessLogPage = nginxAccessLogService.
                    getNginxAccessLogPageByStatus(uri, status, page, pageSize, sortField, sortDirection);
            return new ResponseEntity<>(nginxAccessLogPage, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getNginxAccessLogPage error: " + e.getMessage());
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<>()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
