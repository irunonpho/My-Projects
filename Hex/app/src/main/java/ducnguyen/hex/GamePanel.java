package ducnguyen.hex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by ducng on 12/13/2016.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    public static final int WIDTH = 800, HEIGHT = 480;
    public static final int MAXVOLUME = 51;
    private static final int CRIMSON = Color.rgb(220,20,60);
    private static final int ORANGE = Color.rgb(255,165,0);
    private static final int GOLD = Color.rgb(255,215,0);
    private static final int FORESTGREEN = Color.rgb(34,139,34);
    private static final int SKYBLUE = Color.rgb(135,206,235);
    private static final int INDIGO = Color.rgb(75, 0, 130);
    private static final int VIOLET = Color.rgb(238, 130, 238);
    public static int VOLUME;
    public static int ADLIMIT = 4;

    private Coordinate cursorPos;
    private ArrayList<Drop> drops = new ArrayList<Drop>();
    public Board board;
    private Tile selectedTile;
    private MainThread thread;
    private Player wPlayer;
    private Player bPlayer;
    private Player player;
    private AI aiPlayer;
    private long startReset;
    private long startNewGame;
    private long startGame;
    private long startTap;
    private long startAI;

    private static int EASY = Color.GREEN;
    private static int NORMAL = Color.YELLOW;
    private static int HARD= Color.RED;

    private int player1Color;
    private int player2Color;
    private int adCounter;
    private int NEON;

    private boolean player1;

    private boolean newGameTime;
    private boolean newGameCreated;
    private boolean activeGame;
    private boolean gameEnded;
    private boolean resetTime;
    private boolean reset;
    private boolean wPlayerWins;
    private boolean playerWins;
    private boolean gameDelay;
    private boolean boardChange;
    private boolean tapTime;
    private boolean gameQuit;
    private boolean successfulAction;
    private boolean aiGame;
    private boolean timedLoss;
    boolean menu;
    private boolean timed;
    public static boolean sound;
    private boolean firstPlayerWin;

    private int state;
    private int statebuffer = 0;
    private int difficulty;
    private int aiColor;
    private int orderIndex;
    private int turn;

    private int volumeIndex;
    private int timeIndex;

    private AITree test;

    private int timer = 0;
    private long startTimer = 0;
    long start = 0;
    long startTransition = 0;

    Tile favoriteTile;

    private boolean advertOn;

    float r = 0, g = 0, b = 0;
    float dr = 0, dg = 0, db = 0;
    int y = 0, dy = 40;



    // 0 is games played            0 is white
    // 1 is easy games played       1 is red
    // 2 is medium games played     2 is orange
    // 3 is hard games played       3 is yellow
    // 4 is games won               4 is green
    // 5 is easy won                5 is blue
    // 6 is medium won              6 is indigo
    // 7 is hard games won          7 is violet
    //                              8 is black

    private int gameStatistics[] = new int[8];
    private int colorStatistics[] = new int[9];
    ArrayList<Integer> order = new ArrayList<Integer>();

    private ArrayList<Soundeffects> soundEffects = new ArrayList<Soundeffects>();
    private Context context;

    public Board getBoard(){
        return board;
    }
    public Player getwPlayer(){
        return wPlayer;
    }
    public Player getbPlayer(){
        return bPlayer;
    }
    public Player getPlayer(){
        return player;
    }
    public AI getaiPlayer(){
        return aiPlayer;
    }
    public long getStartReset(){
        return startReset;
    }
    public long getStartNewGame(){
        return startNewGame;
    }
    public long getStartTap(){ return startTap; }
    public boolean getNewGameTime(){
        return newGameTime;
    }
    public boolean getNewGameCreated(){
        return newGameCreated;
    }
    public boolean getActiveGame(){
        return activeGame;
    }
    public boolean getGameEnded(){
        return gameEnded;
    }
    public boolean getResetTime(){
        return resetTime;
    }
    public boolean getReset(){
        return reset;
    }
    public boolean getwPlayerWins(){
        return wPlayerWins;
    }
    public boolean getPlayerWins(){
        return playerWins;
    }
    public boolean getGameDelay(){
        return gameDelay;
    }
    public boolean getboardChange(){
        return boardChange;
    }
    public boolean getTapTime(){
        return tapTime;
    }
    public boolean getTimed() {
        return timed;
    }
    public boolean getSound(){
        return sound;
    }
    public boolean getFirstPlayerWin(){
        return firstPlayerWin;
    }
    public boolean getAiGame(){
        return aiGame;
    }
    public boolean getTimeLoss(){
        return timedLoss;
    }

    public void setState(int state){
        this.statebuffer = state;
        menu = true;
    }
    public int getState(){
        return state;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public int getVolumeIndex(){
        return volumeIndex;
    }
    public int getTimeIndex(){
        return timeIndex;
    }

    public int getTimer(){
        return timer;
    }
    public int getTurn(){
        return turn;
    }

    public int[] getGameStatistics(){
        return gameStatistics;
    }
    public int[] getColorStatistics(){
        return colorStatistics;
    }

    public long getStartGame(){
        return startGame;
    }

    public int getPlayer1Color(){
        return player1Color;
    }
    public int getPlayer2Color(){
        return player2Color;
    }

    public int getAiColor(){
        return aiColor;
    }

    public boolean getAdvertOn(){
        return advertOn;
    }
    public int getAdCounter(){
        return adCounter;
    }

    public GamePanel(Context context){
        super(context);
        ArrayList<String> data = new ArrayList<String>();
        readFromFile(context, data);
        this.context = context;
        //add callback to SurfaceHolder to intercept events
        getHolder().addCallback(this);
        //add the callback to intercept events
        setFocusable(true);
        createGame(data);
    }
    public GamePanel(Context context, Storage s){
        super(context);
        this.context = context;
        //add callback to SurfaceHolder to intercept events
        getHolder().addCallback(this);
        //add the callback to intercept events
        setFocusable(true);

        cursorPos = new Coordinate();
        state = s.getState();
        aiGame = s.getAigame();
        difficulty = s.getDifficulty();//need to save instance of board and make constructor for board
        if(!s.getTileColorList().isEmpty()) {
            board = new Board();
            for (int i = 0; i < board.getTileList().size(); i++) {
                board.getTileList().get(i).setColor(s.getTileColorList().get(i));
            }
        }
        aiColor = s.getAiColor();
        if(state == 5 || (state == 12 && aiGame)) {
            player = new Player(s.getwPlayerColor(), true); //" " wPlayer
            aiPlayer = new AI(s.getAiw(), board, new Player(s.getbPlayerColor(), false), player, difficulty); //" " bPlayer

            if (s.getPlayerTurn()) {
                player.setTurn(true);
                aiPlayer.setTurn(false);
            } else {
                player.setTurn(false);
                aiPlayer.setTurn(true);
            }
            player.setActionCounter(s.getPlayerActionCounter(true));
            aiPlayer.setActionCounter(s.getPlayerActionCounter(false));
            playerWins = s.getwPlayerWins();
        }
        else if(state == 4|| (state == 12 && !aiGame)){
            wPlayer = new Player(s.getwPlayerColor(), true); //" " wPlayer
            bPlayer = new Player(s.getbPlayerColor(), false); //" " bPlayer

            if (s.getPlayerTurn()) {
                wPlayer.setTurn(true);
                bPlayer.setTurn(false);
            } else {
                wPlayer.setTurn(false);
                bPlayer.setTurn(true);
            }
            wPlayer.setActionCounter(s.getPlayerActionCounter(true));
            bPlayer.setActionCounter(s.getPlayerActionCounter(false));
            wPlayerWins = s.getwPlayerWins();
        }
        turn = s.getTurn();
        timedLoss = s.getTimeLoss();

        activeGame = s.getActiveGame();
        gameEnded = s.getGameEnded();
        reset = s.getReset();
        resetTime = s.getResetTime();
        newGameCreated = s.getNewGameCreated();
        newGameTime = s.getNewGameTime();

        startNewGame = s.getStartNewGame();
        startReset = s.getStartReset();
        startGame = s.getStartGame();
        startTap = s.getStartTap();
        boardChange = s.getBoardChange();
        tapTime = s.getTapTime();

        gameDelay = s.getGameDelay();

        player1Color = s.getPlayerColor1();
        player2Color = s.getPlayerColor2();

        player1 = false;

        timeIndex = s.getTimedindex();
        volumeIndex = s.getVolumeindex();

        timed = s.getTime();
        sound = s.getSound();
        firstPlayerWin = s.getFirstPlayerWin();
        timer = s.getTimer();

        gameQuit = false;

        gameStatistics = s.getGameStatistics();
        colorStatistics = s.getColorStatistics();

        order = new ArrayList<Integer>();

        favoriteColor();

        favoriteTile = new Tile(WIDTH/2,400, 0);
        if(!order.isEmpty()) {
            favoriteTile.setColor(order.get(0));
        }
        startTimer = System.nanoTime();

        advertOn = s.getAdvertOn();
        adCounter = s.getAdCounter();

        volumeUpdate();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry&& counter<1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
            //we can safely start the gameloop once the surface is created
    }

    public void update(){
        if(menu){
            if(y > HEIGHT){
                state = statebuffer;
            }
        }
        switch(state){
            case 4:
            if(activeGame && !gameEnded) {
                wPlayer.getTileList().clear();
                bPlayer.getTileList().clear();
                for (Tile T : board.getTileList()) {
                    if (T.getColor() == wPlayer.getPlayerColor()) {
                        wPlayer.getTileList().add(T);
                    }
                    if (T.getColor() == bPlayer.getPlayerColor()) {
                        bPlayer.getTileList().add(T);
                    }
                }
                if (wPlayer.getActionCounter() == 2 && bPlayer.getActionCounter() == 2) {
                    activeGame = false;
                    gameEnded = true;
                    for (Tile t : board.getTileList()) {
                        if (t.getColor() == Color.GRAY) {
                            activeGame = true;
                            timedLoss = false;
                            gameEnded = false;
                        }
                    }
                }
                if(timed && turn > 1) {
                    if (timer > 0) {
                        long timerElapsed = (System.nanoTime() - startTimer) / 1000000;
                        if (timerElapsed > 1000) {
                            timer--;
                            if(timer <= 5) {
                                soundEffects.add(new Soundeffects(context, R.raw.tick));
                            }
                            startTimer = System.nanoTime();
                        }
                    }else {
                        activeGame = false;
                        gameEnded = true;
                        timedLoss = true;
                        if (wPlayer.isTurn()) {
                            wPlayerWins = false;
                        } else {
                            wPlayerWins = true;
                        }
                    }
                }

                /* //this block if for debugging purposes
                if(boardChange){
                    if(wPlayer.isTurn()) {
                        test = new AITree(board, wPlayer, bPlayer, wPlayer.isTurn(), 1);
                    }
                    else{
                        test = new AITree(board, bPlayer, wPlayer, bPlayer.isTurn(), 1);
                    }
                    for(int R: test.getRootsTileID()){
                        System.out.println("RID: " + R);
                        System.out.println("Weight: " + test.getTileWeight(R));
                    }
                    boardChange = false;
                }*/
            }

                if(gameEnded){
                    startReset = System.nanoTime();
                    gameStatistics[0]++;
                    gameEnded = false;
                    resetTime = true;
                    tapTime = true;
                    soundEffects.add(new Soundeffects(context,R.raw.game_end));
                    if(!timedLoss) {
                        if (wPlayer.getTileList().size() > bPlayer.getTileList().size()) {
                            gameStatistics[4]++;
                            wPlayerWins = true;
                        } else {
                            wPlayerWins = false;
                        }
                    }
                }
                if(resetTime){
                    long elapsedTime = (System.nanoTime() - startReset)/1000000;
                    if(elapsedTime > 2000){
                        reset = true;
                        resetTime = false;
                    }
                }
                if(newGameCreated){
                    adCounter++;
                    board = new Board();
                    newGameCreated = false;
                    activeGame = true;
                    reset = false;
                    boardChange = true;
                    if(wPlayerWins && !gameQuit && turn > 0){
                        if(firstPlayerWin) {
                            wPlayer.setTurn(false);
                            bPlayer.setTurn(true);
                            wPlayer.resetactionCounter();
                            bPlayer.resetactionCounter();
                            bPlayer.usedAction();
                        }
                        else{
                            wPlayer.setTurn(true);
                            bPlayer.setTurn(false);
                            wPlayer.resetactionCounter();
                            bPlayer.resetactionCounter();
                            wPlayer.usedAction();
                        }
                    }
                    else if(!gameQuit && turn > 0){
                        if(firstPlayerWin) {
                            wPlayer.setTurn(true);
                            bPlayer.setTurn(false);
                            wPlayer.resetactionCounter();
                            bPlayer.resetactionCounter();
                            wPlayer.usedAction();
                        }
                        else{
                            wPlayer.setTurn(false);
                            bPlayer.setTurn(true);
                            wPlayer.resetactionCounter();
                            bPlayer.resetactionCounter();
                            bPlayer.usedAction();
                        }
                    }
                    else{
                        wPlayer.setTurn(true);
                        bPlayer.setTurn(false);
                        wPlayer.resetactionCounter();
                        bPlayer.resetactionCounter();
                        wPlayer.usedAction();
                    }
                    newGameTime = true;
                    gameQuit = false;
                    turn = 0;
                    startNewGame = System.nanoTime();
                    timerUpdate();
                }
                if(newGameTime){
                    long elapsedTime = (System.nanoTime() - startNewGame)/1000000;
                    if(elapsedTime > 500){
                        newGameTime = false;
                    }
                }
                if(gameQuit){
                    if(turn > 1){
                        gameStatistics[0]++;
                    }
                    turn = 0;
                }

                break;
            case 5:
                if(activeGame && !gameEnded) {
                    player.getTileList().clear();
                    aiPlayer.getTileList().clear();
                    for (Tile T : board.getTileList()) {
                        if (T.getColor() == player.getPlayerColor()) {
                            player.getTileList().add(T);
                        }
                        if (T.getColor() == aiPlayer.getPlayerColor()) {
                            aiPlayer.getTileList().add(T);
                        }
                    }
                    if (player.getActionCounter() == 2 && aiPlayer.getActionCounter() == 2) {
                        activeGame = false;
                        gameEnded = true;
                        for (Tile t : board.getTileList()) {
                            if (t.getColor() == Color.GRAY) {
                                activeGame = true;
                                gameEnded = false;
                                timedLoss = false;
                            }
                        }
                    }
                    if(aiPlayer.isTurn() && boardChange && activeGame){
                        long elapsedTime = (System.nanoTime() - startAI)/1000000;
                        try {
                            if (elapsedTime > 1500) {
                                selectedTile = aiPlayer.moveDecision(board, player);
                                if (aiPlayer.getActionCounter() > 0) {
                                    tileTransition(selectedTile, aiPlayer, player, aiPlayer);
                                    soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                    aiPlayer.usedAction();
                                    if (aiPlayer.getActionCounter() == 0) {
                                        player.endOfTurn(player.isTurn());
                                        aiPlayer.endOfTurn(aiPlayer.isTurn());
                                        boardChange = false;
                                    }
                                }
                                turn++;
                                drops.add(new Drop(selectedTile.getX(), selectedTile.getY()));
                                startAI = System.nanoTime();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        timerUpdate();
                    }
                    if(timed && turn > 1) {
                        if (timer > 0) {
                            long timerElapsed = (System.nanoTime() - startTimer) / 1000000;
                            if (timerElapsed > 1000) {
                                timer--;
                                if(timer <= 5) {
                                    soundEffects.add(new Soundeffects(context, R.raw.tick));
                                }
                                startTimer = System.nanoTime();
                            }
                        }else {
                            activeGame = false;
                            gameEnded = true;
                            timedLoss = true;
                            if (player.isTurn()) {
                                playerWins = false;
                            } else {
                                playerWins = true;
                            }
                        }
                    }
                }
                    if(gameEnded){
                        startReset = System.nanoTime();
                        gameStatistics[difficulty + 1]++;
                        gameEnded = false;
                        resetTime = true;
                        tapTime = true;
                        soundEffects.add(new Soundeffects(context,R.raw.game_end));
                        boolean flag = true;
                        if(!timedLoss) {
                            if (player.getTileList().size() > aiPlayer.getTileList().size()) {
                                playerWins = true;
                                gameStatistics[5 + difficulty]++;
                            } else {
                                playerWins = false;
                            }
                        }
                    }
                    if(resetTime){
                        long elapsedTime = (System.nanoTime() - startReset)/1000000;
                        if(elapsedTime > 1000){
                            reset = true;
                            resetTime = false;
                        }
                    }
                    if(newGameCreated){
                        adCounter++;
                        board = new Board();
                        newGameCreated = false;
                        activeGame = true;
                        reset = false;
                        gameDelay = true;
                        boardChange = true;
                        startGame = System.nanoTime();
                        startAI = System.nanoTime();
                        if(playerWins && !gameQuit && turn > 0){
                            if(firstPlayerWin) {
                                player.setTurn(false);
                                aiPlayer.setTurn(true);
                                player.resetactionCounter();
                                aiPlayer.resetactionCounter();
                                aiPlayer.usedAction();
                                aiPlayer.setAiw(true);
                            }
                            else{
                                player.setTurn(true);
                                aiPlayer.setTurn(false);
                                player.resetactionCounter();
                                aiPlayer.resetactionCounter();
                                player.usedAction();
                                aiPlayer.setAiw(false);
                            }
                        }
                        else if(!gameQuit && turn > 0){
                            if(firstPlayerWin) {
                                player.setTurn(true);
                                aiPlayer.setTurn(false);
                                player.resetactionCounter();
                                aiPlayer.resetactionCounter();
                                player.usedAction();
                                aiPlayer.setAiw(false);
                            }
                            else{
                                player.setTurn(false);
                                aiPlayer.setTurn(true);
                                player.resetactionCounter();
                                aiPlayer.resetactionCounter();
                                aiPlayer.usedAction();
                                aiPlayer.setAiw(true);
                            }
                        }
                        newGameTime = true;
                        gameQuit = false;
                        turn = 0;
                        startNewGame = System.nanoTime();
                        timerUpdate();
                    }
                    if(newGameTime){
                        long elapsedTime = (System.nanoTime() - startNewGame)/1000000;
                        if(elapsedTime > 500){
                            newGameTime = false;
                        }
                    }
                if(gameQuit){
                    if(turn > 1){
                        gameStatistics[difficulty + 1]++;
                    }
                    turn = 0;
                }
                break;
            default:
                break;

        }

    }

    public Tile selectTile(Coordinate pos){
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        pos.setCoordinate(pos.getX()/scaleFactorX,pos.getY()/scaleFactorY);
        float R = 1; //increases after each iteration through while loop
        float r;
        boolean tileSelected = false;
        while(R < Tile.radius + 1) {
            for (Tile t : board.getTileList()) {
                r = board.neighborMetric(pos, t);
                if (r < R){
                    return t;
                }
            }
            R += 1;
        }
        return null;
    }
    public boolean circleSelect(Coordinate cursorPos, float x, float y, float radius){
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        float R = 1;
        float r;
        if(distance(cursorPos.getX(), cursorPos.getY(), x*scaleFactorX, y*scaleFactorY) < radius) {
            return true;
        }
        return false;
    }
    public float distance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    //transitions tile colors by 1 or 2 steps
    public void tileTransition(Tile tile, Player player, Player wPlayer, Player bPlayer){
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
        Tile bufferTile;
        for(Neighbors N: board.getNeighborList()){
            bufferTile = N.getNeighbor(tile);
            if(bufferTile != null){
                adjacentTiles.add(bufferTile);
            }
        }
        if(player.getPlayerColor() == wPlayer.getPlayerColor()) {
            if(tile.getColor() != wPlayer.getPlayerColor()) {
                tile.setColor(wPlayer.getPlayerColor());
            }
            if (player.getActionCounter() == 1) {
               for(Tile T: adjacentTiles){
                   if(T.getColor() == wPlayer.getPlayerColor()){
                       continue;
                   }
                   if(T.getColor() == bPlayer.getPlayerColor()) {
                       T.setColor(Color.GRAY);
                   }
                   else if(T.getColor() == Color.GRAY){
                       T.setColor(wPlayer.getPlayerColor());
                   }
               }
            }
            else if(player.getActionCounter() == 2){
                for(Tile T: adjacentTiles){
                    if(T.getColor() == wPlayer.getPlayerColor()){
                        continue;
                    }
                    T.setColor(wPlayer.getPlayerColor());
                }
            }
        }
        else if(player.getPlayerColor() == bPlayer.getPlayerColor()) {
            if(tile.getColor() != bPlayer.getPlayerColor()) {
                tile.setColor(bPlayer.getPlayerColor());
            }
            if (player.getActionCounter() == 1) {
                for(Tile T: adjacentTiles){
                    if(T.getColor() == bPlayer.getPlayerColor()){
                        continue;
                    }
                    if(T.getColor() == wPlayer.getPlayerColor()) {
                        T.setColor(Color.GRAY);
                    }
                    else if(T.getColor() == Color.GRAY){
                        T.setColor(bPlayer.getPlayerColor());
                    }
                }
            } else if (player.getActionCounter() ==2) {
                for (Tile T : adjacentTiles) {
                    if(T.getColor() == bPlayer.getPlayerColor()){
                        continue;
                    }
                    T.setColor(bPlayer.getPlayerColor());
                }
            }
        }
    }
    //checks to see if player input is a legal action
    public boolean legalAction(Tile tile, Player wPlayer, Player bPlayer){
        if(tile != null) {
            ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
            for (Neighbors N : board.getNeighborList()) {
                adjacentTiles.add(N.getNeighbor(tile));
            }
            if (tile.getColor() == Color.GRAY) {
                return true;
            }
            if (tile.getColor() == wPlayer.getPlayerColor()) {
                for (Tile aT : adjacentTiles) {
                    if (aT != null) {
                        if (aT.getColor() != wPlayer.getPlayerColor()) {
                            return true;
                        }
                    }
                }
            }
            if (tile.getColor() == bPlayer.getPlayerColor()) {
                for (Tile aT : adjacentTiles) {
                    if (aT != null) {
                        if (aT.getColor() != bPlayer.getPlayerColor()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        cursorPos.setCoordinate(event.getX(),event.getY());

        if(!menu) {
            switch (state) {
                case 0:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            statebuffer = 1;
                            startGame = System.nanoTime();
                            tapTime = true;
                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            menuChanged();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 1:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                exit();
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 3) {
                                statebuffer = 2;
                                menuChanged();
                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 3 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 3) {
                                newGameCreated = true;
                                state = 4;
                                aiGame = false;
                                timerUpdate();
                                wPlayer = new Player(player1Color, true); //" " wPlayer
                                bPlayer = new Player(player2Color, false);
                                colorStats(player1Color);
                                colorStats(player2Color);
                                soundEffects.add(new Soundeffects(context, R.raw.game_start));
                                Random rand = new Random();
                                if(adCounter > (ADLIMIT+rand.nextInt(3)) && advertOn) {
                                    if (MainActivity.admobView.isLoaded()) {
                                        MainActivity.admobView.show();
                                        adCounter = 0;
                                    }
                                }
                            } else {
                                statebuffer = 3;
                                menuChanged();
                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            }
                            startGame = System.nanoTime();
                            gameDelay = true;
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 2:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 1;
                                menuChanged();
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 3) {
                                aiColor = Color.GREEN;
                                difficulty = 0;
                                statebuffer = 6;
                                menuChanged();
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 3 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 3) {
                                aiColor = Color.YELLOW;
                                difficulty = 1;
                                statebuffer = 6;
                                menuChanged();
                            } else {
                                aiColor = Color.RED;
                                difficulty = 2;
                                statebuffer = 6;
                                menuChanged();
                            }
                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 3:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 1;
                                menuChanged();
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 3) {
                                player1 = true;
                                statebuffer = 7;
                                menuChanged();
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 3 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 3) {
                                player1 = false;
                                statebuffer = 7;
                                menuChanged();
                            } else {
                                statebuffer = 8;
                                menuChanged();
                            }
                            startGame = System.nanoTime();
                            gameDelay = true;
                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 4:
                    switch (event.getAction()) {
                        case (MotionEvent.ACTION_DOWN):
                            if(!timed) {
                                if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                        && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                    soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                    statebuffer = 12;
                                    aiGame = false;
                                    menuChanged();
                                    return true;
                                }
                            }
                            if (cursorPos.getX() < 130 * scaleFactorX && cursorPos.getX() > 15 * scaleFactorX
                                    && cursorPos.getY() < 60 * scaleFactorY && cursorPos.getX() * scaleFactorY > 30 * scaleFactorY) {
                                statebuffer = 1;
                                menuChanged();
                                activeGame = false;
                                gameQuit = true;
                                soundEffects.add(new Soundeffects(context, R.raw.quit_tap));
                            }
                            if (reset) {
                                newGameCreated = true;
                                soundEffects.add(new Soundeffects(context, R.raw.game_start));
                                drops.add(new Drop(cursorPos));
                                Random rand = new Random();
                                if(adCounter > (ADLIMIT + rand.nextInt(3)) && advertOn) {
                                    if (MainActivity.admobView.isLoaded()) {
                                        MainActivity.admobView.show();
                                        adCounter = 0;
                                    }
                                }
                            }
                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            return true;
                        case (MotionEvent.ACTION_UP):
                            if (gameDelay) {
                                long elapsedTime = (System.nanoTime() - startGame) / 1000000;
                                if (elapsedTime < 500) {
                                    break;
                                }
                                gameDelay = false;
                            }
                            if (activeGame && !newGameTime) {
                                selectedTile = selectTile(cursorPos);
                                successfulAction = legalAction(selectedTile, wPlayer, bPlayer);
                                if (selectedTile != null && successfulAction) {
                                    if (wPlayer.isTurn() && selectedTile.getColor() != bPlayer.getPlayerColor()) {
                                        if (wPlayer.getActionCounter() > 0) {
                                            tileTransition(selectedTile, wPlayer, wPlayer, bPlayer);
                                            wPlayer.usedAction();
                                            if (wPlayer.getActionCounter() == 0) {
                                                wPlayer.endOfTurn(wPlayer.isTurn());
                                                bPlayer.endOfTurn(bPlayer.isTurn());
                                                if (timed && turn > 1) {
                                                    timerUpdate();
                                                }
                                            }
                                        }
                                        soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                    } else if (bPlayer.isTurn() && selectedTile.getColor() != wPlayer.getPlayerColor()) {
                                        if (bPlayer.getActionCounter() > 0) {
                                            tileTransition(selectedTile, bPlayer, wPlayer, bPlayer);
                                            bPlayer.usedAction();
                                            if (bPlayer.getActionCounter() == 0) {
                                                wPlayer.endOfTurn(wPlayer.isTurn());
                                                bPlayer.endOfTurn(bPlayer.isTurn());
                                                if (timed && turn > 1) {
                                                    timerUpdate();
                                                }
                                            }
                                        }
                                        soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                    }
                                    boardChange = true;
                                    turn++;
                                    drops.add(new Drop(cursorPos));
                                }
                                selectedTile = null;
                                return false;
                            }
                    }
                    break;
                case 5:
                    switch (event.getAction()) {
                        case (MotionEvent.ACTION_DOWN):
                            if(!timed) {
                                if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                        && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                    soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                    statebuffer = 12;
                                    aiGame = true;
                                    menuChanged();
                                    return true;
                                }
                            }
                            if (cursorPos.getX() < 130 * scaleFactorX && cursorPos.getX() > 15 * scaleFactorX
                                    && cursorPos.getY() < 60 * scaleFactorY && cursorPos.getX() * scaleFactorY > 30 * scaleFactorY) {
                                statebuffer = 1;
                                menuChanged();
                                activeGame = false;
                                gameQuit = true;
                                soundEffects.add(new Soundeffects(context, R.raw.quit_tap));
                            }
                            if (reset) {
                                newGameCreated = true;
                                soundEffects.add(new Soundeffects(context, R.raw.game_start));
                                drops.add(new Drop(cursorPos));
                                Random rand = new Random();
                                if(adCounter > (ADLIMIT+rand.nextInt(3)) && advertOn) {
                                    if (MainActivity.admobView.isLoaded()) {
                                        MainActivity.admobView.show();
                                        adCounter = 0;
                                    }
                                }
                            }
                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            return true;
                        case (MotionEvent.ACTION_UP):
                            if (gameDelay) {
                                long elapsedTime = (System.nanoTime() - startGame) / 1000000;
                                if (elapsedTime < 500) {
                                    break;
                                }
                                gameDelay = false;
                            }
                            if (activeGame && !newGameTime && player.isTurn()) {
                                selectedTile = selectTile(cursorPos);
                                successfulAction = legalAction(selectedTile, player, aiPlayer);
                                if (selectedTile != null && successfulAction) {
                                    if (selectedTile.getColor() != aiPlayer.getPlayerColor()) {
                                        if (player.getActionCounter() > 0) {
                                            tileTransition(selectedTile, player, player, aiPlayer);
                                            player.usedAction();
                                            if (player.getActionCounter() == 0) {
                                                player.endOfTurn(player.isTurn());
                                                aiPlayer.endOfTurn(aiPlayer.isTurn());
                                                startAI = System.nanoTime();
                                                if (timed && turn > 1) {
                                                    timerUpdate();
                                                }
                                            }
                                        }
                                        soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                    }
                                    boardChange = true;
                                    turn++;
                                    drops.add(new Drop(cursorPos));
                                }
                                selectedTile = null;
                                return false;
                            }
                    }
                    break;
                case 6:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 30) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                statebuffer = 2;
                                menuChanged();
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 3) {
                                state = 5;
                                player = new Player(player1Color, true);
                                aiPlayer = new AI(false, board, new Player(aiColor, false), player, difficulty);
                                colorStats(player1Color);
                                timerUpdate();
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 3 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 3) {
                                state = 5;
                                player = new Player(player2Color, false);
                                aiPlayer = new AI(true, board, new Player(aiColor, true), player, difficulty);
                                colorStats(player2Color);
                                timerUpdate();
                            } else {
                                state = 5;
                                int color;
                                Random rand = new Random();
                                switch (rand.nextInt(9)) {
                                    case 0:
                                        color = Color.WHITE;
                                        break;
                                    case 1:
                                        color = CRIMSON;
                                        break;
                                    case 2:
                                        color = ORANGE;
                                        break;
                                    case 3:
                                        color = GOLD;
                                        break;
                                    case 4:
                                        color = FORESTGREEN;
                                        break;
                                    case 5:
                                        color = SKYBLUE;
                                        break;
                                    case 6:
                                        color = INDIGO;
                                        break;
                                    case 7:
                                        color = VIOLET;
                                        break;
                                    case 8:
                                        color = Color.BLACK;
                                        break;
                                    default:
                                        color = Color.BLACK;
                                        break;
                                }
                                int i = rand.nextInt(2);
                                if (i == 0) {
                                    player = new Player(color, true);
                                    aiPlayer = new AI(false, board, new Player(aiColor, false), player, difficulty);
                                } else {
                                    player = new Player(color, false);
                                    aiPlayer = new AI(true, board, new Player(aiColor, true), player, difficulty);
                                }
                                colorStats(color);
                                timerUpdate();
                                aiGame = true;
                            }
                            newGameCreated = true;
                            startGame = System.nanoTime();
                            startAI = System.nanoTime();
                            gameDelay = true;
                            soundEffects.add(new Soundeffects(context, R.raw.game_start));
                            Random rand = new Random();
                            if(adCounter > (ADLIMIT+rand.nextInt(3)) && advertOn) {
                                if (MainActivity.admobView.isLoaded()) {
                                    MainActivity.admobView.show();
                                    adCounter = 0;
                                }
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 7:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                statebuffer = 3;
                                menuChanged();
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != Color.WHITE) {
                                        player1Color = Color.WHITE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != Color.WHITE) {
                                        player2Color = Color.WHITE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                    ;
                                }
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 9 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != CRIMSON) {
                                        player1Color = CRIMSON;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != CRIMSON) {
                                        player2Color = CRIMSON;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 2 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 3 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != ORANGE) {
                                        player1Color = ORANGE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != ORANGE) {
                                        player2Color = ORANGE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 3 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 4 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != GOLD) {
                                        player1Color = GOLD;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != GOLD) {
                                        player2Color = GOLD;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 4 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 5 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != FORESTGREEN) {
                                        player1Color = FORESTGREEN;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != FORESTGREEN) {
                                        player2Color = FORESTGREEN;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 5 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 6 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != SKYBLUE) {
                                        player1Color = SKYBLUE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != SKYBLUE) {
                                        player2Color = SKYBLUE;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 6 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 7 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != INDIGO) {
                                        player1Color = INDIGO;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != INDIGO) {
                                        player2Color = INDIGO;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else if (cursorPos.getX() > 7 * WIDTH * scaleFactorX / 9 && cursorPos.getX() < 8 * WIDTH * scaleFactorX / 9) {
                                if (player1) {
                                    if (player2Color != VIOLET) {
                                        player1Color = VIOLET;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != VIOLET) {
                                        player2Color = VIOLET;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            } else {
                                if (player1) {
                                    if (player2Color != Color.BLACK) {
                                        player1Color = Color.BLACK;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                } else {
                                    if (player1Color != Color.BLACK) {
                                        player2Color = Color.BLACK;
                                        soundEffects.add(new Soundeffects(context, R.raw.paint_tap));
                                    }
                                }
                            }
                            activeGame = true;
                            startGame = System.nanoTime();
                            startAI = System.nanoTime();
                            gameDelay = true;
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 8:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 3;
                                menuChanged();
                                return true;
                            }
                            if (cursorPos.getX() < WIDTH * scaleFactorX / 3) {
                                statebuffer = 9;
                                menuChanged();
                                boolean flag = false;
                                for (int i = 0; i < 9; i++) {
                                    if (colorStatistics[i] > 0) {
                                        flag = true;
                                        orderIndex = 0;
                                    }
                                }
                                if (flag) {
                                    favoriteColor();
                                    favoriteTile.setColor(indexToColor(order.get(orderIndex)));
                                }
                            } else if (cursorPos.getX() > WIDTH * scaleFactorX / 3 && cursorPos.getX() < 2 * WIDTH * scaleFactorX / 3) {
                                statebuffer = 10;
                                menuChanged();
                                startTransition = System.nanoTime();
                            } else {
                                statebuffer = 11;
                                menuChanged();
                            }
                            startGame = System.nanoTime();
                            gameDelay = true;
                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 9:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 8;
                                menuChanged();
                                return true;
                            }
                            if (!order.isEmpty()) {
                                if (circleSelect(cursorPos, favoriteTile.getX(), favoriteTile.getY(), 70)) {
                                    orderIndex = (orderIndex + 1) % order.size();
                                    favoriteTile.setColor(indexToColor(order.get(orderIndex)));
                                    soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                }
                            }
                            //game statistics
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 10:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 8;
                                menuChanged();
                                return true;
                            }
                            if (cursorPos.getX() > scaleFactorX * (280) && cursorPos.getX() < scaleFactorX * (710)
                                    && cursorPos.getY() > scaleFactorY * (130) && cursorPos.getY() < scaleFactorY * (190)) {
                                ArrayList<Integer> meterDivider = new ArrayList<Integer>();
                                for (int i = 0; i <= 10; i++) {
                                    meterDivider.add((280 + (i) * 40));
                                }
                                for (int i = 0; i < 10; i++) {
                                    if (cursorPos.getX() < scaleFactorX * meterDivider.get(i + 1) && cursorPos.getX() > scaleFactorX * meterDivider.get(i)) {
                                        if (i + 1 > timeIndex) {
                                            timeIndex++;
                                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                        }
                                        if (i + 1 < timeIndex) {
                                            timeIndex--;
                                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                        }
                                    }
                                }
                                start = System.nanoTime();
                            }
                            if (cursorPos.getX() > scaleFactorX * (280) && cursorPos.getX() < scaleFactorX * (710)
                                    && cursorPos.getY() > scaleFactorY * (235) && cursorPos.getY() < scaleFactorY * (295)) {
                                ArrayList<Integer> meterDivider = new ArrayList<Integer>();
                                for (int i = 0; i <= 10; i++) {
                                    meterDivider.add((280 + (i) * 40));
                                }
                                for (int i = 0; i < 10; i++) {
                                    if (cursorPos.getX() < scaleFactorX * meterDivider.get(i + 1) && cursorPos.getX() > scaleFactorX * meterDivider.get(i)) {
                                        if (i + 1 > volumeIndex) {
                                            volumeIndex++;
                                            volumeUpdate();
                                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                        }
                                        if (i + 1 < volumeIndex) {
                                            volumeIndex--;
                                            volumeUpdate();
                                            soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                        }
                                    }
                                }
                                start = System.nanoTime();
                            }
                            if (circleSelect(cursorPos, 730, 160, 40)) {
                                if (timed) {
                                    timed = false;
                                } else {
                                    timed = true;
                                }
                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            }
                            if (circleSelect(cursorPos, 730, 265, 40)) {
                                if (sound) {
                                    sound = false;
                                    volumeUpdate();
                                } else {
                                    sound = true;
                                    volumeUpdate();
                                }
                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            }
                            if (cursorPos.getX() > scaleFactorX * (350) && cursorPos.getX() < scaleFactorX * (650)
                                    && cursorPos.getY() > scaleFactorY * (315) && cursorPos.getY() < scaleFactorY * (400)) {
                                if (firstPlayerWin)
                                    firstPlayerWin = false;
                                else {
                                    firstPlayerWin = true;
                                }
                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                            }
                            //configuration
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            long elapsedTime = (System.nanoTime() - start) / 1000000;
                            if (cursorPos.getX() > scaleFactorX * (280) && cursorPos.getX() < scaleFactorX * (710)
                                    && cursorPos.getY() > scaleFactorY * (130) && cursorPos.getY() < scaleFactorY * (190)) {
                                if (elapsedTime > 50) {
                                    ArrayList<Integer> meterDivider = new ArrayList<Integer>();
                                    for (int i = 0; i <= 10; i++) {
                                        meterDivider.add((280 + (i) * 40));
                                    }
                                    for (int i = 0; i < 10; i++) {
                                        if (cursorPos.getX() < scaleFactorX * meterDivider.get(i + 1) && cursorPos.getX() > scaleFactorX * meterDivider.get(i)) {
                                            if (i + 1 > timeIndex) {
                                                timeIndex++;
                                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                            }
                                            if (i + 1 < timeIndex) {
                                                timeIndex--;
                                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                            }
                                        }
                                    }
                                    start = System.nanoTime();
                                }
                            }
                            if (cursorPos.getX() > scaleFactorX * (280) && cursorPos.getX() < scaleFactorX * (710)
                                    && cursorPos.getY() > scaleFactorY * (235) && cursorPos.getY() < scaleFactorY * (295)) {
                                if (elapsedTime > 50) {
                                    ArrayList<Integer> meterDivider = new ArrayList<Integer>();
                                    for (int i = 0; i <= 10; i++) {
                                        meterDivider.add((280 + (i) * 40));
                                    }
                                    for (int i = 0; i < 10; i++) {
                                        if (cursorPos.getX() < scaleFactorX * meterDivider.get(i + 1) && cursorPos.getX() > scaleFactorX * meterDivider.get(i)) {
                                            if (i + 1 > volumeIndex) {
                                                volumeIndex++;
                                                volumeUpdate();
                                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                            }
                                            if (i + 1 < volumeIndex) {
                                                volumeIndex--;
                                                volumeUpdate();
                                                soundEffects.add(new Soundeffects(context, R.raw.menu_tap));
                                            }
                                        }
                                    }
                                    start = System.nanoTime();
                                }
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 11:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                            if (cursorPos.getX() > scaleFactorX * (WIDTH - 100) && cursorPos.getX() < scaleFactorX * (WIDTH - 10)
                                    && cursorPos.getY() > scaleFactorY * (HEIGHT - 50) && cursorPos.getY() < scaleFactorY * (HEIGHT - 10)) {
                                soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                                statebuffer = 8;
                                menuChanged();
                                return true;
                            }
                            //credits
                            soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                            start = System.nanoTime();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            long elasedTime = (System.nanoTime() - start) / 1000000;
                            if (elasedTime > 450) {
                                soundEffects.add(new Soundeffects(context, R.raw.tile_tap));
                                drops.add(new Drop(cursorPos, scaleFactorX, scaleFactorY));
                                start = System.nanoTime();
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            return false;
                    }
                    break;
                case 12:
                    switch(event.getAction()){
                        case (MotionEvent.ACTION_DOWN):
                            soundEffects.add(new Soundeffects(context, R.raw.back_tap));
                            if(aiGame){
                                statebuffer = 5;
                            }
                            else{
                                statebuffer = 4;
                            }
                            menuChanged();
                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            return true;
                        case (MotionEvent.ACTION_UP):
                            return false;
                    }
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth()/(float)WIDTH;
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        final int savedState = canvas.save();
        canvas.scale(scaleFactorX,scaleFactorY);
        if (canvas != null) {
            drawBackground(canvas);
            switch (state) {
                case 0:
                    drawTitle(canvas);
                    break;
                case 1:
                    drawMainMenu(canvas);
                    break;
                case 2:
                    drawCpuMenu(canvas);
                    break;
                case 3:
                    drawOptionsMenu(canvas);
                    break;
                case 4:
                    board.draw(canvas);
                    drawText(canvas);
                    break;
                case 5:
                    board.draw(canvas);
                    drawTextAI(canvas); //replace with AI draw text;
                    break;
                case 6:
                    drawCpuMenu(canvas);
                    break;
                case 7:
                    drawColors(canvas);
                    break;
                case 8:
                    drawOtherMenu(canvas);
                    break;
                case 9:
                    drawStats(canvas);
                    break;
                case 10:
                    drawConfig(canvas);
                    break;
                case 11:
                    drawCredits(canvas);
                    break;
                case 12:
                    drawRules(canvas);
                    break;
            }
            drawTransition(canvas);
            drawDrop(canvas);


            play();
            canvas.restoreToCount(savedState);
        }
    }

    public void drawBackground(Canvas canvas){
        canvas.drawRGB(192, 192, 192);
    }
    public void drawTransition(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(192,192,192));
        if(menu){
            if(y > HEIGHT) {
                y = HEIGHT;
                menu = false;
            }
            canvas.drawRect(0,y,WIDTH,0,paint);
            y += dy;
            return;
        }
        if(y > 0){
            if(y < 0){
                y = 0;
                return;
            }
            canvas.drawRect(0,HEIGHT,WIDTH,HEIGHT-y,paint);
            y -= dy;
        }
    }

    public void drawRules(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(20);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC));
        Paint paint2 = neonGenerator();
        paint2.setTextSize(60);

        canvas.drawText("RULES", WIDTH/2 -60, 50, paint);
        canvas.drawText("The objective of the game is to own more tiles than your opponent", 100, 90, paint1);
        canvas.drawText("at the end of the game.", 100, 110, paint1);

        canvas.drawText("Each turn consist of two *actions initiated by tapping a \"legal tile\".", 100, 140, paint1);

        canvas.drawText("A \"legal tile\" must satisfy the following conditions:", 100, 170, paint1);
        canvas.drawText("1.) The tile must not be your opponent's color.", 100, 190, paint1);
        canvas.drawText("2.) The tile must be able to transition/convert adjacent tiles.", 100, 210, paint1);

        canvas.drawText("The first *action will convert all adjacent, unowned tiles to your color.", 100, 240, paint1);

        canvas.drawText("The second *action will transition all adjacent, unowned tiles one color", 100, 270, paint1);
        canvas.drawText("towards your color.", 100, 290, paint1);

        canvas.drawText("A game ending position is at the moment when a player begins his turn,", 100, 320, paint1);
        canvas.drawText("there are no longer any gray tiles.", 100, 340, paint1);

        canvas.drawText("GLHF", WIDTH/2 - 80, HEIGHT - 60, paint2);
    }

    public void play(){
        for(Soundeffects S: soundEffects){
            if(!S.isPlayedOnce()) {
                S.play();
            }
            if(S.isPlayedOnce() && !S.isPlaying()){
                S.release();
                soundEffects.remove(S);
            }
        }
    }
    public void drawStats(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(player1Color);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));


        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(75);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint3 = new Paint();
        paint3.setColor(EASY);
        paint3.setTextSize(40);
        paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint4 = new Paint();
        paint4.setColor(NORMAL);
        paint4.setTextSize(40);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint5 = new Paint();
        paint5.setColor(HARD);
        paint5.setTextSize(40);
        paint5.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawText("STATISTICS", 180, 75, paint1);
        canvas.drawText("Games Played: ", 20, 130, paint);
        canvas.drawText("Easy Played: ", 20, 180, paint3);
        canvas.drawText("Medium Played: ", 20, 230, paint4);
        canvas.drawText("Hard Played: ", 20, 280, paint5);

        canvas.drawText(Integer.toString(gameStatistics[0]), WIDTH/2 - 60, 130, paint);
        canvas.drawText(Integer.toString(gameStatistics[1]), WIDTH/2 - 60, 180, paint3);
        canvas.drawText(Integer.toString(gameStatistics[2]), WIDTH/2 - 60, 230, paint4);
        canvas.drawText(Integer.toString(gameStatistics[3]), WIDTH/2 - 60, 280, paint5);

        canvas.drawText("Games Won: ", WIDTH/2 + 40, 130, paint);
        canvas.drawText("Easy Won: ", WIDTH/2 + 40, 180, paint3);
        canvas.drawText("Medium Won: ", WIDTH/2 + 40, 230, paint4);
        canvas.drawText("Hard Won: ", WIDTH/2 + 40, 280, paint5);

        canvas.drawText(Integer.toString(gameStatistics[4]), WIDTH - 80, 130, paint);
        canvas.drawText(Integer.toString(gameStatistics[5]), WIDTH - 80, 180, paint3);
        canvas.drawText(Integer.toString(gameStatistics[6]), WIDTH - 80, 230, paint4);
        canvas.drawText(Integer.toString(gameStatistics[7]), WIDTH - 80, 280, paint5);

        favoriteTile.drawGrowing(canvas);

        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint2);
    }
    public void drawConfig(Canvas canvas){

        ArrayList<Meter> timeMeter = new ArrayList<Meter>();
        ArrayList<Meter> soundMeter = new ArrayList<Meter>();

        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);

        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(75);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        generateMeter(timeMeter, timeIndex, true);
        generateMeter(soundMeter, volumeIndex, false);

        Paint paint3 = new Paint();
        paint3.setColor(Color.BLACK);

        Paint paint4 = new Paint();
        paint4.setColor(Color.RED);
        paint4.setTextSize(75);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawRect(260, 120, 770, 420, paint);

        for(Meter t: timeMeter){
            canvas.drawRect(t.getRect(),t.getPaint());
        }
        for(Meter s: soundMeter){
            canvas.drawRect(s.getRect(),s.getPaint());
        }

        canvas.drawCircle(730, 160, 30, paint1);
        if(timed){
            canvas.drawCircle(730, 160, 15, paint3);
        }

        canvas.drawCircle(730, 265, 30, paint1);
        if(sound){
            canvas.drawCircle(730, 265, 15, paint3);
        }

        canvas.drawText("TIME", 70, 180, paint1);
        canvas.drawText("SOUND", 10, 290, paint1);
        canvas.drawText("FIRST", 50, 400, paint1);
        if(firstPlayerWin) {
            canvas.drawText("WINNER", 370, 390, paint1);
        }
        else{
            canvas.drawText("WINNER", 370, 390, paint4);
        }
        canvas.drawText("CONFIGURATION", 120, 75, paint1);

        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint2);
    }
    public void drawCredits(Canvas canvas){
        canvas.drawRGB(0,0,0);

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint1 = new Paint();
        paint1.setColor(Color.GRAY);
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        if(statebuffer == 11) {
            canvas.drawText("Created by: IRunOnPho", WIDTH / 2 - 140, HEIGHT / 2 - 30 - y, paint2);
            canvas.drawText("(Duc Nguyen)", WIDTH / 2 - 80, HEIGHT / 2 + 30 - y, paint2);
        }
        else{
            canvas.drawText("Created by: IRunOnPho", WIDTH / 2 - 140, HEIGHT / 2 - 30 + y, paint2);
            canvas.drawText("(Duc Nguyen)", WIDTH / 2 - 80, HEIGHT / 2 + 30 + y, paint2);
        }

        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint2);
    }
    public void drawOtherMenu(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint1 = new Paint();
        paint1.setColor(Color.LTGRAY);
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawText("STATS", WIDTH / 6 - 90, HEIGHT / 2, paint);
        canvas.drawText("CONFIG", 3 * WIDTH / 6 - 110, HEIGHT / 2, paint);
        canvas.drawText("CREDITS", 5 * WIDTH / 6 - 120, HEIGHT / 2, paint);
        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint2);
        canvas.drawLine((float) WIDTH/3, (float) HEIGHT, (float) WIDTH/3, (float) 0, paint);
        canvas.drawLine((float) 2*WIDTH/3, (float) HEIGHT, (float) 2*WIDTH/3, (float) 0, paint);
    }
    public void drawColors(Canvas canvas){


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Paint paint1 = new Paint();
        paint1.setColor(CRIMSON);
        Paint paint2 = new Paint();
        paint2.setColor(ORANGE);
        Paint paint3 = new Paint();
        paint3.setColor(GOLD);
        Paint paint4 = new Paint();
        paint4.setColor(FORESTGREEN);
        Paint paint5 = new Paint();
        paint5.setColor(SKYBLUE);
        Paint paint6 = new Paint();
        paint6.setColor(INDIGO);
        Paint paint7 = new Paint();
        paint7.setColor(VIOLET);
        Paint paint8 = new Paint();
        paint8.setColor(Color.BLACK);

        Paint paint9 = new Paint();
        paint9.setColor(Color.WHITE);
        paint9.setTextSize(30);
        paint9.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint10 = new Paint();
        if(player1){
            paint10.setColor(player1Color);
        }
        else{
            paint10.setColor(player2Color);
        }
        Paint paint11 = new Paint();
        paint11.setColor(Color.BLACK);
        Paint paint12 = new Paint();
        paint12.setColor(Color.GRAY);

        if(player1) {
            if(player2Color == Color.WHITE){
                canvas.drawRect(0, 0, WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(0, 0, WIDTH / 9, HEIGHT, paint);
            }
            if(player2Color == CRIMSON){
                canvas.drawRect(WIDTH / 9, 0, 2 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(WIDTH / 9, 0, 2 * WIDTH / 9, HEIGHT, paint1);
            }
            if(player2Color == ORANGE){
                canvas.drawRect(2 * WIDTH / 9, 0, 3 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(2 * WIDTH / 9, 0, 3 * WIDTH / 9, HEIGHT, paint2);
            }
            if(player2Color == GOLD){
                canvas.drawRect(3 * WIDTH / 9, 0, 4 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(3 * WIDTH / 9, 0, 4 * WIDTH / 9, HEIGHT, paint3);
            }
            if(player2Color == FORESTGREEN){
                canvas.drawRect(4 * WIDTH / 9, 0, 5 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(4 * WIDTH / 9, 0, 5 * WIDTH / 9, HEIGHT, paint4);
            }
            if(player2Color == SKYBLUE){
                canvas.drawRect(5 * WIDTH / 9, 0, 6 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(5 * WIDTH / 9, 0, 6 * WIDTH / 9, HEIGHT, paint5);
            }
            if(player2Color == INDIGO){
                canvas.drawRect(6 * WIDTH / 9, 0, 7 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(6 * WIDTH / 9, 0, 7 * WIDTH / 9, HEIGHT, paint6);
            }
            if(player2Color == VIOLET){
                canvas.drawRect(7 * WIDTH / 9, 0, 8 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(7 * WIDTH / 9, 0, 8 * WIDTH / 9, HEIGHT, paint7);
            }
            if(player2Color == Color.BLACK){
                canvas.drawRect(8 * WIDTH / 9, 0, WIDTH, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(8 * WIDTH / 9, 0, WIDTH, HEIGHT, paint8);
            }
        }
        else{
            if(player1Color == Color.WHITE){
                canvas.drawRect(0, 0, WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(0, 0, WIDTH / 9, HEIGHT, paint);
            }
            if(player1Color == CRIMSON){
                canvas.drawRect(WIDTH / 9, 0, 2 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(WIDTH / 9, 0, 2 * WIDTH / 9, HEIGHT, paint1);
            }
            if(player1Color == ORANGE){
                canvas.drawRect(2 * WIDTH / 9, 0, 3 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(2 * WIDTH / 9, 0, 3 * WIDTH / 9, HEIGHT, paint2);
            }
            if(player1Color == GOLD){
                canvas.drawRect(3 * WIDTH / 9, 0, 4 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(3 * WIDTH / 9, 0, 4 * WIDTH / 9, HEIGHT, paint3);
            }
            if(player1Color == FORESTGREEN){
                canvas.drawRect(4 * WIDTH / 9, 0, 5 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(4 * WIDTH / 9, 0, 5 * WIDTH / 9, HEIGHT, paint4);
            }
            if(player1Color == SKYBLUE){
                canvas.drawRect(5 * WIDTH / 9, 0, 6 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(5 * WIDTH / 9, 0, 6 * WIDTH / 9, HEIGHT, paint5);
            }
            if(player1Color == INDIGO){
                canvas.drawRect(6 * WIDTH / 9, 0, 7 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(6 * WIDTH / 9, 0, 7 * WIDTH / 9, HEIGHT, paint6);
            }
            if(player1Color == VIOLET){
                canvas.drawRect(7 * WIDTH / 9, 0, 8 * WIDTH / 9, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(7 * WIDTH / 9, 0, 8 * WIDTH / 9, HEIGHT, paint7);
            }
            if(player1Color == Color.BLACK){
                canvas.drawRect(8 * WIDTH / 9, 0, WIDTH, HEIGHT, paint12);
            }
            else {
                canvas.drawRect(8 * WIDTH / 9, 0, WIDTH, HEIGHT, paint8);
            }
        }
        canvas.drawRect(WIDTH / 18 - 2, 20 - 2, 3 * WIDTH / 18 + 2, 120 + 2, paint11);
        canvas.drawRect(WIDTH / 18, 20, 3 * WIDTH / 18, 120, paint10);
        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint9);
    }
    public void drawDrop(Canvas canvas){
        for(Drop d: drops){
            d.draw(canvas);
            long elapsedTime = (System.nanoTime()-d.getStartDrop())/1000000;
            if(elapsedTime > 1000){
                drops.remove(d);
            }
        }
    }
    public void drawTitle(Canvas canvas){
        if(tapTime) {
            r = 192;
            g = 192;
            b = 192;
            dr = 63.0f / 20;
            db = 63.0f / 20;
            dg = 63.0f / 20;
            tapTime = false;
        }
        long elapsedTime = (System.nanoTime() - startTap)/1000000;


            r += dr;
            b += db;
            g += dg;

        Paint paint = new Paint();
        paint.setColor(Color.rgb((int)r,(int)g,(int)b));
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint1 = new Paint();
        paint1.setColor(Color.GRAY);
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        paint2.setTextSize(20);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawText("PHO", WIDTH / 2 - 45, HEIGHT / 2 - 20, paint1);
        canvas.drawText("TAP TO BEGIN", WIDTH / 2 - 90, HEIGHT / 2 + 30, paint);
        canvas.drawText("Created by: IRunOnPho", WIDTH / 2 - 100, HEIGHT / 2 + 70, paint2);
        canvas.drawText("(Duc Nguyen)", WIDTH / 2 - 60, HEIGHT / 2 + 90, paint2);

        if(r > 254) {
            dr = -63.0f / 20;
            db = -63.0f / 20;
            dg = -63.0f / 20;
        }
        if(r <= 192) {
            dr = 63.0f / 20;
            db = 63.0f / 20;
            dg = 63.0f / 20;
        }
    }
    public void drawMainMenu(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(30);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawText("SINGLE", WIDTH / 6 - 110, HEIGHT / 2, paint);
        canvas.drawText("VERSUS", 3 * WIDTH / 6 - 110, HEIGHT / 2, paint);
        canvas.drawText("OPTION", 5 * WIDTH / 6 - 110, HEIGHT / 2, paint);
        canvas.drawText("EXIT", WIDTH - 100, HEIGHT - 20, paint2);
        canvas.drawLine((float) WIDTH/3, (float) HEIGHT, (float) WIDTH/3, (float) 0, paint);
        canvas.drawLine((float) 2*WIDTH/3, (float) HEIGHT, (float) 2*WIDTH/3, (float) 0, paint);
    }
    public void drawCpuMenu(Canvas canvas){
        Paint paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setTextSize(30);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        switch(state) {
            case 2:
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(60);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                Paint paint1 = new Paint();
                paint1.setColor(EASY);
                paint1.setTextSize(60);
                paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                Paint paint2 = new Paint();
                paint2.setColor(NORMAL);
                paint2.setTextSize(60);
                paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                Paint paint3 = new Paint();
                paint3.setColor(HARD);
                paint3.setTextSize(60);
                paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));


                canvas.drawText("EASY", WIDTH / 6 - 75, HEIGHT / 2, paint1);
                canvas.drawText("NORMAL", 3 * WIDTH / 6 - 120, HEIGHT / 2, paint2);
                canvas.drawText("HARD", 5 * WIDTH / 6 - 75, HEIGHT / 2, paint3);
                canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint4);
                canvas.drawLine((float) WIDTH / 3, (float) HEIGHT, (float) WIDTH / 3, (float) 0, paint);
                canvas.drawLine((float) 2 * WIDTH / 3, (float) HEIGHT, (float) 2 * WIDTH / 3, (float) 0, paint);
                break;
            case 6:
                Random rand = new Random();
                if (rand.nextInt(1) == 0){
                    dr = 4 * rand.nextFloat();
                    dg = 4 * rand.nextFloat();
                    db = 4 * rand.nextFloat();
                }
                else{
                    dr = -4 * rand.nextFloat();
                    dg = -4 * rand.nextFloat();
                    db = -4 * rand.nextFloat();
                }
                if(r >= 254){
                    dr = -dr;
                }
                if(g >= 254){
                    dg = -dg;
                }
                if(b >= 254){
                    db = -db;
                }

                r+=dr;
                g+=dg;
                b+=db;

                Paint paint5 = new Paint();
                paint5.setColor(player1Color);
                paint5.setTextSize(60);
                paint5.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                Paint paint6 = new Paint();
                paint6.setColor(aiColor);
                paint6.setTextSize(60);
                paint6.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                Paint paint7 = new Paint();
                paint7.setColor(Color.rgb((int)r, (int)g, (int)b));
                paint7.setTextSize(60);
                paint7.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

                canvas.drawText("PLAYER", WIDTH / 6 - 110, HEIGHT / 2, paint5);
                canvas.drawText("AI", 3 * WIDTH / 6 - 30, HEIGHT / 2, paint6);
                canvas.drawText("RANDOM", 5 * WIDTH / 6 - 120, HEIGHT / 2, paint7);
                canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint4);
                canvas.drawLine((float) WIDTH / 3, (float) HEIGHT, (float) WIDTH / 3, (float) 0, paint4);
                canvas.drawLine((float) 2 * WIDTH / 3, (float) HEIGHT, (float) 2 * WIDTH / 3, (float) 0, paint4);
            default:
                break;
        }
    }
    public void drawOptionsMenu(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint1 = new Paint();
        paint1.setColor(player1Color);
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint2 = new Paint();
        paint2.setColor(player2Color);
        paint2.setTextSize(60);
        paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint3 = new Paint();
        paint3.setColor(Color.LTGRAY);
        paint3.setTextSize(60);
        paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        Paint paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setTextSize(30);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        canvas.drawText("PLAYER", WIDTH / 6 - 110, HEIGHT / 2, paint1);
        canvas.drawText("PLAYER", 3 * WIDTH / 6 - 110, HEIGHT / 2, paint2);
        canvas.drawText("OTHER", 5 * WIDTH / 6 - 100, HEIGHT / 2, paint);
        canvas.drawText("BACK", WIDTH - 100, HEIGHT - 20, paint4);
        canvas.drawLine((float) WIDTH/3, (float) HEIGHT, (float) WIDTH/3, (float) 0, paint);
        canvas.drawLine((float) 2*WIDTH/3, (float) HEIGHT, (float) 2*WIDTH/3, (float) 0, paint);
    }
    public void drawText(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(wPlayer.getPlayerColor());
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        Paint paint1 = new Paint();
        paint1.setColor(bPlayer.getPlayerColor());
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        Paint paint2 = neonGenerator();

        Paint paint3 = new Paint();
        paint3.setColor(Color.GRAY);
        paint3.setTextSize(30);
        paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        Paint paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setTextSize(30);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        if(!timed) {
            canvas.drawText("RULES", WIDTH - 100, HEIGHT - 20, paint4);
        }
        else{
            canvas.drawText("TIME", WIDTH - 100, HEIGHT - 20, paint4);
        }

        canvas.drawText("QUIT", 15, 30, paint3);

        if(wPlayer.isTurn() && activeGame) {
            canvas.rotate(270, 30, HEIGHT-30);
            if(!timed) {
                canvas.drawText("PLAYER", 10, HEIGHT - 10, paint);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, 10, HEIGHT - 10, paint);
            }
            canvas.rotate(-270, 30, HEIGHT-30);
            canvas.rotate(90, WIDTH - 30, 10);
            if(!timed) {
                canvas.drawText("PLAYER", WIDTH - 30, 30, paint);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, WIDTH - 30, 30, paint);
            }
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        else if(bPlayer.isTurn() && activeGame){
            canvas.rotate(270, 30, HEIGHT-30);
            if(!timed) {
                canvas.drawText("PLAYER", 10, HEIGHT - 10, paint1);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, 10, HEIGHT - 10, paint1);
            }
            canvas.rotate(-270, 30, HEIGHT-30);
            canvas.rotate(90, WIDTH - 30, 10);
            if(!timed) {
                canvas.drawText("PLAYER", WIDTH - 30, 30, paint1);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, WIDTH - 30, 30, paint1);
            }
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        if(!activeGame && wPlayerWins && !gameQuit){
            canvas.rotate(270, 30, HEIGHT-30);
            canvas.drawText("WINNER", 10, HEIGHT - 10, paint);
            canvas.rotate(-270, 30, HEIGHT-30);
            canvas.rotate(90, WIDTH - 30, 10);
            canvas.drawText("WINNER", WIDTH - 30, 30, paint);
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        else if(!activeGame && !wPlayerWins && !gameQuit) {
            canvas.rotate(270, 30, HEIGHT-30);
            canvas.drawText("WINNER", 10, HEIGHT-10, paint1);
            canvas.rotate(-270, 30, HEIGHT-30);
            canvas.rotate(90, WIDTH - 30, 10);
            canvas.drawText("WINNER", WIDTH - 30, 30, paint1);
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        if(!activeGame && !reset && !gameQuit){
            canvas.drawText("GAME OVER", WIDTH/2 - 90, HEIGHT/2+10, paint2);

        }
        else if(!activeGame && reset){
            canvas.drawText("TAP TO RESET", WIDTH/2 - 100, HEIGHT/2+10, paint2);
        }
    }
    public void drawTextAI(Canvas canvas) {
        if (tapTime) {
            r = 192;
            g = 192;
            b = 192;
            dr = 63.0f / 20;
            db = -192.0f / 20;
            dg = -192.0f / 20;
            tapTime = false;
        }
        Paint paint = new Paint();
        paint.setColor(player.getPlayerColor());
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        Paint paint1 = new Paint();
        paint1.setColor(aiPlayer.getPlayerColor());
        paint1.setTextSize(60);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        Paint paint2 = neonGenerator();

        Paint paint3 = new Paint();
        paint3.setColor(Color.GRAY);
        paint3.setTextSize(30);
        paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        Paint paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setTextSize(30);
        paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        if(!timed) {
            canvas.drawText("RULES", WIDTH - 100, HEIGHT - 20, paint4);
        }
        else{
            canvas.drawText("TIME", WIDTH - 100, HEIGHT - 20, paint4);
        }

        canvas.drawText("QUIT", 15, 30, paint3);

        if(player.isTurn() && activeGame) {
            canvas.rotate(270, 30, HEIGHT-30);
            if(!timed) {
                canvas.drawText("PLAYER", 10, HEIGHT - 10, paint);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, 10, HEIGHT - 10, paint);
            }
            canvas.rotate(-270, 30, HEIGHT-30);
            canvas.rotate(90, WIDTH - 30, 10);
            if(!timed) {
                canvas.drawText("PLAYER", WIDTH - 30, 30, paint);
            }
            else{
                String stringTimer = Integer.toString(timer);
                canvas.drawText(stringTimer, WIDTH - 30, 30, paint);
            }
            canvas.rotate(-90, WIDTH - 30, 10);
        } else if (aiPlayer.isTurn() && activeGame) {
            canvas.rotate(270, 30, HEIGHT - 30);
            canvas.drawText("THINKING", 10, HEIGHT - 10, paint1);
            canvas.rotate(-270, 30, HEIGHT - 30);
            canvas.rotate(90, WIDTH - 30, 10);
            canvas.drawText("THINKING", WIDTH - 30, 30, paint1);
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        if (!activeGame && playerWins && !gameQuit) {
            canvas.rotate(270, 30, HEIGHT - 30);
            canvas.drawText("WINNER", 10, HEIGHT - 10, paint);
            canvas.rotate(-270, 30, HEIGHT - 30);
            canvas.rotate(90, WIDTH - 30, 10);
            canvas.drawText("WINNER", WIDTH - 30, 30, paint);
            canvas.rotate(-90, WIDTH - 30, 10);
        } else if (!activeGame && !playerWins && !gameQuit) {
            canvas.rotate(270, 30, HEIGHT - 30);
            canvas.drawText("WINNER", 10, HEIGHT - 10, paint1);
            canvas.rotate(-270, 30, HEIGHT - 30);
            canvas.rotate(90, WIDTH - 30, 10);
            canvas.drawText("WINNER", WIDTH - 30, 30, paint1);
            canvas.rotate(-90, WIDTH - 30, 10);
        }
        if (!activeGame && !reset && !gameQuit) {
            canvas.drawText("GAME OVER", WIDTH / 2 - 90, HEIGHT / 2 + 10, paint2);
        } else if (!activeGame && reset) {
            canvas.drawText("TAP TO RESET", WIDTH / 2 - 100, HEIGHT / 2 + 10, paint2);
        }
    }

    public void exit(){
        String player1ColorString = "Player1Color: " + player1Color + '\n';
        String player2ColorString = "Player2Color: " + player2Color + '\n';
        String timedString;
        if(timed){
            timedString = "Timed: " + 1 + '\n';
        }
        else{
            timedString = "Timed: " + 0 + '\n';
        }
        String soundString;
        if(sound){
            soundString = "Sound: " + 1 + '\n';
        }
        else{
            soundString = "Sound: " + 0 + '\n';
        }
        String firstPlayerWinString;
        if(firstPlayerWin) {
            firstPlayerWinString = "First: " + 1 + '\n';
        }
        else{
            firstPlayerWinString = "First: " + 0 + '\n';
        }

        String timeIndexString = "Time Index: " + timeIndex + '\n';
        String volumeIndexString = "Volume Index: " + volumeIndex + '\n';
        String gameStatisticsString[] = new String[8];
        for(int i = 0; i<8; i++){
            gameStatisticsString[i] = 'G' + i + ": " + gameStatistics[i] + '\n';
        }
        String colorStatisticsString[] = new String[9];
        for(int i = 0; i<9; i++){
            colorStatisticsString[i] = 'C' + i + ": " + colorStatistics[i] + '\n';
        }
        String advertOnString;
        if(advertOn) {
            advertOnString = "Advert on: " + 1 + '\n';
        }
        else{
            advertOnString = "Advert on: " + 0 + '\n';
        }
        String adCounterString = "Ad counter: " + adCounter + '\n';

        String data = player1ColorString + player2ColorString + timedString + soundString
                + firstPlayerWinString + timeIndexString + volumeIndexString;
        for(int i = 0; i<8; i++){
            data += gameStatisticsString[i];
        }
        for(int i = 0; i<9; i++){
            data += colorStatisticsString[i];
        }
        data += advertOnString + adCounterString;
        writeToFile(data, context);

        ((Activity) context).finish();

    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Save.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private void readFromFile(Context context, ArrayList<String> ret) {

        try {
            InputStream inputStream = context.openFileInput("Save.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    ret.add(receiveString);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    private void createGame(ArrayList<String> data){
        if(data.isEmpty()) {
            cursorPos = new Coordinate();
            board = new Board(); //need to save instance of board and make constructor for board
            wPlayer = null; //" " wPlayer
            bPlayer = null; //" " bPlayer
            player = null;
            aiPlayer = null;
            activeGame = false;
            gameEnded = false;
            reset = false;
            resetTime = false;
            newGameCreated = false;
            newGameTime = false;
            boardChange = true;
            tapTime = true;
            gameQuit = false;

            Arrays.fill(gameStatistics,0);
            Arrays.fill(colorStatistics,0);

            volumeIndex = 5; //need to store
            timeIndex = 10; //need to store
            firstPlayerWin = true; //need to store
            timedLoss = false;
            aiGame = false;

            timed = false; //need to store
            sound = true; //need to store

            player1Color = Color.WHITE;
            player2Color = Color.BLACK;
            player1 = false;

            aiColor = 0; //needs to be parsed
            difficulty = 0;
            state = 0;
            adCounter = 0;
            advertOn = true;
            favoriteTile = new Tile(WIDTH/2,400, 0);
            volumeUpdate();
        }
        else{
            String buffer;
            cursorPos = new Coordinate();
            board = null; //need to save instance of board and make constructor for board
            wPlayer = null; //" " wPlayer
            bPlayer = null; //" " bPlayer
            player = null;
            aiPlayer = null;
            activeGame = false;
            gameEnded = false;
            reset = false;
            resetTime = false;
            newGameCreated = false;
            newGameTime = false;
            boardChange = true;
            tapTime = true;
            gameQuit = false;

            int color, num;
            buffer = data.get(0).substring("Player1Color: ".length(), data.get(0).length());
            color = toInt(buffer);
            player1Color = color;
            buffer = data.get(1).substring("Player2Color: ".length(), data.get(1).length());
            color = toInt(buffer);
            player2Color = color;
            buffer = data.get(2).substring("Timed: ".length(), data.get(2).length());
            num = toInt(buffer);
            if(num == 1) {
                timed = true;
            }
            else{
                timed = false;
            }
            buffer = data.get(3).substring("Sound: ".length(), data.get(3).length());
            num = toInt(buffer);
            if(num == 1) {
                sound = true;
            }
            else{
                sound = false;
            }
            buffer = data.get(4).substring("First: ".length(), data.get(4).length());
            num = toInt(buffer);
            if(num == 1) {
                firstPlayerWin = true;
            }
            else{
                firstPlayerWin = false;
            }
            buffer = data.get(5).substring("Time Index: ".length(), data.get(5).length());
            num = toInt(buffer);
            timeIndex = num; //need to store
            buffer = data.get(6).substring("Volume Index: ".length(), data.get(6).length());
            num = toInt(buffer);
            volumeIndex = num; //need to store
            for(int i = 0; i < 8; i++){
                String buffer1 = 'G' + i + ": ";
                buffer = data.get(7+i).substring(buffer1.length(), data.get(7+i).length());
                num = toInt(buffer);
                gameStatistics[i] = num;
            }
            for(int i = 0; i < 9; i++){
                String buffer1 = 'C' + i + ": ";
                buffer = data.get(15+i).substring(buffer1.length(), data.get(15+i).length());
                num = toInt(buffer);
                colorStatistics[i] = num;
            }
            System.out.println(data.get(24));
            buffer = data.get(24).substring("Advert on: ".length(), data.get(24).length());
            num = toInt(buffer);
            if(num == 1) {
                advertOn = true;
            }
            else{
                advertOn = false;
            }
            buffer = data.get(25).substring("Ad counter: ".length(),data.get(25).length());
            num = toInt(buffer);
            adCounter = num;

            player1 = false;
            aiColor = 0;
            difficulty = 0;
            state = 0;

            favoriteTile = new Tile(WIDTH/2,400, 0);
            boolean flag = false;
            for(int i=0; i<9; i++) {
                if (colorStatistics[i] > 0)
                    flag = true;
            }
            if(flag) {
                favoriteColor();
                favoriteTile.setColor(indexToColor(order.get(0)));
            }



            volumeUpdate();
        }
    }

    public void generateMeter(ArrayList<Meter> meter, int index, boolean timeMeter){
        if(timeMeter) {
            for (int i = 0; i < index; i++) {
                meter.add(new Meter(280+i*40,130,310+i*40,190,i));
            }
        }
        else{
            for (int i = 0; i < index; i++) {
                meter.add(new Meter(280+i*40,235,310+i*40,295,i));
            }
        }
    }
    public int toInt(String numberString){
        int number = 0;
        String buffer;
        buffer =  numberString.substring(numberString.length()-1,numberString.length());

        if(numberString.substring(0,1).equals("-")){
            String inputString = numberString.substring(1,numberString.length());
            number += -1*toInt(inputString);
        }
        else{
            if(numberString.length() > 1) {
                if(buffer.equals("1")){
                    number = 1;
                }
                else if(buffer.equals("2")){
                    number = 2;
                }
                else if(buffer.equals("3")){
                    number = 3;
                }
                else if(buffer.equals("4")){
                    number = 4;
                }
                else if(buffer.equals("5")){
                    number = 5;
                }
                else if(buffer.equals("6")){
                    number = 6;
                }
                else if(buffer.equals("7")){
                    number = 7;
                }
                else if(buffer.equals("8")){
                    number = 8;
                }
                else if(buffer.equals("9")){
                    number = 9;
                }
                else if(buffer.equals("0")){
                    number = 0;
                }
                else{
                    number = 0;
                }
                number = number + 10 * toInt(numberString.substring(0,numberString.length()-1));
            }
            else{
                if(buffer.equals("1")){
                    number = 1;
                }
                else if(buffer.equals("2")){
                    number = 2;
                }
                else if(buffer.equals("3")){
                    number = 3;
                }
                else if(buffer.equals("4")){
                    number = 4;
                }
                else if(buffer.equals("5")){
                    number = 6;
                }
                else if(buffer.equals("6")){
                    number = 6;
                }
                else if(buffer.equals("7")){
                    number = 7;
                }
                else if(buffer.equals("8")){
                    number = 8;
                }
                else if(buffer.equals("9")){
                    number = 9;
                }
                else if(buffer.equals("0")){
                    number = 0;
                }
                else{
                    number = 0;
                }
            }
        }
        return number;
    }
    public void volumeUpdate(){
        VOLUME = 1 + 5 * volumeIndex;
    }
    public void timerUpdate(){
        timer = timeIndex * 5;
        startTimer = System.nanoTime();
    }
    public void colorStats(int color1){
        if(color1 == Color.WHITE){
            colorStatistics[0]++;
        }
        else if(color1 == CRIMSON){
            colorStatistics[1]++;
        }
        else if(color1 == ORANGE){
            colorStatistics[2]++;
        }
        else if(color1 == GOLD){
            colorStatistics[3]++;
        }
        else if(color1 == FORESTGREEN){
            colorStatistics[4]++;
        }
        else if(color1 == SKYBLUE){
            colorStatistics[5]++;
        }
        else if(color1 == INDIGO){
            colorStatistics[6]++;
        }
        else if(color1 == VIOLET){
            colorStatistics[7]++;
        }
        else if(color1 == Color.BLACK){
            colorStatistics[8]++;
        }
    }
    public void favoriteColor(){
        order.clear();
        int buffer[] = new int[9];
        for(int i = 0; i < 9; i++){
            buffer[i] = 0;
            int buffer1 = 0;
            for(int j = 8; j >= 0; j--){
                if(i > 0) {
                    if(buffer[i-1] >= colorStatistics[j] && colorStatistics[j] >= buffer[i]) {
                        boolean flag = true;
                        for(Integer O: order){
                            if(O == j) {
                                flag = false;
                            }
                        }
                        if(flag){
                            buffer[i] = colorStatistics[j];
                            buffer1 = j;
                        }
                    }
                }
                else{
                    if(colorStatistics[j] >= buffer[i]){
                        buffer[i] = colorStatistics[j];
                        buffer1 = j;
                    }
                }
            }
            if(buffer[i] > 0) {
                order.add(buffer1);
            }
        }
    }
    public void menuChanged(){
        menu = true;
    }
    public int indexToColor(int i){
        switch(i){
            case 0:
                return Color.WHITE;
            case 1:
                return CRIMSON;
            case 2:
                return ORANGE;
            case 3:
                return GOLD;
            case 4:
                return FORESTGREEN;
            case 5:
                return SKYBLUE;
            case 6:
                return INDIGO;
            case 7:
                return VIOLET;
            case 8:
                return Color.BLACK;
            default:
                return 0;
        }
    }
    public Paint neonGenerator(){
        if(tapTime) {
            r = 192;
            g = 192;
            b = 192;
            dr = 63.0f/20;
            db = -192.0f/20;
            dg = -192.0f/20;
            tapTime = false;
        }
        Paint paint = new Paint();
        paint.setColor(NEON);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        NEON = Color.rgb((int)r,(int)g,(int)b);
        if(r >= 254) {
            dr = -63.0f/20;
        }
        if(r <= 1){
            dr = 63.0f/20;
        }
        if(g >= 254) {
            dg = -63.0f/20;
        }
        if(g <= 1){
            dg = 63.0f/20;
        }
        if(b >= 254) {
            db = -63.0f/20;
        }
        if(b <= 1){
            db = 63.0f/20;
        }
        r += dr;
        db += db;
        g += dg;
        return paint;
    }
}
