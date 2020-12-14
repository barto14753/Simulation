package agh.cs.lab2;

import java.awt.*;
import java.util.*;

public class Teritory implements IWorldMap, IPositionChangeObserver
{
    private int epoch = 0;

    private final int width;
    private final int height;
    private final int maxElementsOnSquare;
    private final int firstAnimalsCount;

    //jungle
    private Vector2d jungleLeftBottomCorner;
    private Vector2d jungleRightTopCorner;
    private final int jungleWidth;
    private final int jungleHeight;


    //energy
    private final int startEnergy;
    private final int dailyEnergyCost;
    private final int grassEnergyBonus;

    //genes
    private final int typeOfGenes = 8;
    private final int sizeOfGenes = 32;

    //stats
    private TeritoryStats stats;


    //objects on teritory
    private Map<Vector2d, LinkedList<Animal>> animalsField = new HashMap<>(); // using Linkedlist to remove and add animals on same pos easily
    private Map<Vector2d, Grass> grassField = new HashMap<>();
    private Map<Genes, LinkedList<Animal>> genesTypesMap = new HashMap<>();

    private LinkedList<Animal> animals = new LinkedList<>();
    private LinkedList<Grass> grasses = new LinkedList<>();
    private LinkedList<Animal> deadAnimals = new LinkedList<>();

    private final AnimalsEnergyComperator energyComperator = new AnimalsEnergyComperator();

    public Teritory(int width, int height, int firstAnimalsCount, int maxElementsOnSquare,
                    int jungleWidth, int jungleHeight, int startEnergy, int dailyEnergyCost, int grassEnergyBonus)
    {
        this.stats = new TeritoryStats(this);
        this.width = width;
        this.height = height;
        this.firstAnimalsCount = firstAnimalsCount;
        this.maxElementsOnSquare = maxElementsOnSquare;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.startEnergy = startEnergy;
        this.dailyEnergyCost = dailyEnergyCost;
        this.grassEnergyBonus = grassEnergyBonus;

        if (firstAnimalsCount > jungleWidth*jungleHeight)
        {
            throw new IllegalArgumentException("Too many inital animals");
        }

        this.setJungle();
        this.putFirstAnimals();
    }

    private void putFirstAnimals()
    {
        // We put first animals in jungle
        for (int i=0; i<this.firstAnimalsCount; i++)
        {
            Vector2d position = this.getRandomJunglePosition();
            while (this.animalsField.get(position) != null)
            {
                position = this.getRandomJunglePosition();
            }


            Genes genes = new Genes(this.typeOfGenes, this.sizeOfGenes);
            Animal animal = new Animal(this, null, null, position, MapDirection.NORTH, genes, this.startEnergy);
            this.place(animal);
        }
    }

    private void setJungle() // setting jungle borders (left-bottom and right-top corners)
    {
        int widthGap = (this.width - this.jungleWidth) / 2;
        int heightGap = (this.height - this.jungleHeight) / 2;


        this.jungleLeftBottomCorner = new Vector2d(widthGap,heightGap);
        this.jungleRightTopCorner = new Vector2d(widthGap + jungleWidth, heightGap + jungleHeight);


    }

    public Vector2d repairPosition(Vector2d position) // function repairs animal if it cross border
    {
        int x = position.x % this.width;
        int y = position.y % this.height;

        if (x < 0)
        {
            x = this.width + x;
        }
        if (y < 0)
        {
            y = this.height + y;
        }


        return new Vector2d(x,y);

    }

    @Override
    //can be placed if there is not max animals on square
    public boolean canMoveTo(Vector2d position) {

        LinkedList<Animal> animalsOnPos = this.animalsField.get(this.repairPosition(position));

        return animalsOnPos == null || animalsOnPos.size() < this.maxElementsOnSquare;
    }

