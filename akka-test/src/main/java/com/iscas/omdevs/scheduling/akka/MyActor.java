package com.iscas.omdevs.scheduling.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyActor extends AbstractActor {

    static Props props() {
        return Props.create(MyActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    log.info("Received String message: {}", s);
                })
                .matchAny(o -> {
                    log.info("received unknown message " + o);
                })
                .build();
    }
}
