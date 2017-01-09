package ducnguyen.hex;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ducng on 12/16/2016.
 */

public class AITree {
    //AITree simulates each events and constructs branch. Each node has an action weight. Should be used after first few moves of AI
    private class Node {
        private int actionWeight;
        int gameEnd;
        private ArrayList<Node> children;
        private Node parent;

        Node() {
            actionWeight = 0;
            gameEnd = 0;
            parent = null;
            children = new ArrayList<Node>();
        }

        Node(int actionWeight, int gameEnd, Node parent) {
            this.actionWeight = actionWeight;
            this.gameEnd = 0;
            this.parent = parent;
            children = new ArrayList<Node>();
        }

        public void setActionWeight(int actionWeight) {
            this.actionWeight = actionWeight;
        }

        public int getActionWeight() {
            return actionWeight;
        }

        public void setGameEnd(int gameEnd) {
            this.gameEnd = gameEnd;
        }

        public int getGameEnd() {
            return gameEnd;
        }

        public void setChildren(ArrayList<Node> children) {
            this.children = children;
        }

        public ArrayList<Node> getChildren() {
            return children;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public void setNode(Node n) {
            actionWeight = n.getActionWeight();
            parent = n.getParent();
            children = n.getChildren();
        }
    }

    private ArrayList<Node> roots = new ArrayList<Node>();
    private ArrayList<Integer> rootsTileID = new ArrayList<Integer>();

    private boolean turnorder[];

    private boolean isAiw;
    private int depth;
    private int aggression;

    public static int WIN = 100000;
    public static int LOSS = -1000000;

    public AITree(Board board, Player wplayer, Player bplayer, boolean isAIW, int depth) {

        ArrayList<AITile> tileList = new ArrayList<AITile>();
        ArrayList<AINeighbors> neighborList = new ArrayList<AINeighbors>();
        this.isAiw = isAIW;
        this.depth = depth;

        Random rand = new Random();

        if(depth-1 > 0) {
            aggression = depth + rand.nextInt(depth);
        }
        else{
            aggression = 0;
        }

        Player wPlayer;
        Player bPlayer;

        turnorder = new boolean[depth + 1];

        boolean flag;
        int counter;

        if (wplayer.isTurn()) {
            flag = true;
            counter = wplayer.getActionCounter();
        } else {
            flag = false;
            counter = bplayer.getActionCounter();
        }

        for (int i = 0; i <= depth; i++) {
            turnorder[i] = flag;
            counter--;
            if (counter == 0) {
                if (flag) {
                    flag = false;
                } else {
                    flag = true;
                }
                counter = 2;
            }
        }
        Player player;
        //simulation part of AITree constructor, this first constructs the roots
        int color;
        for (Tile T : board.getTileList()) {
            wPlayer = new Player(wplayer.getPlayerColor(), true);
            bPlayer = new Player(bplayer.getPlayerColor(), false);
            wPlayer.setTurn(wplayer.isTurn());
            bPlayer.setTurn(bplayer.isTurn());
            wPlayer.setActionCounter(wplayer.getActionCounter());
            bPlayer.setActionCounter(bplayer.getActionCounter());
            if (wPlayer.isTurn()) {
                color = bPlayer.getPlayerColor();
            } else {
                color = wPlayer.getPlayerColor();
            }
            if (legalAction(T, board.getNeighborList(), wplayer, bplayer) && T.color != color) {
                AITile selectedTile = null;
                for (Tile t : board.getTileList()) {
                    tileList.add(new AITile(t));
                }
                for (AITile t : tileList) {
                    if (T.getTileID() == t.getTileID()) {
                        selectedTile = t;
                    }
                }
                for (Neighbors n : board.getNeighborList()) {
                    neighborList.add(new AINeighbors(n, tileList));
                }
                Node node = new Node();
                wPlayer = new Player(wplayer.getPlayerColor(), true);
                bPlayer = new Player(bplayer.getPlayerColor(), false);
                wPlayer.setTurn(wplayer.isTurn());
                bPlayer.setTurn(bplayer.isTurn());
                wPlayer.setActionCounter(wplayer.getActionCounter());
                bPlayer.setActionCounter(bplayer.getActionCounter());
                if (wPlayer.isTurn()) {
                    tileTransition(selectedTile, tileList, neighborList, wPlayer, wPlayer, bPlayer, node);
                    wPlayer.usedAction();
                    if (wPlayer.getActionCounter() == 0) {
                        wPlayer.resetactionCounter();
                        wPlayer.endOfTurn(wPlayer.isTurn());
                        bPlayer.endOfTurn(bPlayer.isTurn());
                    }
                } else {
                    tileTransition(selectedTile, tileList, neighborList, bPlayer, wPlayer, bPlayer, node);
                    bPlayer.usedAction();
                    if (bPlayer.getActionCounter() == 0) {
                        bPlayer.resetactionCounter();
                        wPlayer.endOfTurn(wPlayer.isTurn());
                        bPlayer.endOfTurn(bPlayer.isTurn());
                    }
                }
                roots.add(node);
                rootsTileID.add(selectedTile.getTileID());
                if (depth > 0) {
                    if (wPlayer.isTurn()) {
                        color = bPlayer.getPlayerColor();
                    } else {
                        color = wPlayer.getPlayerColor();
                    }
                    for(AITile t: tileList) {
                        if(legalAction(t,neighborList,wPlayer,bPlayer) && t.getColor() != color) {
                            selectedTile = null;
                            Player wPlayer1 = new Player(wPlayer.getPlayerColor(), true);
                            Player bPlayer1 = new Player(bPlayer.getPlayerColor(), false);
                            wPlayer1.setTurn(wPlayer.isTurn());
                            bPlayer1.setTurn(bPlayer.isTurn());
                            wPlayer1.setActionCounter(wPlayer.getActionCounter());
                            bPlayer1.setActionCounter(bPlayer.getActionCounter());
                            ArrayList<AITile> tempTileList = new ArrayList<AITile>();
                            for (AITile t1 : tileList) {
                                tempTileList.add(new AITile(t1));
                            }
                            for (AITile t1 : tempTileList) {
                                if (t.getTileID() == t1.getTileID()) {
                                    selectedTile = t1;
                                    break;
                                }
                            }
                            ArrayList<AINeighbors> tempNeighborList = new ArrayList<AINeighbors>();
                            for (AINeighbors N : neighborList) {
                                tempNeighborList.add(new AINeighbors(N, tempTileList));
                            }
                            node.addChild(generateChildren(selectedTile, tempTileList, tempNeighborList, node, wPlayer1, bPlayer1, depth - 1));
                        }
                    }
                }
            }
            tileList.clear();
            neighborList.clear();
        }
    }

