package com.iscas.omdevs.scheduling.worker.actor;

import akka.actor.ActorPath;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.akka.event.common.Event;
import com.iscas.omdevs.scheduling.akka.event.common.ServiceUpdateEvent;

public class RootBehavior extends AbstractBehavior<Void> {

    public static Behavior<Void> create() {
        return Behaviors.setup(RootBehavior::new);
    }

    private RootBehavior(ActorContext<Void> context) {
        super(context);

        // 初始化actor
        final ActorRef<Command> backend = context.spawn(BackendHandler.create(), "backend");
        final ActorRef<ServiceUpdateEvent> serviceDiscovery = context.spawn(ServiceDiscovery.create(), "service-discovery");
        final ActorRef<Event> workerHealthy = context.spawn(WorkerHealthReporter.create(backend.path().uid()), "worker-healthy");

        // 监控子actor生命周期
        context.watch(serviceDiscovery);
        context.watch(workerHealthy);
        context.watch(backend);
    }

    @Override
    public Receive<Void> createReceive() {
        return (Receive<Void>) Behaviors.<Void>empty();
    }
}
