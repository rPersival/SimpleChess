package org.example.chess.model.chess;

public enum ChessType {
    PAWN("", 'p', '♙'),
    KNIGHT("N", 'n', '♘'),
    QUEEN("Q", 'q', '♕'),
    BISHOP("B", 'b', '♗'),
    ROOK("R", 'r', '♖'),
    KING("K", 'k', '♔');

    private final String notation;

    private final char notationFEN;

    private final char symbol;

    ChessType(String notation, char notationFEN, char symbol) {
        this.notation = notation;
        this.notationFEN = notationFEN;
        this.symbol = symbol;
    }

    public String getNotation() {
        return notation;
    }

    public char getFEN() {
        return notationFEN;
    }

    public static ChessType getByFEN(char FEN) {
        // TODO: find better approach
        FEN = Character.toLowerCase(FEN);
        return switch (FEN) {
            case 'p' -> ChessType.PAWN;
            case 'n' -> ChessType.KNIGHT;
            case 'q' -> ChessType.QUEEN;
            case 'b' -> ChessType.BISHOP;
            case 'r' -> ChessType.ROOK;
            case 'k' -> ChessType.KING;
            default -> throw new IllegalArgumentException("Invalid FEN character");
        };
    }

    public char getSymbol() {
        return symbol;
    }
}
