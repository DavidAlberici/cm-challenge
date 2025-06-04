package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
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

        applyMegaverseChanges(goalMegaverse, currentMegaverse);
    }

    public void resetMegaverse() {
        Megaverse currentMegaverse = megaverseReaderRepository.getCurrentMegaverse();
        Megaverse newMegaverse = currentMegaverse.clone();
        newMegaverse.reset();

        applyMegaverseChanges(newMegaverse, currentMegaverse);
    }

    private void applyMegaverseChanges(Megaverse newMegaverse, Megaverse oldMegaverse) {
        List<Runnable> changes = megaverseChangeDetector.detectChanges(newMegaverse, oldMegaverse);
        log.info("Detected " + changes.size() + " changes to apply to the megaverse.");
        megaverseChangeExecutor.execute(changes);
    }
}