    @Override
    // placing animal on square with updating animalsField and genesField
    public boolean place(Animal animal)
    {
        this.animals.push(animal);
        this.addNewAnimalGenes(animal);
        LinkedList<Animal> square = this.animalsField.get(animal.getPosition());

        if (square ==  null) // in case if sqaure hasn't been visited yet
        {
            LinkedList<Animal> newL = new LinkedList<>();
            newL.push(animal);
            this.animalsField.put(animal.getPosition(), newL);
        }
        else
        {
            square.push(animal);
        }
        return true;
    }

    // grass can be placed only if square is empty (no animals and no grasses)
    public boolean canPlaceGrass(Vector2d position)
    {
        return !this.isOccupied(position);
    }


    // function returns first free step square
    public Vector2d existingFreeStepSquareForGrass()
    {
        for (int i=0; i<this.width; i++)
        {
            for (int j=0; j<this.height; j++)
            {
                Vector2d position = new Vector2d(i,j);
                if (!this.isJunglePosition(position))
                {
                    if(canPlaceGrass(position)) return position;
                }
            }
        }
        return null;
    }

    // function returns first free jungle square
    public Vector2d existingFreeJungleSquareForGrass()
    {
        for (int i=0; i<this.jungleWidth; i++)
        {
            for (int j=0; j<this.jungleHeight; j++)
            {
                int x = this.jungleLeftBottomCorner.x + i;
                int y = this.jungleLeftBottomCorner.y + j;
                Vector2d position = new Vector2d(x,y);

                if (this.canPlaceGrass(position))
                {
                    return position;
                }
            }
        }
        return null;
    }

    // new born child can be placed only on square with no animals
    public boolean canPlaceNewBorn(Vector2d position)
    {
        if (this.animalsField.get(position) == null)
        {
            return true;
        }
        return this.animalsField.get(position).isEmpty();
    }


    private void placeGrass(Grass grass)
    {
        this.grassField.put(grass.getPosition(), grass);
        this.grasses.push(grass);
    }

    // updating info about genes after animal's birth
    private void addNewAnimalGenes(Animal animal)
    {
        LinkedList<Animal> animalsList = this.getAnimalsWithGenes(animal.getGenes());
        if (animalsList == null)
        {
            LinkedList<Animal> newAnimalsList = new LinkedList<Animal>();
            newAnimalsList.push(animal);
            this.genesTypesMap.put(animal.getGenes(), newAnimalsList);
        }
        else
        {
            animalsList.push(animal);
        }
    }

    // we only care about living animals genes
    private void removeDeadAnimalGenes(Animal animal)
    {
        LinkedList<Animal> animalsList = this.getAnimalsWithGenes(animal.getGenes());
        animalsList.remove(animal);
    }

    // removing animal from animalsField and adding to deadAnimals
    public void removeDeadAnimal(Animal animal)
    {
        this.deadAnimals.push(animal);
        this.removeDeadAnimalGenes(animal);

        LinkedList<Animal> squareBasedOnPosition = this.animalsField.get(animal.getPosition());
        squareBasedOnPosition.remove(animal);

        this.animals.remove(animal);
    }


    public void removeAnimal(Animal animal) // removing animal because of position changed (not death)
    {
        LinkedList<Animal> square = this.animalsField.get(animal.getPosition());
        square.remove(animal);
    }

    public void removeGrass(Grass grass) // we need only pos because there can be only one grass on pos
    {
        this.grassField.remove(grass.getPosition());
        this.grasses.remove(grass);
    }

    public boolean isJunglePosition(Vector2d position)
    {
        int x = position.x - jungleLeftBottomCorner.x;
        int y = position.y - jungleLeftBottomCorner.y ;
        return (x >= 0 && x < this.jungleWidth && y >= 0 && y < this.jungleHeight);
    }

    public Vector2d getRandomPosition()
    {
        int x = (int) (Math.random() * this.width);
        int y = (int) (Math.random() * this.height);
        return new Vector2d(x,y);
    }

