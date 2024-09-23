import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

class GameController {
    private InputHandler inputHandler;
    private Map<String, GameStats> gameStatsMap;
    public GameController() {
        this.inputHandler = new InputHandler();
        this.gameStatsMap = new HashMap<>();
        gameStatsMap.put("TicTacToe", new GameStats("TicTacToe"));
        gameStatsMap.put("OrderChaos", new GameStats("OrderChaos"));
    }

    public void startGame() {
        while (true) {
            System.out.println("Choose a game:");
            System.out.println("1. Tic Tac Toe");
            System.out.println("2. Order & Chaos");
            System.out.println("3. Exit");

            int choice = inputHandler.getIntInput("Enter your choice (1-3): ", 1, 3);

            if (choice == 3) {
                break;
            }

            String gameType = "";
            int boardSize = 0;

            if (choice == 1) {
                System.out.println("Welcome to Tic-Tac-Toe!");
                gameType = "TicTacToe";
                boardSize = inputHandler.getIntInput("Enter board size (3-10): ", 3, 10);
            }

            if (choice == 2) {
                System.out.println("Welcome to Order and Chaos!");
                gameType = "OrderChaos";
                boardSize = inputHandler.getIntInput("Enter board size (6-10): ", 6, 10);
            }

            Team[] teams = createTeams(choice);
            GameConfiguration config = new GameConfiguration(boardSize, teams);
            Game game = GameFactory.createGame(gameType);

            // Game Starts
            game.configure(config);
            game.play();
            updateGameStats(gameType, game.getWinner());

            // Ask the user for another game
            String playAgain = inputHandler.getStringInput("Do you want to play another game? (yes/no): ", "yes", "no", "y", "n");
            if (!playAgain.equals("yes") && !playAgain.equals("y")) {
                break;
            }
        }

        displayGameStats();
        inputHandler.close();
    }

    private Team[] createTeams(int gameChoice) {
        Team[] teams = new Team[2];
        Set<String> usedSymbols = new HashSet<>(); // HashSet to track used symbols

        for (int i = 0; i < 2; i++) {
            String teamName = inputHandler.getStringInput("Enter name for Team " + (i + 1) + ": ");
            String teamSymbol = "";

            if (gameChoice == 1) { //Ask for symbol in case of TTT Game only
                while (true) {
                    teamSymbol = inputHandler.getSymbolInput("Enter symbol for " + teamName + ": ");
                    if (!usedSymbols.contains(teamSymbol)) {
                        usedSymbols.add(teamSymbol); // Add the symbols to the HashSet
                        break; // unique check
                    } else {
                        System.out.println("Symbol '" + teamSymbol + "' is already taken. Please choose another symbol.");
                    }
                }
            }

            teams[i] = new Team(teamName, teamSymbol);
        }
        return teams;
    }


    private void updateGameStats(String gameType, Team winner) {
        GameStats stats = gameStatsMap.get(gameType);
        if (winner != null) {
            stats.recordWin(winner.getName());
        } else {
            stats.recordTie();
        }
    }

    private void displayGameStats() {
        System.out.println("\nGame Statistics:");
        for (GameStats stats : gameStatsMap.values()) {
            stats.displayStats();
        }
    }
}
