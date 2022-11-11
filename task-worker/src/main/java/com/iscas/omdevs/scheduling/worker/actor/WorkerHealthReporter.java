package com.iscas.omdevs.scheduling.worker.actor;

import akka.actor.ActorPath;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.akka.event.common.Event;
import com.iscas.omdevs.scheduling.akka.event.common.TimerTick;
import com.iscas.omdevs.scheduling.akka.event.common.WorkerHeartbeat;
import com.iscas.omdevs.scheduling.worker.context.ServiceStatusHolder;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.Duration;
import java.util.List;

public class WorkerHealthReporter extends AbstractBehavior<Event> {

    private final int uid;

    public static Behavior<Event> create(int uid) {
        return Behaviors.setup(context ->
                Behaviors.withTimers(timers ->
                        new WorkerHealthReporter(context, timers, uid)
                )
        );
    }

    private WorkerHealthReporter(ActorContext<Event> context, TimerScheduler<Event> timers, int uid) {
        super(context);
        timers.startTimerWithFixedDelay(TimerTick.INSTANCE, TimerTick.INSTANCE, Duration.ofSeconds(1));
        this.uid = uid;
    }

    @Override
    public Receive<Event> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals(TimerTick.INSTANCE, this::onTick)
                .build();
    }

    private Behavior<Event> onTick() {
        List<ActorRef<Command>> services = ServiceStatusHolder.getInstance().getServices();
        if (services.isEmpty()) {
            getContext().getLog().warn("服务节点为空");
        } else {
            for (ActorRef<Command> service : services) {
                final int i = Runtime.getRuntime().availableProcessors();
                OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                WorkerHeartbeat workerHeartbeat = new WorkerHeartbeat();
                workerHeartbeat.setWorkerAddress("127.0.0.1:25252")
                                .setUid(uid);
                service.tell(workerHeartbeat);
            }
        }

        return Behaviors.same();
    }
}
