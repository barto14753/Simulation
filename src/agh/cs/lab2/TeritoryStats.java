package agh.cs.lab2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

// class contains and actualize statistic of given in constructor teritory
public class TeritoryStats
{
    private Teritory teritory;
    private int animalsCount;
    private int grassesCount;
    private int averageAnimalEnergy;
    private int averageDeathAge;
    private int averageAnimalChildrenCount;
    private LinkedList<Genes> popularGenes;
    private LinkedList<Animal> animalsWithPopularGenes;
    private int animalsWithPopularGenesCount = 0;



    //average stats from ALL EPOCHS for exporting to file
    private int animalsCountAVG = 0;
    private int grassesCountAVG = 0;
    private int averageAnimalEnergyAVG = 0;
    private int averageDeathAgeAVG = 0;
    private int averageAnimalChildrenCountAVG = 0;



    public TeritoryStats(Teritory teritory)
    {
        this.teritory = teritory;
    }


    // function which gets previous avg stats and actual stats and returns actual avg stats based on epoch
    private int getAverageFromAllEpoch(int previousStat, int statisticNumber)
    {
        return (previousStat * (this.teritory.getEpoch()-1) + statisticNumber)/ this.teritory.getEpoch();
    }
    private void updateAnimalsCount()
    {
        this.animalsCount = this.teritory.getAnimals().size();
        this.animalsCountAVG = this.getAverageFromAllEpoch(this.animalsCountAVG, this.animalsCount);
    }

    private void updateGrassesCount()
    {
        this.grassesCount = this.teritory.getGrasses().size();
        this.grassesCountAVG = this.getAverageFromAllEpoch(this.grassesCountAVG, this.grassesCount);
    }

    private void updateAverageAnimalEnergy()
    {
        int sumOfEnergy = 0;
        LinkedList<Animal>  animalsList = this.teritory.getAnimals();

        if (animalsList.isEmpty())
        {
            this.averageAnimalEnergy = 0;
            return;
        }

        for (Animal animal: animalsList)
        {
            sumOfEnergy += animal.getEnergy();
        }

        this.averageAnimalEnergy = sumOfEnergy / animalsList.size();
        this.averageAnimalEnergyAVG = this.getAverageFromAllEpoch(this.averageAnimalEnergyAVG, this.averageAnimalEnergy);


    }

    private void updateAverageDeathAge()
    {
        int sumOfDeathAges = 0;
        LinkedList<Animal>  deadAnimalsList = this.teritory.getDeadAnimals();

        if (deadAnimalsList.isEmpty())
        {
            this.averageDeathAge = 0;
            return;
        }

        for (Animal animal: deadAnimalsList)
        {
            sumOfDeathAges += animal.getAge();
        }

        this.averageDeathAge = sumOfDeathAges / deadAnimalsList.size();
        this.averageDeathAgeAVG = this.getAverageFromAllEpoch(this.averageDeathAgeAVG, this.averageDeathAge);


    }

    private void updateAverageAnimalChildrenCount()
    {
        int sumOfChildren = 0;
        LinkedList<Animal>  animalsList = this.teritory.getAnimals();

        if (animalsList.isEmpty())
        {
            this.averageAnimalChildrenCount = 0;
            return;
        }

        for (Animal animal: animalsList)
        {
            sumOfChildren += animal.getChildrenCount();
        }

        this.averageAnimalChildrenCount =  sumOfChildren / animalsList.size();
        this.averageAnimalChildrenCountAVG = this.getAverageFromAllEpoch(this.averageAnimalChildrenCountAVG, this.averageAnimalChildrenCount);

    }

    protected void updateAllStats()
    {
        this.updateAverageAnimalChildrenCount();
        this.updateAnimalsCount();
        this.updateAverageAnimalEnergy();
        this.updateAverageDeathAge();
        this.updateGrassesCount();
        this.updateMostPopularGenes();
    }

    private void updateMostPopularGenes()
    {
        Map<Genes, LinkedList<Animal>> genesTypeMap = this.teritory.getGenesTypeMap();
        this.popularGenes = new LinkedList<Genes>();
        for (LinkedList<Animal> animalsList: genesTypeMap.values())
        {
            if (animalsList.size() > this.animalsWithPopularGenesCount)
            {
                this.popularGenes = new LinkedList<Genes>();
                this.popularGenes.add(animalsList.getFirst().getGenes());

                this.animalsWithPopularGenes = new LinkedList<Animal>();
                this.animalsWithPopularGenes.addAll(animalsList);

                this.animalsWithPopularGenesCount = animalsList.size();
            }
            else if (animalsList.size() > 0 && animalsList.size() == this.animalsWithPopularGenesCount)
            {
                this.popularGenes.add(animalsList.getFirst().getGenes());
                this.animalsWithPopularGenes.addAll(animalsList);
            }
        }

    }

    public boolean isPopularGenes(Genes genes)
    {
        if (this.popularGenes == null)
        {
            return false;
        }
        for (Genes popularGene: this.popularGenes)
        {
            if (genes.equals(popularGene))
            {
                return true;
            }
        }
        return false;
    }

    public int getAverageAnimalEnergy()
    {
        return this.averageAnimalEnergy;
    }

    public int getAnimalsCount()
    {
        return this.animalsCount;
    }

    public int getGrassesCount()
    {
        return this.grassesCount;
    }

    public int getAverageDeathAge()
    {
        return this.averageDeathAge;
    }

    public int getAverageAnimalChildrenCount()
    {
        return this.averageAnimalChildrenCount;
    }

    public LinkedList<Genes> getMostPopularGenes()
    {
        return this.popularGenes;
    }

    public int getAnimalsWithPopularGenesCount()
    {
        return this.animalsWithPopularGenesCount;
    }

    public LinkedList<Animal> getAnimalsWithPopularGenes()
    {
        return this.animalsWithPopularGenes;
    }

    public int getAnimalsCountAVG()
    {
        return this.animalsCountAVG;
    }

    public int getGrassesCountAVG()
    {
        return this.grassesCountAVG;
    }

    public int getAverageAnimalEnergyAVG()
    {
        return this.averageAnimalEnergyAVG;
    }

    public int getAverageDeathAgeAVG()
    {
        return this.averageDeathAgeAVG;
    }

    public int getAverageAnimalChildrenCountAVG()
    {
        return this.averageAnimalChildrenCountAVG;
    }

}
