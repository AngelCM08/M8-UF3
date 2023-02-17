/*package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PauseButton extends Actor {
    Rectangle bounds;
    AssetManager manager;
    boolean clickado;

    PauseButton() {
        setSize(32, 32);
        bounds = new Rectangle();
        setVisible(true);
        clickado = false;
    }
    @Override
    public void act(float delta) {
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get("pause.png", Texture.class), getX(), getY());
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}*/