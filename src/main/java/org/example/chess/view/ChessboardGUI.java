package org.example.chess.view;

import org.example.chess.model.chess.ChessPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ChessboardGUI extends JFrame {
    private static final int BOARD_SIZE = 8;
    private static final int IMAGE_SIZE = 150;
    private static final Color LIGHT_SQUARE_COLOR = Color.decode("#edeed1");
    private static final Color DARK_SQUARE_COLOR = Color.decode("#779952");
    private ChessPosition selectedPiecePosition;
    private final SquarePanel[][] squares = new SquarePanel[BOARD_SIZE][BOARD_SIZE];
    private final BufferedImage[][] pieceImages = new BufferedImage[BOARD_SIZE][BOARD_SIZE];

    public ChessboardGUI() {
        setTitle("Chessboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        createSquares();
    }

    private void createSquares() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquarePanel squarePanel = new SquarePanel(row, col);
                squarePanel.addMouseListener(new SquareMouseListener());
                squares[row][col] = squarePanel;
                add(squarePanel);
            }
        }
    }

    public void clear() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                pieceImages[row][col] = null;
                squares[row][col].setBackground((row + col) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                squares[row][col].repaint();
            }
        }
        revalidate();
        repaint();
    }

    public void addChessPiece(int row, int col, String imagePath) {
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/chess/" + imagePath)));
        pieceImages[BOARD_SIZE - row][col] = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = pieceImages[BOARD_SIZE - row][col].getGraphics();
        imageIcon.paintIcon(null, g, 0, 0);
        g.dispose();
        squares[BOARD_SIZE - row][col].repaint();
    }

    public void removeChessPiece(int row, int column) {
        pieceImages[BOARD_SIZE - row][column] = null;
        squares[row][column].repaint();
    }

    public void addChessPiece(ChessPosition position, String piece) {
        addChessPiece(position.getRow(), position.getColumn() - 1, piece);
    }

    public void removeChessPiece(ChessPosition position) {
        removeChessPiece(position.getRow(), position.getColumn() - 1);
    }

    private class SquareMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            SquarePanel clickedSquare = (SquarePanel) e.getSource();
            int row = clickedSquare.getRow();
            int col = clickedSquare.getCol();

            ChessPosition newPosition = ChessPosition.of(BOARD_SIZE - row, col + 1);
            if (selectedPiecePosition == null) {
                // If no piece is selected, select the piece on this square
                if (pieceImages[row][col] != null) {
                    selectedPiecePosition = newPosition;
                    clickedSquare.setSelected(true);
                }
            } else if (selectedPiecePosition.equals(newPosition)) {
                selectedPiecePosition = null;
                clickedSquare.setSelected(false);
            } else {
                // If a piece is already selected, move it to the clicked square
                int fromRow = BOARD_SIZE - selectedPiecePosition.getRow();
                int fromCol = selectedPiecePosition.getColumn() - 1;

                pieceImages[row][col] = pieceImages[fromRow][fromCol];
                pieceImages[fromRow][fromCol] = null;
                selectedPiecePosition = null;

                // Update the GUI
                squares[fromRow][fromCol].setSelected(false);
                squares[row][col].repaint();
                squares[fromRow][fromCol].repaint();
            }
        }
    }

    private class SquarePanel extends JPanel {
        private final int row;
        private final int col;
        private boolean isSelected = false;

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public SquarePanel(int row, int col) {
            this.row = row;
            this.col = col;
            setBackground((row + col) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isSelected) {
                setBackground(Color.decode("#ffff33"));
            } else if (pieceImages[row][col] != null) {
                setBackground((row + col) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
            }

            int width = getWidth();
            int height = getHeight();
            g.drawImage(pieceImages[row][col], 0, 0, width, height, this);
        }


    }
}