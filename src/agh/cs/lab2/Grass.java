package agh.cs.lab2;

public class Grass implements IMapElement{
    public Vector2d position;
    public Teritory teritory;

    public Grass(Teritory teritory, Vector2d position)
    {
        this.teritory = teritory;
        this.position = position;
    }
    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
