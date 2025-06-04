package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MegaverseChangeDetectorTest {

    private MegaverseWriterRepository writerRepo;
    private MegaverseChangeDetector changeDetector;

    @BeforeEach
    void setUp() {
        writerRepo = mock(MegaverseWriterRepository.class);
        changeDetector = new MegaverseChangeDetector(writerRepo);
    }
    @Test
    void detectChanges_shouldFailWhenMegaversesDoNotHaveSameNumberOfRows() {
        // arrange
        Element[][] newElements = new Element[3][3];
        Element[][] oldElements = new Element[2][3]; // different number of rows

        Megaverse newMegaverse = new Megaverse(newElements);
        Megaverse oldMegaverse = new Megaverse(oldElements);

        // act & assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> changeDetector.detectChanges(newMegaverse, oldMegaverse));
        assertTrue(ex.getMessage().contains("Both megaverses must have the same number of rows."));
    }
    @Test
    void detectChanges_shouldFailWhenMegaversesDoNotHaveSameNumberOfColumns() {
        // arrange
        Element[][] newElements = new Element[3][3];
        Element[][] oldElements = new Element[3][2]; // different number of rows

        Megaverse newMegaverse = new Megaverse(newElements);
        Megaverse oldMegaverse = new Megaverse(oldElements);

        // act & assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> changeDetector.detectChanges(newMegaverse, oldMegaverse));
        assertTrue(ex.getMessage().contains("Both megaverses must have the same number of columns."));
    }

    @ParameterizedTest
    @MethodSource("megaverseChangeCases")
    void detectChanges_shouldCorrectlyDetectTheNumberOfChanges(
            Element[][] newElements,
            Element[][] oldElements,
            int expectedChanges
    ) {
        Megaverse newMegaverse = new Megaverse(newElements);
        Megaverse oldMegaverse = new Megaverse(oldElements);

        List<Runnable> changes = changeDetector.detectChanges(newMegaverse, oldMegaverse);

        assertEquals(expectedChanges, changes.size(), "Unexpected number of changes detected.");
    }

    private static Stream<Arguments> megaverseChangeCases() {
        return Stream.of(
                // Case 1: No changes
                Arguments.of(
                        new Element[][]{
                                {new Polyanet(), null},
                                {null, new Soloon(Soloon.Color.RED)}
                        },
                        new Element[][]{
                                {new Polyanet(), null},
                                {null, new Soloon(Soloon.Color.RED)}
                        },
                        0
                ),
                // Case 2: All changed
                Arguments.of(
                        new Element[][]{
                                {new Soloon(Soloon.Color.BLUE), new Cometh(Cometh.Direction.UP)},
                                {new Polyanet(), null}
                        },
                        new Element[][]{
                                {new Polyanet(), new Soloon(Soloon.Color.RED)},
                                {new Cometh(Cometh.Direction.DOWN), new Polyanet()}
                        },
                        7 // 4 deletes + 3 adds
                ),
                // Case 3: Only adds (previously empty)
                Arguments.of(
                        new Element[][]{
                                {new Polyanet(), null},
                                {null, new Cometh(Cometh.Direction.LEFT)}
                        },
                        new Element[][]{
                                {null, null},
                                {null, null}
                        },
                        2 // 2 adds, no deletes
                ),
                // Case 4: Only deletes (new is empty)
                Arguments.of(
                        new Element[][]{
                                {null, null},
                                {null, null}
                        },
                        new Element[][]{
                                {new Polyanet(), null},
                                {null, new Cometh(Cometh.Direction.RIGHT)}
                        },
                        2 // 2 deletes, no adds
                ),
                // Case 5: Mixed changes
                Arguments.of(
                        new Element[][]{
                                {new Soloon(Soloon.Color.RED), null},
                                {null, null}
                        },
                        new Element[][]{
                                {null, new Cometh(Cometh.Direction.DOWN)},
                                {null, null}
                        },
                        2 // 1 add + 1 delete
                )
        );
    }
}