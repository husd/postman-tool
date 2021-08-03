package com.husd.postman.util;

import com.husd.postman.domain.PostmanInfo;
import com.husd.postman.domain.PostmanItemWithFolder;
import com.husd.postman.domain.PostmanItemWithoutFolder;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

public class PostmanUtil {

    public static PostmanInfo postman_info_v2_1_0_json(String id, String name) {

        PostmanInfo postmanInfo = new PostmanInfo();
        postmanInfo.set_postman_id(id);
        postmanInfo.setName(name);
        postmanInfo.setSchema("https://schema.getpostman.com/json/collection/v2.1.0/collection.json");
        return postmanInfo;
    }

    public static PostmanItemWithoutFolder postman_item(String name, String url, RequestMethod method, String json) {

        PostmanItemWithoutFolder postmanItem = new PostmanItemWithoutFolder();

        postmanItem.setName(name);
        postmanItem.setRequest(PostmanRawUtil.postman_item_request_raw(url, method, json));
        postmanItem.setResponse(new ArrayList<>());
        return postmanItem;
    }

    public static PostmanItemWithFolder postman_item_folder(String folderName, String urlName, String url, RequestMethod requestMethod, String json) {

        PostmanItemWithFolder postmanItem = new PostmanItemWithFolder();

        postmanItem.setName(folderName);
        List<PostmanItemWithoutFolder> item = new ArrayList<>();
        item.add(postman_item(urlName, url, requestMethod, json));
        postmanItem.setItem(item);
        return postmanItem;
    }
}
