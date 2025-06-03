package com.davidalberici.cm_challenge.element;

import lombok.Getter;

@Getter
public class Cometh extends Element {
    private Direction direction;
    public Cometh(Direction direction) {
        this.direction = direction;
    }
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
