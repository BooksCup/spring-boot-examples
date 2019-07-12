package com.bc.mongodb.test;

import com.bc.mongodb.MongoDbApplication;
import com.bc.mongodb.entity.NginxAccessLog;
import com.bc.mongodb.service.NginxAccessLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试操作mongodb
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MongoDbApplication.class)
public class TestMongoDbOps {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestMongoDbOps.class);

    @Resource
    private NginxAccessLogService nginxAccessLogService;

    @Test
    public void testGetNginxAccessLogPageByStatus() {
        PageImpl<NginxAccessLog> nginxAccessLogPage = nginxAccessLogService.
                getNginxAccessLogPageByStatus(404, 2, 1, "request_time", "desc");
        int totalPages = nginxAccessLogPage.getTotalPages();
        long totalElements = nginxAccessLogPage.getTotalElements();
        logger.info("totalPages: " + totalPages);
        logger.info("totalElements: " + totalElements);
        List<NginxAccessLog> nginxAccessLogList = nginxAccessLogPage.getContent();
        for (NginxAccessLog nginxAccessLog : nginxAccessLogList) {
            logger.info("nginxAccessLog: " + nginxAccessLog);
        }
    }
}
