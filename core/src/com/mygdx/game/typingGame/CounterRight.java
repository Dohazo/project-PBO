package com.mygdx.game.typingGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.MyGdxGame;

public class CounterRight extends Counter{
    private final int multiplier;
    private double attPer;
    public double getAttPer(){
        return attPer;
    }

    public CounterRight(TextBox tb) {
        super(tb);
        if (tb.getMomon() == monType.MONSTER)
            multiplier = 100;
        else if (tb.getMomon() == monType.BOSS_PHASE1)
            multiplier = 90;
        else if (tb.getMomon() == monType.BOSS_PHASE2)
            multiplier = 60;
        else
            multiplier = 30;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (CounterTime.timeCount<0){
            double quotesLen = super.getTb().getQuotes().length();
            double correctCount = super.getTb().getWordTyped().length();
            String textCorrect = "Correct: " + correctCount;
            String textRemaining = "Out of: " + quotesLen;
            String textAttackPercentage = "AtK/Def %: ";
            double missing = quotesLen-correctCount;
            if (missing>40)
                missing = 40;
            //IDK multiplier thing
            attPer = Math.round((((correctCount- getTb().banyak_salah/2.0)/ quotesLen))*100*multiplier-missing*100)/100.0;
            MyGdxGame.booster = attPer;
            if (attPer<0)
                textAttackPercentage = textAttackPercentage.concat(attPer + "%");
            else
                textAttackPercentage = textAttackPercentage.concat("+" + attPer + "%");

            String textTyped = textAnimation(textCorrect);
            getFont().setColor(Color.GREEN);
            drawBased(batch, 1, textTyped);

            textTyped = textAnimation(textRemaining);
            getFont().setColor(Color.ORANGE);
            drawBased(batch, -1, textTyped);

            textTyped = textAnimation(textAttackPercentage);
            getFont().setColor(Color.CYAN);
            drawBased(batch, -3, textTyped);
        }
    }
}
