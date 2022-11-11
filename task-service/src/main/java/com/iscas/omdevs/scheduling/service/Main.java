package com.iscas.omdevs.scheduling.service;

import akka.actor.typed.ActorSystem;
import com.iscas.omdevs.scheduling.akka.event.common.Command;
import com.iscas.omdevs.scheduling.service.actor.FrontendHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("akka.remote.artery.canonical.port", "25251");
        overrides.put("akka.cluster.roles", Collections.singletonList("frontend"));

        Config config = ConfigFactory.parseMap(overrides)
                .withFallback(ConfigFactory.load());

        ActorSystem<Command> system = ActorSystem.create(FrontendHandler.create(), "ClusterSystem", config);
    }


}
