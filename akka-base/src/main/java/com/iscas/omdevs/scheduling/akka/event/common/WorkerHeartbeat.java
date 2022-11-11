package com.iscas.omdevs.scheduling.akka.event.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkerHeartbeat implements Command {

    private String workerAddress;

    private int uid;

}
