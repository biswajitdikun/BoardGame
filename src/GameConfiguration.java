class GameConfiguration {
    private int boardSize;
    private Team[] teams;

    public GameConfiguration(int boardSize, Team[] teams) {
        this.boardSize = boardSize;
        this.teams = teams;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Team[] getTeams() {
        return teams;
    }
}
