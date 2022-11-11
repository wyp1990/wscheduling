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
import com.iscas.omdevs.scheduling.akka.event.common.WorkerUpdateEvent;
import com.iscas.omdevs.scheduling.service.context.WorkerStatusHolder;

import java.util.Set;

public class WorkerDiscovery extends AbstractBehavior<WorkerUpdateEvent> {

    public static Behavior<WorkerUpdateEvent> create() {
        return Behaviors.setup(WorkerDiscovery::new);
    }

    private WorkerDiscovery(ActorContext<WorkerUpdateEvent> context) {
        super(context);

        // 监听工作节点变更事件
        ActorRef<Receptionist.Listing> subscriptionAdapter = context.messageAdapter(Receptionist.Listing.class,
                listing -> new WorkerUpdateEvent(listing.getServiceInstances(ServiceKeys.BACKEND_SERVICE_KEY)));
        context.getSystem().receptionist().tell(Receptionist.subscribe(ServiceKeys.BACKEND_SERVICE_KEY,
                subscriptionAdapter));
    }

    @Override
    public Receive<WorkerUpdateEvent> createReceive() {
        return newReceiveBuilder()
                .onMessage(WorkerUpdateEvent.class, workerUpdate -> {
                    final Set<ActorRef<Command>> workers = workerUpdate.getWorkers();
                    WorkerStatusHolder.getInstance().getWorkers().clear();
                    WorkerStatusHolder.getInstance().getWorkers().addAll(workers);
                    return Behaviors.same();
                })
                .build();
    }
}