    public ArrayList<Integer> getRootsTileID() {
        return rootsTileID;
    }

    public Node generateChildren(AITile tile, ArrayList<AITile> tileList, ArrayList<AINeighbors> neighborList, Node parent,
                                 Player wplayer, Player bplayer, int height) {
        Node currentNode = new Node();

        if (wplayer.isTurn()) {
            tileTransition(tile, tileList, neighborList, wplayer, wplayer, bplayer, currentNode);
            wplayer.usedAction();
            if (wplayer.getActionCounter() == 0) {
                wplayer.resetactionCounter();
                wplayer.endOfTurn(wplayer.isTurn());
                bplayer.endOfTurn(bplayer.isTurn());
            }
        } else {
            tileTransition(tile, tileList, neighborList, bplayer, wplayer, bplayer, currentNode);
            bplayer.usedAction();
            if (bplayer.getActionCounter() == 0) {
                bplayer.resetactionCounter();
                wplayer.endOfTurn(wplayer.isTurn());
                bplayer.endOfTurn(bplayer.isTurn());
            }
        }
            //this section adds children to the current node.
        int color;
        if(height > 0 && currentNode.gameEnd == 0) {
            for (AITile T : tileList) {
                Player wPlayer = new Player(wplayer.getPlayerColor(), true);
                Player bPlayer = new Player(bplayer.getPlayerColor(), false);
                wPlayer.setTurn(wplayer.isTurn());
                bPlayer.setTurn(bplayer.isTurn());
                wPlayer.setActionCounter(wplayer.getActionCounter());
                bPlayer.setActionCounter(bplayer.getActionCounter());
                if (wPlayer.isTurn()) {
                    color = bPlayer.getPlayerColor();
                } else {
                    color = wPlayer.getPlayerColor();
                }
                if (legalAction(T, neighborList, wPlayer, bPlayer) && T.getColor() != color) {
                    AITile selectedTile = null;
                    ArrayList<AITile> tempTileList = new ArrayList<AITile>();
                    for (AITile t : tileList) {
                        tempTileList.add(new AITile(t));
                    }
                    for (AITile t : tempTileList) {
                        if (t.getTileID() == T.getTileID()) {
                            selectedTile = t;
                            break;
                        }
                    }
                    ArrayList<AINeighbors> tempNeighborList = new ArrayList<AINeighbors>();
                    for (AINeighbors N : neighborList) {
                        tempNeighborList.add(new AINeighbors(N, tempTileList));
                    }
                    currentNode.addChild(generateChildren(selectedTile, tempTileList, tempNeighborList, currentNode, wPlayer, bPlayer, height - 1));
                }
            }
        }
        currentNode.setParent(parent);
        return currentNode;
        }

