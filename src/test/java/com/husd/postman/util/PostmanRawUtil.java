package com.husd.postman.util;

import com.husd.postman.domain.PostmanItemRequest;
import com.husd.postman.domain.request.PostmanBody;
import com.husd.postman.domain.request.PostmanBodyOptions;
import com.husd.postman.domain.request.PostmanBodyOptionsRaw;
import com.husd.postman.domain.request.PostmanUrl;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

public class PostmanRawUtil {

    private final static String ENV_NAME = "{{your_url}}";

    static PostmanItemRequest postman_item_request_raw(String url, RequestMethod method, String json) {

        PostmanItemRequest request = new PostmanItemRequest();

        request.setMethod(method.toString());
        request.setHeader(new ArrayList<>());
        request.setBody(postman_item_request_body(json));
        request.setUrl(postman_url(url));

        return request;
    }

    private static PostmanUrl postman_url(String url) {

        if (url.startsWith("/")) {
            url = url.substring(1, url.length());
        }
        PostmanUrl postmanUrl = new PostmanUrl();

        postmanUrl.setRaw(ENV_NAME + "/" + url);
        postmanUrl.setHost(ENV_NAME);

        List<String> pathList = new ArrayList<>();
        String[] arr = url.split("/");
        for (String s : arr) {
            pathList.add(s);
        }

        postmanUrl.setPath(pathList);

        return postmanUrl;
    }

    static PostmanBody postman_item_request_body(String json) {

        PostmanBody postmanBody = new PostmanBody();
        postmanBody.setMode("raw");
        postmanBody.setRaw(json);
        postmanBody.setOptions(postman_item_request_body_options());
        return postmanBody;
    }

    private static PostmanBodyOptions postman_item_request_body_options() {

        PostmanBodyOptions options = new PostmanBodyOptions();

        PostmanBodyOptionsRaw raw = new PostmanBodyOptionsRaw();
        raw.setLanguage("json");
        options.setRaw(raw);

        return options;
    }
}
