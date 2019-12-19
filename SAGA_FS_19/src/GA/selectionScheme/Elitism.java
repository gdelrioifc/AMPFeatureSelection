/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.selectionScheme;

import GA.Individual;
import GA.Population;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author beltran
 */
public class Elitism extends ReplacementSchemes{
    private int popSize;
    public Elitism(int popSize) {
        super("Elitism");
        this.popSize = popSize;
    }

    @Override
    public Population select(Population mu, Population lambda) {
        List<Individual> aux = new ArrayList<>(mu.getIndividuals());
        aux.addAll(lambda.getIndividuals());
        Population pop = new Population(popSize);
        Collections.sort(aux, Collections.reverseOrder());
        List<Individual> newGen = new ArrayList<>(aux.size());
        for(int i =0; i<popSize; i++){
            Individual get = aux.get(i);
            Individual clone = new Individual(new HashSet<>(get.getIndexOfFeatures()));
            clone.setFitness(get.getFitness());
            pop.setIndividual(clone);
        }
       
        //pop.setIndividual(newGen);
        return pop;
    }
    
 
    
}
