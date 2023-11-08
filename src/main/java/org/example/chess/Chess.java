package org.example.chess;

import org.example.chess.model.chess.Board;
import org.example.chess.model.chess.ChessPosition;
import org.example.chess.view.ChessboardGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.util.Objects;
import java.util.Scanner;

//@SpringBootApplication
public class Chess {
    public static void main(String[] args) {
//        SpringApplication.run(Chess.class, args);
        runGUI();
    }

    private static void runGUI() {
        ChessboardGUI chessboard = new ChessboardGUI();

        SwingUtilities.invokeLater(() -> chessboard.setVisible(true));

        Scanner scanner = new Scanner(System.in);
        Board board = Board.getStartingPosition();
        while (true) {
            board.paintBoard(chessboard);
            String[] in = scanner.nextLine().split(" ");
            if (Objects.equals(in[0], "0") && Objects.equals(in[1], "0")) {
                chessboard.dispose();
                break;
            }
            ChessPosition current = ChessPosition.of(in[0]);
            ChessPosition target = ChessPosition.of(in[1]);
            board.move(current, target);
        }
    }
}