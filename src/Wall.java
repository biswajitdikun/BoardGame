public class Wall {
    private int row, col;
    private boolean isHorizontal;

    Wall(int row, int col, boolean isHorizontal) {
        this.row = row;
        this.col = col;
        this.isHorizontal = isHorizontal;
    }
    public int getRow() {return row;}
    public int getCol() {return col;}
    public boolean checkHorizontal() {return isHorizontal;}

    @Override
    public String toString() {
        return (isHorizontal ? "H" : "V") + "(" + row + "," + col + ")";
    }
}