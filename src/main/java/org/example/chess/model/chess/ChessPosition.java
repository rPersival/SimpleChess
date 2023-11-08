package org.example.chess.model.chess;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class ChessPosition {
    private final int row;
    private final int column;
    private static final Map<String, ChessPosition> POSITIONS = new HashMap<>();

    private ChessPosition(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public static ChessPosition of(int row, int column) {
        if (row < 1 || column < 1 || row > Board.BOARD_LENGTH || column > Board.BOARD_LENGTH) {
            throw new IllegalArgumentException("Wrong position for this board");
        }
        String key = row + "-" + column;
        return POSITIONS.computeIfAbsent(key, k -> new ChessPosition(row, column));
    }

    public static ChessPosition of(char column, int row) {
        return ChessPosition.of(row, letterToNumber(column));
    }

    public static ChessPosition of(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Wrong position cords");
        }

        char rowAsChar = pos.charAt(1);

        if (!Character.isDigit(rowAsChar)) {
            throw new IllegalArgumentException("Wrong position cords");
        }

        return ChessPosition.of(Character.toLowerCase(pos.charAt(0)), rowAsChar - '0');
    }

    public String getPos() {
        return String.valueOf(getColumnAsLetter()) + row;
    }

    public char getColumnAsLetter() {
        return numberToLetter(column);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public static int letterToNumber(char letter) {
        letter = Character.toLowerCase(letter);

        int outNumber = letter - 'a' + 1;

        if (outNumber < 1 || outNumber > Board.BOARD_LENGTH) {
            throw new IllegalArgumentException("Wrong letter");
        }

        return outNumber;
    }

    public static char numberToLetter(int number) {
        if (number < 1 || number > Board.BOARD_LENGTH) {
            throw new IllegalArgumentException("Wrong letter");
        }

        return (char) ('a' + number - 1);
    }
}
