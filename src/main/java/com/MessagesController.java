package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/message" )
@EnableAutoConfiguration
public class MessagesController {

    private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    private IngestManager ingestManager;


    @RequestMapping(method = RequestMethod.POST)
    public UUID ingestMessage(@RequestBody String message){

        return ingestManager.ingest(message);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{uuid}")
    public ProcessingStatus getMessageStatus(@PathVariable UUID uuid){

        return ingestManager.getMessageStatus(uuid);
    }
}
