package org.example.chess.model.chess;

import java.util.Arrays;

public enum Status {
    WHITE_TO_MOVE(1, ChessColor.WHITE),
    BLACK_TO_MOVE(2, ChessColor.BLACK),
    DRAW(0),
    WHITE_WIN(-1),
    BLACK_WIN(-2);

    private final int code;

    private final ChessColor color;

    Status(int code, ChessColor color) {
        this.code = code;
        this.color = color;
    }

    Status(int code) {
        this(code, null);
    }

    public int getCode() {
        return code;
    }

    public ChessColor getColor() {
        if (color == null || code <= 0) {
            throw new RuntimeException("The game is already ended");
        }
        return color;
    }

    public static Status getByCode(int code) {
        // TODO: maybe linked list with iterator? or maybe not
        return Arrays.stream(values()).filter(x -> x.code == code).findFirst()
                .orElse(null);
    }

    public static Status getStatusByFEN(String FENStatus) {
        // TODO: find better approach
        FENStatus = FENStatus.toLowerCase();
        return switch (FENStatus) {
            case "w" -> Status.WHITE_TO_MOVE;
            case "b" -> Status.BLACK_TO_MOVE;
            default -> throw new IllegalStateException("Invalid FEN move");
        };
    }

    public static Status getNextMove(Status currentStatus) {
        int code = currentStatus.code;
        Status out = Status.getByCode(code + 1);
        if (out == null) {
            out = Status.getByCode(1);
            if (out == null) {
                throw new RuntimeException("Something went very wrong");
            }
            return out;
        }
        return out;
    }
}
