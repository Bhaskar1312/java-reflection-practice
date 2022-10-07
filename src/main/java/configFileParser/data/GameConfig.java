package configFileParser.data;

import java.util.Random;

public class GameConfig {
        private int releaseYear = 2000;
//        private final int releaseYear = 2000; // 2000, not 1993 in reflection
//        private final int releaseYear; // with random, after 1000, again 1993 in reflection
//    private static final int releaseYear = 2000; // error
    public GameConfig() {
//        Random random = new Random();
//        releaseYear = random.nextInt(1000);
    }

    private String gameName;
    private double price;

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public String getGameName() {
        return this.gameName;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "releaseYear=" + releaseYear +
                ", gameName='" + gameName + '\'' +
                ", price=" + price +
                '}';
    }
}
