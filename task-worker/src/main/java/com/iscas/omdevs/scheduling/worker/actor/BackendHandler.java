package com.iscas.omdevs.scheduling.worker.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import com.iscas.omdevs.scheduling.akka.event.ServiceKeys;
import com.iscas.omdevs.scheduling.akka.event.common.Command;

public class BackendHandler extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(BackendHandler::new);
    }

    private BackendHandler(ActorContext<Command> context) {
        super(context);

        // 注册 service key
        context.getSystem().receptionist().tell(Receptionist.register(ServiceKeys.BACKEND_SERVICE_KEY,
                context.getSelf().narrow()));

    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.class, s -> {
                    getContext().getLog().info("");
                    return Behaviors.same();
                })
                .build();
    }
}
