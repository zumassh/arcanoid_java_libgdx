package com.mygdx.arcanoid.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {

    private static final String PREFS_NAME = "arcanoid-prefs";
    private static final String KEY_LIVES_SETTING = "livesSetting";
    private static final String KEY_CURRENT_LEVEL = "currentLevel";
    private static final String KEY_LIVES_REMAINING = "livesRemaining";

    private static final int DEFAULT_LIVES_SETTING = 5;
    private static final int DEFAULT_LEVEL = 1;

    private final Preferences prefs;

    public GameSettings() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public int getLivesSetting() {
        return prefs.getInteger(KEY_LIVES_SETTING, DEFAULT_LIVES_SETTING);
    }

    public void setLivesSetting(int lives) {
        prefs.putInteger(KEY_LIVES_SETTING, lives);
        prefs.flush();
    }

    public void setCurrentLevel(int level) {
        prefs.putInteger(KEY_CURRENT_LEVEL, level);
        prefs.flush();
    }

    public int getLivesRemaining() {
        return prefs.getInteger(KEY_LIVES_REMAINING, getLivesSetting());
    }

    public void setLivesRemaining(int lives) {
        prefs.putInteger(KEY_LIVES_REMAINING, lives);
        prefs.flush();
    }

    public void resetProgress() {
        setCurrentLevel(DEFAULT_LEVEL);
        setLivesRemaining(getLivesSetting());
    }
}
