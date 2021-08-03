package com.husd.postman.domain.request;

import java.util.List;

public class PostmanUrl {

    //{{one}}/app/listByProjectId
    private String raw;
    //{{one}}
    private String host;
    // app listByProjectId
    private List<String> path;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
