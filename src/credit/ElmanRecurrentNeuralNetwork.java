/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package credit;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author oguz
 */
public class ElmanRecurrentNeuralNetwork {

    private static final int INPUT_NODE_NUMBER = 16;

    private NormalDataSet normalDataSet = new NormalDataSet();
    private DataSet dataSet = new DataSet();

    private String[] testSetRange = new String[10];
    private ArrayList<Integer> testSet = new ArrayList<>();

    private int hiddenLayerNodeNumber;
    private double learningRate;
    private double momentum;
    private int epochNumber;

    public ElmanRecurrentNeuralNetwork(int hiddenNode, double learningRate, double momentum, int epochNumber) {

        this.hiddenLayerNodeNumber = hiddenNode;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.epochNumber = epochNumber;

        prepareStratifiedCrossValidation();

        testSetRange[0] = "0,99";
        testSetRange[1] = "100,199";
        testSetRange[2] = "200,299";
        testSetRange[3] = "300,399";
        testSetRange[4] = "400,499";
        testSetRange[5] = "500,599";
        testSetRange[6] = "600,699";
        testSetRange[7] = "700,799";
        testSetRange[8] = "800,899";
        testSetRange[9] = "900,999";
    }

    public void start() {

        double[] inputValues = new double[INPUT_NODE_NUMBER];
        double[][] inputLayerWeights = new double[INPUT_NODE_NUMBER][hiddenLayerNodeNumber];
        double[] hiddenLayerNetInputs = new double[hiddenLayerNodeNumber];
        double[] hiddenLayerNetOutputs = new double[hiddenLayerNodeNumber];
        double[] outputLayerWeights = new double[hiddenLayerNodeNumber];
        double[] hiddenLayerErrorNode = new double[hiddenLayerNodeNumber];
        double[] hiddenContextWeigths = new double[hiddenLayerNodeNumber];
        double[] contextLayerNetOutputs = new double[hiddenLayerNodeNumber];
        double[][] contextHiddenWeigths = new double[hiddenLayerNodeNumber][hiddenLayerNodeNumber];
        double[] contextLayerInput = new double[hiddenLayerNodeNumber];

        double errorAtOutputNode = 0;
        double outputLayerNetInput = 0;
        double outputLayerNetOutput = 0;

        int TPforGood = 0;
        int FPforGood = 0;
        int FNforGood = 0;
        int TNforGood = 0;
        int TPforBad = 0;
        int FPforBad = 0;
        int FNforBad = 0;
        int TNforBad = 0;

        Arrays.fill(inputValues, 0);
        fillArray(inputLayerWeights);
        Arrays.fill(hiddenLayerNetInputs, 0);
        Arrays.fill(hiddenLayerNetOutputs, 0);
        Arrays.fill(outputLayerWeights, -0.9);
        Arrays.fill(hiddenLayerErrorNode, 0);
        Arrays.fill(hiddenContextWeigths, 0.9);
        Arrays.fill(contextLayerNetOutputs, 0);
        fillArray(contextHiddenWeigths);
        Arrays.fill(contextLayerInput, 0);

        for (int k = 0; k < 10; k++) {

            String range = testSetRange[k];
            String[] ranges = range.split(",");
            int lowerBoundary = Integer.parseInt(ranges[0]);
            int upperBoundary = Integer.parseInt(ranges[1]);

            for (int i = 0; i < epochNumber; i++) {

                //Train start here
                for (int dataNumber = 0; dataNumber < 1000; dataNumber++) {

                    //If data is test data do nothing and continue
                    if (dataNumber >= lowerBoundary && dataNumber <= upperBoundary) {
                        continue;
                    }

                    int trainNumber = testSet.get(dataNumber);

                    int[] creditHistory = normalDataSet.getEncodedCreditHistory(trainNumber);
                    double creditAmount = normalDataSet.getNormalCreditAmount(trainNumber);
                    int[] employment = normalDataSet.getEncodedEmployment(trainNumber);
                    int[] propertyMagnitude = normalDataSet.getEncodedPropertyMagnitude(trainNumber);
                    double age = normalDataSet.getNormalAge(trainNumber);

                    inputValues[0] = creditHistory[0];
                    inputValues[1] = creditHistory[1];
                    inputValues[2] = creditHistory[2];
                    inputValues[3] = creditHistory[3];
                    inputValues[4] = creditHistory[4];
                    inputValues[5] = creditAmount;
                    inputValues[6] = employment[0];
                    inputValues[7] = employment[1];
                    inputValues[8] = employment[2];
                    inputValues[9] = employment[3];
                    inputValues[10] = employment[4];
                    inputValues[11] = propertyMagnitude[0];
                    inputValues[12] = propertyMagnitude[1];
                    inputValues[13] = propertyMagnitude[2];
                    inputValues[14] = propertyMagnitude[3];
                    inputValues[15] = age;

                    //Calculates firstly inputs from context layer
                    //First iteration inputs are 0 and context layer dont effect on first iteration
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        for (int contextNode = 0; contextNode < hiddenLayerNodeNumber; contextNode++) {

                            hiddenLayerNetInputs[hiddenNode]
                                    += (contextLayerInput[contextNode] * contextHiddenWeigths[contextNode][hiddenNode]);

                        }

                    }

                    //Calculates all input value of hidden layers
                    for (int inputNode = 0; inputNode < INPUT_NODE_NUMBER; inputNode++) {

                        for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                            hiddenLayerNetInputs[hiddenNode]
                                    += (inputValues[inputNode] * inputLayerWeights[inputNode][hiddenNode]);

                        }
                    }

                    //Calculates hidden nodes output values
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        hiddenLayerNetOutputs[hiddenNode] = calculateSigmoidOutput(hiddenLayerNetInputs[hiddenNode]);

                    }

                    //Calculates sum of input values to output node
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        outputLayerNetInput += hiddenLayerNetOutputs[hiddenNode] * outputLayerWeights[hiddenNode];

                    }

