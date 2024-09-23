interface Game {
    void play();
    void configure(GameConfiguration config);
    Team getWinner();
}
