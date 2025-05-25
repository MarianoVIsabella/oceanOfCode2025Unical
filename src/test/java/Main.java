import com.codingame.game.custom.players.ASPPlayer;
import com.codingame.gameengine.runner.MultiplayerGameRunner;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Setting Game League
        gameRunner.setLeagueLevel(3);

        // Random Game Map
        gameRunner.setSeed(new Random().nextLong());

        // Default Game Map
        // gameRunner.setSeed(1337L);

        // Adding Types of Players
        gameRunner.addAgent(ASPPlayer.class);
        gameRunner.addAgent(ASPPlayer.class);

        gameRunner.start();
    }
}