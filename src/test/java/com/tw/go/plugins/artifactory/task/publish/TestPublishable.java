package com.tw.go.plugins.artifactory.task.publish;

public class TestPublishable implements Publishable {
    private String name;
    private String content;

    public TestPublishable(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String content() {
        return content;
    }
}
