package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;


abstract class Characters implements Skills{
    //Cooldown
    private int primaryCooldown = 0;
    private int secondaryCooldown = 0;

    //Animations
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> primaryAnimation;
    private Animation<TextureRegion> secondaryAnimation;
    private AnimationState currentState;
    private TextureRegion playerFrame;

    enum AnimationState{
        IDLE,
        ATTACKING,
        USING_PRIMARY,
        USING_SECONDARY
    }

    //Attributes
   private String name;
   private double health;
   private double attack;
   private double barrier;
   private double damage;


   //Get

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getAttack() {
        return attack;
    }

    public double getBarrier() {
        return barrier;
    }

    public double getDamage() {
        return damage;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public Animation<TextureRegion> getAttackAnimation() {
        return attackAnimation;
    }

    public Animation<TextureRegion> getPrimaryAnimation() {
        return primaryAnimation;
    }

    public AnimationState getCurrentState() {
        return currentState;
    }

    public TextureRegion getPlayerFrame() {
        return playerFrame;
    }

    public int getPrimaryCooldown() {
        return primaryCooldown;
    }

    public int getSecondaryCooldown() {
        return secondaryCooldown;
    }

    public Animation<TextureRegion> getSecondaryAnimation() {
        return secondaryAnimation;
    }

    //Set
    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void setBarrier(double barrier) {
        this.barrier = barrier;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    public void setAttackAnimation(Animation<TextureRegion> attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    public void setPrimaryAnimation(Animation<TextureRegion> primaryAnimation) {
        this.primaryAnimation = primaryAnimation;
    }

    public void setCurrentState(AnimationState currentState) {
        this.currentState = currentState;
    }

    public void setPlayerFrame(TextureRegion playerFrame) {
        this.playerFrame = playerFrame;
    }

    public void setPrimaryCooldown(int primaryCooldown) {
        this.primaryCooldown = primaryCooldown;
    }

    public void setSecondaryCooldown(int secondaryCooldown) {
        this.secondaryCooldown = secondaryCooldown;
    }

    public void setSecondaryAnimation(Animation<TextureRegion> secondaryAnimation) {
        this.secondaryAnimation = secondaryAnimation;
    }

    // Receive damage
    public void receiveDamage(double _damage){
        if (barrier > 0){
            barrier -= _damage;
            if (barrier < 0){
                health += barrier;
                barrier = 0;
            }
        } else {
            health -= _damage;
        }
    }

    public void updateAnimation(float _stateTime){

    }
}
class Fighter extends Characters {

    Fighter(){
        setCurrentState(AnimationState.IDLE);
        //Set Stats
        setName("Fighter");
        setAttack(50);
        setHealth(200);
        setBarrier(0);

        //Filling sheets
        //Textures
        Texture fighterSheet = new Texture(Gdx.files.internal("character/fighter-Sheet.png"));
        Texture fighterBuffSheet = new Texture(Gdx.files.internal("character/fighter_buff.png"));

        TextureRegion[][] tmpPrimary = TextureRegion.split(fighterSheet,
                fighterSheet.getWidth() / 16,
                fighterSheet.getHeight() / 16);

        TextureRegion[][] tmpSecondary = TextureRegion.split(fighterBuffSheet,
                fighterBuffSheet.getWidth() / 16,
                fighterBuffSheet.getHeight() / 8);

        //Frames
        //Texture Region
        TextureRegion[] idleFrames = new TextureRegion[4];
        TextureRegion[] attackFrames = new TextureRegion[10];
        TextureRegion[] primaryFrames = new TextureRegion[6];
        TextureRegion[] secondaryFrame = new TextureRegion[16];

        //Mengisi Texture Region
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                if (j<4) {
                    idleFrames[index] = tmpPrimary[i][j];
                }
                attackFrames[index++] = tmpPrimary[i + 3][j];
            }
        }

        index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 16; j++) {
                if (j < 6) {
                    primaryFrames[index] = tmpSecondary[i][j];
                }
                secondaryFrame[index] = tmpSecondary[i][j];
                index++;
            }
        }

