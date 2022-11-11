package com.iscas.omdevs.scheduling.akka.event;

import akka.actor.typed.receptionist.ServiceKey;
import com.iscas.omdevs.scheduling.akka.event.common.Command;

public class ServiceKeys {

    public static ServiceKey<Command> BACKEND_SERVICE_KEY =
            ServiceKey.create(Command.class, "Worker");

    public static ServiceKey<Command> FRONTEND_SERVICE_KEY =
            ServiceKey.create(Command.class, "service");
}
