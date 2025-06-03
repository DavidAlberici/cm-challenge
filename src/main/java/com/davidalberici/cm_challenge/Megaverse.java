package com.davidalberici.cm_challenge;

import lombok.Getter;

@Getter
public class Megaverse {
    private final Object[][] elements;

    public Megaverse(Object[][] elements) {
        this.elements = elements;
    }
}
