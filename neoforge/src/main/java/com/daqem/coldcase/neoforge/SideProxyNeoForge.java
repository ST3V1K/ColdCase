package com.daqem.coldcase.neoforge;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.client.ColdCaseClient;

public class SideProxyNeoForge {

    SideProxyNeoForge() {
    }

    public static class Server extends SideProxyNeoForge {
        Server() {
            ColdCase.initServer();
        }
    }

    public static class Client extends SideProxyNeoForge {
        Client() {
            ColdCaseClient.init();
        }
    }
}
