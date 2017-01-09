package ducnguyen.hex;

import java.util.ArrayList;

/**
 * Created by ducng on 12/16/2016.
 */

public class Storage {
    private ArrayList<Integer> tileColorList = new ArrayList<Integer>();

    private int wPlayerColor;
    private int bPlayerColor;
    private int wPlayerActionCounter;
    private int bPlayerActionCounter;
    private int adCounter;

    private long startReset;
    private long startNewGame;
    private long startGame;
    private long startTap;

    private boolean wPlayerTurn;
    private boolean aiw;
    private boolean aigame;

    private boolean newGameTime;
    private boolean newGameCreated;
    private boolean activeGame;
    private boolean gameEnded;
    private boolean resetTime;
    private boolean reset;
    private boolean wPlayerWins;
    private boolean gameDelay;
    private boolean boardChange;
    private boolean tapTime;
    private boolean advertOn;

    private int playerColor1;
    private int playerColor2;
    private int aiColor;

    private int volumeindex;
    private int timedindex;
    private int turn;

    private boolean time;
    private boolean sound;
    private boolean firstPlayerWin;
    private boolean timeLoss;

    private int timer;

    private int gameStatistics[] = new int[8];
    private int colorStatistics[] = new int[9];

    private int difficulty;

    private int state;

    public void setTileColorList(ArrayList<Integer> tileColorList){
        this.tileColorList = tileColorList;
    }
    public ArrayList<Integer> getTileColorList(){
        return tileColorList;
    }
    public void setwPlayerColor(int color){
        wPlayerColor = color;
    }
    public int getwPlayerColor(){
        return wPlayerColor;
    }
    public void setbPlayerColor(int color){
        bPlayerColor = color;
    }
    public int getbPlayerColor(){
        return bPlayerColor;
    }
    public void setPlayerTurn(boolean b1){
        wPlayerTurn = b1;
    }
    public boolean getPlayerTurn(){
        return wPlayerTurn;
    }
    public void setPlayerActionCounter(int c, boolean wPlayer){
        if(wPlayer){
            wPlayerActionCounter = c;
        }
        else{
            bPlayerActionCounter = c;
        }
    }
    public int getPlayerActionCounter(boolean wPlayer){
        if(wPlayer){
            return wPlayerActionCounter;
        }
        else{
            return bPlayerActionCounter;
        }
    }
    public void setStartReset(long startReset){
        this.startReset = startReset;
    }
    public long getStartReset(){
        return startReset;
    }
    public void setStartNewGame(long startNewGame){
        this.startNewGame = startNewGame;
    }
    public long getStartNewGame(){
        return startNewGame;
    }
    public void setNewGameTime(boolean b){
        newGameTime = b;
    }
    public boolean getNewGameTime(){
        return newGameTime;
    }
    public void setNewGameCreated(boolean b){
        newGameCreated = b;
    }
    public boolean getNewGameCreated(){
        return newGameCreated;
    }
    public void setActiveGame(boolean b){
        activeGame = b;
    }
    public boolean getActiveGame(){
        return activeGame;
    }
    public void setGameEnded(boolean b){
        gameEnded = b;
    }
    public boolean getGameEnded(){
        return gameEnded;
    }
    public void setResetTime(boolean b){
        resetTime = b;
    }
    public boolean getResetTime(){
        return resetTime;
    }
    public void setReset(boolean b){
        reset = b;
    }
    public boolean getReset(){
        return reset;
    }
    public void setwPlayerWins(boolean b){
        wPlayerWins = b;
    }
    public boolean getwPlayerWins(){
        return wPlayerWins;
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return state;
    }
    public void setGameDelay(boolean b){
        gameDelay = b;
    }
    public boolean getGameDelay(){
        return gameDelay;
    }
    public void setStartGame(long startGame){
        this.startGame = startGame;
    }
    public long getStartGame(){
        return startGame;
    }
    public void setStartTap(long startTap){
        this.startTap = startTap;
    }
    public long getStartTap(){
        return startTap;
    }
    public void setTapTime(boolean tapTime){
        this.tapTime = tapTime;
    }
    public boolean getTapTime(){
        return tapTime;
    }
    public void setBoardChange(boolean boardChange){
        this.boardChange = boardChange;
    }
    public boolean getBoardChange(){
        return boardChange;
    }
    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public void setAiw(boolean aiw){
        this.aiw = aiw;
    }
    public boolean getAiw(){
        return aiw;
    }
    public void setPlayerColor1(int playerColor1){
        this.playerColor1 = playerColor1;
    }
    public int getPlayerColor1(){
        return playerColor1;
    }
    public void setPlayerColor2(int playerColor2){
        this.playerColor2 = playerColor2;
    }
    public int getPlayerColor2(){
        return playerColor2;
    }
    public void setVolumeindex(int volumeindex){
        this.volumeindex = volumeindex;
    }
    public int getVolumeindex(){
        return volumeindex;
    }
    public void setTimedindex(int timedindex){
        this.timedindex = timedindex;
    }
    public int getTimedindex(){
        return timedindex;
    }
    public void setTime(boolean time){
        this.time = time;
    }
    public boolean getTime(){
        return time;
    }
    public void setSound(boolean sound){
        this.sound = sound;
    }
    public boolean getSound(){
        return sound;
    }
    public void setFirstPlayerWin(boolean firstPlayerWin){
        this.firstPlayerWin = firstPlayerWin;
    }
    public boolean getFirstPlayerWin(){
        return firstPlayerWin;
    }
    public void setTimer(int timer){
        this.timer = timer;
    }
    public int getTimer(){
        return timer;
    }
    public void setColorStatistics(int[] colorStatistics){
        this.colorStatistics = colorStatistics;
    }
    public int[] getColorStatistics(){
        return colorStatistics;
    }
    public void setGameStatistics(int[] gameStatistics){
        this.gameStatistics = gameStatistics;
    }
    public int[] getGameStatistics(){
        return gameStatistics;
    }
    public void setTurn(int turn){
        this.turn = turn;
    }
    public int getTurn(){
        return turn;
    }
    public void setAiColor(int aiColor){
        this.aiColor = aiColor;
    }
    public int getAiColor(){
        return aiColor;
    }
    public void setAdvertOn(boolean advertOn){
        this.advertOn = advertOn;
    }
    public boolean getAdvertOn(){
        return advertOn;
    }
    public void setAdCounter(int adCounter){
        this.adCounter = adCounter;
    }
    public int getAdCounter(){
        return adCounter;
    }
    public void setAigame(boolean aigame){
        this.aigame = aigame;
    }
    public boolean getAigame(){
        return aigame;
    }
    public void setTimeLoss(boolean timeLoss){
        this.timeLoss = timeLoss;
    }
    public boolean getTimeLoss(){
        return timeLoss;
    }
}
