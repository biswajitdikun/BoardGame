class Board {
    private final int size;
    private final Tile[][] tiles;

    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
        resetBoard();
    }

    public int getSize() {
        return size;
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new Tile(); // Initializes each tile
            }
        }
    }

    public boolean placeSymbol(int row, int col, Piece piece) {
        if (!tiles[row][col].isOccupied()) {
            tiles[row][col].placeSymbol(piece.getSymbol().charAt(0)); // Place piece symbol in the tile
            return true;
        }
        return false;
    }

    public char getTile(int row, int col) {
        return tiles[row][col].getSymbol();
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!tiles[i][j].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(" " + tiles[i][j].getSymbol() + " ");
                if (j < size - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < size - 1) {
                // Separator line
                for (int j = 0; j < size; j++) {
                    System.out.print("---");
                    if (j < size - 1) {
                        System.out.print("+");
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }
}
