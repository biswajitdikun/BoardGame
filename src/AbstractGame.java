abstract class AbstractGame implements Game {
    protected Board board;
    protected Team[] teams;
    protected int currentPlayerIndex;

    public void configure(GameConfiguration config) {
        this.board = new Board(config.getBoardSize());
        this.teams = config.getTeams();
        this.currentPlayerIndex = 0;
    }
    protected Team getCurrentTeam() {
        return teams[currentPlayerIndex];
    }

    protected void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % teams.length;
    }
}
