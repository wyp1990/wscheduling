package com.iscas.omdevs.scheduling.service.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.akka.event.common.WorkerHeartbeat;
import com.iscas.omdevs.scheduling.service.context.WorkerStatusHolder;

import java.util.Map;

public class WorkerMessageHandler extends AbstractBehavior<Command> {

    public static Behavior<Command> create() {
        return Behaviors.setup(WorkerMessageHandler::new);
    }

    private WorkerMessageHandler(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(WorkerHeartbeat.class, this::onHeartbeat)
                .build();
    }

    private Behavior<Command> onHeartbeat(WorkerHeartbeat workerHeartbeat) {
        WorkerStatusHolder workerStatusHolder = WorkerStatusHolder.getInstance();
        final Map<Integer, String> workerInfo = workerStatusHolder.getWorkerInfo();
        getContext().getLog().info(String.valueOf(workerHeartbeat));
        workerInfo.putIfAbsent(workerHeartbeat.getUid(), workerHeartbeat.getWorkerAddress());
        return Behaviors.same();
    }
}
