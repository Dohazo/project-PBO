package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;


public class StartMenu implements Screen {
    private Game game;
    SpriteBatch batch;
    Stage stage;
    Texture texture;
    Skin skin;
    Viewport viewport;
    Table table;
    Music selectMenu;
    Sprite sprite;
    Music bgm;
    TextureRegion img;
    Music click;
private int offset ;
    public StartMenu(final Game game) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        // camera
        viewport = new FillViewport(1920, 1080);
        viewport.apply();
        batch = new SpriteBatch();
        offset = 0;
        //setMusic
        click = Gdx.audio.newMusic(Gdx.files.internal("Cat-meow.mp3"));
        selectMenu = Gdx.audio.newMusic(Gdx.files.internal("Blip_select.mp3"));
        bgm = Gdx.audio.newMusic(Gdx.files.internal("Fluffing-a-Duck.mp3"));
//        // editMusik
        bgm.play();
        bgm.setLooping(true);
        // Set Background agar bisa fit screen
        texture = new Texture(Gdx.files.internal("forest.jpg"));

       img = new TextureRegion(texture, 7500, 2250);
        Image background = new Image(texture);
        //declare skin
        skin = new Skin(Gdx.files.internal("comic-ui.json"));

        // Full screen

        stage = new Stage(viewport);
        // supaya Table ngisi screen
        table = new Table();

        Gdx.input.setInputProcessor(stage); // inputProcessor dibawa ke stage
        //add Button
        TextButton startButton = new TextButton("Start", skin);

        TextButton exitButton = new TextButton("Exit", skin);
        final Label label = new Label("HELLO", skin, "title");
        label.setVisible(false);
        //edit listener button
        startButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selectMenu.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                selectMenu.play();
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                bgm.stop();
                game.setScreen(new GameMenu(game));
                bgm.dispose();

            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selectMenu.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                selectMenu.play();
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        background.setSize(7500, 2250);
        table.setFillParent(true);
        //add button ke table
        table.add(startButton).size(300,100).padBottom(50);
        table.row();
        table.add(exitButton).size(300,100).padBottom(50);
        table.row();
        table.add(label);
        //edit table

        table.setPosition(0, -50);
        //agar dibawah
        table.row();
        //add ke actor
        stage.addActor(table);
        stage.getViewport().apply();
        //update camera
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {


        ScreenUtils.clear(1, 1, 1, 1);
        viewport.apply();
batch.begin();
if (Gdx.input.justTouched()){
    click.play();
}
offset++;
if (offset % 1200 == 0){
    offset = 0;
}


        batch.draw(img,-offset,0,1920,1080);
        batch.end();
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
