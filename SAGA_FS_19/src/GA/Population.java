/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 *
 * @author bio00
 */
public class Population {

    private List<Individual> population;
    private int populationSize;
    private double populationFitness = -1;
    private Random rnd;

    private final int START = 2;

    public Population(int populationSize, long seed) {
        this.populationSize = populationSize;
        population = new ArrayList<>(populationSize);
        this.rnd = new Random(seed);
    }

    public Population(int populationSize) {
        this.populationSize = populationSize;
        population = new ArrayList<>(populationSize);
        this.rnd = new Random();
    }

    public List<Individual> getIndividuals() {
        return this.population;
    }

    public Individual getFittest(int offset) {
        Collections.sort(population, Collections.reverseOrder());
        Individual best = new Individual((HashSet) population.get(offset).getChromosome().clone());
        best.setFitness(population.get(offset).getFitness());
        return population.get(offset);
    }

    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    public double getPopulationFitness() {
        return this.populationFitness;
    }

    public int size() {
        return this.populationSize;
    }

    public void setIndividual(int offset, Individual individual) {
        population.set(offset, individual);
    }

    public void setIndividual(Individual individual) {
        population.add(individual);

    }

    public Individual getIndividual(int offset) {
        return population.get(offset);
    }

    public void suffle() {
        Collections.shuffle(population, rnd);
        // Collections.shuffle(population);
    }

    @Override
    public String toString() {
        String pop = "Fitness \t\t\t attribute set \n";

        for (Individual ind : population) {
            pop += ind.getFitness() + "\t\t\t" + ind.toString() + "\n";
        }
        return pop;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setIndividual(List<Individual> Individuals) {
        boolean addAll = population.addAll(Individuals);
    }

}
