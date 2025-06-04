package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import lombok.Getter;

@Getter
public class Megaverse {
    private final Element[][] elements;

    public Megaverse(Element[][] elements) {
        this.elements = elements;
    }

    public void checkIsValid() {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                if (elements[i][j] instanceof Soloon) {
                    if (!hasAdjacentPolyanet(i, j)) {
                        throw new RuntimeException("Invalid Megaverse: Soloon at (" + i + ", " + j + ") has no adjacent Polyanet");
                    }
                }
            }
        }
    }

    private boolean hasAdjacentPolyanet(int i, int j) {
        if (i > 0 && elements[i - 1][j] instanceof Polyanet) return true; // up
        if (i < elements.length - 1 && elements[i + 1][j] instanceof Polyanet) return true; // down
        if (j > 0 && elements[i][j - 1] instanceof Polyanet) return true; // left
        if (j < elements[i].length - 1 && elements[i][j + 1] instanceof Polyanet) return true; // right
        return false;
    }
}
