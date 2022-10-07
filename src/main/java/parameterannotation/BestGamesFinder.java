package parameterannotation;

import parameterannotation.annotations.Annotations.*;
import parameterannotation.database.Database;

import java.util.*;

public class BestGamesFinder {

    private Database database = new Database();

    @Operation("All-Games")
    public Set<String> getAllGames() {
        return database.readAllGames();
    }

    @Operation("Game-To-Price")
    public Map<String, Float> getGameToPrice(@DependOn("All-Games") Set<String> allGames) {
        return database.readGameToPrice(allGames);
    }

    @Operation("Game-To-Rating")
    public Map<String, Float> getGameToRating( @DependOn("All-Games") Set<String> allGames) {
        return database.readGameToRatings(allGames);
    }

    @Operation("Score-To-Game")
    public SortedMap<Double, String> scoreGames(@DependOn("Game-To-Price") Map<String, Float> gameToPrice,
                                               @DependOn("Game-To-Rating") Map<String, Float> gameToRating) {
        SortedMap<Double, String> gameToScore = new TreeMap<>(Collections.reverseOrder());
        for (String gameName : gameToPrice.keySet()) {
            double score = (double) gameToRating.get(gameName) / gameToPrice.get(gameName);
            gameToScore.put(score, gameName);
        }
        return gameToScore;
    }

    @FinalResult
    public List<String> getTopGames(@DependOn("Score-To-Game") SortedMap<Double, String> gameToScore) {
        return new ArrayList<>(gameToScore.values());
    }

}
