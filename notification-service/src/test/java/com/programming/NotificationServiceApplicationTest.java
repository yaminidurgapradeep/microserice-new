package com.programming;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class NotificationServiceApplicationTest {

    private ListAppender<ILoggingEvent> listAppender;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @BeforeEach
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(NotificationServiceApplication.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    public void testHandleNotification() {
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent("12345");
        NotificationServiceApplication notificationServiceApplication = new NotificationServiceApplication(kafkaTemplate);

        notificationServiceApplication.handleNotification(orderPlacedEvent);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Received Notification for Order - 12345");
    }
}