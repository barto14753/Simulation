package agh.cs.lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Simulation implements ActionListener {
    JFrame frame;
    JPanel board;
    JPanel board1;
    JPanel board2;
    Teritory map1;
    Teritory map2;

    private Timer timer;
    private int delay = 1;
    private JSONParser Parser;

    // class which get simulation parameters from JSON FILE and starts two separated simulation using class MyPanel
    public Simulation() {

        this.Parser = new JSONParser();

        this.map1 = new Teritory(this.Parser.getWidth(),
                this.Parser.getHeight(),
                this.Parser.getFirstAnimalsCount(),
                this.Parser.getMaxAnimalsOnSquare(),
                this.Parser.getJungleWidth(),
                this.Parser.getJungleHeight(),
                this.Parser.getStartEnergy(),
                this.Parser.getDailyEnergyCost(),
                this.Parser.getGrassEnergyBonus());

        this.map2 = new Teritory(this.Parser.getWidth(),
                this.Parser.getHeight(),
                this.Parser.getFirstAnimalsCount(),
                this.Parser.getMaxAnimalsOnSquare(),
                this.Parser.getJungleWidth(),
                this.Parser.getJungleHeight(),
                this.Parser.getStartEnergy(),
                this.Parser.getDailyEnergyCost(),
                this.Parser.getGrassEnergyBonus());


        this.frame = new JFrame("Simulation");
        this.board = new JPanel();
        this.board.setLayout(new GridLayout(1,2));
        this.board1 = new MyPanel(map1, 400, 400);
        this.board2 = new MyPanel(map2, 400, 400);
        this.board.add(board1);
        this.board.add(board2);
        this.frame.setPreferredSize(new Dimension(1000,800));
        this.frame.setResizable(false);
        this.frame.add(board);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);

        this.timer = new Timer(delay, this);
    }

    public void startSimulation()
    {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.board.repaint();

    }
}