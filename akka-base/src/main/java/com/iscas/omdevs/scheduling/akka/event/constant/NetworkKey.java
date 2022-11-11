package com.iscas.omdevs.scheduling.akka.event.constant;

public interface NetworkKey {

    public static final String PREFERRED_NETWORK_INTERFACE = "powerjob.network.interface.preferred";

    public static final String BIND_LOCAL_ADDRESS = "powerjob.network.local.address";

    /**
     * Java regular expressions for network interfaces that will be ignored.
     */
    public static final String IGNORED_NETWORK_INTERFACE_REGEX = "powerjob.network.interface.ignored";

    public static final String WORKER_STATUS_CHECK_PERIOD = "powerjob.worker.status-check.normal.period";

}