                    outputLayerNetOutput = calculateSigmoidOutput(outputLayerNetInput);

                    errorAtOutputNode = outputLayerNetOutput * (1 - outputLayerNetOutput)
                            * (normalDataSet.getEncodedClass(dataNumber) - outputLayerNetOutput);

                    //Calculates Hidden node error
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        double output = hiddenLayerNetOutputs[hiddenNode];

                        hiddenLayerErrorNode[hiddenNode]
                                = outputLayerWeights[hiddenNode] * output * (1 - output) * errorAtOutputNode;

                    }

                    //Weight update between hidden and output nodes
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        outputLayerWeights[hiddenNode]
                                += (learningRate * momentum * errorAtOutputNode * hiddenLayerNetOutputs[hiddenNode]);

                    }

                    //Weight update between input and hidden nodes
                    for (int inputNode = 0; inputNode < INPUT_NODE_NUMBER; inputNode++) {

                        for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                            inputLayerWeights[inputNode][hiddenNode]
                                    += (learningRate * momentum * hiddenLayerErrorNode[hiddenNode] * inputValues[inputNode]);
                        }
                    }

                    //Weight update between hidden and context layer
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        hiddenContextWeigths[hiddenNode]
                                += (learningRate * momentum * hiddenLayerErrorNode[hiddenNode] * contextLayerInput[hiddenNode]);

                    }

                    //After one iteration update context nodes from hidden nodes
                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        contextLayerInput[hiddenNode] = hiddenLayerNetOutputs[hiddenNode];

                    }

                    //After one iteration reset variables except weigths
                    //Because train set weights according to errors with backpropagation
                    //every iteration 
                    Arrays.fill(inputValues, 0);
                    Arrays.fill(hiddenLayerNetInputs, 0);
                    Arrays.fill(hiddenLayerNetOutputs, 0);
                    outputLayerNetInput = 0;
                    outputLayerNetOutput = 0;
                    errorAtOutputNode = 0;

                }

            }

            Arrays.fill(contextLayerInput, 0);

            //Train Ends
            //Test Starts here
            for (int dataNumber = lowerBoundary; dataNumber < upperBoundary; dataNumber++) {

                int testData = testSet.get(dataNumber);

                int[] creditHistory = normalDataSet.getEncodedCreditHistory(testData);
                double creditAmount = normalDataSet.getNormalCreditAmount(testData);
                int[] employment = normalDataSet.getEncodedEmployment(testData);
                int[] propertyMagnitude = normalDataSet.getEncodedPropertyMagnitude(testData);
                double age = normalDataSet.getNormalAge(testData);

                inputValues[0] = creditHistory[0];
                inputValues[1] = creditHistory[1];
                inputValues[2] = creditHistory[2];
                inputValues[3] = creditHistory[3];
                inputValues[4] = creditHistory[4];
                inputValues[5] = creditAmount;
                inputValues[6] = employment[0];
                inputValues[7] = employment[1];
                inputValues[8] = employment[2];
                inputValues[9] = employment[3];
                inputValues[10] = employment[4];
                inputValues[11] = propertyMagnitude[0];
                inputValues[12] = propertyMagnitude[1];
                inputValues[13] = propertyMagnitude[2];
                inputValues[14] = propertyMagnitude[3];
                inputValues[15] = age;

                for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                    for (int contextNode = 0; contextNode < hiddenLayerNodeNumber; contextNode++) {

                        hiddenLayerNetInputs[hiddenNode]
                                += (contextLayerInput[contextNode] * contextHiddenWeigths[contextNode][hiddenNode]);

                    }

                }

                for (int inputNode = 0; inputNode < INPUT_NODE_NUMBER; inputNode++) {

                    for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                        hiddenLayerNetInputs[hiddenNode]
                                += (inputValues[inputNode] * inputLayerWeights[inputNode][hiddenNode]);

                    }
                }

                for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                    hiddenLayerNetOutputs[hiddenNode] = calculateSigmoidOutput(hiddenLayerNetInputs[hiddenNode]);

                }

                for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                    outputLayerNetInput += hiddenLayerNetOutputs[hiddenNode] * outputLayerWeights[hiddenNode];

                }

                outputLayerNetOutput = calculateSigmoidOutput(outputLayerNetInput);

                //After one iteration update context nodes from hidden nodes
                for (int hiddenNode = 0; hiddenNode < hiddenLayerNodeNumber; hiddenNode++) {

                    contextLayerInput[hiddenNode] = hiddenLayerNetOutputs[hiddenNode];

                }

                String predicted;

                if (outputLayerNetOutput < 0.5) {
                    predicted = "bad";
                } else {
                    predicted = "good";
                }

                if (predicted.equals(dataSet.getClass(dataNumber))) {

                    if (predicted.equals("bad")) {

                        TPforBad++;

                    } else {

                        TPforGood++;
                    }

                } else {

                    if (predicted.equals("bad") && dataSet.getClass(dataNumber).equals("good")) {

                        FNforGood++;
                        FPforBad++;

                    } else if (predicted.equals("good") && dataSet.getClass(dataNumber).equals("bad")) {

                        FNforBad++;
                        FPforGood++;

                    }

                }

                System.out.print("actual = " + dataSet.getClass(dataNumber));
                System.out.println("     predicted = " + predicted);

                Arrays.fill(hiddenLayerNetInputs, 0);
                Arrays.fill(hiddenLayerNetOutputs, 0);
                outputLayerNetInput = 0;
                outputLayerNetOutput = 0;
            }

            TNforBad = TPforGood;
            TNforGood = TPforBad;

        }//Test end

        System.out.println("For good");
        System.out.println("TP = " + TPforGood);
        System.out.println("FP = " + FPforGood);
        System.out.println("TN = " + TNforGood);
        System.out.println("FN = " + FNforGood);
        System.out.println("Accuracy" + (double)(TPforGood + TNforGood) / (TPforGood + FPforGood + TNforGood + FNforGood));
        
        System.out.println("===================");
        
        System.out.println("For bad");
        System.out.println("TP = " + TPforBad);
        System.out.println("FP = " + FPforBad);
        System.out.println("TN = " + TNforBad);
        System.out.println("FN = " + FNforBad);
        System.out.println("Accuracy" + (double)(TPforBad + TNforBad) / (TPforBad + FPforBad + TNforBad + FNforBad));
        
        
        Arrays.fill(inputValues, 0);
        fillArray(inputLayerWeights);
        Arrays.fill(hiddenLayerNetInputs, 0);
        Arrays.fill(hiddenLayerNetOutputs, 0);
        Arrays.fill(outputLayerWeights, -0.9);
        Arrays.fill(hiddenLayerErrorNode, 0);
        Arrays.fill(hiddenContextWeigths, 0.9);
        Arrays.fill(contextLayerNetOutputs, 0);
        fillArray(contextHiddenWeigths);
        Arrays.fill(contextLayerInput, 0);

    }

    private double calculateSigmoidOutput(double input) {

        return (1 / (1 + Math.pow(Math.E, (-1 * input))));

    }

    private void fillArray(double[][] array) {

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = -0.3;
            }
        }

    }

    private void prepareStratifiedCrossValidation() {

        int good = 0;
        int bad = 0;
        int returnNum = 0;

        for (int i = 0; i < 1000; i++) {

            if (testSet.contains(i)) {

                continue;

            }

            if (normalDataSet.getEncodedClass(i) == 1) {

                if (good < 70) {

                    good++;
                    testSet.add(i);

                }

            } else {

                if (bad < 30) {

                    bad++;
                    testSet.add(i);

                }

            }

            if (good == 70 && bad == 30) {

                bad = 0;
                good = 0;
                returnNum++;

                if (returnNum <= 10) {
                    i = 0;
                }

            }

        }

    }

}