package com.mygdx.arcanoid.levels;

import com.badlogic.gdx.utils.Array;
import com.mygdx.arcanoid.entities.Ball;
import com.mygdx.arcanoid.entities.Brick;
import com.mygdx.arcanoid.entities.Obstacle;

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
