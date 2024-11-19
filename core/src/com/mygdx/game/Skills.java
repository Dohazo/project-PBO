package com.mygdx.game;

public interface Skills {
    void primarySkill(Characters _enemy, double _booster);
    void secondarySkill(Characters _enemy, double _booster);
    void attack(Characters _enemy, double _booster);
}
