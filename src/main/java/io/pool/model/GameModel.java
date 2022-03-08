package io.pool.model;

public class GameModel {

    /**There will be five game types
     1:Solo game
     2:PvP game
     3:Easy Bot
     4:Medium Bot
     5:Hard Bot
     */
   private int gameType;

    public GameModel() {

    }

    public int getGameType() {return gameType;}

    public void setGameType(int gameType) {this.gameType = gameType;}
}
