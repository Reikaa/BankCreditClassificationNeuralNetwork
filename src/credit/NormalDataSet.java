package credit;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author oguz
 */

public class NormalDataSet {
    
    private DataSet dataSet = new DataSet();
    
    private Map<Integer, int[]> encodedCreditHistoryMap = new HashMap<>();
    private Map<Integer, Double> normalCreditAmountMap = new HashMap<>();
    private Map<Integer, int[]> encodedEmploymentMap = new HashMap<>();
    private Map<Integer, int[]> encodedPropertyMagnitudeMap = new HashMap<>();
    private Map<Integer, Double> normalAgeMap = new HashMap<>();
    private Map<Integer, Double> encodedClassMap = new HashMap<>();
    
    private int criticalOtherExistingCredit[] = {1,0,0,0,0};
    private int allPaid[] = {0,1,0,0,0};
    private int delayedPreviously[] = {0,0,1,0,0};
    private int noCreditsAllPaid[] = {0,0,0,1,0};
    private int existingPaid[] = {0,0,0,0,1};
    
    private int smallerThanOne[] = {1,0,0,0,0};
    private int betweenOneAndFourOrEqualOne[] = {0,1,0,0,0};
    private int greaterThanSevenOrEqual[] = {0,0,1,0,0};
    private int betweenFourAndSevenOrEqualFour[] = {0,0,0,1,0};
    private int unemployed[] = {0,0,0,0,1};
    
    private int noKnownProperty[] = {1,0,0,0};
    private int car[] = {0,1,0,0};
    private int lifeInsurance[] = {0,0,1,0};
    private int realEstate[] = {0,0,0,1};
    
    private double good = 1;
    private double bad = 0;
    
    private Map<String, int[]> creditHistoryMap = new HashMap<>(); {
       
        creditHistoryMap.put("'critical/other existing credit'", criticalOtherExistingCredit);
        creditHistoryMap.put("'all paid'", allPaid);
        creditHistoryMap.put("'delayed previously'", delayedPreviously);
        creditHistoryMap.put("'no credits/all paid'", noCreditsAllPaid);
        creditHistoryMap.put("'existing paid'", existingPaid);
        
    }
    
    private Map<String, int[]> employmentMap = new HashMap<>(); {
        
        employmentMap.put("<1", smallerThanOne);
        employmentMap.put("1<=X<4", betweenOneAndFourOrEqualOne);
        employmentMap.put(">=7", greaterThanSevenOrEqual);
        employmentMap.put("4<=X<7", betweenFourAndSevenOrEqualFour);
        employmentMap.put("unemployed", unemployed);
        
    }
    
    private Map<String, int[]> propertyMagnitudeMap = new HashMap<>(); {
    
        propertyMagnitudeMap.put("'no known property'", noKnownProperty);
        propertyMagnitudeMap.put("car", car);
        propertyMagnitudeMap.put("'life insurance'", lifeInsurance);
        propertyMagnitudeMap.put("'real estate'", realEstate);
        
    }
    
    private Map<String, Double> classMap = new HashMap<>(); {
    
        classMap.put("good", good);
        classMap.put("bad", bad);
        
    }
    
    public NormalDataSet () {
        
        for (int i = 0; i < 1000; i++) {
           
           encodedCreditHistoryMap.put(i, creditHistoryMap.get(dataSet.getcreditHistory(i)));
           
           normalCreditAmountMap.put(i, 
                   normalize(dataSet.getCreditAmount(i),dataSet.getMinCreditAmount(),dataSet.getMaxCreditAmount()));
           
           encodedEmploymentMap.put(i, employmentMap.get(dataSet.getEmployment(i)));
           encodedPropertyMagnitudeMap.put(i, propertyMagnitudeMap.get(dataSet.getPropertyMagnitude(i)));
           normalAgeMap.put(i, normalize(dataSet.getAge(i), dataSet.getMinAge(), dataSet.getMaxAge()));
           encodedClassMap.put(i, classMap.get(dataSet.getClass(i)));
            
        }
        
    }
    
    private double normalize(double value, double min, double max) {
        return (value-min)/(max-min);
    }
    
    public int[] getEncodedCreditHistory(int recordNumber) {
        return encodedCreditHistoryMap.get(recordNumber);
    }
     
    public double getNormalCreditAmount(int recordNumber) {
        return normalCreditAmountMap.get(recordNumber);
    }
    
    public int[] getEncodedEmployment(int recordNumber) {
        return encodedEmploymentMap.get(recordNumber);
    }
    
    public int[] getEncodedPropertyMagnitude(int recordNumber) {
        return encodedPropertyMagnitudeMap.get(recordNumber);
    }
    
    public double getNormalAge(int recordNumber) {
        return normalAgeMap.get(recordNumber);
    }
    
    public double getEncodedClass(int recordNumber) {
        return encodedClassMap.get(recordNumber);
    }
    
}