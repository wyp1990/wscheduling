package com.iscas.omdevs.scheduling.worker.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import com.iscas.omdevs.scheduling.akka.event.ServiceKeys;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.akka.event.common.ServiceUpdateEvent;
import com.iscas.omdevs.scheduling.worker.context.ServiceStatusHolder;

import java.util.Set;


public class ServiceDiscovery extends AbstractBehavior<ServiceUpdateEvent> {

    public static Behavior<ServiceUpdateEvent> create() {
        return Behaviors.setup(ServiceDiscovery::new);
    }

    private ServiceDiscovery(ActorContext<ServiceUpdateEvent> context) {
        super(context);

        // 监听服务节点变更事件
        ActorRef<Receptionist.Listing> subscriptionAdapter = context.messageAdapter(Receptionist.Listing.class,
                listing -> new ServiceUpdateEvent(listing.getServiceInstances(ServiceKeys.FRONTEND_SERVICE_KEY)));
        context.getSystem().receptionist().tell(Receptionist.subscribe(ServiceKeys.FRONTEND_SERVICE_KEY,
                subscriptionAdapter));
    }

    @Override
    public Receive<ServiceUpdateEvent> createReceive() {
        return newReceiveBuilder()
                .onMessage(ServiceUpdateEvent.class, serviceUpdate -> {
                    final Set<ActorRef<Command>> serviceSet = serviceUpdate.getServices();
                    ServiceStatusHolder.getInstance().getServices().clear();
                    ServiceStatusHolder.getInstance().getServices().addAll(serviceSet);
                    return Behaviors.same();
                })
                .build();
    }

}
