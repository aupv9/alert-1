package com.alert.open;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.actuate.metrics.web.tomcat.TomcatMetricsBinder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerMetricsConfiguration {

    @Bean
    public TomcatMetricsBinder tomcatMetricsBinder(MeterRegistry registry) {
        return new TomcatMetricsBinder(registry);
    }

    @Bean
    @ConfigurationProperties(prefix = "server.tomcat")
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;
                protocol.setMaxConnections(10000);
                protocol.setMaxThreads(400);
                protocol.setConnectionTimeout(5000);
            }
        });
        return factory;
    }
}