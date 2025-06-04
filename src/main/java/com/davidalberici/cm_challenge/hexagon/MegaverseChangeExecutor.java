package com.davidalberici.cm_challenge.hexagon;

import java.util.List;

public class MegaverseChangeExecutor {
    public void execute(List<Runnable> changes) {
        for (Runnable change : changes) {
            System.out.print("O");
            change.run();
            System.out.print("-");
            try {
                Thread.sleep(650);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }
}
