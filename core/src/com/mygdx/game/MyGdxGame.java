package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.typingGame.TypingGameStage;
import com.mygdx.game.typingGame.monType;

import java.security.PrivateKey;
import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.input;

public class MyGdxGame extends ApplicationAdapter implements Screen {
	private Game game;
	//Music
	Music combatMusic;
	Music bossMusic;

	//size
	int width = 640;
	int height = 480;
	int enemyIndex;
	public static double booster;

	//Texture
	private Texture healthBar;
	private Texture barrierBar;
	private Texture backgroundTexture;
	private Texture hudTexture;

	//Viewport
	private Viewport fitViewport;

	//Button
	private TextButton attack;
	private TextButton primary;
	private TextButton secondary;
	private Label win;
	private Label lose;
	private TextButton backMenu;
	private TextButton backChara;

	//Characters
	private Characters player;
	private Characters enemy;
	private Characters boss;

	//Battle
	private battle.battleTurn turn;
	float stateTime;
	private Skin comic;
	private BitmapFont font;

	//Sprite
	SpriteBatch spriteBatch;

	//Stage
	private Stage stage;
	private boolean wining;
	private TypingGameStage typingGameStage;///////////////
	private CurrentSkill cs;

	//Enemies
	ArrayList<Characters> enemies;
	public MyGdxGame(final Game game){
		this.game = game;


		// Button & Label

		//Music
		combatMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/combatMusic.mp3"));
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/bossMusic.mp3"));
		combatMusic.play();

		//Camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		//Viewport

		fitViewport = new FitViewport(640, 480);

//		fitViewport.setScreenPosition(640, 480);
		//Healthbar
		healthBar = new Texture(Gdx.files.internal("character/healthbar.png"));
		barrierBar = new Texture(Gdx.files.internal("character/barrierBar.png"));

		//background
		backgroundTexture = new Texture(Gdx.files.internal("character/battleBackground.png"));
		hudTexture = new Texture(Gdx.files.internal("character/battleHud.png"));

		//Inisiasi karakter
		turn = battle.battleTurn.PLAYER_TURN;
		if (GameMenu.pilihan == 1) {
			player = new Fighter();
		} else if (GameMenu.pilihan == 2) {
			player = new Assassin();
		}

		boss = new Boss();
		enemy = new Monster();
		enemies = new ArrayList<>();
		enemyIndex = 0;

//		enemies.add(enemy);
//		enemies.add(new Monster());
		enemies.add(new Monster());
		enemies.add(boss);

		//health
		font = new BitmapFont();

		//Stage
		stage = new Stage(fitViewport);
		input.setInputProcessor(stage);


		Skin skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
		comic = new Skin(Gdx.files.internal("comic-ui.json"));
		//Button
		attack = new TextButton("Attack", skin);
		attack.setVisible(true);

		primary = new TextButton("Primary", skin);
		primary.setVisible(true);

		secondary = new TextButton("Secondary", skin);
		secondary.setVisible(true);

		win = new Label("You Win", comic,"title");
		win.setVisible(false);

		win.setPosition(225,400);
		lose = new Label("You Lose", comic,"title");

		lose.setVisible(false);
		lose.setPosition(225,400);

		backMenu = new TextButton("Main Menu", comic);
		backMenu.setVisible(false);
		backMenu.setPosition(200, 300);


		backChara = new TextButton("Character Select", comic);
		backChara.setVisible(false);
		backChara.setPosition(145,150);

		backChara.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameMenu(game));
				bossMusic.stop();
				combatMusic.stop();
			}
		});


		backMenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new StartMenu(game));
				bossMusic.stop();
				combatMusic.stop();
			}
		});


		attack.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				attackType();////////////////
				cs = CurrentSkill.ATTACK;
			}
		});

		primary.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				attackType();////////////////
				cs = CurrentSkill.PRIMARY;
			}
		});

		secondary.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				attackType();///////////////////
				cs = CurrentSkill.SECONDARY;
			}
		});

		stage.addActor(attack);
		stage.addActor(primary);
		stage.addActor(secondary);
		stage.addActor(win);
		stage.addActor(lose);
		stage.addActor(backMenu);
		stage.addActor(backChara);
		stage.getViewport().apply();

		//Table
		Table root = new Table();
		root.setFillParent(true);

		stage.addActor(root);
		root.add(attack);
		root.add(primary);
		root.add(secondary);
		root.setPosition(-175, -188);
		//Sprite Batch
		spriteBatch = new SpriteBatch();

	}

	private void attackType(){
		if (enemies.get(enemyIndex) instanceof Monster) {
			startNewTyping(monType.MONSTER);
		}
		else
		{
			double health = enemies.get(enemyIndex).getHealth();
			if (health < 100)
				startNewTyping(monType.BOSS_PHASE3);
			else if (health<200) {
				startNewTyping(monType.BOSS_PHASE2);
			} else
				startNewTyping(monType.BOSS_PHASE1);
		}
	}

	// implements Screen
	@Override
	public void show() {
	Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(new Color(0, 0, 0, 0));
		stateTime += Gdx.graphics.getDeltaTime();
		fitViewport.apply();

		//Battle State
		if (enemies.get(enemyIndex).getHealth() <=0){
			enemies.get(enemyIndex).setHealth(0);
			if (enemyIndex + 1 == enemies.size()){
				turn = battle.battleTurn.END;
				attack.setVisible(false);
				primary.setVisible(false);
				secondary.setVisible(false);
				backChara.setVisible(true);
				backMenu.setVisible(true);
				win.setVisible(true);
			} else {
				enemyIndex++;
			}
		}

		if (player.getHealth() <= 0){
			attack.setVisible(false);
			primary.setVisible(false);
			secondary.setVisible(false);
			lose.setVisible(true);
			backChara.setVisible(true);
			backMenu.setVisible(true);
			turn = battle.battleTurn.END;
		}

		//music
		if (enemies.get(enemyIndex) instanceof Boss){
			combatMusic.stop();
			bossMusic.play();
			bossMusic.setVolume(0.5f);
		}

		//cooldown
		if (player.getPrimaryCooldown() > 0){
			primary.setDisabled(true);
			primary.setText("Cooldown(" + player.getPrimaryCooldown() + ")");
		}else {
			primary.setDisabled(false);
			primary.setText("Primary");
		}

		if (player.getSecondaryCooldown() > 0){
			secondary.setDisabled(true);
			secondary.setText("Cooldown(" + player.getSecondaryCooldown()+ ")");
		} else {
			secondary.setDisabled(false);
			secondary.setText("Secondary");
		}

		//turn
		if (turn == battle.battleTurn.ENEMY_TURN){
			if (enemies.get(enemyIndex) instanceof Boss){
				((Boss) enemies.get(enemyIndex)).skillRandomizer(player);
			} else {
				enemies.get(enemyIndex).attack(player, booster);
				enemies.get(enemyIndex).setCurrentState(Characters.AnimationState.ATTACKING);
			}

			turn = battle.battleTurn.PLAYER_TURN;
//				player.receiveDamage(enemies.get(enemyIndex).getDamage());

			System.out.println(enemy.getHealth());
			System.out.println(player.getHealth());
		}


		if (enemies.get(enemyIndex).getAttackAnimation().isAnimationFinished(stateTime)){
			turn = battle.battleTurn.PLAYER_TURN;
		}

		player.updateAnimation(stateTime);
		enemies.get(enemyIndex).updateAnimation(stateTime);

		spriteBatch.begin();
		//Draw Background
		spriteBatch.draw(backgroundTexture, 0, 0, width, height);
		spriteBatch.draw(hudTexture, 0, 0, width, 104);

		//Draw character
		spriteBatch.draw(player.getPlayerFrame(), -100, 50, 500, 500);
		spriteBatch.draw(enemies.get(enemyIndex).getPlayerFrame(), 350, 125, 250, 250);

		//Draw player's healthbars & barrier
		spriteBatch.draw(healthBar, 20, height - 35,
				width * (float) player.getHealth()/600, 20);

		spriteBatch.draw(barrierBar, 20, height - 55,
				width * (float) player.getBarrier()/300, 20);

		//Draw enemy's healthbars
		spriteBatch.draw(healthBar, 600, height - 35,//350 -> 750
				width * (float) enemies.get(enemyIndex).getHealth()/900 * -1, 20);

		font.draw(spriteBatch, "Player Health: " + player.getHealth(), 20, height - 20);
		font.draw(spriteBatch, "Player Barrier: " + player.getBarrier(), 20, height - 40);
		font.draw(spriteBatch, "Boss Health: " + enemies.get(enemyIndex).getHealth(), 450, height - 20);
		spriteBatch.end();

		//stage
		stage.act();
		stage.draw();

		///// stage typing

		drawTypingGame();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		fitViewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private void drawTypingGame(){
		if (!TypingGameStage.isEnd && typingGameStage != null) {
			typingGameStage.draw();
		} else {
			Gdx.input.setInputProcessor(stage);// pindah ke Main
			attackOnce();
			System.out.println(booster);
		}
	}

	private void startNewTyping(monType momon){
		if (typingGameStage!= null)
			typingGameStage.dispose();
		typingGameStage = new TypingGameStage(fitViewport, momon);
		Gdx.input.setInputProcessor(typingGameStage);
	}

	enum CurrentSkill{
		ATTACK, PRIMARY, SECONDARY, NONE
	}

	public void attackOnce(){
		Characters emeni = enemies.get(enemyIndex);
		if (cs == CurrentSkill.ATTACK) {
			player.attack(emeni, booster);
			turn = battle.battleTurn.ENEMY_TURN;
			player.setCurrentState(Characters.AnimationState.ATTACKING);
			stateTime = 0f;
		} else if (cs == CurrentSkill.PRIMARY){
			player.primarySkill(emeni, booster);
			turn = battle.battleTurn.ENEMY_TURN;
			player.setCurrentState(Characters.AnimationState.USING_PRIMARY);
			stateTime = 0f;
		} else if (cs == CurrentSkill.SECONDARY) {
			player.secondarySkill(emeni, booster);
			turn = battle.battleTurn.ENEMY_TURN;
			player.setCurrentState(Characters.AnimationState.USING_SECONDARY);
			stateTime = 0f;
		}
		cs = CurrentSkill.NONE;
	}

}

