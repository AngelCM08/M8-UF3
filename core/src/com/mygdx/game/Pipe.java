package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

public class Pipe extends Actor {
    Random rd = new Random();
    Rectangle bounds;
    boolean straight;
    AssetManager manager;
    boolean superado;
    boolean movil;
    boolean moving_up = true;

    Pipe(int probabilidad) {
        setSize(64, 230);
        bounds = new Rectangle();
        setVisible(false);
        superado = false;
        if(rd.nextInt(probabilidad) == 1) movil = true;
    }
    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        if(movil){
            if(moving_up){
                moveBy(0, 100 * delta);
                if(straight && this.bounds.y >= -80){
                    moving_up = false;
                }else if(!straight && this.bounds.y >= 450){
                    moving_up = false;
                }
            }else{
                moveBy(0, -100 * delta);
                if(straight && this.bounds.y <= -230){
                    moving_up = true;
                }else if(!straight && this.bounds.y <= 300){
                    moving_up = true;
                }
            }
        }
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( manager.get( straight ? "pipe_up.png" : "pipe_down.png", Texture.class), getX(), getY() );
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public boolean isStraight() {
        return straight;
    }
    public void setStraight(boolean straight) {
        this.straight = straight;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}
