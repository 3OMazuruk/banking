package com.banking.threeom.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking-3-om-kafka")
public class Banking3OmKafkaResource {

    private final Logger log = LoggerFactory.getLogger(Banking3OmKafkaResource.class);

    /**
     * ToDO:
     * - implement state of the art emitter repository to become 12 factor
     * - @PostMapping("/publish")
     * - @GetMapping("/register")
     * - @GetMapping("/unregister")
     * - @StreamListener
     */
}
