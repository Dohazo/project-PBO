package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import static com.badlogic.gdx.Gdx.input;

public class Catatan extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    boolean clicked = false;

    private long animationCoolDown;

    //Animasi
    private static final int FRAME_COLS = 16, FRAME_ROWS = 16;
    public Animation<TextureRegion> idleAnimation;
    public Animation<TextureRegion> kickAnimation;
    Texture fighterSheet;
    SpriteBatch spriteBatch;

    float stateTime;
    int i = 0;
    int j = 0;
    int x = 1;
    int y = 10;
    TextureRegion[] walkFrames;
    TextureRegion[] kickFrames;
    int index;
    TextureRegion[][] tmp;

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        fighterSheet = new Texture(Gdx.files.internal("fighter-Sheet.png"));
        tmp = TextureRegion.split(fighterSheet,
                fighterSheet.getWidth() / FRAME_COLS,
                fighterSheet.getHeight() / FRAME_ROWS);

        walkFrames = new TextureRegion[4];
        kickFrames = new TextureRegion[10];
        index = 0;
        int index2 = 0;
        for (i = 0; i < x; i++) {
            for (j = 0; j < y; j++) {
                System.out.println("Zzzz");
                if (j<4) {
                    walkFrames[index] = tmp[i][j];
                }
                kickFrames[index] = tmp[i+3][j];
                index++;
            }
        }

        idleAnimation = new Animation<TextureRegion>(0.1f, walkFrames);
        kickAnimation = new Animation<TextureRegion>(0.1f, kickFrames);

        spriteBatch = new SpriteBatch();
        stateTime = 0f;

        input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                if (input.isKeyJustPressed(Input.Keys.F)){
                    System.out.println();
                }
                clicked = true;

                return true;
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
        });
    }



    @Override
    public void render () {
        ScreenUtils.clear(new Color(0xFFFFFF));
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        if (clicked){
            currentFrame = kickAnimation.getKeyFrame(stateTime, true);
            animationCoolDown = TimeUtils.nanoTime();
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }

        if (TimeUtils.nanoTime() - animationCoolDown > 100){
            animationCoolDown = TimeUtils.nanoTime();
            clicked = false;
        }

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50, 500, 500); // Draw current frame at (50, 50)
        spriteBatch.end();
    }

    @Override
    public void dispose () {

    }
}
