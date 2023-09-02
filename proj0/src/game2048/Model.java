package game2048;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        board = new Board(rawValues);
        this.score = score;
        this.maxScore = maxScore;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists(board) || !atLeastOneMoveExists(board);
    }

    /** Checks if the game is over and sets the maxScore variable
     *  appropriately.
     */
    private void checkGameOver() {
        if (gameOver()) {
            maxScore = Math.max(score, maxScore);
        }
    }
    
    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for (int i = 0; i < b.size(); i++) { //根据board的size()决定遍历多少列；
            for (int j = 0; j < b.size(); j++) { // 根据board的size()决定遍历多少行；
                if (b.tile(i, j) == null) { //如果i列j行没有tile
                    return true; // 返回true；
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        // 遍历board
        for (int i = 0; i < b.size();i++){
            for (int j = 0; j <b.size(); j++){
                Tile t = b.tile(i,j); // 获取当前位置的tile
                if (t != null){ // 如果该tile不是null
                    int value = t.value(); // 获取该tile的value
                    if (value == MAX_PIECE){ // 如果value为MAX_PIECE(2048)
                        return true; // 返回true.
                    }
                }
            }
        }
        return false;
    }
    /**
     * This is a helper function for atLeastOneMoveExists.
     * it takes two arguments, Tile curr and Board b,
     * firstly it will check whether the current tile has adjacent tiles(top, bottom, left and right)
     * based on its relative coordinate,
     * i.e. if curr is (0,0), then the top is (0,1), bot is (0,-1), left(-1,0) and right(1,0).
     * then check whether the adjacent tile is null, if it's not,
     * then check if the adjacent has the same value as the curr tile and return true if they are.
     * */
    public static boolean hasAdjacentTiles(Tile curr, Board b) {
        int size = b.size();
        int row = curr.row();
        int col = curr.col(); // column from 0 - 3

        Tile topTile = row < size - 1 ? b.tile(col, row + 1) : null;
        // if the current row is > 0, meaning the curr tile has a botTile adjacent;
        // otherwise current tile is at the bottom row and doesn't have a botTile adjacent, therefore return null.
        Tile botTile = row > 0 ? b.tile(col, row - 1) : null;
        Tile rightTile = col < size - 1 ? b.tile(col + 1, row) : null;
        Tile leftTile = col > 0 ? b.tile(col - 1, row) : null;
        if (topTile != null && equalValue(curr, topTile)) {
            return true;
        } else if (botTile != null && equalValue(curr, botTile)) {
            return true;
        } else if (leftTile != null && equalValue(curr, leftTile)) {
            return true;
        } else return rightTile != null && equalValue(curr, rightTile);
    }

    /** This is a helper function for hasAdjacentTiles.
     *  It compares whether two tiles has the same value and return true if they are.*/
    public static boolean equalValue(Tile a, Tile b){
        return  (a.value() == b.value());
    }
    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        if(emptySpaceExists(b)) return true;
        else {
            for (int i = 0; i < size; i++){
                for (int j = 0; j < size; j++){
                    Tile curr = b.tile(i,j); // take out the current tile
                    if (hasAdjacentTiles(curr,b)) return true; // check whether it has adjacentTiles and then return.
                }
            }
        }
        return false;

    }

    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public void tilt(Side side) {
        // TODO: Modify this.board (and if applicable, this.score) to account
        // for the tilt to the Side SIDE.


        checkGameOver();
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}

