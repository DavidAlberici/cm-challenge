package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MegaverseTest {

    @Test
    void checkIsValid_shouldNotThrowAnythingWhenValid() {
        Element[][] elements = new Element[3][3];
        elements[1][1] = new Soloon(Soloon.Color.PURPLE);
        elements[0][1] = new Polyanet();  // adjacent above

        Megaverse megaverse = new Megaverse(elements);

        assertDoesNotThrow(megaverse::checkIsValid);
    }

    @Test
    void checkIsValid_shouldThrowErrorWhenAnySoloonIsNotAdjacentToPolyanet() {
        Element[][] elements = new Element[3][3];
        elements[1][1] = new Soloon(Soloon.Color.PURPLE);  // no adjacent Polyanet

        Megaverse megaverse = new Megaverse(elements);

        RuntimeException exception = assertThrows(RuntimeException.class, megaverse::checkIsValid);
        assertTrue(exception.getMessage().contains("Invalid Megaverse"));
    }
}