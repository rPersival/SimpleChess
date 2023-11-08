package org.example.chess.model.chess;

public class ChessPiece {
    private final ChessType chessType;
    private final ChessColor color;
    private ChessPosition position;

    public ChessPiece(ChessType chessType, ChessColor color) {
        this.chessType = chessType;
        this.color = color;
    }

    public ChessType getChessType() {
        return chessType;
    }

    public void updatePosition(ChessPosition position) {
        this.position = position;
    }

    public static ChessPiece getPieceByFEN(char FEN) {
        ChessType type = ChessType.getByFEN(FEN);
        ChessColor color = Character.isLowerCase(FEN) ? ChessColor.BLACK : ChessColor.WHITE;
        return new ChessPiece(type, color);
    }

    public char getFEN() {
        char FEN = chessType.getFEN();

        if (color.equals(ChessColor.WHITE)) {
            FEN = Character.toUpperCase(FEN);
        }

        return FEN;
    }

    public String getPNGPath() {
        return String.valueOf(color.getColorNotation()) + chessType.getFEN() + ".png";
    }

    public ChessColor getColor() {
        return color;
    }
}
