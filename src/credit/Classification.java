/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package credit;

/**
 *
 * @author oguz
 */
public class Classification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ElmanRecurrentNeuralNetwork elmanRecurrentNeuralNetwork = new ElmanRecurrentNeuralNetwork(20, 0.1, 0.2, 5);
        
        elmanRecurrentNeuralNetwork.start();
        
    }

}
