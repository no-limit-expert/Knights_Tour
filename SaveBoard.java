import java.util.ArrayList;

public record SaveBoard(ArrayList<ArrayList<Integer>> board, int[] cord, int steps) {

    public ArrayList<ArrayList<Integer>> getBoard(){
        return board;
    }
    public int[] getCord(){
        return cord;
    }

    public int getSteps(){
        return steps;
    }

}
