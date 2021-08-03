package com.husd.postman.domain.request;

public class PostmanBody {

    private String mode;
    private String raw;
    private PostmanBodyOptions options;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public PostmanBodyOptions getOptions() {
        return options;
    }

    public void setOptions(PostmanBodyOptions options) {
        this.options = options;
    }
}

