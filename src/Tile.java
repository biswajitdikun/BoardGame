class Tile {
    private char symbol;
    private boolean occupied;

    public Tile() {
        this.symbol = ' ';
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public char getSymbol() {
        return symbol;
    }

    public void placeSymbol(char symbol) {
        this.symbol = symbol;
        this.occupied = true;
    }

}
