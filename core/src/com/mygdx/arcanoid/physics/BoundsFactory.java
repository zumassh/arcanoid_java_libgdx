package com.mygdx.arcanoid.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_HEIGHT;
import static com.mygdx.arcanoid.physics.Box2DConstants.WORLD_WIDTH;
import static com.mygdx.arcanoid.physics.Box2DConstants.toMeters;

public final class BoundsFactory {

    private BoundsFactory() {
    }

    public static void createBounds(World world) {
        createWall(world, 0f, 0f, 0f, WORLD_HEIGHT);
        createWall(world, WORLD_WIDTH, 0f, WORLD_WIDTH, WORLD_HEIGHT);

        createSensor(world, 0f, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT, BoundaryType.TOP_SENSOR);
        createSensor(world, 0f, 0f, WORLD_WIDTH, 0f, BoundaryType.BOTTOM_SENSOR);
    }

    private static void createWall(World world, float x1Px, float y1Px, float x2Px, float y2Px) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        EdgeShape shape = new EdgeShape();
        shape.set(toMeters(x1Px), toMeters(y1Px), toMeters(x2Px), toMeters(y2Px));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    private static void createSensor(World world, float x1Px, float y1Px, float x2Px, float y2Px, BoundaryType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        body.setUserData(type);

        EdgeShape shape = new EdgeShape();
        shape.set(toMeters(x1Px), toMeters(y1Px), toMeters(x2Px), toMeters(y2Px));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
