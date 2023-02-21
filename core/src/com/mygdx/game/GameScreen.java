package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen, InputProcessor {
    final Bird game;
    OrthographicCamera camera;
    Stage stage;
    Player player;
    boolean dead;
    Array<Pipe> pipes;
    Array<Rocket> rockets;
    long lastObstacleTime;
    int score;
    long timeBetweenPipe;
    int speedy = 400;
    int probability_movil_pipe = 30;
    int probability_rocket = 50;
    Random rd = new Random();

    public GameScreen(final Bird gam) {
        score = 0;
        timeBetweenPipe = 3500000000L;
        this.game = gam;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);

        // create the obstacles array and spawn the first obstacle
        pipes = new Array<>();
        rockets = new Array<>();
        spawnObstacle();
        game.manager.get("start_game.mp3", Sound.class).play();
    }

    private void spawnObstacle() {
        // Calcula la alçada de l'obstacle aleatòriament
        float holey = MathUtils.random(50, 230);
        // Crea dos obstacles: Una tubería superior i una inferior
        Pipe pipe1 = new Pipe(probability_movil_pipe);
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setStraight(true);
        pipe1.setManager(game.manager);
        pipes.add(pipe1);
        stage.addActor(pipe1);

        Pipe pipe2 = new Pipe(probability_movil_pipe);
        pipe2.setX(800);
        pipe2.setY(holey + 230);
        pipe2.setStraight(false);
        pipe2.setManager(game.manager);
        pipes.add(pipe2);
        stage.addActor(pipe2);

        Rocket rocket = new Rocket();
        rocket.setManager(game.manager);
        rocket.setX(800);
        rocket.setY(rd.nextFloat()*300+100);
        rockets.add(rocket);
        stage.addActor(rocket);

        lastObstacleTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        // begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();

        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();

        // Game batch: HUD
        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + (int)score, 10, 470);
        game.batch.end();

        // process user input
        if (Gdx.input.justTouched()) {
            player.impulso(speedy);
            game.manager.get("flap_v2.mp3", Sound.class).play();
        }
        stage.act();

        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45)
            player.setY( 480 - 45 );
        if (player.getBounds().y < -45) {
            dead = true;
        }

        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > timeBetweenPipe) spawnObstacle();

        // Comprova si les tuberies colisionen amb el jugador
        Iterator<Pipe> iter = pipes.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getBounds().overlaps(player.getBounds())) {
                dead = true;
            }
        }

        // Treure de l'array les tuberies que estan fora de pantalla
        iter = pipes.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if(player.getX() > pipe.getX() && pipe.straight && !pipe.superado){
                score += 1;
                if(score % 5 == 0) {
                    game.manager.get("level_up.mp3", Sound.class).play();
                    timeBetweenPipe -= 300000000L;
                    speedy -= 20;
                    if((probability_movil_pipe-3) >= 0){
                        probability_movil_pipe -= 3;
                    }


                }
                pipe.superado = true;
            }
            if (pipe.getX() < -64) {
                pipes.removeValue(pipe, true);
            }
        }

        // Comprova si els cohets colisionen amb el jugador
        Iterator<Rocket> rocketIterator = rockets.iterator();
        while (rocketIterator.hasNext()) {
            Rocket rocket = rocketIterator.next();
            if (rocket.getBounds().overlaps(player.getBounds())) {
                dead = true;
            }
        }

        // Treure de l'array els cohets que estan fora de pantalla
        rocketIterator = rockets.iterator();
        while (iter.hasNext()) {
            Rocket rocket = rocketIterator.next();
            if(player.getX() > rocket.getX() && !rocket.superado){
                score += 1;
                if(score % 5 == 0) {
                    game.manager.get("level_up.mp3", Sound.class).play();
                    timeBetweenPipe -= 300000000L;
                    speedy -= 20;
                    if((probability_movil_pipe-3) >= 0){
                        probability_movil_pipe -= 3;
                    }


                }
                rocket.superado = true;
            }
            if (rocket.getX() < -48) {
                rockets.removeValue(rocket, true);
            }
        }

        //La puntuació augmenta amb el temps de joc
        //score += Gdx.graphics.getDeltaTime();

        if(dead) {
            game.manager.get("fail_v2.mp3", Sound.class).play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore) game.topScore = game.lastScore;
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }
    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
