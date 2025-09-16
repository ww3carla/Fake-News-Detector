package fakenewsdetector.data;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static class NewsSample {
        public final String  text;
        public final String label;

        public NewsSample(String text, String label) {
            this.text = text;
            this.label = label;
        }
    }

    public static List<NewsSample> loadSample(){
        List<NewsSample> samples = new ArrayList<>();
        try(var in = new InputStreamReader(
                DataLoader.class.getResourceAsStream("/data/sample.csv"))){

            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for(CSVRecord record : parser){
                samples.add(new NewsSample(
                        record.get("text"),
                        record.get("label")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return samples;
    }
}
