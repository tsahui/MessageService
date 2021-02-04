package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@EnableScheduling
public class ProcessManager {

    private static ConcurrentLinkedQueue<IngestManager.Message> messagesQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    private DBManager dbManager;

    @Async
    public void processMessage(IngestManager.Message message){
        try {
            messagesQueue.add(message);
        } catch (Exception e) {
            dbManager.saveMessageStatus(message, ProcessingStatus.Error);
        }
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 30000)
    public void messagesProcessingWorker(){
        while ( ! messagesQueue.isEmpty() ){

            IngestManager.Message message = messagesQueue.poll();

            try {
                dbManager.saveMessageStatus(message, ProcessingStatus.Processing);

                String messageStr = message.getMessage();
                System.out.println(messageStr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //do nothing
                }
                messageStr = messageStr.toUpperCase();
                System.out.println(messageStr);

                dbManager.saveMessageStatus(message, ProcessingStatus.Complete);
            } catch (Exception e) {
                dbManager.saveMessageStatus(message, ProcessingStatus.Error);
            }
        }
    }
}
