package ducnguyen.hex;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends Activity{

    GamePanel gamePanel;
    static public InterstitialAd admobView;
    Button mNewGameButton;
    static String android_id;
    static String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turns title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //sets to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        System.out.println(mNewGameButton);
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceID = md5(android_id).toUpperCase();

        startGame(savedInstanceState);

        admobView = new InterstitialAd(this);
        admobView.setAdUnitId("ca-app-pub-3341633085092955/8183624823");
        requestNewInterstitial();


        admobView.setAdListener( new AdListener() {
                                           @Override
                                           public void onAdClosed() {
                                               requestNewInterstitial();
                                               setContentView(gamePanel);
                                           }
                                       });
    }

    public void startGame(Bundle savedInstanceState){

        if(savedInstanceState != null){
            Storage s = new Storage();
            s.setState(savedInstanceState.getInt("STATE"));
            s.setDifficulty(savedInstanceState.getInt("DIFF"));
            s.setTileColorList(savedInstanceState.getIntegerArrayList("TCOLORS"));
            s.setwPlayerColor(savedInstanceState.getInt("WPLAYERC"));
            s.setbPlayerColor(savedInstanceState.getInt("BPLAYERC"));
            if(savedInstanceState.getInt("WPLAYERT") == 1){
                s.setPlayerTurn(true);
            }
            else{
                s.setPlayerTurn(false);
            }
            s.setPlayerActionCounter(savedInstanceState.getInt("WPLAYERA"), true);
            s.setPlayerActionCounter(savedInstanceState.getInt("BPLAYERA"), false);
            if(savedInstanceState.getInt("WP") == 1){
                s.setwPlayerWins(true);
            }
            else{
                s.setwPlayerWins(false);
            }
            if(s.getState() == 5){
                if(savedInstanceState.getInt("AIW") == 1){
                    s.setAiw(true);
                }
                else{
                    s.setAiw(false);
                }
            }

            s.setPlayerColor1(savedInstanceState.getInt("P1C"));
            s.setPlayerColor2(savedInstanceState.getInt("P2C"));

            s.setAiColor(savedInstanceState.getInt("AIC"));

            s.setVolumeindex(savedInstanceState.getInt("VI"));
            s.setTimedindex(savedInstanceState.getInt("TI"));
            s.setTimer(savedInstanceState.getInt("t"));

            s.setTurn(savedInstanceState.getInt("TURN"));
            s.setStartReset(savedInstanceState.getLong("SR"));
            s.setStartReset(savedInstanceState.getLong("SNG"));
            s.setStartGame(savedInstanceState.getLong("SG"));
            s.setStartTap(savedInstanceState.getLong("ST"));
            if(savedInstanceState.getInt("AG") == 1){
                s.setActiveGame(true);
            }
            else{
                s.setActiveGame(false);
            }
            if(savedInstanceState.getInt("GE") == 1){
                s.setGameEnded(true);
            }
            else{
                s.setGameEnded(false);
            }
            if(savedInstanceState.getInt("NGC") == 1){
                s.setNewGameCreated(true);
            }
            else{
                s.setNewGameCreated(false);
            }
            if(savedInstanceState.getInt("NGT") == 1){
                s.setNewGameTime(true);
            }
            else{
                s.setNewGameTime(false);
            }
            if(savedInstanceState.getInt("R") == 1){
                s.setReset(true);
            }
            else{
                s.setReset(false);
            }
            if(savedInstanceState.getInt("RT") == 1){
                s.setResetTime(true);
            }
            else{
                s.setResetTime(false);
            }
            if(savedInstanceState.getInt("GD") == 1){
                s.setGameDelay(true);
            }
            else{
                s.setGameDelay(false);
            }
            if(savedInstanceState.getInt("BC") == 1){
                s.setBoardChange(true);
            }
            else{
                s.setBoardChange(false);
            }
            if(savedInstanceState.getInt("TT") == 1){
                s.setTapTime(true);
            }
            else{
                s.setTapTime(false);
            }
            if(savedInstanceState.getInt("T") == 1){
                s.setTime(true);
            }
            else{
                s.setTime(false);
            }
            if(savedInstanceState.getInt("S") == 1){
                s.setSound(true);
            }
            else{
                s.setSound(false);
            }
            if(savedInstanceState.getInt("FPW") == 1){
                s.setFirstPlayerWin(true);
            }
            else{
                s.setFirstPlayerWin(false);
            }
            if(savedInstanceState.getInt("AIG") == 1){
                s.setAigame(true);
            }
            else{
                s.setAigame(false);
            }
            if(savedInstanceState.getInt("TL") == 1){
                s.setTimeLoss(true);
            }
            else{
                s.setTimeLoss(false);
            }
            int bufferArray1[] = new int[8];
            for(int i = 0; i<8; i++){
                bufferArray1[i] = savedInstanceState.getInt("G" + i);
            }
            s.setGameStatistics(bufferArray1);
            int bufferArray2[] = new int[9];
            for(int i = 0; i<9; i++){
                bufferArray2[i] = savedInstanceState.getInt("C" + i);
            }
            s.setColorStatistics(bufferArray2);
            if(savedInstanceState.getInt("AO") == 1){
                s.setAdvertOn(true);
            }
            else{
                s.setAdvertOn(false);
            }
            s.setAdCounter(savedInstanceState.getInt("ADC"));
            gamePanel = new GamePanel(this, s);
            //setContentView(gamePanel);
        }

        else{
            gamePanel = new GamePanel(this);
            //setContentView(gamePanel);
        }
        setContentView(gamePanel);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Integer> tileColorList = new ArrayList<Integer>();
        if(gamePanel.board != null) {
            for (Tile t : gamePanel.getBoard().getTileList()) {
                tileColorList.add(t.getColor());
            }
        }
        outState.putInt("STATE", gamePanel.getState());
        outState.putInt("DIFF", gamePanel.getDifficulty());
        outState.putIntegerArrayList("TCOLORS", tileColorList); //insert tile color information
        int wPlayerColor;
        int bPlayerColor;
        if(gamePanel.getState() == 5 || (gamePanel.getState() == 12 && gamePanel.getAiGame())) {
            wPlayerColor = gamePanel.getPlayer().getPlayerColor();
            bPlayerColor = gamePanel.getaiPlayer().getPlayerColor();
            outState.putInt("WPLAYERC",wPlayerColor);
            outState.putInt("BPLAYERC",bPlayerColor);
            if(gamePanel.getPlayer().isTurn() == true){
                outState.putInt("WPLAYERT",1);
            }
            else{
                outState.putInt("WPLAYERT",0);
            }
            int wPlayerActionCounter = gamePanel.getPlayer().getActionCounter();
            int bPlayerActionCounter = gamePanel.getaiPlayer().getActionCounter();
            outState.putInt("WPLAYERA", wPlayerActionCounter);
            outState.putInt("BPLAYERA", bPlayerActionCounter);
            if(gamePanel.getPlayerWins()){
                outState.putInt("WP", 1);
            }
            else{
                outState.putInt("WP",0);;
            }
            if(gamePanel.getaiPlayer().getAiw()){
                outState.putInt("AIW", 1);
            }
            else{
                outState.putInt("AIW", 0);
            }
            gamePanel.getPlayer().displayPlayer();
            gamePanel.getaiPlayer().displayPlayer();
        }
        else if(gamePanel.getState() == 4 || (gamePanel.getState() == 12 && !gamePanel.getAiGame())){
            wPlayerColor = gamePanel.getwPlayer().getPlayerColor();
            bPlayerColor = gamePanel.getbPlayer().getPlayerColor();
            outState.putInt("WPLAYERC",wPlayerColor);
            outState.putInt("BPLAYERC",bPlayerColor);
            if(gamePanel.getwPlayer().isTurn() == true){
                outState.putInt("WPLAYERT",1);
            }
            else{
                outState.putInt("WPLAYERT",0);
            }
            int wPlayerActionCounter = gamePanel.getwPlayer().getActionCounter();
            int bPlayerActionCounter = gamePanel.getbPlayer().getActionCounter();
            outState.putInt("WPLAYERA", wPlayerActionCounter);
            outState.putInt("BPLAYERA", bPlayerActionCounter);
            if(gamePanel.getwPlayerWins()){
                outState.putInt("WP", 1);
            }
            else{
                outState.putInt("WP",0);;
            }
            gamePanel.getwPlayer().displayPlayer();
            gamePanel.getbPlayer().displayPlayer();
        }

        outState.putInt("P1C", gamePanel.getPlayer1Color());
        outState.putInt("P2C", gamePanel.getPlayer2Color());

        outState.putInt("AIC", gamePanel.getAiColor());

        outState.putInt("VI", gamePanel.getVolumeIndex());
        outState.putInt("TI", gamePanel.getTimeIndex());
        outState.putInt("t",gamePanel.getTimer());

        outState.putInt("TURN", gamePanel.getTurn());
        outState.putLong("SR", gamePanel.getStartReset());
        outState.putLong("SNG", gamePanel.getStartNewGame());
        outState.putLong("SG", gamePanel.getStartGame());
        outState.putLong("ST", gamePanel.getStartTap());
        if(gamePanel.getActiveGame()){
            outState.putInt("AG",1);
        }
        else{
            outState.putInt("AG",0);
        }
        if(gamePanel.getGameEnded()){
            outState.putInt("GE",1);
        }
        else{
            outState.putInt("GE",0);
        }
        if(gamePanel.getNewGameCreated()){
            outState.putInt("NGC",1);
        }
        else{
            outState.putInt("NGC",0);
        }
        if(gamePanel.getNewGameTime()){
            outState.putInt("NGT",1);
        }
        else{
            outState.putInt("NGT",0);
        }
        if(gamePanel.getReset()){
            outState.putInt("R",1);
        }
        else{
            outState.putInt("R",0);
        }
        if(gamePanel.getResetTime()){
            outState.putInt("RT",1);
        }
        else{
            outState.putInt("RT",0);
        }
        if(gamePanel.getGameDelay()){
            outState.putInt("GD",1);
        }
        else{
            outState.putInt("GD",0);
        }
        if(gamePanel.getboardChange()){
            outState.putInt("BC",1);
        }
        else{
            outState.putInt("BC",0);
        }
        if(gamePanel.getTapTime()){
            outState.putInt("TT",1);
        }
        else{
            outState.putInt("TT",0);
        }
        if(gamePanel.getTimed()){
            outState.putInt("T",1);
        }
        else{
            outState.putInt("T",0);
        }
        if(gamePanel.getSound()){
            outState.putInt("S",1);
        }
        else{
            outState.putInt("S",0);
        }
        if(gamePanel.getFirstPlayerWin()){
            outState.putInt("FPW",1);
        }
        else{
            outState.putInt("FPW",0);
        }
        if(gamePanel.getAiGame()){
            outState.putInt("AIG",1);
        }
        else{
            outState.putInt("AIG",0);
        }
        if(gamePanel.getTimeLoss()){
            outState.putInt("TL",1);
        }
        else{
            outState.putInt("TL",0);
        }
        for(int i = 0; i < 8; i++){
            outState.putInt("G" + i, gamePanel.getGameStatistics()[i]);
        }
        for(int i = 0; i < 9; i++){
            outState.putInt("C" + i, gamePanel.getColorStatistics()[i]);
        }
        if(gamePanel.getAdvertOn()){
            outState.putInt("AO",1);
        }
        else{
            outState.putInt("AO",0);
        }
        outState.putInt("ADC", gamePanel.getAdCounter());

        super.onSaveInstanceState(outState);
    }
    @Override
    public void onBackPressed(){
        if(gamePanel.getState() == 1) {
            gamePanel.setState(0);
        }
        else if(gamePanel.getState() == 2) {
            gamePanel.setState(1);
        }
        else if(gamePanel.getState() == 3) {
            gamePanel.setState(1);
        }
        else if(gamePanel.getState() == 4) {
            //implement gamePanel
        }
        else if(gamePanel.getState() == 5){
        }
        else if(gamePanel.getState() == 6){
            gamePanel.setState(2);
        }
        else if(gamePanel.getState() == 7){
            gamePanel.setState(3);
        }
        else if(gamePanel.getState() == 8){
            gamePanel.setState(3);
        }
        else if(gamePanel.getState() == 9){
            gamePanel.setState(8);
        }
        else if(gamePanel.getState() == 10){
            gamePanel.setState(8);
        }
        else if(gamePanel.getState() == 11){
            gamePanel.setState(8);
        }
        else if(gamePanel.getState() == 12){
            if(gamePanel.getAiGame()) {
                gamePanel.setState(5);
            }
            else{
                gamePanel.setState(4);
            }
        }
        else{
            gamePanel.exit();
            super.onBackPressed();
        }
    }

    public static void requestNewInterstitial() {
        System.out.println("Android ID: " + android_id);
        System.out.println("Device ID: " + deviceID);
        AdRequest adRequest = new AdRequest.Builder().build();

        admobView.loadAd(adRequest);
    }
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
}