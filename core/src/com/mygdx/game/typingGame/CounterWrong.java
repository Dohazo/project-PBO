package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

public class CounterWrong extends Counter {
    private final TextBox tb;
    CounterWrong(TextBox textBox){
        super(textBox);
        tb = textBox;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (CounterTime.timeCount > -1) {
            if (tb.isStart) {
                layout.setText(super.getFont(), "Wrong: " + (super.getTb().banyak_salah));
                super.getFont().draw(batch
                        , "Wrong: " + (super.getTb().banyak_salah)
                        , 50
                        , Gdx.graphics.getHeight() - layout.height * 3
                        , Gdx.graphics.getWidth()
                        , Align.left, false);
            }
        }
        else {
            String text = "Wrong: " + (super.getTb().banyak_salah);
            String textTyped = textAnimation(text);
            super.getFont().setColor(Color.RED);
            drawBased(batch, 3, textTyped);
        }
    }
}
