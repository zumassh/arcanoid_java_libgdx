package com.mygdx.arcanoid.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_WIDTH;
import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;
import static com.mygdx.arcanoid.physics.Box2DConstants.toPixels;

public class Paddle {

    public static final float WIDTH_PX = 120f;
    public static final float HEIGHT_PX = 20f;

    private final Body body;
    private final float widthPx;
    private final float yPx;

    private Paddle(Body body, float widthPx, float yPx) {
        this.body = body;
        this.widthPx = widthPx;
        this.yPx = yPx;
    }

    public static Paddle create(World world, float centerXPx, float centerYPx, float widthPx, float heightPx) {
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

        Paddle paddle = new Paddle(body, widthPx, centerYPx);
        body.setUserData(paddle);
        return paddle;
    }

    public void moveTo(float targetXPx) {
        float clampedX = MathUtils.clamp(targetXPx, widthPx / 2f, WORLD_WIDTH - widthPx / 2f);
        body.setTransform(toMeters(clampedX), toMeters(yPx), 0f);
    }

    public float getXPx() {
        return toPixels(body.getPosition().x);
    }

    public float getYPx() {
        return yPx;
    }

    public float getWidthPx() {
        return widthPx;
    }

    public Body getBody() {
        return body;
    }
}
