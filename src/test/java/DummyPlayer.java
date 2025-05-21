import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class DummyPlayer {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int myId = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String line = in.nextLine();
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println("7 4");

        int count = 0;

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int myLife = in.nextInt();
            int oppLife = in.nextInt();
            int torpedoCooldown = in.nextInt();
            int sonarCooldown = in.nextInt();
            int silenceCooldown = in.nextInt();
            int mineCooldown = in.nextInt();
            String sonarResult = in.next();
            if (in.hasNextLine()) {
                in.nextLine();
            }
            String opponentOrders = in.nextLine();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.err.println(x + " " + y + " " + myLife + " " + oppLife + " " + torpedoCooldown + " " + sonarCooldown + " " + silenceCooldown + " " + mineCooldown);
            System.err.println(sonarResult);
            System.err.println(opponentOrders);

            if (count < 3) { System.out.println("MOVE S TORPEDO"); }
            else if (count < 6) { System.out.println("MOVE S MINE"); }
            else if (count == 6) { System.out.println("SURFACE | MOVE S SILENCE | TORPEDO 7 9 | MOVE N");}
            else { System.out.println("SURFACE"); }

            count++;
        }
    }
}
