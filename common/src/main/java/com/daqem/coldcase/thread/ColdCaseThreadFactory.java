package com.daqem.coldcase.thread;

import io.netty.util.concurrent.DefaultThreadFactory;

public class ColdCaseThreadFactory extends DefaultThreadFactory {

    private static final String THREAD_NAME = "ColdCase thread";

    public ColdCaseThreadFactory() {
        super(THREAD_NAME);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = super.newThread(r);
        thread.setName(THREAD_NAME);
        return thread;
    }
}
