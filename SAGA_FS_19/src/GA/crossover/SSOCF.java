/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.crossover;

import GA.Individual;
import GA.Population;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author beltran
 */
public class SSOCF extends Crossover {

    private final int lc; //chromosome length
    Random rand;

    public SSOCF(double pC, int chLength, long seed) {
        super("Subset Size Oriented Common Features Operator", pC);
        this.lc = chLength;
        rand = new Random(seed);
      //  rand = new Random(System.currentTimeMillis());
    }

    @Override
    public Population recombine(Population p) {
        Population offsprings = new Population(p.getPopulationSize());
        List<Individual> parents = p.getIndividuals();
        Collections.shuffle(parents, rand);
        for (int i = 1; i < p.getPopulationSize(); i = i + 2) {
            List<Individual> recombine = recombine(p.getIndividual(i - 1), p.getIndividual(i));
            offsprings.setIndividual(recombine);
        }

        return offsprings;
    }

    @Override
    public List<Individual> recombine(Individual parent1, Individual parent2) {
        List<Individual> offsprings = new ArrayList<>(2);
        //   System.out.println(parent1.getChromosome());
        //   System.out.println(parent2.getChromosome());
        if (rand.nextDouble() < this.pC) {
            //commonFeatureCrossover(parent1, parent2, offsprings);
            cFC(parent1, parent2, offsprings);
        } else {//create two individuals asexually
            offsprings.add(new Individual(new HashSet<>(parent1.getIndexOfFeatures())));
            offsprings.add(new Individual(new HashSet<>(parent2.getIndexOfFeatures())));
        }
        return offsprings;
    }

    private void cFC(Individual parent1, Individual parent2, List<Individual> offsprings) {
        Set<Integer> commonlySelectedFeatures = intersection(parent1.getChromosome(), parent2.getChromosome());

        
        //copy common features indices from both parent into the offsprings
        HashSet<Integer> offspring1 = new HashSet<>(commonlySelectedFeatures);
        HashSet<Integer> offspring2 = new HashSet<>(commonlySelectedFeatures);

        //Get the non-shared features indices between parent1 and parent2
        Set<Integer> nonCommonFeaturesP1 =commonlySelectedFeatures.isEmpty()? parent1.getChromosome():difference(parent1.getChromosome(), commonlySelectedFeatures);
        Set<Integer> nonCommonFeaturesP2 =commonlySelectedFeatures.isEmpty()? parent2.getChromosome():difference(parent2.getChromosome(), commonlySelectedFeatures);

        //Calculate the probability that the non-shared features indices are inherited by the  first offspring from the first parent.
        double nu =(nonCommonFeaturesP1.size() + nonCommonFeaturesP2.size()*1.0); 
        double p1 = (nu==0)? 0:(parent1.getChromosomeLength() - commonlySelectedFeatures.size()) /nu ;//(parent1.getChromosomeLength() + parent2.getChromosomeLength() - (2 * commonlySelectedFeatures.size())*1.0);
        double p2 = (nu==0)? 0:(parent2.getChromosomeLength() - commonlySelectedFeatures.size()) /nu;//(parent1.getChromosomeLength() + parent2.getChromosomeLength() - (2 * commonlySelectedFeatures.size())*1.0);

//        System.out.println(parent1 +  " |p1|= " + parent1.getChromosomeLength());
//        System.out.println(parent2 +  " |p2|= " + parent2.getChromosomeLength());
//        System.out.println("C =" + commonlySelectedFeatures.toString() + " |C|=" + commonlySelectedFeatures.size());
//        System.out.println("p1=" + p1 + ",p2=" + p2);
//        //Generate nonCommonFeaturesP1 random varaibles from a uniform distribution between [0,1)
//        double[] randi = rand.doubles(nonCommonFeaturesP1.size()).toArray();
//    
//        //Generate nonCommonFeaturesp2 random variable from a uniform distribution between [0,1=
//        double[] randj =rand.doubles(nonCommonFeaturesP2.size()).toArray();
//
        nonCommonFeaturesP1.stream().forEach((Integer i) -> {
            if (rand.nextDouble() < p1) {
                offspring1.add(i);
            } else {
                offspring2.add(i);
            }
        });

        nonCommonFeaturesP2.stream().forEach((Integer j) -> {
            if (rand.nextDouble() < p2) {
                offspring1.add(j);
            } else {
                offspring2.add(j);
            }
        });
        
//        System.out.println(offspring1.toString() + ",|O1| =" + offspring1.size());
//         System.out.println(offspring2.toString() + ",|O2| =" + offspring2.size());
//         System.out.println("-------------------------------------------------------------");
        offsprings.add(new Individual(offspring1));
        offsprings.add(new Individual(offspring2));

    }

    private void commonFeatureCrossover(Individual parent1, Individual parent2, List<Individual> offsprings) {
        Set<Integer> commonlySelectedFeatures = intersection(parent1.getChromosome(), parent2.getChromosome());
        Set<Integer> nonCommonFeatures = difference(union(parent1.getChromosome(), parent2.getChromosome()), commonlySelectedFeatures);

        double p1 = nonCommonFeatures.isEmpty() ? 0.0 : (parent1.getChromosomeLength() - commonlySelectedFeatures.size()) / (nonCommonFeatures.size() * 1.0);
        double p2 = nonCommonFeatures.isEmpty() ? 0.0 : (parent2.getChromosomeLength() - commonlySelectedFeatures.size()) / (nonCommonFeatures.size() * 1.0);
        //Both offspring preservee the common features of their parents.
        //Add commonlyselectedFeatures to offspring1
        HashSet<Integer> offspring1 = new HashSet<>(commonlySelectedFeatures);
        HashSet<Integer> offspring2 = new HashSet<>(commonlySelectedFeatures);

        //The non shared features are inherited by the offspring corresponding to the ith 
        //parenth with the probability pi.
        for (Integer i : nonCommonFeatures) {
            if (rand.nextDouble() <= p1) {
                offspring1.add(i);
            }
            if (rand.nextDouble() <= p2) {
                offspring2.add(i);
            }
        }

        offsprings.add(new Individual(offspring1));
        offsprings.add(new Individual(offspring2));

    }

    private <T> Set<T> union(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new HashSet<>(setA);
        tmp.addAll(setB);
        return tmp;
    }

    private <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new HashSet<>();
        setA.stream().filter((x) -> (setB.contains(x))).forEach((x) -> {
            tmp.add(x);
        });
        return tmp;
    }

    public <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new HashSet<>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    public <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
        Set<T> tmpA;
        Set<T> tmpB;

        tmpA = union(setA, setB);
        tmpB = intersection(setA, setB);
        return difference(tmpA, tmpB);
    }

    public <T> boolean isSubset(Set<T> setA, Set<T> setB) {
        return setB.containsAll(setA);
    }

    public <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
        return setA.containsAll(setB);
    }

}
