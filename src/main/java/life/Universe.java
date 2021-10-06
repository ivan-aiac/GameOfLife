package life;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Universe {

    private boolean[][] cells;
    private int universeSize;
    private int currentGeneration;
    private boolean lastGeneration;

    public Universe(int universeSize) {
        this.universeSize = universeSize;
        createNewUniverse();
    }

    public void createNewUniverse() {
        cells = new boolean[universeSize][universeSize];
        currentGeneration = 1;
        for (int i = 0; i < universeSize; i++) {
            for (int j = 0; j < universeSize; j++) {
                cells[i][j] = ThreadLocalRandom.current().nextBoolean();
            }
        }
    }

    public void nextGeneration() {
        if (lastGeneration) {
            return;
        }
        boolean[][] nextGeneration = new boolean[universeSize][universeSize];
        for (int i = 0; i < universeSize; i++) {
            for (int j = 0; j < universeSize; j++) {
                nextGeneration[i][j] = cellEvolution(i, j);
            }
        }
        currentGeneration++;
        compareCellsWith(nextGeneration);
        cells = nextGeneration;
    }

    public void setUniverseSize(int universeSize) {
        this.universeSize = universeSize;
    }

    public int getUniverseSize() {
        return universeSize;
    }

    public long getNumberOfAliveCells() {
        return Arrays.stream(cells)
                .flatMap(c -> IntStream.range(0, c.length).mapToObj(i -> c[i]))
                .filter(c -> c)
                .count();
    }

    public boolean[][] getCells() {
        return cells;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    private boolean cellEvolution(int row, int col) {
        int aliveNeighbors = numberOfAliveNeighbors(row, col);
        return cells[row][col] ? aliveNeighbors == 2 || aliveNeighbors == 3 : aliveNeighbors == 3;
    }

    private int numberOfAliveNeighbors(int row, int col) {
        int count = 0;
        for (Neighbor neighbor : Neighbor.values()) {
            count += findNeighbor(neighbor, row, col) ? 1 : 0;
        }
        return count;
    }

    private boolean findNeighbor(Neighbor neighbor, int row, int col) {
        row = Math.floorMod(row + neighbor.getRow(), universeSize);
        col = Math.floorMod(col + neighbor.getCol(), universeSize);
        return cells[row][col];
    }

    private void compareCellsWith(boolean[][] nextGeneration) {
        lastGeneration = true;
        for (int i = 0; i < nextGeneration.length; i++) {
            for (int j = 0; j < nextGeneration.length; j++) {
                lastGeneration = cells[i][j] == nextGeneration[i][j];
                if (!lastGeneration) {
                    return;
                }
            }
        }
    }
}
