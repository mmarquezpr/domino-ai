package icom5015.domino.models.implementations;

import icom5015.domino.models.Board;
import icom5015.domino.models.Domino;
import icom5015.domino.models.Player;

import java.util.List;
import java.util.Random;

/**
 * Created by enrique on 4/30/14.
 */
public class RandomPlayer extends Player {

    public RandomPlayer(List<Domino> hand) {
        super(hand);
    }

    @Override
    public Move getDomino(Board board) {



        List<Domino> upperSide = getPosibleDominosList(board.getUpperValue());
        List<Domino> lowerSide = getPosibleDominosList(board.getLowerValue());

        //Pass
        if(upperSide.size() == 0 && lowerSide.size() == 0){
            return new Move(PASS, null);
        }

        if (upperSide.size() > lowerSide.size()){

            Random rnd = new Random();
            int index = rnd.nextInt(upperSide.size());
            Domino domino = upperSide.get(index);
            hand.remove(domino);
            return new Move(Domino.UPPER_SIDE, domino);

        } else if(upperSide.size() < lowerSide.size()){
            Random rnd = new Random();
            int index = rnd.nextInt(lowerSide.size());
            Domino domino = lowerSide.get(index);
            hand.remove(domino);
            return new Move(Domino.LOWER_SIDE, domino);
        }
        else{

            if(Math.random() < 0.5){
                Random rnd = new Random();
                int index = rnd.nextInt(upperSide.size());
                Domino domino = upperSide.get(index);
                hand.remove(domino);
                return new Move(Domino.UPPER_SIDE, domino);
            }
            else{
                Random rnd = new Random();
                int index = rnd.nextInt(lowerSide.size());
                Domino domino = lowerSide.get(index);
                hand.remove(domino);
                return new Move(Domino.LOWER_SIDE, domino);
            }
        }
    }
}