    public Vector2d getRandomJunglePosition()
    {
        int x = (int) (Math.random() * this.jungleWidth) + this.jungleLeftBottomCorner.x;
        int y = this.jungleRightTopCorner.y - (int) (Math.random() * this.jungleHeight)-1;
        return new Vector2d(x,y);

    }

    // function returns position where new grass can be placed in jungle
    public Vector2d getNewJungleGrassPosition()
    {
        int attemptCount = 0; // if looking for new position last too long, function check if such place exists
        while (true) // chcecking random positions
        {
            Vector2d possiblePosition = this.getRandomJunglePosition();
            if (canPlaceGrass(possiblePosition))
            {
                return possiblePosition;
            }

            if (attemptCount == this.jungleWidth * this.jungleHeight)
            {
                return this.existingFreeJungleSquareForGrass();
            }
            attemptCount++;
        }
    }

    // function returns position where new grass can be placed in stepe
    public Vector2d getNewStepGrassPosition()
    {
        int attemptCount = 0; // if looking for new position last too long, function check if such place exists
        while (true)
        {
            Vector2d possiblePosition = this.getRandomPosition();
            if (!this.isJunglePosition(possiblePosition) && this.canPlaceGrass(possiblePosition))
            {
                return possiblePosition;
            }

            if (attemptCount == this.width * this.height)
            {
                return this.existingFreeStepSquareForGrass();
            }
            attemptCount++;
        }
    }

    private void growGrass() // growing grass in jungle and on step
    {
        Vector2d jungleGrassPosition = this.getNewJungleGrassPosition();
        Vector2d stepGrassPosition = this.getNewStepGrassPosition();

        if (jungleGrassPosition != null)
        {
            Grass jungleGrass = new Grass(this, jungleGrassPosition);
            this.placeGrass(jungleGrass);
        }
        if (stepGrassPosition != null)
        {
            Grass stepGrass = new Grass(this, stepGrassPosition);
            this.placeGrass(stepGrass);
        }


    }

    // function adding animals bonus for eating grass
    private void eatGrass(Animal animal, int eatBonus)
    {
        animal.eatGrass(eatBonus);
    }



    // getting animals which are on given grass (function used for eating feature)
    public LinkedList<Animal> getAnimalsOnGrassPosition(Grass grass)
    {
        LinkedList<Animal> animalsList = this.animalsField.get(grass.getPosition());

        if (animalsList == null || animalsList.size() == 0) // there can be no animals on square
        {
            return null;
        }
        else
        {
            animalsList.sort(this.energyComperator); // sorting animals, becasue strongest animals can eat

            LinkedList<Animal> animalsOnGrass = new LinkedList<>(); // init list for strongest animals

            for (int i=0; i<animalsList.size(); i++)
            {
                if (animalsList.get(i).getEnergy() == animalsList.getFirst().getEnergy()) // checking if next animal is same strong otherwise we can return list
                {
                    animalsOnGrass.push(animalsList.get(i));
                }
                else
                {
                    return animalsOnGrass;
                }
            }
            return animalsOnGrass;
        }
    }

    private void eating()
    {
        for (int i=0; i<this.grasses.size(); i++) // going through all grasses
        {
            Grass grass = this.grasses.get(i);
            LinkedList<Animal> animalsOnGrass = this.getAnimalsOnGrassPosition(grass); // getting animals which can eat

            if (animalsOnGrass != null)
            {
                int energyForOneAnimal = this.grassEnergyBonus / animalsOnGrass.size(); // calculate bonus for each animal

                for (Animal animal : animalsOnGrass) {
                    this.eatGrass(animal, energyForOneAnimal); // giving bonus for each animal
                }
                this.removeGrass(grass); // removing eaten grass
            }
        }
    }

    // animals lose energy each day
    private void reducingEnergy()
    {
        for (int i =0; i<this.animals.size(); i++)
        {
            Animal animal = this.animals.get(i);
            animal.reduceEnergy(this.dailyEnergyCost);
        }
    }

