package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class Counter extends Actor {
    private final TextBox tb;
    private final BitmapFont font = new BitmapFont(Gdx.files.internal("Typing/Catcat.fnt"));
    protected BitmapFont getFont(){
        return font;
    }
    public Counter(TextBox tb) {
        this.tb = tb;
    }
    public TextBox getTb() {
        return tb;
    }
    protected GlyphLayout layout = new GlyphLayout();

    @Override
    public void draw(Batch batch, float parentAlpha) {
    }
    private int tempCount = 0;
    private long lastTyped=0;
    public String textAnimation(String text){
        if (TimeUtils.nanoTime() - lastTyped > 200000000) {
            lastTyped = TimeUtils.nanoTime();
            tempCount++;
        }
        String textTyped = "";
        if (tempCount<text.length()) {
            textTyped = textTyped.concat(text.substring(0, tempCount));
        }
        else textTyped = text;
        return textTyped;
    }
    public void drawBased(Batch batch, int x, String text){
        layout.setText(font, text);
        font.draw(batch
                    , text
                    , 10
                    , Gdx.graphics.getHeight()/2 + layout.height*x
                    , Gdx.graphics.getWidth()
                    , Align.center, false);
    }
}
