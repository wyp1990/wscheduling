package com.iscas.omdevs.scheduling.service.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import com.iscas.omdevs.scheduling.akka.event.ServiceKeys;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.akka.event.common.WorkerHeartbeat;
import com.iscas.omdevs.scheduling.akka.event.common.WorkerUpdateEvent;


public class FrontendHandler extends AbstractBehavior<Command> {

    private final ActorRef<Command> workerMessageHandler;

    public static Behavior<Command> create() {

        return Behaviors.setup(FrontendHandler::new);
    }

    private FrontendHandler(ActorContext<Command> context) {
        super(context);

        // 初始化actor
        workerMessageHandler = context.spawn(WorkerMessageHandler.create(), "worker-message-handler");
        ActorRef<WorkerUpdateEvent> workerDiscovery = context.spawn(WorkerDiscovery.create(), "worker-discovery");

        // 注册 service key
        context.getSystem().receptionist().tell(Receptionist.register(ServiceKeys.FRONTEND_SERVICE_KEY,
                context.getSelf().narrow()));

        // 监控子actor生命周期
        context.watch(workerMessageHandler);
        context.watch(workerDiscovery);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(WorkerHeartbeat.class, commend -> {
                    workerMessageHandler.tell(commend);
                    return Behaviors.same();
                }).build();
    }
}
