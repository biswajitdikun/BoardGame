class Team {
    private String name;
    private Piece piece;

    public Team(String name, String symbol) {
        this.name = name;
        this.piece = new Piece(symbol); // Initialize's the Piece
    }

    public String getName() {
        return name;
    }

    public Piece getPiece() {
        return piece;
    }
}
