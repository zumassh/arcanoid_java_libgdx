package com.mygdx.arcanoid.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;
import static com.mygdx.arcanoid.physics.Box2DConstants.toPixels;

public class Brick {

    private final Body body;
    private final BrickColor color;
    private final float widthPx;
    private final float heightPx;

    private Brick(Body body, BrickColor color, float widthPx, float heightPx) {
        this.body = body;
        this.color = color;
        this.widthPx = widthPx;
        this.heightPx = heightPx;
    }

    public static Brick create(World world, float centerXPx, float centerYPx, float widthPx, float heightPx, BrickColor color) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
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

        Brick brick = new Brick(body, color, widthPx, heightPx);
        body.setUserData(brick);
        return brick;
    }

    public BrickColor getColor() {
        return color;
    }

    public Body getBody() {
        return body;
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
}
