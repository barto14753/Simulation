package agh.cs.lab2;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Animal implements IMapElement
{
    private Vector2d position;
    private Teritory teritory;
    private Genes genes;
    private int currentGene;
    private MapDirection direction;
    private List<IPositionChangeObserver> observers = new LinkedList<IPositionChangeObserver>();

    //
    private int energy;
    private int startEnergy;
    private boolean isAlive = true;
    private int deathEpoch = 0;
    private int childrenCount = 0;
    private int age=0;

    //counting descendants
    private Animal mother;
    private Animal father;
    private int descendantsCount = 0;
    private Animal lastDescandt;



    public Animal(Teritory map, Animal mother, Animal father, Vector2d position, MapDirection direction, Genes genes, int startEnergy)
    {
        this.teritory = map;
        this.mother = mother;
        this.father = father;
        this.position = position;
        this.direction = direction;
        this.genes = genes;
        this.energy = startEnergy;
        this.startEnergy = startEnergy;
        this.updateGene();
        this.addObserver(map);
    }

    // function which updates its gene before rotating and moving after each day
    public void updateGene()
    {
        this.currentGene = this.genes.getRandomGene();
    }


    // recursive function which updates descendants stats for each family member after new animal birth
    public void informFamilyAboutNewBorn(Animal newBorn)
    {
        if (this.lastDescandt != newBorn) // this ensure that this function won't be called twice for same birth
        {
            this.descendantsCount++;
            this.lastDescandt = newBorn;
            if (this.mother != null)
            {
                this.mother.informFamilyAboutNewBorn(newBorn);
            }
            if (this.father != null)
            {
                this.father.informFamilyAboutNewBorn(newBorn);
            }

        }
    }

    private void rotate() // change animal direction based on actual gene
    {
        for (int i=0; i<this.currentGene; i++)
        {
            this.direction = this.direction.next();
        }
    }


    private Vector2d nextPlaceToMove()
    {
        Vector2d newPos = this.position.add(this.direction.forward());
        return this.teritory.repairPosition(newPos);
    }

    // daily move which contain updating genes, rotation, moving (if its legal) and informing observators about position change
    public void move()
    {
        this.updateGene();
        this.rotate();
        Vector2d newPosition = this.nextPlaceToMove();
        if (this.teritory.canMoveTo(newPosition))
        {
            Vector2d oldPosition = this.getPosition();
            this.positionChanged(oldPosition, newPosition, this);
            this.position = newPosition;


        }
    }

    //observators

    private void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPos, Vector2d newPos, Animal animal) {
        for (IPositionChangeObserver o : observers) {
            o.positionChanged(oldPos, newPos, animal);
        }
    }


    @Override
    public Vector2d getPosition() {
        return this.position;
    }


    public void eatGrass(int energy)
    {
        this.energy = this.energy + energy;
    }

    public void reduceEnergy(int energy)
    {
        this.energy = this.energy - energy;

        if (this.energy <= 0) // checking if animal is still alive
        {
            this.isAlive = false;
            this.deathEpoch = this.teritory.getEpoch();
            this.teritory.removeDeadAnimal(this); // actualizing animal's teritory
        }
    }

    public Animal copulate(Animal partner)
    {
        if  (this.genes.getTypesOfGenes() != partner.genes.getTypesOfGenes())
        {
            throw new IllegalArgumentException("Two animals can't copulate, because they have got diffrent types of genes");

        }

        Genes childGenes = new Genes(this.genes, partner.genes, this.genes.getTypesOfGenes()); // creating genes for newborn
        Vector2d childPosition = this.teritory.getPositionForNewBorn(this.position); // getting position for new born

        int childStartEnergy = this.energy/4 + partner.energy/4; // calculating child energy and taking this energy from parents
        this.reduceEnergy(this.energy/4);
        partner.reduceEnergy(partner.getEnergy()/4);

        Animal child = new Animal(this.teritory, this, partner, childPosition, MapDirection.NORTH, childGenes, childStartEnergy); // creating newborn

        partner.childrenCount++;
        this.childrenCount++;

        partner.informFamilyAboutNewBorn(child); // informing both families
        this.informFamilyAboutNewBorn(child);

        return child;
    }

    public void gettingOld()
    {
        this.age++;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public int getDeathEpoch()
    {
        return this.deathEpoch;
    }

    public int getAge()
    {
        return this.age;
    }

    public boolean isAlive()
    {
        return this.isAlive;
    }

    public int getChildrenCount()
    {
        return this.childrenCount;
    }

    public int getDescendantsCount()
    {
        return this.descendantsCount;
    }
    public Genes getGenes()
    {
        return this.genes;
    }

    public Color getColor()
    {
        float energyChange = (float)this.energy/this.startEnergy;
        if (energyChange < 0.5) return new Color(255, 48, 48);
        else if(energyChange<1) return new Color(255, 0, 86);
        else if(energyChange<1.5) return new Color(255, 218, 0);
        else return new Color(0, 247, 255);


    }









}
