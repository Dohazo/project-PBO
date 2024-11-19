package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TypingGameStage extends Stage {
    private final TextBox textBox;
    private final CounterWrong wCount;
    private final CounterTime cTime;
    private final CounterRight rCount;
    private final CounterTimeStartEnd pspsCount;
    private final Texture bongoT = new Texture(Gdx.files.internal("Typing/BongoBongo1.png"));
    private final MyBongo bongo;
    public static boolean isEnd = true;

    public TypingGameStage(Viewport viewport, monType monType) {
        super(viewport);
        isEnd = false;
        bongo = new MyBongo(bongoT);
        bongo.setScale(0.25f);
        bongo.setPosition(7.5f,50);
        textBox = new TextBox(bongo, monType);
        textBox.generateText();
        wCount = new CounterWrong(textBox);
        if (monType == com.mygdx.game.typingGame.monType.MONSTER){
            cTime = new CounterTime(textBox,15);
        }else if (monType == com.mygdx.game.typingGame.monType.BOSS_PHASE1){
            cTime = new CounterTime(textBox,40);
        }else if (monType == com.mygdx.game.typingGame.monType.BOSS_PHASE2){
            cTime = new CounterTime(textBox,30);
        }else
            cTime = new CounterTime(textBox,20);

        rCount = new CounterRight(textBox);
        pspsCount = new CounterTimeStartEnd(textBox);
        this.addActor(bongo);
        this.addActor(cTime);
        this.addActor(textBox);
        this.addActor(wCount);
        this.addActor(rCount);
        this.addActor(pspsCount);
        this.setKeyboardFocus(textBox);
    }

    @Override
    public void dispose() {
        textBox.getFont().dispose();
        wCount.getFont().dispose();
        cTime.getFont().dispose();
        rCount.getFont().dispose();
        bongoT.dispose();
        bongo.dispose();
        pspsCount.getFont().dispose();
        this.getActors().clear();
        super.dispose();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
