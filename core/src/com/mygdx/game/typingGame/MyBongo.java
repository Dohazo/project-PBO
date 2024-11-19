package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MyBongo extends Image {
    Texture background1 = new Texture(Gdx.files.internal("Typing/BongoBongo1.png"));
    Texture background2 = new Texture(Gdx.files.internal("Typing/BongoBongo2.png"));
    private int count = 0;
    public void dispose(){
        background1.dispose();
        background2.dispose();
    }

    public MyBongo(Texture texture) {
        super(texture);
    }

    public void change(){
        if (count == 0){
            this.setDrawable(new SpriteDrawable(new Sprite(background2)));
            count++;
        } else {
            this.setDrawable(new SpriteDrawable(new Sprite(background1)));
            count = 0;
        }
    }
}
