package com.mygdx.arcanoid.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;
import static com.mygdx.arcanoid.physics.Box2DConstants.toPixels;

public class Modifier {

    public static final float WIDTH_PX = 24f;
    public static final float HEIGHT_PX = 24f;
    public static final float FALL_SPEED_PX_PER_SEC = 150f;

    private final Body body;
    private final BrickColor color;

    private Modifier(Body body, BrickColor color) {
        this.body = body;
        this.color = color;
    }

    public static Modifier create(World world, float centerXPx, float centerYPx, BrickColor color) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(toMeters(centerXPx), toMeters(centerYPx));
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(toMeters(WIDTH_PX / 2f), toMeters(HEIGHT_PX / 2f));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearVelocity(0f, -toMeters(FALL_SPEED_PX_PER_SEC));

        Modifier modifier = new Modifier(body, color);
        body.setUserData(modifier);
        return modifier;
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
}
