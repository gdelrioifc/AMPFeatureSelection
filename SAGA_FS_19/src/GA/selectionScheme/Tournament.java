/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.selectionScheme;

import GA.Individual;
import GA.Population;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author beltran
 */
public class Tournament extends SelectionSchemes{
    private final int T; // the tournament size
    private final Random r;
    
    public Tournament(int T, long seed) {
        super("Tournament Selection");
        this.T = T;     
        this.r = new Random(seed);
    }
    
    
    @Override
    public Population select(Population p) {
        int length =p.size();
        Population intermediatePopulation = new Population(length);
        
        for(int i=0; i<length; i++){
            Individual bestFit = pickTRandomIndividuals(p.getIndividuals(),r).stream().max(Individual::compareTo).get();
            intermediatePopulation.setIndividual(bestFit);
        }
        
        
        return intermediatePopulation;
    }
    
    
    
    
    
    private List<Individual> pickTRandomIndividuals(List<Individual> pop, Random r){
        int length = pop.size();
        
        if (length<T) return null;
        
        for(int i=length-1; i>=length-T; i--){
            Collections.swap(pop, i, r.nextInt(i+1));
        }
        
        
        return pop.subList(length-T, length);
    }
    
}
