package com.davidalberici.cm_challenge.port;

import com.davidalberici.cm_challenge.hexagon.Megaverse;

public interface MegaverseReaderRepository {
    // Megaverse
    Megaverse getCurrentMegaverse();
    Megaverse getGoalMegaverse();
}
