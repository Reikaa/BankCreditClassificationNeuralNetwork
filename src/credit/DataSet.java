package credit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author oguz
 */
public class DataSet {

    private Map<Integer, String> creditHistoryMap = new HashMap<>();
    private Map<Integer, Double> creditAmountMap = new HashMap<>();
    private Map<Integer, String> employmentMap = new HashMap<>();
    private Map<Integer, String> propertyMagnitudeMap = new HashMap<>();
    private Map<Integer, Double> ageMap = new HashMap<>();
    private Map<Integer, String> classMap = new HashMap<>();
    
    public DataSet() {
        
        loadDataMapsFromFile();
        
    }

    private void loadDataMapsFromFile() {
        
        File file = new File("credit.txt");
        
        try {

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            String[] buffer;
            int recordNumber = 0;
            
            while ((line = bufferedReader.readLine()) != null) {
                
                buffer = line.split(",");
                
                String creditHistory = buffer[0];
                double creditAmount = Double.parseDouble(buffer[1]);
                String employment = buffer[2];
                String propertyMagnitude = buffer[3];
                double age = Double.parseDouble(buffer[4]);
                String classOutput = buffer[5];
   
                creditHistoryMap.put(recordNumber, creditHistory);
                creditAmountMap.put(recordNumber, creditAmount);
                employmentMap.put(recordNumber, employment);
                propertyMagnitudeMap.put(recordNumber, propertyMagnitude);
                ageMap.put(recordNumber, age);
                classMap.put(recordNumber, classOutput);

                recordNumber++;
            }
            
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    public String getcreditHistory(int recordNumber) {
        return creditHistoryMap.get(recordNumber);
    }
    
    public double getCreditAmount(int recordNumber) {
        return creditAmountMap.get(recordNumber);
    }
    
    public double getMaxCreditAmount() {
        return Collections.max(creditAmountMap.values());
    }
    
    public double getMinCreditAmount() {
        return Collections.min(creditAmountMap.values());
    }
    
    public String getEmployment(int recordNumber) {
        return employmentMap.get(recordNumber);
    }
    
    public String getPropertyMagnitude(int recordNumber) {
        return propertyMagnitudeMap.get(recordNumber);
    }
    
    public double getAge(int recordNumber) {
        return ageMap.get(recordNumber);
    }
    
    public double getMaxAge() {
        return Collections.max(ageMap.values());
    }
    
    public double getMinAge() {
        return Collections.min(ageMap.values());
    }
    
    public String getClass(int recordNumber) {
        return classMap.get(recordNumber);
    }
       
}