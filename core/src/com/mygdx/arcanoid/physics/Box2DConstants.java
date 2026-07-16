package com.mygdx.arcanoid.physics;

public final class Box2DConstants {

    private Box2DConstants() {
    }

    public static final float PIXELS_PER_METER = 32f;

    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 480f;

    public static final float TIME_STEP = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    public static final float MAX_FRAME_TIME = 0.25f;

    public static float toMeters(float pixels) {
        return pixels / PIXELS_PER_METER;
    }

    public static float toPixels(float meters) {
        return meters * PIXELS_PER_METER;
    }
}
