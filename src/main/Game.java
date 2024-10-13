package main;
import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameBoard gameBoard = new GameBoard();
            gameBoard.setVisible(true);
        });
    }
}
