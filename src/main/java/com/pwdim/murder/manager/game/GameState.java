package com.pwdim.murder.manager.game;

public enum GameState {
    WAITING, STARTING, PLAYING, ENDING, RESTARTING;

    public boolean canTransitionTo(GameState next) {
        switch (this) {
            case WAITING: return next == STARTING;
            case STARTING: return next == PLAYING || next == WAITING;
            case PLAYING: return next == ENDING;
            case ENDING: return next == RESTARTING;
            case RESTARTING: return next == WAITING;
            default: return false;
        }
    }
}
