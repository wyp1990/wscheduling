package com.iscas.omdevs.scheduling.akka.event.common;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class WorkerUpdateEvent implements Event {

    private Set<ActorRef<Command>> workers;
}
