package com.davidalberici.cm_challenge.element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Soloon extends Element {
    private Color color;

    public Soloon(Color color) {
        this.color = color;
    }

    public enum Color {
        WHITE,
        BLUE,
        RED,
        PURPLE
    }
}
