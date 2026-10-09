package com.gateway.model;

public class RequestDTO {

    private String ipAddress;
    private HttpMethod method;
    private String url;
    private String payload;

    public RequestDTO() {

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getPayload() {
        return payload;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
