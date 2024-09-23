class GameFactory {
    public static Game createGame(String type) {
        switch (type) {
            case "TicTacToe":
                return new TicTacToe();
            case "OrderChaos":
                return new OrderChaos();
            default:
                throw new IllegalArgumentException("Unknown game type: " + type);
        }
    }
}
