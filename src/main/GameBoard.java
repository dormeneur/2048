package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputFilter.Config;
import java.util.Random;

public class GameBoard extends JFrame {
    private static final int SIZE = Config.SIZE; // Use the size from Config class
    private static final int TILE_SIZE = Config.TILE_SIZE; // Use the tile size from Config class
    private static final int GAP = 15;
    private Tile[][] tiles;
    private int score;

    public GameBoard() {
        setTitle("2048 Game");
        setSize(SIZE * (TILE_SIZE + GAP) + GAP, SIZE * (TILE_SIZE + GAP) + GAP + 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tiles = new Tile[SIZE][SIZE];
        initGame();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:    move(Direction.UP); break;
                    case KeyEvent.VK_DOWN:  move(Direction.DOWN); break;
                    case KeyEvent.VK_LEFT:  move(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT: move(Direction.RIGHT); break;
                }
            }
        });
    }

    private void initGame() {
        score = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j] = new Tile(0);
            }
        }
        addRandomTile();
        addRandomTile();
        repaint();
    }

    // private void move(Direction direction) {
    //     boolean moved = false;

    //     switch (direction) {
    //         case UP:
    //             for (int col = 0; col < SIZE; col++) {
    //                 for (int row = 1; row < SIZE; row++) {
    //                     if (tiles[row][col].getValue() != 0) {
    //                         moved |= mergeTile(row, col, -1);
    //                     }
    //                 }
    //             }
    //             break;
    //         case DOWN:
    //             for (int col = 0; col < SIZE; col++) {
    //                 for (int row = SIZE - 2; row >= 0; row--) {
    //                     if (tiles[row][col].getValue() != 0) {
    //                         moved |= mergeTile(row, col, 1);
    //                     }
    //                 }
    //             }
    //             break;
    //         case LEFT:
    //             for (int row = 0; row < SIZE; row++) {
    //                 for (int col = 1; col < SIZE; col++) {
    //                     if (tiles[row][col].getValue() != 0) {
    //                         moved |= mergeTile(row, col, -1);
    //                     }
    //                 }
    //             }
    //             break;
    //         case RIGHT:
    //             for (int row = 0; row < SIZE; row++) {
    //                 for (int col = SIZE - 2; col >= 0; col--) {
    //                     if (tiles[row][col].getValue() != 0) {
    //                         moved |= mergeTile(row, col, 1);
    //                     }
    //                 }
    //             }
    //             break;
    //     }

    //     if (moved) {
    //         addRandomTile();
    //         repaint();
    //     }
    // }
    private void move(Direction direction) {
        boolean moved = false;
        boolean[][] merged = new boolean[SIZE][SIZE]; // Track merged tiles

        switch (direction) {
            case UP:
                for (int col = 0; col < SIZE; col++) {
                    for (int row = 1; row < SIZE; row++) {
                        if (tiles[row][col].getValue() != 0) {
                            int targetRow = row;
                            while (targetRow > 0 && tiles[targetRow - 1].getValue() == 0) {
                                targetRow--; // Move up if the tile above is empty
                            }
                            if (targetRow > 0 && tiles[targetRow - 1].getValue() == tiles[row][col].getValue() && !merged[targetRow - 1][col]) {
                                // Merge
                                tiles[targetRow - 1].setValue(tiles[targetRow - 1].getValue() * 2);
                                score += tiles[targetRow - 1].getValue();
                                tiles[row][col].setValue(0);
                                merged[targetRow - 1][col] = true; // Mark as merged
                                moved = true;
                            } else if (targetRow != row) {
                                // Move the tile
                                tiles[targetRow][col].setValue(tiles[row][col].getValue());
                                tiles[row][col].setValue(0);
                                moved = true;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int col = 0; col < SIZE; col++) {
                    for (int row = SIZE - 2; row >= 0; row--) {
                        if (tiles[row][col].getValue() != 0) {
                            int targetRow = row;
                            while (targetRow < SIZE - 1 && tiles[targetRow + 1].getValue() == 0) {
                                targetRow++; // Move down if the tile below is empty
                            }
                            if (targetRow < SIZE - 1 && tiles[targetRow + 1].getValue() == tiles[row][col].getValue() && !merged[targetRow + 1][col]) {
                                // Merge
                                tiles[targetRow + 1].setValue(tiles[targetRow + 1].getValue() * 2);
                                score += tiles[targetRow + 1].getValue();
                                tiles[row][col].setValue(0);
                                merged[targetRow + 1][col] = true; // Mark as merged
                                moved = true;
                            } else if (targetRow != row) {
                                // Move the tile
                                tiles[targetRow][col].setValue(tiles[row][col].getValue());
                                tiles[row][col].setValue(0);
                                moved = true;
                            }
                        }
                    }
                }
                break;
            case LEFT:
                for (int row = 0; row < SIZE; row++) {
                    for (int col = 1; col < SIZE; col++) {
                        if (tiles[row][col].getValue() != 0) {
                            int targetCol = col;
                            while (targetCol > 0 && tiles[row][targetCol - 1].getValue() == 0) {
                                targetCol--; // Move left if the tile to the left is empty
                            }
                            if (targetCol > 0 && tiles[row][targetCol - 1].getValue() == tiles[row][col].getValue() && !merged[row][targetCol - 1]) {
                                // Merge
                                tiles[row][targetCol - 1].setValue(tiles[row][targetCol - 1].getValue() * 2);
                                score += tiles[row][targetCol - 1].getValue();
                                tiles[row][col].setValue(0);
                                merged[row][targetCol - 1] = true; // Mark as merged
                                moved = true;
                            } else if (targetCol != col) {
                                // Move the tile
                                tiles[row][targetCol].setValue(tiles[row][col].getValue());
                                tiles[row][col].setValue(0);
                                moved = true;
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int row = 0; row < SIZE; row++) {
                    for (int col = SIZE - 2; col >= 0; col--) {
                        if (tiles[row][col].getValue() != 0) {
                            int targetCol = col;
                            while (targetCol < SIZE - 1 && tiles[row][targetCol + 1].getValue() == 0) {
                                targetCol++; // Move right if the tile to the right is empty
                            }
                            if (targetCol < SIZE - 1 && tiles[row][targetCol + 1].getValue() == tiles[row][col].getValue() && !merged[row][targetCol + 1]) {
                                // Merge
                                tiles[row][targetCol + 1].setValue(tiles[row][targetCol + 1].getValue() * 2);
                                score += tiles[row][targetCol + 1].getValue();
                                tiles[row][col].setValue(0);
                                merged[row][targetCol + 1] = true; // Mark as merged
                                moved = true;
                            } else if (targetCol != col) {
                                // Move the tile
                                tiles[row][targetCol].setValue(tiles[row][col].getValue());
                                tiles[row][col].setValue(0);
                                moved = true;
                            }
                        }
                    }
                }
                break;
        }

        if (moved) {
            addRandomTile();
            repaint();
        }
    }


    private boolean mergeTile(int row, int col, int delta) {
        int targetRow = row + (delta == -1 ? -1 : (delta == 1 ? 1 : 0));
        int targetCol = col + (delta == -1 ? 0 : (delta == 1 ? 0 : (delta == -1 ? -1 : 1)));

        if (targetRow >= 0 && targetRow < SIZE && targetCol >= 0 && targetCol < SIZE) {
            if (tiles[targetRow][targetCol].getValue() == 0) {
                tiles[targetRow][targetCol].setValue(tiles[row][col].getValue());
                tiles[row][col].setValue(0);
                return true;
            } else if (tiles[targetRow][targetCol].getValue() == tiles[row][col].getValue()) {
                tiles[targetRow][targetCol].setValue(tiles[targetRow][targetCol].getValue() * 2);
                score += tiles[targetRow][targetCol].getValue();
                tiles[row][col].setValue(0);
                return true;
            }
        }
        return false;
    }

    private void addRandomTile() {
        Random random = new Random();
        int emptyCount = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.getValue() == 0) emptyCount++;
            }
        }
        if (emptyCount > 0) {
            int index = random.nextInt(emptyCount);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (tiles[i][j].getValue() == 0) {
                        if (index == 0) {
                            tiles[i][j].setValue(random.nextBoolean() ? 2 : 4);
                            return;
                        }
                        index--;
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(187, 173, 160));
        g.fillRect(0, 0, getWidth(), getHeight() - 100);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                g.setColor(getTileColor(tiles[i][j].getValue()));
                g.fillRoundRect(GAP + j * (TILE_SIZE + GAP), GAP + i * (TILE_SIZE + GAP), TILE_SIZE, TILE_SIZE, 15, 15);
                g.setColor(Color.BLACK);
                g.drawRoundRect(GAP + j * (TILE_SIZE + GAP), GAP + i * (TILE_SIZE + GAP), TILE_SIZE, TILE_SIZE, 15, 15);

                if (tiles[i][j].getValue() != 0) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 40)); // Increase font size and style
                    FontMetrics fm = g.getFontMetrics();
                    int x = GAP + j * (TILE_SIZE + GAP) + (TILE_SIZE - fm.stringWidth(String.valueOf(tiles[i][j].getValue()))) / 2;
                    int y = GAP + i * (TILE_SIZE + GAP) + (TILE_SIZE + fm.getAscent()) / 2 - 5; // Center the text
                    g.drawString(String.valueOf(tiles[i][j].getValue()), x, y);
                }
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24)); // Font for score
        g.drawString("Score: " + score, GAP, getHeight() - 70);
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 0: return new Color(204, 192, 179);
            case 2: return new Color(238, 228, 218);
            case 4: return new Color(237, 224, 200);
            case 8: return new Color(242, 177, 121);
            case 16: return new Color(245, 149, 99);
            case 32: return new Color(246, 124, 95);
            case 64: return new Color(246, 94, 59);
            case 128: return new Color(237, 207, 114);
            case 256: return new Color(237, 204, 97);
            case 512: return new Color(237, 200, 80);
            case 1024: return new Color(237, 197, 63);
            case 2048: return new Color(237, 194, 46);
            default: return new Color(0, 0, 0);
        }
    }
}