    // function returns legal position for new born animal
    public Vector2d getPositionForNewBorn(Vector2d parentsPosition)
    {
        Vector2d childPosition = new Vector2d(0,0);
        for (MapDirection direction: MapDirection.values()) // first loooking for nearby position
        {
            childPosition = parentsPosition.add(direction.unit());
            childPosition = this.repairPosition(childPosition);

            if (this.canPlaceNewBorn(childPosition))
            {
                return childPosition;
            }
        }

        // if all nearby position is taken we put it on random taken nearby position
        int random = (int) (Math.random() * 8);
        MapDirection direction = MapDirection.NORTH;

        for (int i=0; i<random; i++)
        {
            direction = direction.next();
        }

        childPosition = parentsPosition.add(direction.unit());
        return this.repairPosition(childPosition);
    }


    private void copulatingOnSquare(LinkedList<Animal> animalsList)
    {
        animalsList.sort(this.energyComperator); // sorting desceding, getting 'strongest' animals

        if (animalsList.size() < 2)
        {
            throw new IllegalArgumentException("There is not enough animals to copulate on square");
        }
        Animal animal1 = animalsList.get(0);
        Animal animal2 = animalsList.get(1);

        Animal child = animal1.copulate(animal2);
        this.place(child);

    }

    private void copulating()
    {
        for (int i=0; i<this.width; i++) // going through all sqaures
        {
            for (int j=0; j<this.height; j++)
            {
                Vector2d position = new Vector2d(i,j);
                LinkedList<Animal> animalsList = this.animalsField.get(position);
                if (animalsList != null && animalsList.size() > 1)
                {
                    this.copulatingOnSquare(animalsList);
                }
            }
        }
    }


    private void movingAnimals() // calling each animal to make move
    {
        for (int i=0; i<this.animals.size(); i++)
        {
            animals.get(i).move();
        }
    }

    private void ageingAnimals() // calling each animal to increase its age
    {
        for (int i=0; i<this.animals.size(); i++)
        {
            animals.get(i).gettingOld();
        }
    }

    public void dailyRoutine()
    {
        this.epoch++;
        this.reducingEnergy();
        this.ageingAnimals();
        this.movingAnimals();
        this.eating();
        this.copulating();
        this.growGrass();
        this.stats.updateAllStats();

    }


    @Override
    public void run(MoveDirection[] directions) {
        return;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (this.objectAt(position) != null);
    }

    @Override
    public Object objectAt(Vector2d position) {
        LinkedList<Animal> square = this.animalsField.get(position);

        if (square == null || square.size() == 0)
        {
            return this.grassField.get(position); // checking if there is a grass on position
        }
        else
        {
            return square.getFirst();
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal)
    {
        this.removeAnimal(animal);

        LinkedList<Animal> square = this.animalsField.get(newPosition);
        if (square == null)
        {
            LinkedList<Animal> newL = new LinkedList<>();
            newL.push(animal);
            this.animalsField.put(newPosition, newL);
        }
        else
        {
            square.push(animal);
        }

    }

    public Color getEmptyStepColor()
    {
        return new Color(243,255,161);
    }

    public Color getEmptyJungleColor()
    {
        return new Color(116,247,89);
    }

    public Color getGrassColor() {return new Color(0,212,7);}

    public int getEpoch()
    {
        return this.epoch;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }
    public LinkedList<Animal> getAnimals()
    {
        return this.animals;
    }

    public LinkedList<Grass> getGrasses()
    {
        return this.grasses;
    }

    public TeritoryStats getStats()
    {
        return this.stats;
    }

    public LinkedList<Animal> getDeadAnimals()
    {
        return this.deadAnimals;
    }

    public LinkedList<Animal> getAnimalsOnPosition(Vector2d position)
    {
        return this.animalsField.get(position);
    }

    public LinkedList<Animal> getAnimalsWithGenes(Genes genes)
    {
        return this.genesTypesMap.get(genes);
    }

    public Map<Genes, LinkedList<Animal>> getGenesTypeMap()
    {
        return this.genesTypesMap;
    }
}
