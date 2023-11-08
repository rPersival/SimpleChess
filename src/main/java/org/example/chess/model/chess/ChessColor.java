package org.example.chess.model.chess;

public enum ChessColor {
    WHITE('w'),
    BLACK('b');

    private final char colorNotation;

    ChessColor(char colorNotation) {
        this.colorNotation = colorNotation;
    }

    public char getColorNotation() {
        return colorNotation;
    }
}
