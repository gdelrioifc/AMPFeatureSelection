/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.crossover;

import GA.Individual;
import GA.Population;
import java.util.List;

/**
 *
 * @author beltran
 * Recombination the process whereby a new individual solution is created from
 * the information contained within two parent solutions, is considered by many
 * to be one of the most important features in genetic algorihtms.
 */
public abstract class Crossover {
    private final String name;
    final double pC; //Crossoverrate which is typically in the range [0.5, 1.0]
     public Crossover(String name, double pC) {
        this.name = name;
        this.pC=pC;
    }
     
    public abstract Population recombine(Population p); 
    
    public abstract List<Individual> recombine(Individual parent1, Individual parent2);
}
