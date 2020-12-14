package agh.cs.lab2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.*;

public class JSONParser
{
    private int width;
    private int height;
    private int jungleWidth;
    private int jungleHeight;
    private int firstAnimalsCount;
    private int maxAnimalsOnSquare;
    private int startEnergy;
    private int dailyEnergyCost;
    private int grassEnergyBonus;

    public JSONParser() {

        String root = new File("").getAbsolutePath();
        String fileName = root + "/src/agh/cs/lab2/settings.json";
        try
        {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONObject o = new JSONObject(content);
            this.width = o.getInt("width");
            this.height = o.getInt("height");
            this.jungleWidth = o.getInt("jungleWidth");
            this.jungleHeight = o.getInt("jungleHeight");
            this.firstAnimalsCount = o.getInt("firstAnimalsCount");
            this.maxAnimalsOnSquare = o.getInt("maxAnimalsOnSquare");
            this.startEnergy = o.getInt("startEnergy");
            this.dailyEnergyCost = o.getInt("dailyEnergyCost");
            this.grassEnergyBonus = o.getInt("grassEnergyBonus");
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }

    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getJungleWidth()
    {
        return this.jungleWidth;
    }

    public int getJungleHeight()
    {
        return this.jungleHeight;
    }

    public int getFirstAnimalsCount()
    {
        return this.firstAnimalsCount;
    }

    public int getMaxAnimalsOnSquare()
    {
        return this.maxAnimalsOnSquare;
    }

    public int getStartEnergy()
    {
        return this.startEnergy;
    }

    public int getDailyEnergyCost()
    {
        return this.dailyEnergyCost;
    }

    public int getGrassEnergyBonus()
    {
        return this.grassEnergyBonus;
    }
}
