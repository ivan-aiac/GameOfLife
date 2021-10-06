package life;

public enum Neighbor {
    NW(-1, -1),
    N (-1,  0),
    NE(-1,  1),
    E ( 0,  1),
    SE( 1,  1),
    S ( 1,  0),
    SW( 1, -1),
    W ( 0, -1);

    private final int row;
    private final int col;

    Neighbor(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
