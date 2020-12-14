package agh.cs.lab2;

import java.util.Comparator;

public class AnimalsEnergyComperator implements Comparator<Animal>
{

    @Override
    public int compare(Animal animal1, Animal animal2) {
        return animal2.getEnergy() - animal1.getEnergy();
    }
}
