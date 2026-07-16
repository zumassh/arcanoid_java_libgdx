package com.mygdx.arcanoid.entities;

public enum BallColor {
    RED,
    GREEN;

    public boolean matches(BrickColor brickColor) {
        return brickColor != BrickColor.METAL && name().equals(brickColor.name());
    }
}
