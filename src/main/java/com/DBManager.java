package com;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DBManager {

    private ConcurrentHashMap<UUID, ProcessingStatus> messagesStatus = new ConcurrentHashMap<>();

    public void saveMessageStatus(IngestManager.Message message, ProcessingStatus status){
        messagesStatus.put(message.getUuid(), status);
    }

    public ProcessingStatus getMessageStatus(UUID uuid) {
        return messagesStatus.get(uuid);
    }

}