    public boolean legalAction(Tile tile, ArrayList<Neighbors> neighborList, Player wPlayer, Player bPlayer){
        if(tile != null) {
            ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
            for (Neighbors N : neighborList) {
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
    public boolean legalAction(AITile tile, ArrayList<AINeighbors> neighborList, Player wPlayer, Player bPlayer){
        if(tile != null) {
            ArrayList<AITile> adjacentTiles = new ArrayList<AITile>();
            for (AINeighbors N : neighborList) {
                adjacentTiles.add(N.getNeighbor(tile));
            }
            if (tile.getColor() == Color.GRAY) {
                return true;
            }
            if (tile.getColor() == wPlayer.getPlayerColor()) {
                for (AITile aT : adjacentTiles) {
                    if (aT != null) {
                        if (aT.getColor() != wPlayer.getPlayerColor()) {
                            return true;
                        }
                    }
                }
            }
            if (tile.getColor() == bPlayer.getPlayerColor()) {
                for (AITile aT : adjacentTiles) {
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
    public void tileTransition(AITile tile, ArrayList<AITile> tileList, ArrayList<AINeighbors> neighborList, Player player, Player wPlayer, Player bPlayer, Node node){
        int actionWeight = 0;
        ArrayList<AITile> adjacentTiles = new ArrayList<AITile>();
        adjacentTiles.add(tile);
        AITile bufferTile;
        for(AINeighbors N: neighborList){
            bufferTile = N.getNeighbor(tile);
            if(bufferTile != null){
                adjacentTiles.add(bufferTile);
            }
        }
        if(player.getPlayerColor() == wPlayer.getPlayerColor()) {
            if (player.getActionCounter() == 1) {
                for(AITile T: adjacentTiles){
                    if(T.getColor() == bPlayer.getPlayerColor()) {
                        T.setColor(Color.GRAY);
                        actionWeight += 2;
                    }
                    else if(T.getColor() == Color.GRAY){
                        T.setColor(wPlayer.getPlayerColor());
                        actionWeight += 2 + aggression;
                    }
                }
                boolean gameEndFlag = true;
                for(AITile t: tileList){
                    if(t.getColor() == Color.GRAY){
                        gameEndFlag = false;
                    }
                }
                if(gameEndFlag){
                    System.out.println("Winning Move: " + tile.getTileID());
                    int i = 0;
                    for(AITile T: tileList){
                        if(T.getColor() == player.getPlayerColor()){
                            i++;
                        }
                    }
                    if(i > 15) {
                        node.setGameEnd(1);
                    }
                    else{
                        node.setGameEnd(-1);
                    }
                }
            }
            else if(player.getActionCounter() == 2){
                for(AITile T: adjacentTiles){
                    if(T.getColor() == bPlayer.getPlayerColor()){
                        actionWeight += 4 + aggression;
                    }
                    else if(T.getColor() == Color.GRAY){
                        actionWeight += 2;
                    }
                    T.setColor(wPlayer.getPlayerColor());
                }
            }
        }
        else if(player.getPlayerColor() == bPlayer.getPlayerColor()) {
            if (player.getActionCounter() == 1) {
                for(AITile T: adjacentTiles){
                    if(T.getColor() == wPlayer.getPlayerColor()) {
                        T.setColor(Color.GRAY);
                        actionWeight += 2;
                    }
                    else if(T.getColor() == Color.GRAY){
                        T.setColor(bPlayer.getPlayerColor());
                        actionWeight += 2;
                    }
                }
                boolean gameEndFlag = true;
                for(AITile t: tileList){
                    if(t.getColor() == Color.GRAY){
                        gameEndFlag = false;
                    }
                }
                if(gameEndFlag){
                    int i = 0;
                    for(AITile T: tileList){
                        if(T.getColor() == player.getPlayerColor()){
                            i++;
                        }
                    }
                    if(i > 15) {
                        node.setGameEnd(1);
                    }
                    else{
                        node.setGameEnd(-1);
                    }
                }
            }
            else if (player.getActionCounter() ==2) {
                for (AITile T : adjacentTiles) {
                    if(T.getColor() == wPlayer.getPlayerColor()){
                        actionWeight += 4;
                    }
                    else{
                        actionWeight += 2;
                    }
                    T.setColor(bPlayer.getPlayerColor());
                }
            }
        }
        node.setActionWeight(actionWeight);
    }
    public int getWeight(Node nodeAction, int height){
        int sign;
        int sign1 = 1;
        int index = depth - height;
        if(turnorder[index]){
            sign = 1;
        }
        else{
            sign = -1;
        }
        int branchWeight = 0;
        int buffer = 0;
        branchWeight += sign * nodeAction.getActionWeight();

        //System.out.println("Children: " + nodeAction.getChildren().size());
        //System.out.println("Depth: " + index);
        //System.out.println("Initial Branch weight: " + branchWeight);
        if(height > 0){
            buffer = 2*LOSS;
            index++;
            if(turnorder[index]){
                sign1 = 1;
            }
            else{
                sign1 = -1;
            }
            for(int i = 0; i < nodeAction.getChildren().size(); i++){
                int x = sign1*getWeight(nodeAction.getChildren().get(i),height - 1);
                if(buffer < x ) {
                    buffer = x;
                    //System.out.println("Buffer: " + buffer);
                }
            }
        }
        if(nodeAction.getGameEnd() == 0){
            buffer = sign1*buffer;
        }
        else{
            if(nodeAction.getGameEnd() == 1){
                return WIN;
            }
            else if(nodeAction.getGameEnd() == -1){
                return LOSS;
            }
        }
        branchWeight += buffer;
        //System.out.println("Final Branch weight: " + branchWeight);
        return branchWeight;
    }
    public int getTileWeight(int id){
        int index = 0;
        for(int i = 0; i < rootsTileID.size(); i++){
            if(rootsTileID.get(i) == id){
                index = i;
                break;
            }
        }
        return getWeight(roots.get(index), depth);
    }
}
