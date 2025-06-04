package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HexagonApi {

    private final MegaverseReaderRepository megaverseReaderRepository;
    public Megaverse getCurrentMegaverse() {
        return megaverseReaderRepository.getCurrentMegaverse();
    }

    public Megaverse getGoalMegaverse() {
        return megaverseReaderRepository.getGoalMegaverse();
    }
}