        //Buat animasi
        setIdleAnimation(new Animation<>(0.1f, idleFrames));
        setAttackAnimation(new Animation<>(0.1f, attackFrames));
        setPrimaryAnimation(new Animation<>(0.1f, primaryFrames));
        setSecondaryAnimation(new Animation<>(0.05f, secondaryFrame));
    }
    @Override
    public void primarySkill(Characters _enemy, double _booster) {
        setBarrier((getBarrier() + 1.5 * getAttack()) +
                (getBarrier() + 1.5 * getAttack()) * _booster/100);
        setPrimaryCooldown(2);

        setSecondaryCooldown(getSecondaryCooldown() - 1);
        if (getSecondaryCooldown() < 0){
            setSecondaryCooldown(0);
        }
    }

    @Override
    public void secondarySkill(Characters _enemy, double _booster) {
        _enemy.setHealth(_enemy.getHealth() - ((this.getAttack() + getBarrier() / 2) +
                (this.getAttack() + getBarrier() / 2) * _booster/100));

        setHealth(getHealth() - getHealth() * 0.1);
        setBarrier(0);
        setSecondaryCooldown(3);

        setPrimaryCooldown(getPrimaryCooldown() - 1);
        if (getPrimaryCooldown() < 0){
            setPrimaryCooldown(0);
        }
    }

    @Override
    public void attack(Characters _enemy, double _booster) {
        _enemy.setHealth(_enemy.getHealth() - ((getAttack() / 2) +
                (getAttack() / 2) * _booster/100));
        setPrimaryCooldown(getPrimaryCooldown() - 1);
        setSecondaryCooldown(getSecondaryCooldown() - 1);

        if (getPrimaryCooldown() < 0){
            setPrimaryCooldown(0);
        }

        if (getSecondaryCooldown() < 0){
            setSecondaryCooldown(0);
        }
    }

    @Override
    public void updateAnimation (float _stateTime){
        switch (this.getCurrentState()) {
            case USING_SECONDARY:
                setPlayerFrame(this.getSecondaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case USING_PRIMARY:
                setPlayerFrame(this.getPrimaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case ATTACKING:
                setPlayerFrame(this.getAttackAnimation().getKeyFrame(_stateTime, true));
                if (this.getAttackAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            default:
                setPlayerFrame(this.getIdleAnimation().getKeyFrame(_stateTime, true));
                break;
        }
    }
}

class Assassin extends Characters {
    Assassin(){
        setCurrentState(AnimationState.IDLE);
        //Set Stats
        setName("Assassin");
        setAttack(80);
        setHealth(150);
        setBarrier(0);

        //Filling sheets
        //Textures
        Texture AssassinSheet = new Texture(Gdx.files.internal("character/assassin-Sheet.png"));
        Texture gunSheet = new Texture(Gdx.files.internal("character/gun-Sheet.png"));

        TextureRegion[][] tmpPrimary = TextureRegion.split(AssassinSheet,
                AssassinSheet.getWidth() / 16,
                AssassinSheet.getHeight() / 16);

        TextureRegion[][] tmpSecondary = TextureRegion.split(gunSheet,
                gunSheet.getWidth() / 16,
                gunSheet.getHeight() / 16);

        //Frames
        //Texture Region
        TextureRegion[] idleFrames = new TextureRegion[8];
        TextureRegion[] attackFrames = new TextureRegion[10];
        TextureRegion[] primaryFrames = new TextureRegion[12];
        TextureRegion[] secondaryFrame = new TextureRegion[8];

        //Mengisi Texture Region
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 12; j++) {

                if (j < 8) {
                    idleFrames[index] = tmpSecondary[i+1][j];
                }
                if (j < 10) {
                    attackFrames[index] = tmpPrimary[i + 9][j];
                }
                    primaryFrames[index] = tmpPrimary[i+8][j];
                if (j < 8) {
                    secondaryFrame[index] = tmpSecondary[i + 2][j];
                }
                index++;
            }
        }

        //Buat animasi
        setIdleAnimation(new Animation<>(0.1f, idleFrames));
        setAttackAnimation(new Animation<>(0.1f, attackFrames));
        setPrimaryAnimation(new Animation<>(0.1f, primaryFrames));
        setSecondaryAnimation(new Animation<>(0.05f, secondaryFrame));
    }

    @Override
    public void primarySkill(Characters _enemy, double _booster) {
        setAttack(this.getAttack() + _enemy.getAttack()/2 + _enemy.getAttack()/2 * _booster/100);
        setPrimaryCooldown(2);

        setSecondaryCooldown(getSecondaryCooldown() - 1);
        if (getSecondaryCooldown() < 0){
            setSecondaryCooldown(0);
        }

        if (getSecondaryCooldown() == 0){
            setAttack(80);
        }
    }

    @Override
    public void secondarySkill(Characters _enemy, double _booster) {
        _enemy.setHealth(_enemy.getHealth() - ((this.getAttack()/2 + (200 - this.getHealth())) +
                (200 - this.getHealth()) * _booster/100));

        setHealth(getHealth() - getHealth() * 0.1);
        setBarrier(0);
        setSecondaryCooldown(3);

        setPrimaryCooldown(getPrimaryCooldown() - 1);
        if (getPrimaryCooldown() < 0){
            setPrimaryCooldown(0);
        }
    }

    @Override
    public void attack(Characters _enemy, double _booster) {
        _enemy.setHealth(_enemy.getHealth() - ((getAttack() / 2) +
                (getAttack() / 2) * _booster/100));
        setPrimaryCooldown(getPrimaryCooldown() - 1);
        setSecondaryCooldown(getSecondaryCooldown() - 1);

        if (getPrimaryCooldown() < 0){
            setPrimaryCooldown(0);
        }

        if (getSecondaryCooldown() < 0){
            setSecondaryCooldown(0);
        }
    }

    @Override
    public void updateAnimation (float _stateTime){
        switch (this.getCurrentState()) {
            case USING_SECONDARY:
                setPlayerFrame(this.getSecondaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case USING_PRIMARY:
                setPlayerFrame(this.getPrimaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case ATTACKING:
                setPlayerFrame(this.getAttackAnimation().getKeyFrame(_stateTime, true));
                if (this.getAttackAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            default:
                setPlayerFrame(this.getIdleAnimation().getKeyFrame(_stateTime, true));
                break;
        }
    }
}
class Monster extends Characters {

    Monster(){
        setCurrentState(AnimationState.IDLE);
        setName("Enemy");
        setAttack(Math.floor(Math.random() * 50));
        setHealth(Math.floor(Math.random() * 50));
        setBarrier(0);

        //Filling sheets
        //Textures
        Texture enemySheet = new Texture(Gdx.files.internal("character/slime-Sheet.png"));


        TextureRegion[][] tmpPrimary = TextureRegion.split(enemySheet,
                enemySheet.getWidth() / 4,
                enemySheet.getHeight() / 5);

        //Frames
        //Texture Region
        TextureRegion[] idleFrames = new TextureRegion[4];
        TextureRegion[] attackFrames = new TextureRegion[4];
        TextureRegion[] primaryFrames = new TextureRegion[4];

        //Mengisi Texture Region
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {

                idleFrames[index] = tmpPrimary[i][j];
                attackFrames[index] = tmpPrimary[i + 2][j];
                primaryFrames[index] = tmpPrimary[i+3][j];

                index++;
            }
        }

        //Buat animasi
        setIdleAnimation( new Animation<>(0.1f, idleFrames));
        setAttackAnimation( new Animation<>(0.1f, attackFrames));
        setPrimaryAnimation( new Animation<>(0.1f, primaryFrames));
    }

    @Override
    public void primarySkill(Characters _enemy, double _booster) {

    }

    @Override
    public void secondarySkill(Characters _enemy, double _booster) {

    }

    @Override
    public void attack(Characters _player, double _booster) {
        if (_player.getBarrier() > 0){
            _player.setBarrier(_player.getBarrier() - (this.getAttack()/2));
            if (_player.getBarrier() < 0){
                _player.setHealth(_player.getHealth() + _player.getBarrier());
                _player.setBarrier(0);
            }
        } else {
            _player.setHealth(_player.getHealth() - (getAttack() / 2));
        }
    }

    @Override
    public void updateAnimation (float _stateTime){
        switch (this.getCurrentState()) {
            case USING_PRIMARY:
                setPlayerFrame(this.getPrimaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case ATTACKING:
                setPlayerFrame(this.getAttackAnimation().getKeyFrame(_stateTime, true));
                if (this.getAttackAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            default:
                setPlayerFrame(this.getIdleAnimation().getKeyFrame(_stateTime, true));
                break;
        }
    }
}

class Boss extends Characters {
    Texture phase1Sheet;
    Texture phase2Sheet;
    Texture phase3Sheet;
    Random rand = new Random();
    int skillChoice;
    Boss(){
        setCurrentState(AnimationState.IDLE);

        //Set Stats
        setName("Ultimax");
        setAttack(50);
        setHealth(300);
        setBarrier(0);

        //Filling sheets
        //Textures
        phase1Sheet = new Texture(Gdx.files.internal("character/bossPhase1.png"));
        phase2Sheet = new Texture(Gdx.files.internal("character/bossPhase2.png"));
        phase3Sheet = new Texture(Gdx.files.internal("character/bossPhase3.png"));

        TextureRegion[][] tmpPhase = TextureRegion.split(phase1Sheet,
                phase1Sheet.getWidth() / 4,
                phase1Sheet.getHeight() / 2);


        //Frames
        //Texture Region
        TextureRegion[] idleFrames = new TextureRegion[8];
        TextureRegion[] attackFrames = new TextureRegion[8];
        TextureRegion[] primaryFrames = new TextureRegion[8];

        //Mengisi Texture Region
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {

                idleFrames[index] = tmpPhase[i][j];
                attackFrames[index] = tmpPhase[i][j];
                primaryFrames[index] = tmpPhase[i][j];

                index++;
            }
        }

        //Buat animasi
        setIdleAnimation( new Animation<>(0.1f, idleFrames));
        setAttackAnimation( new Animation<>(0.1f, attackFrames));
        setPrimaryAnimation( new Animation<>(0.1f, primaryFrames));
    }

    public void skillRandomizer(Characters _enemy){
        skillChoice = rand.nextInt(3) + 1;
        switch (skillChoice){
            case 1:
                attack(_enemy, 1);
                break;
            case 2:
                primarySkill(_enemy, 1);
                break;
            case 3:
                secondarySkill(_enemy, 1);
        }
    }
    @Override
    public void primarySkill(Characters _player, double _booster) {
        _player.setHealth(_player.getHealth() - (getAttack() / 2));
        this.setHealth(this.getHealth() + (getAttack() / 2));
    }

    @Override
    public void secondarySkill(Characters _player, double _booster) {
        _player.setPrimaryCooldown(_player.getPrimaryCooldown() + 1);
    }

    @Override
    public void attack(Characters _player, double _booster) {
        if (_player.getBarrier() > 0){
            _player.setBarrier(_player.getBarrier() - (this.getAttack()/2));
            if (_player.getBarrier() < 0){
                _player.setHealth(_player.getHealth() + _player.getBarrier());
                _player.setBarrier(0);
            }
        } else {
            _player.setHealth(_player.getHealth() - (getAttack() / 2));
        }
    }

    @Override
    public void updateAnimation (float _stateTime){
        if (getHealth() <= 200){
            TextureRegion[][] tmpPhase = TextureRegion.split(phase2Sheet,
                    phase2Sheet.getWidth() / 4,
                    phase2Sheet.getHeight() / 2);


            //Frames
            //Texture Region
            TextureRegion[] idleFrames = new TextureRegion[8];
            TextureRegion[] attackFrames = new TextureRegion[8];
            TextureRegion[] primaryFrames = new TextureRegion[8];

            //Mengisi Texture Region
            int index = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {

                    idleFrames[index] = tmpPhase[i][j];
                    attackFrames[index] = tmpPhase[i][j];
                    primaryFrames[index] = tmpPhase[i][j];

                    index++;
                }
            }

            //Buat animasi
            setIdleAnimation( new Animation<>(0.1f, idleFrames));
            setAttackAnimation( new Animation<>(0.1f, attackFrames));
            setPrimaryAnimation( new Animation<>(0.1f, primaryFrames));
        }
        if (getHealth() <= 100) {
            TextureRegion[][] tmpPhase = TextureRegion.split(phase3Sheet,
                    phase3Sheet.getWidth() / 4,
                    phase3Sheet.getHeight() / 2);


            //Frames
            //Texture Region
            TextureRegion[] idleFrames = new TextureRegion[8];
            TextureRegion[] attackFrames = new TextureRegion[8];
            TextureRegion[] primaryFrames = new TextureRegion[8];

            //Mengisi Texture Region
            int index = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {

                    idleFrames[index] = tmpPhase[i][j];
                    attackFrames[index] = tmpPhase[i][j];
                    primaryFrames[index] = tmpPhase[i][j];

                    index++;
                }
            }

            //Buat animasi
            setIdleAnimation( new Animation<>(0.1f, idleFrames));
            setAttackAnimation( new Animation<>(0.1f, attackFrames));
            setPrimaryAnimation( new Animation<>(0.1f, primaryFrames));
        }

        switch (this.getCurrentState()) {
            case USING_PRIMARY:
                setPlayerFrame(this.getPrimaryAnimation().getKeyFrame(_stateTime, true));
                if (this.getPrimaryAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            case ATTACKING:
                setPlayerFrame(this.getAttackAnimation().getKeyFrame(_stateTime, true));
                if (this.getAttackAnimation().isAnimationFinished(_stateTime)){
                    setCurrentState(AnimationState.IDLE);
                }
                break;
            default:
                setPlayerFrame(this.getIdleAnimation().getKeyFrame(_stateTime, true));
                break;
        }
    }
}
