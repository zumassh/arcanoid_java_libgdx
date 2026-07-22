package com.mygdx.arcanoid.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;
import static com.mygdx.arcanoid.physics.Box2DConstants.toPixels;

public class Obstacle {

    private static final float MIN_FLIP_INTERVAL_SEC = 2f;
    private static final float MAX_FLIP_INTERVAL_SEC = 10f;

    private final Body body;
    private final float widthPx;
    private final float heightPx;
    private final float speedPxPerSec;
    private final float minXPx;
    private final float maxXPx;

    private float direction;
    private float timeUntilFlipSec;

    private Obstacle(Body body, float widthPx, float heightPx, float speedPxPerSec, float minXPx, float maxXPx) {
        this.body = body;
        this.widthPx = widthPx;
        this.heightPx = heightPx;
        this.speedPxPerSec = speedPxPerSec;
        this.minXPx = minXPx;
        this.maxXPx = maxXPx;
        this.direction = 1f;
        this.timeUntilFlipSec = MathUtils.random(MIN_FLIP_INTERVAL_SEC, MAX_FLIP_INTERVAL_SEC);
        applyVelocity();
    }

    public static Obstacle create(World world, float centerXPx, float centerYPx, float widthPx, float heightPx,
                                   float speedPxPerSec, float minXPx, float maxXPx) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(toMeters(centerXPx), toMeters(centerYPx));
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(toMeters(widthPx / 2f), toMeters(heightPx / 2f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();

        Obstacle obstacle = new Obstacle(body, widthPx, heightPx, speedPxPerSec, minXPx, maxXPx);
        body.setUserData(obstacle);
        return obstacle;
    }

    public void update(float delta) {
        timeUntilFlipSec -= delta;
        if (timeUntilFlipSec <= 0f) {
            flip();
        } else {
            float xPx = toPixels(body.getPosition().x);
            boolean pastMax = direction > 0 && xPx >= maxXPx;
            boolean pastMin = direction < 0 && xPx <= minXPx;
            if (pastMax || pastMin) {
                flip();
            }
        }
        applyVelocity();
    }

    private void flip() {
        direction = -direction;
        timeUntilFlipSec = MathUtils.random(MIN_FLIP_INTERVAL_SEC, MAX_FLIP_INTERVAL_SEC);
    }

    private void applyVelocity() {
        body.setLinearVelocity(toMeters(direction * speedPxPerSec), 0f);
    }

    public float getXPx() {
        return toPixels(body.getPosition().x);
    }

    public float getYPx() {
        return toPixels(body.getPosition().y);
    }

    public float getWidthPx() {
        return widthPx;
    }

    public float getHeightPx() {
        return heightPx;
    }

    public Body getBody() {
        return body;
    }
}
