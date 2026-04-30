package com.daqem.coldcase.event;

import com.daqem.coldcase.config.ColdCaseConfig;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.thread.ThreadManager;
import dev.architectury.event.events.common.TickEvent;

import java.util.concurrent.ExecutionException;

public class TickEvents {

    private static long lastTick = 0;

    public static void registerEvents() {
        TickEvent.SERVER_POST.register(server -> {
            ThreadManager.getAndRemoveCompleted().forEach(
                    (future, onComplete) -> {
                        try {
                            onComplete.onComplete(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            com.daqem.coldcase.ColdCase.LOGGER.error("Error executing task", e);
                        }
                    }
            );
            if (lastTick % ColdCaseConfig.queueFrequency.get() == 0) {
                ThreadManager.execute(() -> {
                    Database database = com.daqem.coldcase.ColdCase.getDatabase();
                    database.queue.execute();
                    database.batchQueue.execute();
                });
            }

            if (lastTick % ColdCaseConfig.helloFrequency.get() == 0) {
                ThreadManager.execute(() -> {
                    //Send hello packet to server to keep connection alive
                    Database database = com.daqem.coldcase.ColdCase.getDatabase();
                    database.queue.hello();
                });
            }

            lastTick++;
        });
    }
}
