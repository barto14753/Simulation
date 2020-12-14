package agh.cs.lab2;


public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        String toReturn = "(" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ")";
        return toReturn;

    }

    public boolean precedes(Vector2d other)
    {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other)
    {
        return this.x >- other.x && this.y <= other.y;
    }

    public Vector2d upperRight(Vector2d other)
    {
        if (this.x > other.x)
        {
            if (this.y > other.y)
            {
                return new Vector2d(this.x, this.y);
            }
            else
            {
                return new Vector2d(this.x, other.y);
            }
        }
        else
        {
            if (this.y > other.y)
            {
                return new Vector2d(other.x, this.y);
            }
            else
            {
                return new Vector2d(other.x, other.y);
            }
        }
    }

    public Vector2d lowerLeft(Vector2d other)
    {
        if (this.x < other.x)
        {
            if (this.y < other.y)
            {
                return new Vector2d(this.x, this.y);
            }
            else
            {
                return new Vector2d(this.x, other.y);
            }
        }
        else
        {
            if (this.y < other.y)
            {
                return new Vector2d(other.x, this.y);
            }
            else
            {
                return new Vector2d(other.x, other.y);
            }
        }
    }

    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other)
    {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other)
    {
        if (other.getClass() != Vector2d.class) return false;
        return (this.x == ((Vector2d) other).x && this.y == ((Vector2d) other).y);
    }

    public Vector2d opposite()
    {
        return new Vector2d((-1) * this.x, (-1) * this.y);
    }

    public Vector2d getBackward()
    {
        return new Vector2d((-1)*this.x, (-1)*this.y);

    }

    @Override
    public int hashCode()
    {
        int hash = 19;
        hash += this.x * 17;
        hash += this.y * 31;
        return hash;
    }

}
