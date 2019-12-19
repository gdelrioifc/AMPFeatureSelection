/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import GA.crossover.Crossover;
import GA.crossover.SSOCF;
import GA.mutation.KInDels;
import GA.mutation.Mutation;
import GA.selectionScheme.Elitism;
import GA.selectionScheme.ReplacementSchemes;
import GA.selectionScheme.SelectionSchemes;
import GA.selectionScheme.Tournament;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author beltran
 */
public class GeneticAlgorithm {

    private final int POPSIZE; //population size
    private final double PM;  //mutation rate
    private final double CR;  //crossover rate
    private int elitismCount;
    private Random rand;
    private final SelectionSchemes PSELECTION; //parent Selection
    private final Crossover CROSSOVER; //crossover
    private final Mutation MUTATION; //mutation
    private final ReplacementSchemes SURVIVOR;
    private double fittest;
    private int ngwi;
    private final int NFEATURES;

//    public GeneticAlgorithm(int POPSIZE, double PM, double CR, int nFeatures) {
//        this.POPSIZE = POPSIZE;
//        this.PM = PM;
//        this.CR = CR;
//        this.PSELECTION = new Tournament(2);
//        this.CROSSOVER = new SSOCF(CR, nFeatures);
//        this.MUTATION = new KInDels(PM, nFeatures);
//        this.SURVIVOR = new Elitism(POPSIZE);
//        this.ngwi = 0;
//    }

    public GeneticAlgorithm(int POPSIZE, double PM, double CR, int ts,  long seed,int nFeatures) {
        this.POPSIZE = POPSIZE;
        this.PM = PM;
        this.CR = CR;
        this.PSELECTION = new Tournament(ts, seed);
        this.CROSSOVER = new SSOCF(CR, nFeatures, seed);
        this.MUTATION = new KInDels(PM, nFeatures, seed);
        this.SURVIVOR = new Elitism(POPSIZE);
        this.ngwi = 0;
        this.NFEATURES = nFeatures;
        this.rand = new Random(seed);
    }

    public Population initPopulation(int lowerBound, int upperBound) {
        Population population = new Population(this.POPSIZE);
        //Random rand = new Random(1);
        List<Integer> indexOfCandidateFeatures = IntStream.range(0, this.NFEATURES).boxed().collect(Collectors.toList());
   
        //generate randomly chormosome length for each individual
        List<Integer> chormosomeLengths = rand.ints(POPSIZE, lowerBound, upperBound)
                .boxed()
                .collect(Collectors.toList());
        //System.out.println(chormosomeLengths);

        chormosomeLengths.forEach((Integer chromosomeLength) -> {
            //Generate an arbitrary permutation of datasetFeatures
            Collections.shuffle(indexOfCandidateFeatures, rand);
            //From datasetFeature get the first chormosomeLength index features
            HashSet<Integer> chromosome = new HashSet<>(indexOfCandidateFeatures.subList(0, chromosomeLength)
                    .stream()
                    .collect(Collectors.toList()));

            population.setIndividual(new Individual(chromosome));
            //  System.out.println(chromosome);
        });

        return population;
    }

    public Population parentSelection(Population population) {
        population.suffle();
        return PSELECTION.select(population);
    }

    public Population crossover(Population population) {
        return CROSSOVER.recombine(population);
    }

    public void mutation(Population population) {
        MUTATION.mutation(population);
    }

    public void self_adaptive_mutation(Population population, double similarity, double similarityThreshold, double delta) {
        if (similarity < similarityThreshold) {
            delta = -delta;
        }
        MUTATION.adaptiveMutationRate(delta);
        mutation(population);
    }

    public boolean isTerminationConditionMet(int generationCount, int maxGenerations) {

        return (generationCount > maxGenerations);
    }

    public boolean isTerminationConditionMet(int MaxGwI, double fitness) {
        if (fitness == fittest) {
            ngwi++;
        } else {
            fittest = fitness;
            ngwi = 0;
        }

        return (ngwi > MaxGwI);
    }

    public boolean isTerminationConditionMet(double lossDiversity, double min) {
        return (lossDiversity == min);
    }

    public Population survivorSelection(Population parents, Population offsprings) {
        return SURVIVOR.select(parents, offsprings);
    }

}
