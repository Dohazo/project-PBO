package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class CounterTime extends Counter{
    public static int timeCount;
    private long lastTimeMin;
    public CounterTime(TextBox tb, int time) {
        super(tb);
        timeCount = time;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getTb().isEnd) {
            timeCount = -1;
            return;
        }
        if (timeCount > -1 && getTb().isStart) {
            if (TimeUtils.nanoTime() - lastTimeMin > 1000000000) {
                timeCount--;
                lastTimeMin = TimeUtils.nanoTime();
            }
//        if (timeCount == 0)

            layout.setText(super.getFont(), "Time left: " + timeCount);
            super.getFont().draw(batch
                    , "Time left: " + (timeCount)
                    , 50
                    , Gdx.graphics.getHeight() - layout.height - 100
                    , Gdx.graphics.getWidth()
                    , Align.left, false);
        }
    }
}
