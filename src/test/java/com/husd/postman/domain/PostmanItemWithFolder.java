package com.husd.postman.domain;

import java.util.List;

/**
 * v2.1.0
 * 这个是带目录的结构
 */
public class PostmanItemWithFolder extends PostmanItem {

    private List<PostmanItemWithoutFolder> item;

    public List<PostmanItemWithoutFolder> getItem() {
        return item;
    }

    public void setItem(List<PostmanItemWithoutFolder> item) {
        this.item = item;
    }
}
