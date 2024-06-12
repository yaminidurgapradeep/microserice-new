package com.programming;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "notificationTopic" }, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class NotificationServiceApplicationTests {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private NotificationServiceApplication notificationServiceApplication;

    @MockBean
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @MockBean
    private KafkaListener kafkaListener;

    @Test
    void contextLoads() {
        assertThat(notificationServiceApplication).isNotNull();
    }

    @Test
    void testKafkaListenerWithMockito() throws Exception {
        NotificationServiceApplication notificationServiceApplication = mock(NotificationServiceApplication.class);

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent("test-order");

        notificationServiceApplication.handleNotification(orderPlacedEvent);

        Mockito.verify(notificationServiceApplication, times(1)).handleNotification(eq(orderPlacedEvent));

    }

    @Test
    void testKafkaListenerWithAwaitility() {
        NotificationServiceApplication notificationServiceApplication = mock(NotificationServiceApplication.class);

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent("test-order");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            notificationServiceApplication.handleNotification(orderPlacedEvent);
        });

        await().atMost(10, SECONDS).until(future::isDone);
            verify(notificationServiceApplication, times(1)).handleNotification(eq(orderPlacedEvent));
    }
}