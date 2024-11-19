package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.Characters;
import com.mygdx.game.MyGdxGame;

public class GameMenu implements Screen {
    //Sprite
    private Game game;
    private Music clicked;
    private Stage stage;
    private Viewport viewport;
    private Table table;
    private Texture texture;
    private Skin skin;
    private SpriteBatch batch;
    private Music music;
    private Music select;
    private TextureRegion img;
    public static int pilihan;
    private Characters fighterAnimation;
    private Characters assassinAnimation;

    float stateTime;

    public GameMenu(final Game game) {
        stateTime = 0;
        fighterAnimation = new Fighter();
        assassinAnimation = new Assassin();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        // deklarasi awal (seperti create)

        this.game = game;
        viewport = new FillViewport(1920, 1080);
        viewport.apply();
        batch = new SpriteBatch();
        //Deklaarasi Skin

        skin = new Skin(Gdx.files.internal("comic-ui.json"));

        // Set Music
        clicked = Gdx.audio.newMusic(Gdx.files.internal("Cat-meow.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("CharaSelectMusic.mp3"));
        select = Gdx.audio.newMusic(Gdx.files.internal("Blip_select.mp3"));
        music.play();
        music.setLooping(true);
        // set Textur & background

        texture = new Texture(Gdx.files.internal("bgmGame.png"));
        img = new TextureRegion(texture, 1920, 1080);
        // set Cam
        // set viewport

        // set stage
        stage = new Stage(viewport);
        // set table
        table = new Table();
        //setButton dkk
        Label label = new Label("SELECT CHARACTER", skin, "title");
        Label pause = new Label("PAUSED", skin, "title");
        label.setSize(500, 200);
        TextButton fighter = new TextButton("FIGHTER", skin);

        TextButton assassin = new TextButton("ASSASSIN", skin);

        fighter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilihan = 1;

                music.stop();
                game.setScreen(new MyGdxGame(game));
                music.dispose();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                select.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                select.play();
            }
        });
        assassin.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                select.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                select.play();
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilihan = 2;
                music.stop();
                game.setScreen(new MyGdxGame(game));

                music.dispose();
            }
        });


        // add table
        table.add(fighter).size(250,100).padRight(500);
        table.add(assassin).size(250,100);
        table.setFillParent(true);

        // set pos
        label.setPosition(790, 800);
        table.setPosition(0, -400);

        //add stage
        stage.addActor(label);
        stage.addActor(table);
        stage.getViewport().apply();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        stateTime += Gdx.graphics.getDeltaTime();

        fighterAnimation.updateAnimation(stateTime);
        assassinAnimation.updateAnimation(stateTime);

        viewport.apply();
        batch.begin();

        batch.draw(img, 0, 0);
        batch.draw(fighterAnimation.getPlayerFrame(), -40, 50, 480, 480);
        batch.draw(assassinAnimation.getPlayerFrame(), 200, 50, 480, 480);

        batch.end();
        if (Gdx.input.justTouched()) {
            clicked.play();
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
