package com.iscas.omdevs.scheduling.worker;

import akka.actor.typed.ActorSystem;
import com.iscas.omdevs.scheduling.worker.actor.RootBehavior;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("akka.remote.artery.canonical.port", "25252");
        overrides.put("akka.cluster.roles", Collections.singletonList("frontend"));

        Config config = ConfigFactory.parseMap(overrides)
                .withFallback(ConfigFactory.load());

        ActorSystem<Void> system = ActorSystem.create(RootBehavior.create(), "ClusterSystem", config);


    }
}
