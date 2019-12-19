/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;


import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Armando Beltran
 * @email abeltran@cicese.edu.mx
 *
 */
public class Individual implements Comparable<Individual>{

    private HashSet<Integer> chromosome;
    private double fitness;
    private int chromosomeLength;

    public Individual(HashSet genotype) {
        this.chromosome = genotype;
        this.chromosomeLength = genotype.size();
        this.fitness = -1;
    }

    public HashSet<Integer> getChromosome() {
        return this.chromosome;
    }

   

    public void setFitness(double fintess) {
        this.fitness = fintess;
    }

    public double getFitness() {
        return this.fitness;
    }

    public int getChromosomeLength() {
        return this.chromosomeLength;
    }
    
    public List<Integer> getIndexOfFeatures(){
        return chromosome.parallelStream().collect(Collectors.toList());
    }
  
    @Override
    public String toString() {
        return this.chromosome.toString();
    }

    @Override
    public int compareTo(Individual other) {
        //compareTo should return < 0  if this is supposed to be
        //less than other, > 0 if this is supposed to be greater than
        //other and 0 if they are supposed to be equal
        int r=0;
        double oFit = other.fitness;
        if(this.fitness<oFit)
            r = -1;
        return fitness > oFit? r=1:r; 
    }



}
