package com.mfrancetic.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    // Sprinte = image, character, background
    private SpriteBatch batch;
    private Texture background;

    private Texture[] birds;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private int gameState = 0;

    private Texture topTube;
    private Texture bottomTube;
    private float gap = 400;
    private float maxTubeOffset;
    private Random randomGenerator;
    float tubeVelocity = 4;
    private int numberOfTubes = 4;
    private float[] tubeX = new float[numberOfTubes];
    private float[] tubeOffset = new float[numberOfTubes];
    private float distanceBetweenTubes;

    private float screenWidth;
    private float screenHeight;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        maxTubeOffset = screenHeight / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = screenWidth * 3 / 4;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (screenHeight - gap - 200);
            // first tube in the center, other one in the half of the screen, then 1 screen away...
            tubeX[i] = screenWidth / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes;
        }

        birdY = screenHeight / 2 - birds[0].getHeight() / 2;
    }

    @Override
    // happens continuosly
    public void render() {
        beginDrawing();
        drawBackground();

        // if the game is being played
        if (gameState != 0) {
            jumpIfScreenTouched();
            drawTubes();
            fallDown();
        } else {
            // if the game hasn't started yet, start it (set the gameState to 1)
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        defineBirdFlapState();
        drawBird();
        endDrawing();
    }

    private void drawBackground() {
        batch.draw(background, 0, 0, screenWidth, screenHeight);
    }

    private void beginDrawing() {
        // we will start displaying sprites
        batch.begin();
    }

    private void endDrawing() {
        batch.end();
    }

    private void drawBird() {
        batch.draw(birds[flapState], screenWidth / 2 - birds[flapState].getWidth() / 2, birdY);
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
            float gravity = 2;
            velocity += gravity;
            birdY -= velocity;
        }
    }

    private void drawTubes() {
        for (int i = 0; i < numberOfTubes; i++) {
            // redraw tube when it is passed the screen
            if (tubeX[i] < -topTube.getWidth()) {
                tubeX[i] += numberOfTubes * distanceBetweenTubes;
            }

            tubeX[i] -= tubeVelocity;
            batch.draw(topTube, tubeX[i], screenHeight / 2 + gap / 2 + tubeOffset[i]);
            batch.draw(bottomTube, tubeX[i], screenHeight / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
        }
    }
}