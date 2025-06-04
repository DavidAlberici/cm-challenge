package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MegaverseUploader {
    private final MegaverseWriterRepository megaverseWriterRepository;
    private final MegaverseReaderRepository megaverseReaderRepository;

    public void uploadMegaverse(Megaverse newMg) {
        Megaverse oldMg = megaverseReaderRepository.getCurrentMegaverse();
        checkBothMegaversesHaveTheSameSize(newMg, oldMg);
    }

    private void checkBothMegaversesHaveTheSameSize(@NonNull Megaverse newMg, @NonNull Megaverse oldMg) {
        if (newMg.getElements().length != oldMg.getElements().length) {
            throw new IllegalArgumentException("Both megaverses must have the same number of rows.");
        }
        if (newMg.getElements()[0].length != oldMg.getElements()[0].length) {
            throw new IllegalArgumentException("Both megaverses must have the same number of columns.");
        }
    }

}
