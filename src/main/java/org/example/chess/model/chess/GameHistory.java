package org.example.chess.model.chess;

import java.util.LinkedList;

public class GameHistory {
    private boolean isRecording = false;
    private final LinkedList<GameRecord> history = new LinkedList<>();

    public static GameHistory startRecording() {
        return new GameHistory();
    }

    public void move(ChessPosition pastPosition, ChessPosition currentPosition, ChessPiece piece, boolean captured) {
        history.add(new GameRecord(pastPosition, currentPosition, piece, captured));
        //TODO: implement
    }

    public void undo() {
        history.pollLast();
    }

    public String asNotation() {
        StringBuilder stringBuilder = new StringBuilder();

        // I use for-each because it optimized for linked list
        int i = 1;
        for (GameRecord record : history) {
            stringBuilder.append(i).append(". ").append(record.asNotation()).append("\n");
            i++;
        }

        return stringBuilder.toString();
    }

    private record GameRecord(ChessPosition pastPosition, ChessPosition currentPosition, ChessPiece piece, boolean captured) {
        public String asNotation() {
            return piece.getChessType().getNotation() + (captured ? getCapturedPos() : "") + currentPosition.getPos();
        }

        private String getCapturedPos() {
            StringBuilder stringBuilder = new StringBuilder();
            if (piece.getChessType() == ChessType.PAWN) {
                stringBuilder.append(pastPosition.getColumnAsLetter());
            }

            return stringBuilder.append("x").toString();
        }
    }
}
