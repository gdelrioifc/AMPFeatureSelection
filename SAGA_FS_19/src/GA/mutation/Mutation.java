/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.mutation;

import GA.Individual;
import GA.Population;

/**
 *
 * @author beltran
 */
public abstract class Mutation {
     private final String name;
     double pm; // mutation rate
    
    public Mutation(String name, double pm) {
        this.name = name;
        this.pm = pm;
    }
    public abstract void mutation(Population offsprings); 
    
    public abstract void mutationInd(Individual offspring);
    
    public abstract void adaptiveMutationRate(double delta);
    public double getPm(){return this.pm;}
    public void setPm(double pm){this.pm = pm;}
}
