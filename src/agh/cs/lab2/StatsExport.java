package agh.cs.lab2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


// class which allows to save statistic of a current simulation
public class StatsExport
{
    private TeritoryStats stats; // we gain inforamtion from this class
    private String fileName;
    private static FileWriter file;

    public StatsExport(TeritoryStats stats)
    {
        this.stats = stats;

        String root = new File("").getAbsolutePath();
        this.fileName = root + "/src/agh/cs/lab2/statistics.json";

    }

    public void exportStats() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("Animals Count AVG", this.stats.getAnimalsCountAVG());
        obj.put("Grasses Count AVG", this.stats.getGrassesCountAVG());
        obj.put("Animal Energy AVG", this.stats.getAverageAnimalEnergyAVG());
        obj.put("Animal Death Age AVG", this.stats.getAverageDeathAgeAVG());
        obj.put("Animal Children AVG", this.stats.getAverageAnimalChildrenCountAVG());


        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter(this.fileName);
            file.write(obj.toString());


        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
