package com.mygdx.arcanoid.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.arcanoid.entities.Ball;
import com.mygdx.arcanoid.entities.Brick;

public class GameContactListener implements ContactListener {

    public final Array<Brick> bricksToBreak = new Array<>();
    public final Array<Ball> ballsLostBottom = new Array<>();
    public boolean levelComplete = false;
    public boolean anyBallContact = false;

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if (a instanceof Ball || b instanceof Ball) {
            anyBallContact = true;
        }
        handlePair(a, b);
        handlePair(b, a);
    }

    private void handlePair(Object first, Object second) {
        if (!(first instanceof Ball)) {
            return;
        }
        Ball ball = (Ball) first;

        if (second instanceof Brick) {
            Brick brick = (Brick) second;
            if (ball.getColor().matches(brick.getColor()) && !bricksToBreak.contains(brick, true)) {
                bricksToBreak.add(brick);
            }
        } else if (second == BoundaryType.BOTTOM_SENSOR) {
            if (!ballsLostBottom.contains(ball, true)) {
                ballsLostBottom.add(ball);
            }
        } else if (second == BoundaryType.TOP_SENSOR) {
            levelComplete = true;
        }
    }

    public void reset() {
        bricksToBreak.clear();
        ballsLostBottom.clear();
        levelComplete = false;
        anyBallContact = false;
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
