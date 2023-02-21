package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

public class Rocket extends Actor {
    Random rd = new Random();
    Rectangle bounds;
    AssetManager manager;
    int speed;
    boolean superado;

    Rocket() {
        setSize(64, 40);
        bounds = new Rectangle();
        setVisible(true);
        superado = false;
        speed = rd.nextInt(100) + 250;
    }
    @Override
    public void act(float delta) {
        moveBy(-speed * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if (getX() < -64)
            remove();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( manager.get( "rocket.png", Texture.class), getX(), getY());
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}
