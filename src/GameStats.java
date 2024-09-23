import java.util.HashMap;
import java.util.Map;

class GameStats {
    private String gameName;
    private int wins;
    private int ties;
    private Map<String, Integer> teamWins;

    public GameStats(String gameName) {
        this.gameName = gameName;
        this.teamWins = new HashMap<>();
    }

    public void recordWin(String teamName) {
        wins++;
        teamWins.put(teamName, teamWins.getOrDefault(teamName, 0) + 1);
    }

    public void recordTie() {
        ties++;
    }

    public void displayStats() {
        if (wins > 0 || ties > 0) {
            System.out.println(gameName + " Stats:");
            System.out.println("Wins: " + wins + ", Ties: " + ties);
            for (Map.Entry<String, Integer> entry : teamWins.entrySet()) {
                System.out.println(entry.getKey() + " Wins: " + entry.getValue());
            }
            System.out.println();
        }
    }
}
