package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyMenu extends Game {

    public void create() {
        this.setScreen(new StartMenu(this));
    }
    public void render () {
        super.render();
    }


    public void dispose () {

    }
}
