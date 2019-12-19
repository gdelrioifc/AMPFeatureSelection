/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.mutation;

import GA.Individual;
import GA.Population;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 *
 * @author beltran
 */
public class KInDels extends Mutation {

    private final int lc; //chromosome length
    private final int nMutations;
    private final Random rand;
    private final double lBound;
    private final double uBound;

    public KInDels(double pm, int chLength, long seed) {
        super("KInDels", pm);
        this.lc = chLength;
        nMutations = 1;   //(int) (0.1 * lc);
        rand = new Random(seed);
        this.lBound = 0.01;
        this.uBound = 0.99;
    }

    @Override
    public void mutation(Population offsprings) {
        List<Individual> individuals = offsprings.getIndividuals();

        for (Individual ind : individuals) {
            if(rand.nextDouble()<this.getPm()) 
               mutationInd(ind);
        }
    }

    @Override
    public void mutationInd(Individual offspring) {
        // System.out.println("aft: "+offspring.getChromosome());
        int nTimes = nMutations == 0 ? 1 : rand.nextInt(nMutations) + 1;
        for (int i = 0; i < nTimes; i++) {

            inDel(offspring);
        }
        // System.out.println("bef:" + offspring.getChromosome());
    }

    public void insertFeature(Individual offspring) {
        HashSet<Integer> chromosome = offspring.getChromosome();
        Integer feature;
        do {
            feature = rand.nextInt(lc);
        } while (chromosome.contains(feature));
        chromosome.add(feature);
        

    }

    public void inDel(Individual offspring) {
        HashSet<Integer> chromosome = offspring.getChromosome();

        Integer feature = rand.nextInt(lc);
        if (chromosome.contains(feature) && chromosome.size() > 1) {
            chromosome.remove(feature);
        } else {
            chromosome.add(feature);
        }
    }

    public void positional(Individual offspring) {
        HashSet<Integer> chromosome = offspring.getChromosome();

        if (chromosome.size() < lc) {

            Integer pos = rand.nextInt(chromosome.size());
            Integer feature;
            do {
                feature = rand.nextInt(lc);
            } while (chromosome.contains(feature));
            boolean remove;
            remove = chromosome.remove(chromosome.toArray()[pos]);
            chromosome.add(feature);
        }
    }

    @Override
    public void adaptiveMutationRate(double delta) {
        if (delta > 0) {
            this.setPm((this.getPm() + delta) <= uBound ? this.getPm() + delta : uBound);
        } else {
            this.setPm((this.getPm() + delta) >= lBound ? this.getPm() + delta : lBound);
        }
    }

}
