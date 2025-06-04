package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class Megaverse {
    private final Element[][] elements;

    public Megaverse(Element[][] elements) {
        checkArraySizeIsValid(elements);
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

    public void reset() {
        for (Element[] element : elements) {
            Arrays.fill(element, null);
        }
    }

    private boolean hasAdjacentPolyanet(int i, int j) {
        if (i > 0 && elements[i - 1][j] instanceof Polyanet) return true; // up
        if (i < elements.length - 1 && elements[i + 1][j] instanceof Polyanet) return true; // down
        if (j > 0 && elements[i][j - 1] instanceof Polyanet) return true; // left
        if (j < elements[i].length - 1 && elements[i][j + 1] instanceof Polyanet) return true; // right
        return false;
    }

    private void checkArraySizeIsValid(Element[][] elements) {
        if (elements.length == 0 || elements[0].length == 0) {
            throw new RuntimeException("Megaverse cannot be empty");
        }
        int firstRowLength = elements[0].length;
        for (int rowIndex = 1; rowIndex < elements.length; rowIndex++) {
            if (elements[rowIndex].length != firstRowLength) {
                //modify this throw, so it indicates which row is failing
                throw new RuntimeException("All rows in the Megaverse must have the same length. " +
                        "Row length mismatch at row index " + Arrays.asList(elements).indexOf(rowIndex));
            }
        }
    }
}
