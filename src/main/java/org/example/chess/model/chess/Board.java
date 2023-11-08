package org.example.chess.model.chess;

import org.example.chess.view.ChessboardGUI;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public static final int BOARD_LENGTH = 8;
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final ChessMap chessMap;
    private final GameHistory history;
    private int totalMoves;
    private int movesWithoutCapturing;
    private boolean canWhiteCastleKingside;
    private boolean canWhiteCastleQueenside;
    private boolean canBlackCastleKingside;
    private boolean canBlackCastleQueenside;
    private ChessPosition enPassantTarget;
    private Status status;

    private Board(ChessMap chessMap, boolean recordHistory, int totalMoves, int movesWithoutCapturing,
                  boolean canWhiteCastleKingside, boolean canWhiteCastleQueenside,
                  boolean canBlackCastleKingside, boolean canBlackCastleQueenside,
                  ChessPosition enPassantTarget, Status status) {
        this.chessMap = chessMap;
        this.history = recordHistory ? GameHistory.startRecording() : null;
        this.totalMoves = totalMoves;
        this.movesWithoutCapturing = movesWithoutCapturing;
        this.canWhiteCastleKingside = canWhiteCastleKingside;
        this.canWhiteCastleQueenside = canWhiteCastleQueenside;
        this.canBlackCastleKingside = canBlackCastleKingside;
        this.canBlackCastleQueenside = canBlackCastleQueenside;
        this.enPassantTarget = enPassantTarget;
        this.status = status;
    }

    public Board(ChessMap chessMap) {
        this(chessMap, true, 1, 0,
                true, true, true,
                true, null, Status.WHITE_TO_MOVE);
    }

    public Board() {
        this(ChessMap.arrangeStartingPosition(), true,
                1, 0, true,
                true, true, true,
                null, Status.WHITE_TO_MOVE);
    }

    public static Board getStartingPosition() {
        return Board.loadFEN(STARTING_POSITION);
    }

    public static Board loadFEN(String FEN) {
        if (FEN.isBlank()) {
            throw new IllegalArgumentException("FEN cannot be empty");
        }

        String[] FENSections = FEN.split(" ");
        ChessMap chessMap = ChessMap.fromSimpleFEN(FENSections[0]);

        if (FENSections.length > 1) {
            if (FENSections.length != 6) {
                throw new IllegalArgumentException("Wrong FEN special cases count");
            }

            Status status = Status.getStatusByFEN(FENSections[1]);
            String castlingAbility = FENSections[2];
            boolean canWhiteCastleKingside = castlingAbility.contains("K");
            boolean canWhiteCastleQueenside = castlingAbility.contains("Q");
            boolean canBlackCastleKingside = castlingAbility.contains("k");
            boolean canBlackCastleQueenside = castlingAbility.contains("q");

            if (!(canWhiteCastleKingside || canWhiteCastleQueenside ||
                    canBlackCastleKingside || canBlackCastleQueenside)) {
                if (!castlingAbility.equals("-")) {
                    throw new IllegalArgumentException("Wrong FEN: castling");
                }
            }

            ChessPosition position = FENSections[3].equals("-") ? null : ChessPosition.of(FENSections[3]);
            int movesWithoutCapturing = Integer.parseInt(FENSections[4]);
            int moves = Integer.parseInt(FENSections[5]);

            return new Board(chessMap, true, moves, movesWithoutCapturing,
                    canWhiteCastleKingside, canWhiteCastleQueenside,
                    canBlackCastleKingside, canBlackCastleKingside, position, status);
        }
        return new Board(chessMap);
    }

    public void move(ChessPosition currentPosition, ChessPosition targetPosition) {
        if (!isLegalMove(currentPosition, targetPosition)) {
            throw new IllegalArgumentException("Illegal position");
        }
        ChessPiece opponentPiece = chessMap.getPiece(targetPosition);
        boolean isCaptured = opponentPiece != null;
        totalMoves++;
        status = Status.getNextMove(status);
        chessMap.makeMove(currentPosition, targetPosition, chessMap.getPiece(currentPosition));

        if (isCaptured) {
            movesWithoutCapturing = 0;
        } else {
            movesWithoutCapturing++;
        }

        checkGameStatus();
    }

    private void checkGameStatus() {
        Status outStatus = chessMap.isMate();
        if (outStatus != null) {
            status = outStatus;
            announceGameStatus();
        } else if (movesWithoutCapturing > 55) {
            status = Status.DRAW;
            announceGameStatus();
        }
    }

    private void announceGameStatus() {
        if (!isOngoing()) {
            System.out.println("Game has ended!");
            switch (status) {
                case DRAW -> System.out.println("Draw!");
                case BLACK_WIN -> System.out.println("Black Win!");
                case WHITE_WIN -> System.out.println("White Win!");
            }
        } else {
            System.out.println("Game is still running, check again later.");
        }
    }

    private boolean isLegalMove(ChessPosition currentPosition, ChessPosition targetPosition) {
        if (!isOngoing()) {
            throw new RuntimeException("Game has already ended");
        }
        if (currentPosition.equals(targetPosition)) {
            return false;
        }

        ChessPiece thisPiece = chessMap.getPiece(currentPosition);
        if (!isCorrectPiece(thisPiece)) {
            return false;
        }

        ChessPiece opponentPiece = chessMap.getPiece(targetPosition);
        if (opponentPiece != null && (opponentPiece.getColor() == status.getColor()
                || opponentPiece.getChessType() == ChessType.KING)) {
            return false;
        }


        return chessMap.isLegalMove(currentPosition, targetPosition);
    }

    private boolean isCorrectPiece(ChessPiece piece) {
        return piece != null && piece.getColor() == status.getColor();
    }

    public boolean isOngoing() {
        return status.getCode() > 0;
    }

    public String printBoard() {
        return chessMap.printBoard();
    }


    public void paintBoard(ChessboardGUI gui) {
        chessMap.paintBoard(gui);
        gui.revalidate();
        gui.repaint();
    }

    // TODO: maybe move this class outside?
    public static class ChessMap {
        private final Map<ChessPosition, ChessPiece> map = new HashMap<>();

        private static final String STARTING_POSITION = Board.STARTING_POSITION.split(" ")[0];

        public Map<ChessPosition, ChessPiece> asMap() {
            return map;
        }

        public String asSimpleFEN() {
            StringBuilder FENBuilder = new StringBuilder();

            for (int i = BOARD_LENGTH - 1; i >= 0; i--) {
                int emptySquares = 0;
                for (int j = 0; j < BOARD_LENGTH; j++) {
                    ChessPosition pos = ChessPosition.of(i + 1, j + 1);
                    ChessPiece piece = map.getOrDefault(pos, null);

                    if (piece != null) {
                        if (emptySquares > 0) {
                            FENBuilder.append(emptySquares);
                            emptySquares = 0;
                        }
                        FENBuilder.append(piece.getFEN());
                    } else {
                        emptySquares++;
                    }
                }
                if (emptySquares > 0) {
                    FENBuilder.append(emptySquares);
                }
                FENBuilder.append('/');
            }

            return FENBuilder.deleteCharAt(FENBuilder.length() - 1).toString();
        }

        public static ChessMap fromSimpleFEN(String FEN) {
            if (FEN.isBlank()) {
                throw new IllegalArgumentException("FEN cannot be empty");
            }

            String[] boardRows = FEN.split("/");

            if (boardRows.length != BOARD_LENGTH) {
                throw new IllegalArgumentException("Wrong FEN for this board");
            }
            ChessMap thisMap = new ChessMap();

            for (int i = 0; i < BOARD_LENGTH; i++) {
                String row = boardRows[i];

                for (int j = 0, emptySquares = 0; j < BOARD_LENGTH && emptySquares < BOARD_LENGTH; j++, emptySquares++) {
                    char FENChar = row.charAt(j);

                    if (Character.isDigit(FENChar)) {
                        emptySquares += FENChar - '0' - 1;
                        if (emptySquares >= BOARD_LENGTH) {
                            throw new IllegalArgumentException("Wrong FEN for this board");
                        }
                        continue;
                    }

                    ChessPosition position = ChessPosition.of(BOARD_LENGTH - i, emptySquares + 1);
                    ChessPiece piece = ChessPiece.getPieceByFEN(FENChar);
                    thisMap.asMap().put(position, piece);
                    piece.updatePosition(position);
                }

            }

            return thisMap;
        }

        public static ChessMap arrangeStartingPosition() {
            return ChessMap.fromSimpleFEN(STARTING_POSITION);
        }

        public ChessPiece getPiece(ChessPosition position) {
            return map.get(position);
        }

        public ChessPiece makeMove(ChessPosition previous, ChessPosition position, ChessPiece piece) {
            map.remove(previous);
            return map.put(position, piece);
        }

        public boolean isLegalMove(ChessPosition currentPosition, ChessPosition targetPosition) {
            // TODO: implement
            ChessType type = this.getPiece(currentPosition).getChessType();
            return true;
        }

        public Status isMate() {
            return null;
        }

        public String printBoard() {
            StringBuilder board = new StringBuilder();

            for (int i = BOARD_LENGTH - 1; i >= 0; i--) {
                for (int j = 0; j < BOARD_LENGTH; j++) {
                    ChessPosition pos = ChessPosition.of(i + 1, j + 1);
                    ChessPiece piece = map.getOrDefault(pos, null);
                    board.append("|").append(piece != null ? piece.getChessType().getSymbol() : "—").append("|");
                }
                board.append('\n');
            }
            return board.toString();
        }

        public void paintBoard(ChessboardGUI gui) {
            gui.clear();

            for (int i = BOARD_LENGTH - 1; i >= 0; i--) {
                for (int j = 0; j < BOARD_LENGTH; j++) {
                    ChessPosition pos = ChessPosition.of(i + 1, j + 1);
                    ChessPiece piece = map.getOrDefault(pos, null);
                    if (piece != null) {
                        gui.addChessPiece(pos, String.valueOf(piece.getPNGPath()));
                    }
                }
            }
        }
    }
}
