package com.husd.postman.domain;

import java.util.List;

/**
 * v2.1.0
 */
public class Postman {

    private PostmanInfo info;
    private List<PostmanItem> item;

    public PostmanInfo getInfo() {
        return info;
    }

    public void setInfo(PostmanInfo info) {
        this.info = info;
    }

    public List<PostmanItem> getItem() {
        return item;
    }

    public void setItem(List<PostmanItem> item) {
        this.item = item;
    }
}
