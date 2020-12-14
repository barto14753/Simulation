package agh.cs.lab2;

import java.util.Map;

public enum MapDirection
{
    NORTH, NORTHEAST, NORTHWEST,
    EAST,
    SOUTH, SOUTHEAST, SOUTHWEST,
    WEST;

    public MapDirection next()
    {
        switch (this)
        {
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case NORTHWEST: return NORTH;
            default: return null;
        }
    }

    public Vector2d unit() // returns Vector2d which represents position change after moving in given MapDirection
    {
        switch (this)
        {
            case NORTH: return new Vector2d(0, -1);
            case NORTHEAST: return new Vector2d(1, -1);
            case EAST: return new Vector2d(1,0);
            case SOUTHEAST: return new Vector2d(1, 1);
            case SOUTH: return new Vector2d(0,1);
            case SOUTHWEST: return new Vector2d(-1,1);
            case WEST: return new Vector2d(-1,0);
            case NORTHWEST: return new Vector2d(-1, -1);
            default: return null;
        }
    }

    public Vector2d forward()
    {
        return this.unit();
    } // returns position change Vector2d after moving forward in given direction

    public Vector2d backward()
    {
        return this.unit().getBackward();
    } // returns position change Vector2d after moving backward in given direction

}
