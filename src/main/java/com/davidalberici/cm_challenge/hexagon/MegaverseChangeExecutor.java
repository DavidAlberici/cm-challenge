package com.davidalberici.cm_challenge.hexagon;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MegaverseChangeExecutor {

    private static final int PROGRESS_INTERVAL = 5_000;
    private static final int INTERVAL_BETWEEN_CHANGES = 900;
    public void execute(List<Runnable> changes) {
        final AtomicInteger completedChanges = new AtomicInteger();
        final Thread reporter = buildAndStartReporterThread(changes, completedChanges);

        for (Runnable change : changes) {
            // I am aware having a wait time in this case does not follow hexagonal, since this class (as I made it)
            // should completely ignore the changes content. Of course I could change this class, maybe have a custom
            // interface for the changes, that allows for waiting time, batching, etc, and this class could decide what
            // to do based on it. But I will leave it as it is
            waitBetweenChanges();
            change.run();
            completedChanges.incrementAndGet();
        }

        reporter.interrupt();
    }

    private static Thread buildAndStartReporterThread(List<Runnable> changes, AtomicInteger completed) {

        final int total = changes.size();
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int percent = completed.get() * 100 / total;
                    log.info("Completed {} % of changes so far ({} / {})",
                            percent, completed.get(), total);
                    Thread.sleep(PROGRESS_INTERVAL);
                }
            } catch (InterruptedException ignored) {}
        }, "MegaverseChangeExecutor-reporter");
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private void waitBetweenChanges() {
        try {
            Thread.sleep(INTERVAL_BETWEEN_CHANGES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
