import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter side length: ");
        int sideLenght = input.nextInt();
        int startRow = -1;
        int startcolumn = -1;
        while (startRow < 0 || startRow > sideLenght) {
            System.out.println("Enter starting row between 1 and " + sideLenght+".");
            startRow = input.nextInt();
        }
        while (startcolumn < 0 || startcolumn > sideLenght) {
            System.out.println("Enter starting column between 1 and " + sideLenght+".");
            startcolumn = input.nextInt();

        }
        Board game = new Board(sideLenght, startRow-1, startcolumn-1);
        game.solve(false, true);
    }
}
