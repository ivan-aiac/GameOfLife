package life;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Objects;

public class GameOfLife extends JFrame {

    private final JLabel generationLabel;
    private final JLabel aliveLabel;
    private final Universe universe;
    private final EvolutionThread evolutionThread;
    private final JToggleButton playToggleButton;
    private final JButton resetButton;
    private final CellPanel cellPanel;
    private final ImageIcon playIcon;
    private final ImageIcon pauseIcon;
    private final ImageIcon repeatIcon;

    public GameOfLife() {
        super("Game of Life");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        playIcon = new ImageIcon();
        pauseIcon = new ImageIcon();
        repeatIcon = new ImageIcon();

        playToggleButton = new JToggleButton();
        resetButton = new JButton();

        generationLabel = new JLabel();
        aliveLabel = new JLabel();

        universe = new Universe(128);
        cellPanel = new CellPanel(universe);
        evolutionThread = new EvolutionThread(this::updateUniverse, 100);

        initComponents();

        evolutionThread.start();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Control Panel
        JPanel panel = new JPanel(new GridBagLayout());

        //Load Button Icons
        playIcon.setImage(loadImage("play.png"));
        pauseIcon.setImage(loadImage("pause.png"));
        repeatIcon.setImage(loadImage("repeat.png"));

        //PlayToggleButton
        playToggleButton.setName("PlayToggleButton");
        playToggleButton.setIcon(playIcon);
        playToggleButton.setFocusPainted(false);
        playToggleButton.addItemListener(e -> {
            if (playToggleButton.isSelected()) {
                evolutionThread.playEvolution();
                playToggleButton.setIcon(pauseIcon);
            } else {
                evolutionThread.pauseEvolution();
                evolutionThread.interrupt();
                playToggleButton.setIcon(playIcon);
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.insets = new Insets(10,10,10,10);
        panel.add(playToggleButton, c);

        //ResetButton
        resetButton.setName("ResetButton");
        resetButton.setIcon(repeatIcon);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> {
            evolutionThread.interrupt();
            universe.createNewUniverse();
            cellPanel.repaint();
            generationLabel.setText(String.format("Generation #%d", universe.getCurrentGeneration()));
            aliveLabel.setText(String.format("Alive: %d", universe.getNumberOfAliveCells()));
        });
        c.gridx = 2;
        panel.add(resetButton, c);

        //Generation Label
        generationLabel.setName("GenerationLabel");
        generationLabel.setText(String.format("Generation #%d", universe.getCurrentGeneration()));
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.gridwidth = 4;
        panel.add(generationLabel, c);

        //Alive Label
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText(String.format("Alive: %d", universe.getNumberOfAliveCells()));
        c.gridy = 2;
        panel.add(aliveLabel, c);

        //Speed Slider
        JLabel speedLabel = new JLabel("Evolution Speed:");
        c.gridy = 3;
        panel.add(speedLabel, c);
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        table.put(1, new JLabel("0.1s"));
        table.put(5, new JLabel("0.5s"));
        table.put(10, new JLabel("1s"));
        speedSlider.setLabelTable(table);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(e -> {
            if(!speedSlider.getValueIsAdjusting()) {
                evolutionThread.setDelay(speedSlider.getValue() * 100);
                evolutionThread.interrupt();
            }
        });
        c.gridy = 4;
        panel.add(speedSlider, c);

        //Color Chooser
        JLabel chooserLabel = new JLabel("Cells color:");
        c.gridy = 5;
        panel.add(chooserLabel, c);

        JColorChooser colorChooser = new JColorChooser(Color.BLACK);
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.setChooserPanels(new AbstractColorChooserPanel[]{colorChooser.getChooserPanels()[0]});
        colorChooser.getSelectionModel().addChangeListener(e -> cellPanel.setCellColor(colorChooser.getColor()));
        c.gridy = 6;
        panel.add(colorChooser, c);

        //Control Panel
        c.gridy = 0;
        c.gridwidth = 1;
        add(panel, c);

        //Cell Panel
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 2;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        add(cellPanel, c);
        setVisible(true);
    }

    private void updateUniverse() {
        universe.nextGeneration();
        generationLabel.setText(String.format("Generation #%d", universe.getCurrentGeneration()));
        aliveLabel.setText(String.format("Alive: %d", universe.getNumberOfAliveCells()));
        cellPanel.repaint();
    }

    private Image loadImage(String imageName) {
        int size = 32;
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imageName)));
            return image.getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
