package com.husd.postman.domain;

import java.util.List;

/**
 * v2.1.0
 * 这个是不带目录的结构
 */
public class PostmanItemWithoutFolder extends PostmanItem {

    private PostmanItemRequest request;
    private List<PostmanItemResponse> response;

    public PostmanItemRequest getRequest() {
        return request;
    }

    public void setRequest(PostmanItemRequest request) {
        this.request = request;
    }

    public List<PostmanItemResponse> getResponse() {
        return response;
    }

    public void setResponse(List<PostmanItemResponse> response) {
        this.response = response;
    }
}
