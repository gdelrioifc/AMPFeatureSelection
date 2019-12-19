/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.performances;

import GA.Individual;
import GA.performances.GAPerformances;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author beltran
 */
public class Statistics {

    private final double PI;

    public Statistics() {
        this.PI = Math.PI;

    }

    /**
     * The fuction s: R to Z_0^+ assigs to each fitness value f \in R the number
     * of individuals in a population P \in J^N carrying this fitness value. s
     * is called the fitness distribution of a population P.
     *
     * @param individuals
     * @return s
     */
    public GAPerformances computeSts(List<Individual> individuals) {
        Map<Double, Integer> fitDistribution = new TreeMap<>();
        Map<Double, Integer> cumulativeFitDistribution = new TreeMap<>();
        TreeSet<Double> sortedFitness = new TreeSet<>();
        double avgFit = 0.0;
        double varFit = 0.0;
        double minFit = Double.POSITIVE_INFINITY;
        double maxFit = Double.NEGATIVE_INFINITY;
        double median = 0;
        double q1 = 0;
        double q3 = 0;

        for (Individual ind : individuals) {
            double fitness_ind = ind.getFitness();
            sortedFitness.add(fitness_ind);
            //Get the unfittest(worst fitness) value from population  
            if (fitness_ind < minFit) {
                minFit = fitness_ind;
            }

            //Get the fittest value from the population
            if (fitness_ind > maxFit) {
                maxFit = fitness_ind;
            }

            //if the fitness value does not in fitness distribution then it is added. 
            fitDistribution.putIfAbsent(fitness_ind, 0);

            //increment the frequency for the fitness value
            fitDistribution.replace(fitness_ind, fitDistribution.get(fitness_ind) + 1);

            avgFit += fitness_ind;
        }
        avgFit = avgFit / individuals.size(); //calcualte the mean from the population

        Integer cFitness = 0;

        //calculate the cumulative fitness distribution and the fitness variance.
        for (Map.Entry<Double, Integer> entry : fitDistribution.entrySet()) {
            varFit += (entry.getValue() * Math.pow((entry.getKey() - avgFit), 2));
            cFitness += entry.getValue();
            cumulativeFitDistribution.putIfAbsent(entry.getKey(), cFitness);
        }

        varFit = varFit / individuals.size(); //calculate the fitness variance
        List<Double> X = sortedFitness.parallelStream().collect(Collectors.toList());

        //Calculate the five number summary from fitness distribution
        //Get the unfittest(worst fitness) value from X 
        minFit = getPthPercentile(0, X.size(), X);
        //Get the fittest value from the X 
        maxFit = getPthPercentile(100, X.size(), X);
        //Get the median value from X
        median = getPthPercentile(50, X.size(), X);
        //Get the Q1 quartile from the X
        q1 = getPthPercentile(25, X.size(), X);
        //Get the Q3 quartile form the X
        q3 = getPthPercentile(75, X.size(), X);

        return new GAPerformances(fitDistribution, cumulativeFitDistribution, avgFit, varFit, maxFit, minFit, median, q1, q3);
    }

    //based on http://onlinestatbook.com/2/introduction/percentiles.html
    private double getPthPercentile(int P, int NEntries, List<Double> X) {
        //the first step is to compute the rank (R) of the P-th percentile. 
        Double r = (P / 100.0) * (NEntries + 1);
        r = (r >= NEntries) ? NEntries : r;
        Integer rank = (r == 0.0 || r.intValue() == 0.0) ? 0 : (r.intValue() - 1);
        //  List<Double> X = sortedFitness.parallelStream().collect(Collectors.toList());

        double percentileValue;
        if (NEntries % 2 != 0) {
            percentileValue = X.get(rank);
        } else {
            double j = X.get(rank);
            double j_1 = ((rank + 1) >= NEntries) ? 0.0 : X.get(rank + 1);
            double w = r - r.intValue();
            percentileValue = (1 - w) * j + w * j_1;
        }

        return percentileValue;
    }

