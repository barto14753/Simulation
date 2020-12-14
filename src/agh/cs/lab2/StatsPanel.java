package agh.cs.lab2;

import javax.swing.*;
import java.awt.*;


// class which shows statistics of simulation and observable animal
public class StatsPanel extends JPanel
{
    private Teritory map;
    private TeritoryStats stats;
    private Animal observableAnimal;

    private JLabel epochsLabel;
    private JLabel animalsCountLabel;
    private JLabel grassesCountLabel;
    private JLabel popularGenesLabel;
    private JLabel othersGenesLabel;
    private JLabel averageEnergyLabel;
    private JLabel averageDeathAgeLabel;
    private JLabel averageAnimalChildrenLabel;
    private JLabel animalStatsTitle;
    private JLabel animalChildrenCountLabel;
    private JLabel animalDescendantsCountLabel;
    private JLabel animalDeathEpochLabel;
    private Box box;

    public StatsPanel(Teritory map)
    {
        this.map = map;
        this.stats = this.map.getStats();
        this.observableAnimal = null;
        this.box = Box.createVerticalBox();
        this.epochsLabel = new JLabel();
        this.animalsCountLabel = new JLabel();
        this.grassesCountLabel = new JLabel();
        this.popularGenesLabel = new JLabel();
        this.othersGenesLabel = new JLabel();
        this.averageEnergyLabel = new JLabel();
        this.averageDeathAgeLabel = new JLabel();
        this.averageAnimalChildrenLabel = new JLabel();
        this.animalStatsTitle = new JLabel();
        this.animalChildrenCountLabel = new JLabel();
        this.animalDescendantsCountLabel = new JLabel();
        this.animalDeathEpochLabel = new JLabel();

        this.box.add(this.epochsLabel);
        this.box.add(this.animalsCountLabel);
        this.box.add(this.grassesCountLabel);
        this.box.add(this.popularGenesLabel);
        this.box.add(this.othersGenesLabel);
        this.box.add(this.averageEnergyLabel);
        this.box.add(this.averageDeathAgeLabel);
        this.box.add(this.averageAnimalChildrenLabel);
        this.box.add(this.animalStatsTitle);
        this.box.add(this.animalChildrenCountLabel);
        this.box.add(this.animalDescendantsCountLabel);
        this.box.add(this.animalDeathEpochLabel);

        this.add(box);
    }

    public void updateStats()
    {
        this.epochsLabel.setText("Epoch:" + this.map.getEpoch());
        this.animalsCountLabel.setText("Animals Count: " + this.stats.getAnimalsCount());
        this.grassesCountLabel.setText("Grasses Count: " + this.stats.getGrassesCount());
        this.popularGenesLabel.setText("Popular Genee: " + this.stats.getMostPopularGenes().getFirst());
        this.othersGenesLabel.setText("Had by " + this.stats.getAnimalsWithPopularGenesCount() + " animal/s | There are " + this.stats.getMostPopularGenes().size() + " same popular genes");
        this.averageEnergyLabel.setText("Average Animal Energy: " + this.stats.getAverageAnimalEnergy());
        this.averageDeathAgeLabel.setText("Average Death Age: " + this.stats.getAverageDeathAge());
        this.averageAnimalChildrenLabel.setText("Average Animal Children: " + this.stats.getAverageAnimalChildrenCount());
        this.animalStatsTitle.setText("--Observable animal--");

        if (observableAnimal != null) // this labels are shown if user has chosen animal to observe
        {
            this.animalChildrenCountLabel.setText("Children: " + this.observableAnimal.getChildrenCount());
            this.animalDescendantsCountLabel.setText("Descendants: " + this.observableAnimal.getDescendantsCount());
            this.animalDeathEpochLabel.setText("Death epoch: " + this.observableAnimal.getDeathEpoch());
        }
        else
        {
            this.animalChildrenCountLabel.setText("");
            this.animalDeathEpochLabel.setText("");
        }
    }

    public void setObservableAnimal(Animal animal)
    {
        this.observableAnimal = animal;
        this.updateStats();
    }
}
