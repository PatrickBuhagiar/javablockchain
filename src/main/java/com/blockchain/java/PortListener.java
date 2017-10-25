package com.blockchain.java;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PortListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static int port;

    @Override
    public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
        port = event.getEmbeddedServletContainer().getPort();
    }

    static int getPort() {
        return port;
    }
}