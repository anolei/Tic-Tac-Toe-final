package TicTacToe;

import TicTacToe.Board;

public class Algorithms {
    private Algorithms() {}

    public static void alphaBetaAdvanced (Board board) {
        AlphaBetaAdvanced.run(board.getTurn(), board);
    }
    public static void alphaBetaAdvanced (Board.State player, Board board) {
        AlphaBetaAdvanced.run(player, board);
    }

}
