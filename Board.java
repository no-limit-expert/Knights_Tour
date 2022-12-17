import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Board {
    private final int sideLength;
    private int row;
    private int column;
    private int steps = 0;
    private final int squares;
    private boolean solved = false;
    ArrayList<ArrayList<Integer>> board;

    ArrayList<SaveBoard> savedBoards = new ArrayList<>();

    int checkedPaths = 0;
    int branchesEncountered = 0;

    final Instant startTime = Instant.now();



    public Board(int sideLength, int startRow, int startColumn) {
        this.sideLength = sideLength;
        this.squares = sideLength*sideLength;

        this.row = startRow;
        this.column = startColumn;

        // Create a board (n*n).
        this.board = new ArrayList<>();
        for (int x = 0; x < sideLength; x++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int y = 0; y < sideLength; y++) {
                row.add(-1);
            }
            board.add(row);
        }
        board.get(startRow).set(startColumn,0);
    }

    public void drawBoard() {
        for (ArrayList<Integer> rad : board) {
            System.out.println(rad);
        }
    }

    private ArrayList<int[]> findPossibleMoves(int rowY, int columnX) {
        int[][] moves =   {{rowY + 2, columnX + 1}, {rowY + 2, columnX - 1},
                                {rowY - 2, columnX + 1}, {rowY - 2, columnX - 1},
                                {rowY + 1, columnX + 2}, {rowY - 1, columnX + 2},
                                {rowY + 1, columnX - 2}, {rowY - 1, columnX - 2}};

        ArrayList<int[]> possibleMoves = new ArrayList<>();
        for (int[] cord : moves) {
            int row = cord[0];
            int column = cord[1];
            if (row >= 0 && row < sideLength) {
                if (column >= 0 && column < sideLength) {
                    if (board.get(row).get(column) == -1) {
                        possibleMoves.add(cord);
                    }
                }
            }
        }
        return possibleMoves;
        }

    private ArrayList<int[]> leastPossibilitiesAfterMove() {
        ArrayList<int[]> moves = findPossibleMoves(row, column);
        ArrayList<int[]> leastMoves = new ArrayList<>();
        int least = 8;

        for(int[] cord : moves){
            ArrayList<int[]> movementsOfMovement = findPossibleMoves(cord[0], cord[1]);
            if(movementsOfMovement.size() <= least){
                least = movementsOfMovement.size();
            }
        }
        for(int[] cord : moves){
            ArrayList<int[]> movementsOfMovement = findPossibleMoves(cord[0], cord[1]);
            if(movementsOfMovement.size() == least){
                leastMoves.add(cord);
            }
        }

        return leastMoves;
    }

    private void update(int[] nextStep, boolean drawStep){
        if(!solved) {
            row = nextStep[0];
            column = nextStep[1];
            steps++;
            checkedPaths++;

            board.get(row).set(column, steps);
            if(drawStep){
                drawBoard();}
        }
    }

    private int findCoveredArea(){
        int coveredArea = 0;
        for(ArrayList<Integer> row : board){
            for(int rute : row){
                if(rute != -1){
                    coveredArea++;
                }
            }
        }
        return coveredArea;
    }

    // Trace back to previous branch
    private void stepBack(){
        // Making sure current board or position has no ties to saved copies of the board.
        // Therefore, using copies of values.

        // Copying position from storage.
        SaveBoard previous = savedBoards.get(savedBoards.size()-1);
        int[] positionCopy = new int[2];
        int [] position = previous.getCord();
        System.arraycopy(position,0,positionCopy,0,2);
        //Copying board from storage.
        ArrayList<ArrayList<Integer>> boardCopy = new ArrayList<>();
        for(ArrayList<Integer> row : previous.getBoard()){
            ArrayList<Integer> rowCopy = new ArrayList<>(row);
            boardCopy.add(rowCopy);
        }
        // Overwriting old board and position.
        board = boardCopy;
        row = positionCopy[0];
        column = positionCopy[1];
        steps = previous.getSteps();
    }

    public void solve(boolean statusReport, boolean showResult) {
        // This part will keep repeating until the board is solved.
        if(!solved){
            ArrayList<int[]> leastPosMoves = leastPossibilitiesAfterMove();
            int moveAmount = leastPosMoves.size();

            if(statusReport){
                System.out.println("Possible move(s): "+moveAmount);
                System.out.println();
            }
            // Branching if the amount of moves possible is greater than 1.
            if(moveAmount > 1){
                // Saves position and board before branching.
                int[] cord;
                cord = new int[]{row, column};
                ArrayList<ArrayList<Integer>> boardCopy = new ArrayList<>();
                for(ArrayList<Integer> row : board){
                    ArrayList<Integer> rowCopy = new ArrayList<>(row);
                    boardCopy.add(rowCopy);
                }
                SaveBoard currentBoard = new SaveBoard(boardCopy, cord, steps);
                savedBoards.add(currentBoard);

                branchesEncountered++;
                if(statusReport){System.out.println("Total Branch: "+ savedBoards.size());}

                for(int[] move : leastPosMoves){
                    if(!solved) {
                        update(move, statusReport);
                        solve(statusReport, showResult);
                        if(!solved){
                            stepBack();
                            if(statusReport){System.out.println("Stepped back to Branch:" + savedBoards.size());}
                        }
                    }
                }
                if(!solved) {
                    // Remove dead ends
                    if(statusReport) {
                        System.out.println("Branch " + savedBoards.size() + " leads to dead end");
                        System.out.println("Branch "+ savedBoards.size()+" removed.");
                        System.out.println();
                    }
                    savedBoards.remove(savedBoards.size() - 1);
                    if(savedBoards.size() == 0){
                        System.out.println("No more paths to check.");
                        System.out.println();
                        final Instant endTime = Instant.now();
                        long timeSpent = Duration.between(startTime, endTime).toMillis();
                        System.out.println("---");
                        System.out.println("Found no solution.");
                        System.out.println("Time spent: "+timeSpent+"ms");
                        System.out.println("Amount of paths checked: "+checkedPaths);
                        System.out.println("Amount of branches checked: "+ branchesEncountered);
                        System.out.println("---");
                        System.exit(0);
                    }
                }

            }
            else if(moveAmount == 1){
                update(leastPosMoves.get(0), statusReport);
                solve(statusReport, showResult);
            }
            else{
                if(findCoveredArea() == squares){
                    if(steps == squares -1){
                        solved = true;
                        solve(statusReport, showResult);
                    }
                }
                else{
                    if(statusReport) {
                        System.out.println("Dead end");
                        System.out.println();
                    }
                }
            }
        }
        else{
            final Instant endTime = Instant.now();
            long timeSpent = Duration.between(startTime, endTime).toMillis();
            System.out.println("------------------------------");
            if(showResult) {
                drawBoard();
                System.out.println();
            }
            System.out.println("Found solution!");
            System.out.println("Time spent: "+timeSpent+"ms");
            System.out.println("Amount of branches to solution: " + savedBoards.size());
            System.out.println("Amount of paths checked: "+checkedPaths);
            System.out.println("Amount of branches encountered: "+ branchesEncountered);
            System.out.println("------------------------------");
            System.exit(0);
        }

    }
}


