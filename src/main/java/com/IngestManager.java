package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IngestManager {

    @Autowired
    private ProcessManager processManager;

    @Autowired
    private DBManager dbManager;

    public UUID ingest(String message){

        Message messageObj = new Message(UUID.randomUUID(), message);

        dbManager.saveMessageStatus(messageObj, ProcessingStatus.Accepted);

        processManager.processMessage(messageObj);

        return messageObj.uuid;
    }

    public ProcessingStatus getMessageStatus(UUID uuid) {

        ProcessingStatus status = dbManager.getMessageStatus(uuid);
        return status == null ? ProcessingStatus.Not_Found : status;
    }

    public class Message {
        private UUID uuid;
        private String message;

        public Message(UUID uuid, String message) {
            this.uuid = uuid;
            this.message = message;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getMessage() {
            return message;
        }
    }
}
