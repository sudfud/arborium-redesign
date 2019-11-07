package com.mygdx.arborium.screen.farm;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

// Wrapper for the physics bodies of fruit that falls from trees
public class FruitBody {

    Body body;
    Fixture fixture;
    BodyDef bodyDef;

    public FruitBody(World world, TextureRegion texture, float startX, float startY) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY);

        body = world.createBody(bodyDef);
        Sprite sprite = new Sprite(texture);
        body.setUserData(sprite);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = 0x1;
        fixtureDef.filter.maskBits = 0x1 << 1;

        fixture = body.createFixture(fixtureDef);

        circle.dispose();
    }

    public void update() {
        Sprite sprite = getSprite();
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
    }

    public Sprite getSprite() {
        return (Sprite) body.getUserData();
    }
}
