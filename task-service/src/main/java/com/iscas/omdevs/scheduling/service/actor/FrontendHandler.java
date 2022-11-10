package com.iscas.omdevs.scheduling.service.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class FrontendHandler extends AbstractBehavior<String> {


    public static Behavior<String> create() {
        return Behaviors.setup(FrontendHandler::new);
    }

    private FrontendHandler(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessage(String.class, s -> {
                    return Behaviors.same();
                }).build();
    }
}
