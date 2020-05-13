package com.mfrancetic.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

    // Sprinte = image, character, background
    SpriteBatch batch;
    Texture background;

    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float gravity = 2;
    float velocity = 0;
    int gameState = 0;

    private float screenWidth;
    private float screenHeight;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();

        birdY = screenHeight / 2 - birds[0].getHeight() / 2;
    }

    @Override
    // happens continuosly
    public void render() {
        // if the game is being played
        if (gameState != 0) {
            jumpIfScreenTouched();
            fallDown();
        } else {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        defineBirdFlapState();
        displayTextures();
    }

    private void defineBirdFlapState() {
        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }
    }

    private void jumpIfScreenTouched() {
        // jump if the screen was touched, but not above the screen
        if (Gdx.input.justTouched() && birdY < (screenHeight - 250)) {
            velocity = -30;
        }
    }

    @Override
    public void dispose() {

    }


    private void fallDown() {
        // increase velocity / the bird will fall faster and faster - but only until the
        // bottom of the screen
        if (birdY > 10 || velocity < 0) {
            velocity += gravity;
            birdY -= velocity;
        }
    }

    private void displayTextures() {
        // we will start displaying sprites
        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);
        batch.draw(birds[flapState], screenWidth / 2 - birds[flapState].getWidth() / 2, birdY);

        batch.end();
    }
}