package icom5015.domino.controllers.implementation;

import icom5015.domino.controllers.DominoGame;
import icom5015.domino.generators.DominoGenerator;
import icom5015.domino.generators.HandGenerator;
import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;
import icom5015.domino.models.implementations.GeneticPlayer;
import icom5015.domino.models.implementations.RandomPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enrique on 4/30/14.
 */
public class AutomationDominoGame extends DominoGame {
    //Constants
    private static final int RUNNING = -1;

    //Variables
    private int winner = RUNNING;
    private int score = 0;

    private void init() {
        players = new HashMap<Integer, Player>();
        //Generate Tiles
        List<Domino> tiles = DominoGenerator.generateDominoBoard();
        HandGenerator handGenerator = new HandGenerator(tiles);
        //Set Players
        players.put(Player.PLAYER_1, new GeneticPlayer(handGenerator.generateHand(false))); //Set Genetic Player
        players.put(Player.PLAYER_2, new RandomPlayer(handGenerator.generateHand(false)));
        players.put(Player.PLAYER_3, new RandomPlayer(handGenerator.generateHand(false)));
        players.put(Player.PLAYER_4, new RandomPlayer(handGenerator.generateHand(false)));

    }

    @Override
    public void run() {
        init();
        generateOrder();

        //Pass Counter
        int passCounter = 0;

        Player firstPlayer = players.get(order[0]);
        Domino doubleSix = firstPlayer.getDoubleSix();
        Board board = new Board(doubleSix);

        //Other Players
        for (int i = 1; i < order.length; i++) {
            Player actualPlayer = players.get(order[i]);
            Player.Move move = actualPlayer.getDomino(board);


            if (move.getPlayedSide() == Domino.UPPER_SIDE) {
                board.setUpperValue(move.getDomino());
            } else if (move.getPlayedSide() == Domino.LOWER_SIDE) {
                board.setLowerValue(move.getDomino());
            } else { //Pass
                passCounter++;
            }
        }

        while (winner == RUNNING) {

            for (int i = 0; i < order.length; i++) {

                Player actualPlayer = players.get(order[i]);
                Player.Move move = actualPlayer.getDomino(board);

                if (move.getPlayedSide() == Domino.UPPER_SIDE) {
                    board.setUpperValue(move.getDomino());
                    passCounter = 0;
                } else if (move.getPlayedSide() == Domino.LOWER_SIDE) {
                    board.setLowerValue(move.getDomino());
                    passCounter = 0;
                } else { //Pass
                    passCounter++;
                }

                //Check For Winners
                if (actualPlayer.emptyHand()) {
                    winner = order[i];
                    score = getTotalScore();
                    break;
                }

                //Check if Pass
                if (passCounter == 4) {
                    winner = getWinnerForFullPass(order[i]);
                    score = getTotalScore();
                    break;
                }
            }
        }
    }

    private int getTotalScore() {

        int sum = 0;
        for (Map.Entry<Integer, Player> e : players.entrySet()) {
            sum += e.getValue().getHandSum();
        }
        return sum;
    }

    public int getWinnerForFullPass(int lockPlayer) {

        int winner = lockPlayer;
        int max = players.get(winner).getHandSum();
        for (Map.Entry<Integer, Player> e : players.entrySet()) {
            if (e.getValue().getHandSum() > max) {
                winner = e.getKey();
                max = e.getValue().getHandSum();
            }
        }
        return winner;
    }


    @Override
    public int winner() {
        return winner;
    }

    @Override
    public int score() {
        return score;
    }

}
