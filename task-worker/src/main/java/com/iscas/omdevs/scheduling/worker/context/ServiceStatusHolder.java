package com.iscas.omdevs.scheduling.worker.context;

import akka.actor.typed.ActorRef;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceStatusHolder {

    private static ServiceStatusHolder serviceStatusHolder;

    public static ServiceStatusHolder getInstance() {
        if (serviceStatusHolder == null) {
            serviceStatusHolder = new ServiceStatusHolder();
        }
        return serviceStatusHolder;
    }

    private ServiceStatusHolder() {

    }

    private List<ActorRef<Command>> services = new ArrayList<>();

}
