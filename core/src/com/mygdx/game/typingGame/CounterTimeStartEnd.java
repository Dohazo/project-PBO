package com.mygdx.game.typingGame;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.TimeUtils;

public class CounterTimeStartEnd extends Counter{
    private long lastTime;
    public int timeCount1 = 4, timeCount2 = 6;
    public CounterTimeStartEnd(TextBox tb) {
        super(tb);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!getTb().isEnd && !getTb().isStart) {
            if (TimeUtils.nanoTime() - lastTime > 1000000000) {
                lastTime = TimeUtils.nanoTime();
                if (timeCount1 >= 0)
                    timeCount1--;
            }
            if (timeCount1<0)
                getTb().isStart = true;

            drawBased(batch, 1, String.valueOf(timeCount1));

        }
        if (getTb().isEnd){
            if (TimeUtils.nanoTime() - lastTime > 1000000000) {
                lastTime = TimeUtils.nanoTime();
                if (timeCount2 >= 0)
                    timeCount2--;
            }
            if (timeCount2<0)
                TypingGameStage.isEnd = true;
        }
    }
}
