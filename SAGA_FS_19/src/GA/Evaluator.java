package GA;




import classifier.ModelPerformance;
import classifier.UseModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bio00
 */
public class Evaluator {

    private UseModel model;
    private final int nFeatures;

    public Evaluator(UseModel model, int nFeatures) {
        this.model= model;
        this.nFeatures = nFeatures;
    }

    

    /**
     *
     * @param population
     */
    public void evalPopulation(Population population) {
        
       // System.out.println("paralell fitness evaluation to the population \n");
       // long timeP = System.currentTimeMillis();
        population.getIndividuals().parallelStream().forEach((ind) -> {
            ModelPerformance eval = model.getEval((ArrayList<Integer>) ind.getIndexOfFeatures());
            double fitness = calcFitness(eval, ind.getChromosomeLength());
            ind.setFitness(fitness);
        });

        
    }
    
    public void evalPopulationSeqs(Population population){
        for(Individual ind: population.getIndividuals()){
            ModelPerformance eval = model.getEval((ArrayList<Integer>) ind.getIndexOfFeatures());
            double fitness = calcFitness(eval, ind.getChromosomeLength());
            ind.setFitness(fitness);
        }
    }
    
    public void evalPopulationPSyn(Population population){
        List<Individual>pop = Collections.synchronizedList(population.getIndividuals());
        synchronized(pop) {
    
        

        pop.parallelStream().forEach((ind) -> {
            ModelPerformance eval = model.getEval((ArrayList<Integer>) ind.getIndexOfFeatures());
            double fitness = calcFitness(eval, ind.getChromosomeLength());
            ind.setFitness(fitness);
        });}
    }
     
    
    public void evalPop(Population population){
        population.getIndividuals().parallelStream().forEach((Individual ind) -> ind.setFitness(Math.random()));
        System.out.println(population);
    }
    
    public synchronized double calcFitness(ModelPerformance perfomance, int subsetsize) {
       // return perfomance.getAcc() + (0.01*perfomance.getMCC()) + (0.001*(1 - (subsetsize/(nFeatures*1.0))));
       // return perfomance.getAcc() + 0.001 *(1 - (subsetsize/(nFeatures*1.0)));
       return Math.abs(perfomance.getMCC()) + 0.0001 *(1 - (subsetsize/(nFeatures*1.0)));
       //return perfomance.getMCC() + 0.0001 *(1 - (subsetsize/(nFeatures*1.0)));
        //return perfomance.getwROCArea() + 0.0001 *(1 - (subsetsize/(nFeatures*1.0)));
    }



}
