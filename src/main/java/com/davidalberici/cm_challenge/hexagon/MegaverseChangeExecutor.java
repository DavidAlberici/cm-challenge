package com.davidalberici.cm_challenge.hexagon;

import java.util.List;

public class MegaverseChangeExecutor {
    public void execute(List<Runnable> changes) {
        for (Runnable change : changes) {
            change.run();
        }
    }
}
