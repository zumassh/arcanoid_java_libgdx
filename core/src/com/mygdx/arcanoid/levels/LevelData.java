package com.mygdx.arcanoid.levels;

import com.badlogic.gdx.utils.Array;
import com.mygdx.arcanoid.objects.Ball;
import com.mygdx.arcanoid.objects.Brick;
import com.mygdx.arcanoid.objects.Obstacle;

public class LevelData {

    public final Array<Brick> bricks;
    public final Array<Ball> balls;
    public final Obstacle obstacle;

    public LevelData(Array<Brick> bricks, Array<Ball> balls, Obstacle obstacle) {
        this.bricks = bricks;
        this.balls = balls;
        this.obstacle = obstacle;
    }
}
