package com.bc.mongodb.entity;

/**
 * Nginx访问日志
 *
 * @author zhou
 */
public class NginxAccessLog {
    private String time_local;
    private String remote_addr;
    private String remote_user;
    private Long body_bytes_sent;
    private String request_time;
    private Integer status;
    private String host;
    private String request;
    private String request_method;
    private String uri;
    private String http_referrer;
    private String http_x_forwarded_for;
    private String http_user_agent;

    public String getTime_local() {
        return time_local;
    }

    public void setTime_local(String time_local) {
        this.time_local = time_local;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public Long getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(Long body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest_method() {
        return request_method;
    }

    public void setRequest_method(String request_method) {
        this.request_method = request_method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttp_referrer() {
        return http_referrer;
    }

    public void setHttp_referrer(String http_referrer) {
        this.http_referrer = http_referrer;
    }

    public String getHttp_x_forwarded_for() {
        return http_x_forwarded_for;
    }

    public void setHttp_x_forwarded_for(String http_x_forwarded_for) {
        this.http_x_forwarded_for = http_x_forwarded_for;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    @Override
    public String toString() {
        return "NginxAccessLog{" +
                "time_local='" + time_local + '\'' +
                ", remote_addr='" + remote_addr + '\'' +
                ", remote_user='" + remote_user + '\'' +
                ", body_bytes_sent=" + body_bytes_sent +
                ", request_time='" + request_time + '\'' +
                ", status=" + status +
                ", host='" + host + '\'' +
                ", request='" + request + '\'' +
                ", request_method='" + request_method + '\'' +
                ", uri='" + uri + '\'' +
                ", http_referrer='" + http_referrer + '\'' +
                ", http_x_forwarded_for='" + http_x_forwarded_for + '\'' +
                ", http_user_agent='" + http_user_agent + '\'' +
                '}';
    }
}
