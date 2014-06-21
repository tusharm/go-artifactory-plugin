package com.tw.go.plugins.artifactory;

import org.jfrog.build.api.util.Log;

import static java.lang.String.format;

public class Logger implements Log {
    public static Logger getLogger(Class clazz) {
        return new Logger(com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor(clazz));
    }

    private com.thoughtworks.go.plugin.api.logging.Logger pluginLogger;

    private Logger(com.thoughtworks.go.plugin.api.logging.Logger pluginLogger) {
        this.pluginLogger = pluginLogger;
    }

    @Override
    public void debug(String message) {
        pluginLogger.debug(message);
    }

    @Override
    public void info(String message) {
        pluginLogger.info(message);
    }

    @Override
    public void warn(String message) {
        pluginLogger.warn(message);
    }

    @Override
    public void error(String message) {
        pluginLogger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        error(format("%s. Root cause: %s\n%s", message, throwable.getMessage(), stackTrace(throwable)));
    }

    private String stackTrace(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.append("\t").append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
}