    /**
     * the reproduction rate denotes the ratio of the number of individuals with
     * a certain fitness value f after and before selection
     *
     * @param beforeFitDistribution the fitness distribution before the
     * selection
     * @param afterFitDistribution the fitness distribution after the selection
     * @return
     */
    public Map<Double, Double> getReproductionRate(Map<Double, Integer> beforeFitDistribution, Map<Double, Integer> afterFitDistribution) {
        Map<Double, Double> reproductionRate = new TreeMap<>();

        beforeFitDistribution.keySet().stream().forEach((Double fitness) -> {
            Double rate = afterFitDistribution.containsKey(fitness) ? afterFitDistribution.get(fitness) / (beforeFitDistribution.get(fitness) * 1.0) : 0.0;
            reproductionRate.putIfAbsent(fitness, rate);
        });

        return reproductionRate;

    }

    /**
     * The loss of diversity pd is the proportion of individuals of a population
     * that is not selected during the selection phase
     *
     * @param beforeFitDistribution
     * @param afterFitDistribution
     * @param nEntries
     * @return
     */
    public double getLossOfDiversity(Map<Double, Integer> beforeFitDistribution, Map<Double, Integer> afterFitDistribution, int nEntries) {
        Map<Double, Double> reproductionRate = getReproductionRate(beforeFitDistribution, afterFitDistribution);
        double pd = 0.0; //loss of diversity

        //return all fitness f \in [f0, fz) such that the reproduction rate is less than one
        List<Double> f = reproductionRate.entrySet().stream().filter((entry) -> (entry.getValue() < 1)).map((entry) -> entry.getKey()).collect(Collectors.toList());

        for (Double fitness : f) {
            pd += afterFitDistribution.containsKey(fitness) ? beforeFitDistribution.get(fitness) - afterFitDistribution.get(fitness) : beforeFitDistribution.get(fitness);
        }

        return pd / (nEntries * 1.0);
    }

    public double selectionIntensity(double meanAfterSelection, double meanBeforeSelection, double varBeforeSelection) {
        return (meanAfterSelection - meanBeforeSelection) / Math.sqrt(varBeforeSelection);
    }

    public double standardGaussianDistribution(double x) {
        return 1 / (Math.sqrt(2 * PI)) * Math.exp(-(x * x) / 2);
    }

    public double similarity_percent(Map<Double, Integer> fitDistribution, int popsize){
       
        long count = fitDistribution.entrySet().stream().filter((entry) -> (entry.getValue()>1)).mapToInt((entry) -> entry.getValue()).sum();
        //System.out.println("similarirty:\t "+count);
       // int uniqueFitness = fitDistribution.keySet().size();
       return (count)/(popsize*1.0);
       
    }

//    public static void main(String[] args) {
//        Map<Double, Integer> befFitDist = new TreeMap<>();
//        Map<Double, Integer> afFitDist = new TreeMap<>();
//
//        befFitDist.put(56.41, 2);
//        befFitDist.put(74.68, 2);
//        befFitDist.put(95.46, 1);
//        befFitDist.put(96.761, 2);
//        befFitDist.put(96.762, 3);
//        befFitDist.put(97.01, 3);
//        befFitDist.put(97.69, 3);
//
//        afFitDist.put(74.68, 2);
//        afFitDist.put(95.46, 1);
//        afFitDist.put(96.761, 2);
//        afFitDist.put(96.762, 2);
//        afFitDist.put(97.01, 7);
//        afFitDist.put(97.69, 2);
//
//        Statistics stat = new Statistics();
//        Map<Double, Double> reproductionRate = stat.getReproductionRate(befFitDist, afFitDist);
//
//        reproductionRate.forEach((k, v) -> System.out.println(k + "," + v));
//
//        System.out.println(stat.getLossOfDiversity(befFitDist, afFitDist, 16));
//    }

}
