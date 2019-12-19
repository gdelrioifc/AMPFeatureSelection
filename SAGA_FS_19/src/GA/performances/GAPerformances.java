/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.performances;

import java.util.Map;

/**
 *
 * @author beltran
 */
public class GAPerformances {

    private Map<Double, Integer> fitnessDistribution;
    private Map<Double, Integer> cumulativeFitnessDistribution;
    
    private double avgFitness;
    private double fitnessVariance;
    private double maxFitness;
    private double minFitness;
    private double median;
    private double q1;
    private double q3;

    public GAPerformances(Map<Double, Integer> fitnessDistribution, Map<Double, Integer> cumulativeFitnessDistribution, double avgFitness, double fitnessVariance, double maxFitness, double minFitness, double median, double q1, double q3) {
        this.fitnessDistribution = fitnessDistribution;
        this.cumulativeFitnessDistribution = cumulativeFitnessDistribution;
        this.avgFitness = avgFitness;
        this.fitnessVariance = fitnessVariance;
        this.maxFitness = maxFitness;
        this.minFitness = minFitness;
        this.median = median;
        this.q1 = q1;
        this.q3 = q3;
    }
    
    
       
   

    public Map<Double, Integer> getFitnessDistribution() {
        return fitnessDistribution;
    }

    public Map<Double, Integer> getCumulativeFitnessDistribution() {
        return cumulativeFitnessDistribution;
    }

    public double getAvgFitness() {
        return avgFitness;
    }

    public double getFitnessVariance() {
        return fitnessVariance;
    }

    public double getMaxFitness() {
        return maxFitness;
    }

    public double getMinFitness() {
        return minFitness;
    }

    public double getMedian() {
        return median;
    }

    public double getQ1() {
        return q1;
    }

    public double getQ3() {
        return q3;
    }
    
    
    
    
    @Override
    public String toString(){
        String performaces="Fitness Distribution\n";
        performaces += "====================================\n"
                    + "Fitness  \t   Frequency\n"
                    + "====================================\n";
        
        performaces = this.fitnessDistribution.entrySet().stream().map((entry) ->  entry.getKey() + "\t" + entry.getValue() + "\n").reduce(performaces, String::concat);
        
        performaces += "====================================\n"
                       + "Summary Statistics \n"
                       +"====================================\n";
        
        performaces +="Mean = " + this.avgFitness+ "\n"
                    +"StDev = " + Math.sqrt(this.fitnessVariance)  + "\n" 
                    +"Minimum = " + this.minFitness + "\n"
                    +"Q1 = " + this.q1 + "\n" 
                    +"Median = " + this.median + "\n" 
                    +"Q3 = " + this.q3 + "\n"
                    +"Maximum = " + this.maxFitness + "\n"
                    +"Range = " + (this.maxFitness - this.minFitness ) + "\n"
                    +"IQR = " + (this.q3 - this.q1) + "\n"
                    ;
                    
               
        return performaces;
    } 

    
}
