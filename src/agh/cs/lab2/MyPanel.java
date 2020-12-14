package agh.cs.lab2;
import org.json.JSONException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.*;


// class which draw simulation board, actualize it and enable user to start, stop and export statisctic to file, and chose observable animal

public class MyPanel extends JPanel implements ActionListener, MouseListener
{
    private Teritory map;
    private TeritoryStats mapStats;
    private int startX=10;
    private int startY=350;
    private int width;
    private int height;

    private int squareWidth;
    private int squareHeight;
    private int padding = 1;


    private Timer timer;
    private int delay = 100;

    private Box box;
    private JPanel buttonsPanel;
    private StatsPanel statsPanel;

    private JButton startButton;
    private JButton stopButton;
    private JButton popularGenesButton;
    private JButton exportDataButton;

    private StatsExport statsExport;

    private boolean canShowPopularGenes = false;



    public MyPanel(Teritory map, int width, int height) {
        //setPreferredSize(new Dimension(400, 400));
        this.map = map;
        this.mapStats = this.map.getStats();
        this.box = Box.createVerticalBox();
        this.statsExport = new StatsExport(this.map.getStats());

        this.buttonsPanel = new JPanel();
        this.statsPanel = new StatsPanel(this.map);



        this.width = width;
        this.height = height;
        this.squareWidth = this.width/this.map.getWidth();
        this.squareHeight = this.height/this.map.getHeight();



        this.startButton = new JButton("Start");
        this.stopButton = new JButton("Stop");
        this.popularGenesButton = new JButton("Show Popular genes");
        this.exportDataButton = new JButton("Export Data");
        this.buttonsPanel.add(startButton);
        this.buttonsPanel.add(stopButton);
        this.buttonsPanel.add(popularGenesButton);
        this.buttonsPanel.add(exportDataButton);

        this.statsPanel.add(new StatsPanel(this.map));



        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.popularGenesButton.addActionListener(this);
        this.exportDataButton.addActionListener(this);
        this.addMouseListener((MouseListener) this);

        this.box.add(buttonsPanel);
        this.box.add(statsPanel);
        this.add(this.box);

        this.timer = new Timer(this.delay, this);

    }


    public void startSimulation()
    {
        timer.start();
    }

    public void stopSimulation()
    {
        this.timer.stop();
    }


    private void paintBoard(Graphics2D g2d) // function draw board
    {
        for (int i=0; i<this.map.getWidth(); i++)
        {
            for (int j=0; j<this.map.getHeight(); j++)
            {
                if (this.map.isJunglePosition(new Vector2d(i,j)))
                {
                    g2d.setColor(this.map.getEmptyJungleColor());
                }
                else
                {
                    g2d.setColor(this.map.getEmptyStepColor());
                }
                g2d.fillRect(this.startX+i*this.squareWidth, this.startY+j*this.squareHeight, this.squareWidth-padding, this.squareHeight-padding);
            }
        }
    }

    private void paintGrass(Graphics2D g2d) // function draw grasses
    {
        g2d.setColor(this.map.getGrassColor());
        for (Grass grass: this.map.getGrasses())
        {
            int x = this.startX+grass.getPosition().x * this.squareWidth;
            int y = this.startY + grass.getPosition().y * this.squareHeight;
            g2d.fillRect(x,y, this.squareWidth, this.squareHeight);
        }
    }

    private void paintAnimals(Graphics g2d) // function draw animals
    {
        for (Animal animal: this.map.getAnimals())
        {
            g2d.setColor(animal.getColor());
            int x = this.startX + animal.getPosition().x * this.squareWidth;
            int y = this.startY + (animal.getPosition().y) * this.squareHeight;
            g2d.fillOval(x,y,squareWidth, squareHeight);

        }

        if (this.canShowPopularGenes) // if user has decided to draw animals with most popular genes in diferent way
        {
            this.paintAnimalsWithPopularGenes(g2d);
        }
    }

    private void paintAnimalsWithPopularGenes(Graphics g2d) // animals which have popular genes have drawn a little black dot inside them
    {
        for (Animal animal: this.map.getAnimals())
        {
            if (this.mapStats.isPopularGenes(animal.getGenes()))
            {
                g2d.setColor(Color.BLACK);
                int x = this.startX + animal.getPosition().x * this.squareWidth;
                int y = this.startY + (animal.getPosition().y) * this.squareHeight;
                g2d.fillOval(x+(this.squareWidth/4),y+(this.squareHeight/4),squareWidth/2, squareHeight/2);
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) { // painting board with animals and grasses
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        this.paintBoard(g2d);
        this.paintGrass(g2d);
        this.paintAnimals(g2d);

    }

    public void updateStats()
    {
        this.statsPanel.updateStats();
    }

    @Override
    public void actionPerformed(ActionEvent e) { // servicing buttons and chosing observable animal
        if (e.getSource() == this.startButton)
        {
            this.startSimulation();
        }
        else if (e.getSource() == this.stopButton)
        {
            this.stopSimulation();
        }
        else if (e.getSource() == this.popularGenesButton)
        {
            this.canShowPopularGenes = !this.canShowPopularGenes;

            if (this.canShowPopularGenes)
            {
                this.popularGenesButton.setText("Unshow popular genes");
            }
            else
            {
                this.popularGenesButton.setText("Show popular genes");
            }
        }

        else if (e.getSource() == this.exportDataButton)
        {
            try
            {
                this.statsExport.exportStats();
            }
            catch (JSONException jsonException)
            {
                jsonException.printStackTrace();
            }
        }
        else
        {
            this.map.dailyRoutine();
            this.updateStats();
            this.repaint();
        }

    }

    private Vector2d getMapPosition(Point point) // getting postition on simulation board based on window position
    {
        int x = (point.x - this.startX)/this.squareWidth;
        int y = (point.y - this.startY)/this.squareHeight;
        return new Vector2d(x,y);
    }

    private Animal setObservableAnimal(Point point) // function return animal which was pointed on screen
    {
        Vector2d position = this.getMapPosition(point);
        System.out.print(point + " " + position + "\n");
        LinkedList<Animal> animals = this.map.getAnimalsOnPosition(position);
        if (animals != null && !animals.isEmpty())
        {
            this.statsPanel.setObservableAnimal(animals.getFirst());
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.setObservableAnimal(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}