package com.davidalberici.cm_challenge.hexagon.element;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
