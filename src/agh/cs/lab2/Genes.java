package agh.cs.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Genes
{
    private final int[] genes;
    private final int typesOfGenes;

    public Genes(int[] genes, int typesOfGenes)
    {
        this.genes = genes;
        this.typesOfGenes= typesOfGenes;

        this.repairGenes();
    }

    public Genes(int typeOfGenes, int sizeOfGenes) // this constructor is used during placing first animals, genes are random
    {
        this.genes = new int[sizeOfGenes];
        this.typesOfGenes = typeOfGenes;
        this.fillWithRandomGenes();
        this.repairGenes();

    }

    public Genes(Genes parent1, Genes parent2, int typesOfGenes) // this constructor is used during copulating
    {
        if (parent1.getTypesOfGenes() !=  parent2.getTypesOfGenes())
        {
            throw new IllegalArgumentException("Parents have got diffrent types of genes");
        }
        if (parent1.getSize() != parent2.getSize())
        {
            throw new IllegalArgumentException("Parents have diffrent genome sizes");
        }
        if (parent1.getSize()<3)
        {
            throw new IllegalArgumentException("Parents have so small genomes that they can't be splited");
        }

        this.typesOfGenes = typesOfGenes;
        this.genes = new int[parent1.getSize()]; // creating array of genes

        // setting random pivots (there must exactly three parts)
        int pivot1 = (int) (Math.random() * (parent1.getSize() - 2));
        int pivot2 = (int) (Math.random() * (parent1.getSize() - pivot1 - 1));


        // copying genes from parents based on pivots
        for (int i=0; i<=pivot1; i++)
        {
            this.genes[i] = parent1.getGene(i);
        }

        for (int i=0; i + pivot1 <= pivot2; i++)
        {
            this.genes[pivot1 + i] = parent2.getGene(pivot1 + i);
        }

        for (int i=0; i + pivot1 + pivot2 < parent1.getSize(); i++)
        {
            this.genes[pivot1 + pivot2 + 1] = parent2.getGene(pivot1 + pivot2 + 1);
        }

        this.repairGenes();
    }

    private void fillWithRandomGenes() // function fils genes table with random genes
    {
        for (int i=0; i<this.genes.length; i++)
        {
            this.genes[i] = (int) (Math.random() * this.getTypesOfGenes());
        }

    }

    public void repairGenes() // Modifying genes so they got each type of gene (including sorting)
    {
        if (this.genes.length < this.typesOfGenes)
        {
            throw new IllegalArgumentException("There amount of genes is less than amount of genes types");
        }

        // Creting array of arrays, in each n-array indexes of genes cells which contains n-gene
        List<ArrayList> indexInfo = new ArrayList<ArrayList>();

        // Adding to array n arrays for each type of genom
        for (int i=0; i<this.typesOfGenes; i++)
        {
            indexInfo.add(new ArrayList<Integer>());
        }

        // Containing array
        for (int i=0; i<this.genes.length; i++)
        {
            if (this.genes[i] >= this.typesOfGenes)
            {
                throw new IllegalArgumentException("Genome contains to high gene");
            }
            if (this.genes[i] < 0)
            {
                throw new IllegalArgumentException("Genome contains to negative gene");
            }


            indexInfo.get(this.genes[i]).add(i);
        }

        int empty = 0;

        // Counting how many genes is missing
        for (int i=0; i<indexInfo.size(); i++)
        {
            if (indexInfo.get(i).isEmpty())
            {
                empty++;
            }
        }

        // Creating array which contains free indexes
        List<Integer> free = new ArrayList<>();
        int i =0;


        // Going through array (possible few times) getting as many free index as empty genes
        while (free.size() < empty)
        {
            if (indexInfo.get(i).size() > 1)
            {
                free.add((Integer) indexInfo.get(i).get(0));
                indexInfo.get(i).remove(0);
            }
            i = (i+1) % indexInfo.size();
        }


        int j=0;
        for (i=0; i<indexInfo.size(); i++)
        {
            if (indexInfo.get(i).isEmpty())
            {
                this.genes[free.get(j)] = (Integer) i;
                j++;
            }
        }

        Arrays.sort(this.genes);
    }



    public int getSize()
    {
        return this.genes.length;
    }

    public int getTypesOfGenes()
    {
        return this.typesOfGenes;
    }

    public int[] getGenes()
    {
        return this.genes;
    }

    public int getGene(int index)
    {
        return this.genes[index];
    }

    public int getRandomGene()
    {
        int index = (int) (Math.random() * this.getSize());
        return this.getGene(index);
    }

    public boolean equals(Object obj)
    {
        if (obj.getClass() != this.getClass())
        {
            return false;
        }

        else
        {
            Genes gene = (Genes) obj;
            if (this.getTypesOfGenes() != gene.getTypesOfGenes() || this.getSize() != gene.getSize())
            {
                return false;
            }
            else
            {
                for (int i=0; i<this.getSize(); i++)
                {
                    if (this.getGene(i) != gene.getGene(i))
                    {
                        return false;
                    }
                }
                return true;
            }
        }


    }
    @Override
    public String toString()
    {
        String result = "";
        for (Integer gene: this.genes)
        {
            result = result + gene.toString();
        }
        return result;
    }
}
