package com;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessagesApplication.class)
@WebAppConfiguration
public class SystemTest {

    @Autowired
    private IngestManager ingestManager;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testMessageProcessing(){
        String message = "message";
        UUID uuid = ingestManager.ingest(message);

        ProcessingStatus status = ingestManager.getMessageStatus(uuid);

        Assert.assertNotNull(uuid);
        Assert.assertEquals(ProcessingStatus.Accepted, status);
    }

    @Test
    public void testMessagesProcessingNotFinished(){
        List<UUID> listOfUuids = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++) {
            String message = "message number: " + i;
            listOfUuids.add(ingestManager.ingest(message));
        }

        for (UUID uuid : listOfUuids) {
            ProcessingStatus status = ingestManager.getMessageStatus(uuid);
            Assert.assertEquals(ProcessingStatus.Accepted, status);
        }
    }

    @Test
    public void testMessagesProcessingFinished(){
        List<UUID> listOfUuids = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++) {
            String message = "message number: " + i;
            listOfUuids.add(ingestManager.ingest(message));
        }

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            //do nothing
        }
        for (UUID uuid : listOfUuids) {
            ProcessingStatus status = ingestManager.getMessageStatus(uuid);
            Assert.assertEquals(ProcessingStatus.Complete, status);
        }
    }

    @Test
    public void testMessagesProcessingInProgress(){
        List<UUID> listOfUuids = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++) {
            String message = "message number: " + i;
            listOfUuids.add(ingestManager.ingest(message));
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            //do nothing
        }
        List<ProcessingStatus> statuses = new ArrayList<>();
        for (UUID uuid : listOfUuids) {
            statuses.add(ingestManager.getMessageStatus(uuid));
        }
        Assert.assertTrue(statuses.contains(ProcessingStatus.Processing));
    }

}
