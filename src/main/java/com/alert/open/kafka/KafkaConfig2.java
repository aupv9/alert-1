package com.alert.open.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import java.util.concurrent.TimeUnit;

@Configuration
public class KafkaConfig2 {

//    @Bean
//    public NewTopic bookingTopic() {
//        // Tạo topic với 3 partitions để xử lý song song
//        return TopicBuilder.name("agent-bookings")
//                .partitions(3)
//                .replicas(2)
//                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(TimeUnit.DAYS.toMillis(7)))
//                .build();
//    }

}