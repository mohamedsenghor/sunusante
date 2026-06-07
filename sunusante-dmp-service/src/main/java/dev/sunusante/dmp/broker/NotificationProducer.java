package dev.sunusante.dmp.broker;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    private final StreamBridge streamBridge;

    public NotificationProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendNotification(String patientPseudo, String email, String message) {
        log.debug("Sending notification for patient {} ({}) : {}", patientPseudo, email, message);
        Map<String, String> payload = new HashMap<>();
        payload.put("patientPseudo", patientPseudo);
        payload.put("email", email);
        payload.put("message", message);
        payload.put("type", "EMAIL");
        
        streamBridge.send("notification-out-0", payload);
    }
}
