package com.mygdx.game.typingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBox extends Actor {
    private String quotes;
    protected String getQuotes(){
        return quotes;
    }
    boolean isEnd = false;
    boolean isStart = false;
    private BitmapFont font;

    protected BitmapFont getFont() {
        return font;
    }

    private ArrayList<Character> wordTyped = new ArrayList<>();
    protected String getWordTyped(){
        return wordTyped_to_String();
    }
    protected int banyak_salah;
    MyBongo bongo;
    private final monType momon;

    public monType getMomon() {
        return momon;
    }

    public TextBox(final MyBongo bongo, monType monType) {
        momon = monType;
        if (momon == com.mygdx.game.typingGame.monType.MONSTER)
            font = new BitmapFont(Gdx.files.internal("Typing/Freestyle.fnt"));
        else if (momon == com.mygdx.game.typingGame.monType.BOSS_PHASE3)
            font = new BitmapFont(Gdx.files.internal("Typing/Signature.fnt"));
        else
            font = new BitmapFont(Gdx.files.internal("Typing/Geek.fnt"));
        this.bongo = bongo;
        //jangan lupa bounds supaya bisa interact sama benda lain
        setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        setTouchable(Touchable.enabled);
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                bongo.change();
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                bongo.change();
                return true;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (!isEnd && isStart) {
                    if (character == '\n') {
                        wordTyped = new ArrayList<>();
                        return true;
                    } else if (character == '\b') {
                        if (!wordTyped.isEmpty()) {
                            wordTyped.remove(wordTyped.size() - 1);
                        }
                    }
                    checkNextWord(character);
                }
//
//                for (Character character1 : wordTyped) {
//                    System.out.print(character1);
//                }
//                System.out.println("|");
                return true;
            }
        });
    }
    protected void generateText(){
        quotes = randomQuotes();
    }

    private String randomQuotes() {
        ArrayList<String> allQuotes = new ArrayList<>();
        try {
            File quotes;
            if (momon == monType.MONSTER)
                quotes = Gdx.files.internal("assets/Typing/QuotesMonster").file();
            else quotes = Gdx.files.internal("assets/Typing/QuotesBoss").file();
            Scanner myReader = new Scanner(quotes);
            while (myReader.hasNextLine()) {
                allQuotes.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Collections.shuffle(allQuotes);
        int random = (int) (Math.random()*allQuotes.size());
        return allQuotes.get(random);
    }

    private String wordTyped_to_String(){
        StringBuilder word = new StringBuilder();
        for (char c : wordTyped) {
            word.append(c);
        }
        return word.toString();
    }

    private void checkNextWord(char chaa) {

        //kalo input character salah wrong counter nya nambah + ga di add di array of char
        int index = wordTyped.size();
        if (index != quotes.length()) {
            if (chaa == quotes.charAt(index)) {
                wordTyped.add(chaa);
            } else if (chaa == ' ' && quotes.charAt(index) == '\n'){
                wordTyped.add('\n');
            } else banyak_salah++;
        }

        //ngubah arrayOfChar input jadi String
        String wordCheck = wordTyped_to_String();

        //kalo kata2 nya udah sama ganti kata berikut e
        if (quotes.equals(wordCheck)) {
            isEnd = true;
        }
    }

    private GlyphLayout layout = new GlyphLayout();

    @Override
    public void draw(Batch batch, float parentAlpha) { //supaya bisa draw
        if (CounterTime.timeCount<0){
            isEnd = true;
        }
        if (isEnd) {
            return;
        }
        int width = Gdx.graphics.getWidth()-50;
        int height = Gdx.graphics.getHeight();
        layout.setText(font, quotes);
        if (layout.width > width) {
            String[] quotesArray = quotes.split(" ");
            String quotesTemp = "";
            for (int i = quotesArray.length; i > -1; i--) {
                quotes = "";
                quotesTemp = "";
                int j = 0;
                for (; j < i; j++) {
                    quotes = quotes.concat(quotesArray[j]);
                    if (j != i -1) quotes = quotes.concat(" ");
                }
                for (; j < quotesArray.length; j++) {
                    quotesTemp = quotesTemp.concat(quotesArray[j]);
                    if (j != quotesArray.length -1) quotesTemp = quotesTemp.concat(" ");
                }

                layout.setText(font, quotes);
                if (layout.width < width){
                    quotes = quotes + "\n" + quotesTemp;
                    break;
                }
            }
        }
        if (isStart){
            if (momon == monType.BOSS_PHASE2) {
                font.setColor(Color.PINK);
                font.draw(batch
                        , quotes
                        , width / 2 - layout.width / 2 + 30
                        , height / 2 + layout.height / 3
                        , Gdx.graphics.getWidth()
                        , Align.left, false);
                font.setColor(Color.CYAN);

                font.draw(batch
                        , quotes
                        , width / 2 - layout.width / 2 + 25
                        , height / 2 + layout.height / 3
                        , Gdx.graphics.getWidth()
                        , Align.left, false);
                font.setColor(Color.GREEN);
            }
            else {
                font.setColor(Color.WHITE);
                font.draw(batch
                        , quotes
                        , width / 2 - layout.width / 2 + 25
                        , height / 2 + layout.height / 3
                        , Gdx.graphics.getWidth()
                        , Align.left, false);
            }
            font.setColor(Color.GREEN);
            font.draw(batch, wordTyped_to_String()
                    , width / 2 - layout.width / 2 + 25
                    , height / 2 + layout.height / 3
                    , Gdx.graphics.getWidth()
                    , Align.left, false);
        }
    }
}
