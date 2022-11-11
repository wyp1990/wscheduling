package com.iscas.omdevs.scheduling.service.context;

import akka.actor.typed.ActorRef;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WorkerStatusHolder {

    private static WorkerStatusHolder workerStatusHolder;

    public static WorkerStatusHolder getInstance() {
        if (workerStatusHolder == null) {
            workerStatusHolder = new WorkerStatusHolder();
        }
        return workerStatusHolder;
    }

    private WorkerStatusHolder() {

    }

    private List<ActorRef<Command>> workers = new ArrayList<>();

    private Map<Integer, String> workerInfo = new HashMap<>();

}
