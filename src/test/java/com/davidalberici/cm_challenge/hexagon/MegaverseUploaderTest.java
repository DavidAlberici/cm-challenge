package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MegaverseUploaderTest {

    private MegaverseReaderRepository readerRepo;
    private MegaverseWriterRepository writerRepo;
    private MegaverseUploader uploader;

    @BeforeEach
    void setUp() {
        readerRepo = mock(MegaverseReaderRepository.class);
        writerRepo = mock(MegaverseWriterRepository.class);
        uploader = new MegaverseUploader(writerRepo, readerRepo);
    }
    @Test
    void uploadMegaverse_shouldFailWhenMegaversesDoNotHaveSameNumberOfRows() {
        // arrange
        Element[][] newElements = new Element[3][3];
        Element[][] oldElements = new Element[2][3]; // different number of rows

        Megaverse newMegaverse = new Megaverse(newElements);
        Megaverse oldMegaverse = new Megaverse(oldElements);

        when(readerRepo.getCurrentMegaverse()).thenReturn(oldMegaverse);

        // act & assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> uploader.uploadMegaverse(newMegaverse));
        assertTrue(ex.getMessage().contains("Both megaverses must have the same number of rows."));
    }
    @Test
    void uploadMegaverse_shouldFailWhenMegaversesDoNotHaveSameNumberOfColumns() {
        // arrange
        Element[][] newElements = new Element[3][3];
        Element[][] oldElements = new Element[3][2]; // different number of rows

        Megaverse newMegaverse = new Megaverse(newElements);
        Megaverse oldMegaverse = new Megaverse(oldElements);

        when(readerRepo.getCurrentMegaverse()).thenReturn(oldMegaverse);

        // act & assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> uploader.uploadMegaverse(newMegaverse));
        assertTrue(ex.getMessage().contains("Both megaverses must have the same number of columns."));
    }
}