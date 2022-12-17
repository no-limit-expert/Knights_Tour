import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter side length: ");
        int sideLengde = input.nextInt();
        int startRad = -1;
        int startKolonne = -1;
        while (startRad < 0 || startRad > sideLengde) {
            System.out.println("Enter starting row between 1 and " + sideLengde+".");
            startRad = input.nextInt();
        }
        while (startKolonne < 0 || startKolonne > sideLengde) {
            System.out.println("Enter starting column between 1 and " + sideLengde+".");
            startKolonne = input.nextInt();

        }
        Board spill = new Board(sideLengde, startRad-1, startKolonne-1);
        spill.solve(false, true);
    }
}
