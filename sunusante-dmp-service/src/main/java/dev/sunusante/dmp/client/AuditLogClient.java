package dev.sunusante.dmp.client;

import dev.sunusante.dmp.config.FeignConfiguration;
import java.time.Instant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sunusanteAuditService", configuration = FeignConfiguration.class)
public interface AuditLogClient {
    @PostMapping("/api/audit-logs")
    void createAuditLog(@RequestBody AuditLogDTO auditLogDTO);

    class AuditLogDTO {
        private Long id;
        private Instant timestamp;
        private String principal;
        private String resourceId;
        private String action;
        private String oldValue;
        private String newValue;
        private String clientIp;
        private Boolean isSuccess;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public String getPrincipal() { return principal; }
        public void setPrincipal(String principal) { this.principal = principal; }
        public String getResourceId() { return resourceId; }
        public void setResourceId(String resourceId) { this.resourceId = resourceId; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getOldValue() { return oldValue; }
        public void setOldValue(String oldValue) { this.oldValue = oldValue; }
        public String getNewValue() { return newValue; }
        public void setNewValue(String newValue) { this.newValue = newValue; }
        public String getClientIp() { return clientIp; }
        public void setClientIp(String clientIp) { this.clientIp = clientIp; }
        public Boolean getIsSuccess() { return isSuccess; }
        public void setIsSuccess(Boolean isSuccess) { this.isSuccess = isSuccess; }
    }
}
