package com.mygdx.arcanoid.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;
import static com.mygdx.arcanoid.physics.Box2DConstants.toPixels;

public class Ball {

    public static final float RADIUS_PX = 8f;
    public static final float SPEED_PX_PER_SEC = 260f;

    private final Body body;
    private final BallColor color;

    private Ball(Body body, BallColor color) {
        this.body = body;
        this.color = color;
    }

    public static Ball create(World world, float xPx, float yPx, BallColor color) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(toMeters(xPx), toMeters(yPx));
        bodyDef.bullet = true;
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(toMeters(RADIUS_PX));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearDamping(0f);

        Ball ball = new Ball(body, color);
        body.setUserData(ball);
        return ball;
    }

    public void launch(float angleDeg) {
        float speedMetersPerSec = toMeters(SPEED_PX_PER_SEC);
        body.setLinearVelocity(
            speedMetersPerSec * MathUtils.cosDeg(angleDeg),
            speedMetersPerSec * MathUtils.sinDeg(angleDeg)
        );
    }

    public void renormalizeSpeed() {
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.len2() < 0.0001f) {
            return;
        }
        float speedMetersPerSec = toMeters(SPEED_PX_PER_SEC);
        velocity.nor().scl(speedMetersPerSec);
        body.setLinearVelocity(velocity);
    }

    public BallColor getColor() {
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
