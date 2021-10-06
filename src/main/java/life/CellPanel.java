package life;

import javax.swing.*;
import java.awt.*;

public class CellPanel extends JPanel {

    private final Universe universe;
    private Color cellColor;

    public CellPanel(Universe universe) {
        super();
        this.universe = universe;
        cellColor = Color.BLACK;
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        int posX;
        int posY = 0;
        int xSize = getWidth() / universe.getUniverseSize();
        int ySize = getHeight() / universe.getUniverseSize();
        for (boolean[] row : universe.getCells()) {
            posX = 0;
            graphics.setColor(cellColor);
            for (boolean cell : row) {
                if (cell) {
                    graphics.fillRect(posX, posY, xSize, ySize);
                }
                posX += xSize;
            }
            posY += ySize;
        }
    }
}
