package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HexagonApi {

    private final MegaverseReaderRepository megaverseReaderRepository;
    private final MegaverseChangeDetector megaverseChangeDetector;
    private final MegaverseChangeExecutor megaverseChangeExecutor;
    public Megaverse getCurrentMegaverse() {
        return megaverseReaderRepository.getCurrentMegaverse();
    }

    public Megaverse getGoalMegaverse() {
        return megaverseReaderRepository.getGoalMegaverse();
    }

    public void buildMegaverse() {
        Megaverse currentMegaverse = megaverseReaderRepository.getCurrentMegaverse();
        Megaverse goalMegaverse = megaverseReaderRepository.getGoalMegaverse();

        List<Runnable> changes = megaverseChangeDetector.detectChanges(goalMegaverse, currentMegaverse);
        megaverseChangeExecutor.execute(changes);
    }

    public void resetMegaverse() {
        Megaverse currentMegaverse = megaverseReaderRepository.getCurrentMegaverse();
        Megaverse newMegaverse = currentMegaverse.clone();
        newMegaverse.reset();

        List<Runnable> changes = megaverseChangeDetector.detectChanges(newMegaverse, currentMegaverse);
        megaverseChangeExecutor.execute(changes);
    }
}
