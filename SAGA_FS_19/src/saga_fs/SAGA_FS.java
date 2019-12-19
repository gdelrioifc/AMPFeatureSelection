/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saga_fs;

import GA.Evaluator;
import GA.performances.GAPerformances;
import GA.GeneticAlgorithm;
import GA.Individual;
import GA.Population;
import GA.performances.Statistics;
import classifier.Dataset;
import classifier.Model;
import classifier.UseModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.*;

/**
 *
 * @author beltran
 */
public final class SAGA_FS {

    private UseModel useModel;
    private Dataset dataset;
    private Evaluator evaluator;

    private Options options;

    //GA variables
    private int pop_size;
    private int maxGenerations;
    private int nGenerationWithoutImprovement; // number of generations without improvement 
    private double mutationRate;
    private double crossoverRate;
    private int tournamentSize;
    private long seed;
    private long executionTime;
    private double bestFiness;

    //problem variables
    private int lBound;
    private int hBound;

    public SAGA_FS(String filename, String label) {
        dataset = new Dataset(filename);
        useModel = new UseModel(dataset, new Model(label,1));
        evaluator = new Evaluator(useModel, dataset.getnFeatures());

    }

    public SAGA_FS(String[] parameters) {
        this.options = createOptions();
        setOptions(parameters);
    }

    public void setOptions(String[] args) {
        //create the parser
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                formatter.printHelp("SAGA_FS", options);
            }

            initParameter(cmd);

        } catch (Exception exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            formatter.printHelp("SAGA_FS", options);
        }
    }

    private void initParameter(CommandLine cmd) {
        String parametersSTR = "";
        this.dataset = new Dataset(cmd.getOptionValue("inputFile"));
        String cls = cmd.getOptionValue("classifier");


        this.pop_size = cmd.hasOption("popSize") ? Integer.parseInt(cmd.getOptionValue("popSize").trim()) : 64;
        parametersSTR +=" --popSize=" + pop_size;
        
        this.maxGenerations = cmd.hasOption("maxGen") ? Integer.parseInt(cmd.getOptionValue("maxGen").trim()) : 200;
        parametersSTR +=" --maxGen=" + maxGenerations;
        
        this.nGenerationWithoutImprovement = (int) (maxGenerations / 10.0);
      
        if (cmd.hasOption("ngwi")) {
            this.nGenerationWithoutImprovement = (int) (maxGenerations / (Integer.parseInt(cmd.getOptionValue("ngwi")) * 1.0));
        }
        parametersSTR +=" --ngwi=" + nGenerationWithoutImprovement;
        
        this.mutationRate = cmd.hasOption("pm") ? Double.parseDouble(cmd.getOptionValue("pm")) : 0.1;
        parametersSTR +=" --pm=" + mutationRate;
        
        this.crossoverRate = cmd.hasOption("pc") ? Double.parseDouble(cmd.getOptionValue("pc")) : 0.8;
        parametersSTR +=" --pc=" + crossoverRate;
        
        this.tournamentSize = cmd.hasOption("ts") ? Integer.parseInt(cmd.getOptionValue("ts")) : 2;
        parametersSTR +=" --ts=" + tournamentSize;
        
        this.seed = cmd.hasOption("s") ? Long.parseLong(cmd.getOptionValue("s")) : System.currentTimeMillis();
        parametersSTR +=" --s=" + seed;
        
       this.lBound= 1;
       if (cmd.hasOption("lb")){
           //this.lBound = (int) (dataset.getnFeatures() / Double.parseDouble(cmd.getOptionValue("lb")));
           this.lBound=(int)(dataset.getnFeatures() * (Double.parseDouble(cmd.getOptionValue("lb"))/100.0));
       }
       
       this.hBound= (int)(dataset.getnFeatures()*(10.0/100.0));
       if (cmd.hasOption("ub")){
           this.hBound = (int)(dataset.getnFeatures() * (Double.parseDouble(cmd.getOptionValue("ub"))/100.0));
       }
       this.hBound = hBound <= 1 ? 2 : hBound;
       
       this.lBound = (lBound < 1 || lBound > hBound) ? 1 : lBound;
       parametersSTR +=" --lb=" + lBound;
       parametersSTR +=" --ub=" + hBound;
       
       
       

        this.useModel = new UseModel(dataset, new Model(cls, this.seed));
        this.evaluator = new Evaluator(useModel, dataset.getnFeatures());
       
       
       System.out.println("===========RUN INFO========================= \n"
                          +useModel.toString()
                          +"Search algorithm: Adaptive Genetic Algorithm "
                          +parametersSTR +"\n");
    }

    private Options createOptions() {
        Options options = new Options();

        options.addOption("h", "help", false, "print this message");

        options.addOption(OptionBuilder.withLongOpt("popSize")
                .withArgName("SIZE")
                .hasArgs()
                .withDescription("Number of individuals in a population")
                .create());

        options.addOption(OptionBuilder.withLongOpt("maxGen")
                .withArgName("number of generations")
                .hasArgs()
                .withDescription("Maximum number of generations")
                .create());

        options.addOption(OptionBuilder.withLongOpt("ngwi")
                .withArgName("Percentage of maxGen")
                .hasArgs()
                .withDescription("Number of generation without improvement")
                .create());

        options.addOption(OptionBuilder.withLongOpt("pm")
                .withArgName("float number in the range [0,1]")
                .hasArgs()
                .withDescription("Mutation rate")
                .create());

        options.addOption(OptionBuilder.withLongOpt("pc")
                .withArgName("float number in the range [0,1]")
                .hasArgs()
                .withDescription("Crossover rate")
                .create());

        options.addOption(OptionBuilder.withLongOpt("ts")
                .withArgName("SIZE")
                .hasArgs()
                .withDescription("Tournamen size")
                .create());

        options.addOption(OptionBuilder.withLongOpt("s")
                .withArgName("number")
                .hasArgs()
                .withDescription("The value of the randomSeed")
                .create());

        options.addOption(OptionBuilder.withLongOpt("lb")
                .withArgName("The lower bound is the percentage of input features that will have at least each individual of initial population.")
                .hasArgs()
                .withDescription("The value of the randomSeed")
                .create());
        options.addOption(OptionBuilder.withLongOpt("ub")
                .withArgName("The upper bound is the percentage of input features that will have at most each individual of initial population.")
                .hasArgs()
                .withDescription("The value of the randomSeed")
                .create());
        options.addOption(OptionBuilder.withLongOpt("inputFile")
                .withArgName("ARFF file")
                .hasArgs()
                .isRequired()
                .withDescription("PATH of training file")
                .create());

        options.addOption(OptionBuilder.withLongOpt("classifier")
                .withArgName("SMO|MP|LIBLINEAR")
                .hasArgs()
                .isRequired()
                .withDescription("abbreviation for the machine learning classifier")
                .create());

        return options;
    }

    public List<Integer> run() {
        System.out.println("=============RUN============================");
        Statistics stat = new Statistics();
        executionTime = System.nanoTime();
        GeneticAlgorithm ga = new GeneticAlgorithm(this.pop_size, 
                                                   this.mutationRate, 
                                                   this.crossoverRate,
                                                   this.tournamentSize,
                                                   this.seed, 
                                                   dataset.getnFeatures());
        
        double lossOfDiversity = Double.POSITIVE_INFINITY;
        int generation = 1;
        
       //Initialize population
        Population population = ga.initPopulation(lBound, hBound);

        //Evaluate population
        evaluator.evalPopulation(population);
        //evaluator.evalPopulationSeqs(population);
        Individual best = population.getFittest(0);
        
        double fittest = best.getFitness();

        while (ga.isTerminationConditionMet(generation, this.maxGenerations) == false && ga.isTerminationConditionMet(this.nGenerationWithoutImprovement, fittest) == false) {
            System.out.println(population);
            System.out.println("Best solution: " + population.getFittest(0).toString());
            
            GAPerformances before = stat.computeSts(population.getIndividuals());
            double sPop = stat.similarity_percent(before.getFitnessDistribution(), this.pop_size);
            
            System.out.println("psimilarity:\t" + sPop);
            //System.out.println(before);
            //System.out.println("AFTER SELECTION");

            Population parentSelection = ga.parentSelection(population);

            GAPerformances after = stat.computeSts(parentSelection.getIndividuals());
            
            //System.out.println(after);
            System.out.println("REPRODUCTION RATE");
            
            stat.getReproductionRate(before.getFitnessDistribution(), after.getFitnessDistribution()).forEach((k, v) -> System.out.println(k + "->" + v));
            
            lossOfDiversity = stat.getLossOfDiversity(before.getFitnessDistribution(), after.getFitnessDistribution(), population.getPopulationSize());
            
            System.out.println("\nLOSS OF DIVERSITY\n" + lossOfDiversity);

            Population offsprings = ga.crossover(parentSelection);
            //ga.mutation(offsprings);

            //ga.self_adaptive_mutation(population, sPop, 0.15, 0.01);
            ga.self_adaptive_mutation(offsprings, sPop, 0.15, 0.01);
            evaluator.evalPopulation(offsprings);
            //evaluator.evalPopulationSeqs(offsprings);
            population = ga.survivorSelection(population, offsprings);

            //Increment the current generation
            generation++;
            System.out.println("generation: \t" + generation);
            best = getBestSolution(best,population.getFittest(0));
            
            fittest = best.getFitness();
        }
        executionTime = System.nanoTime() - executionTime;
        bestFiness = best.getFitness();
        System.out.println("time:\t" + executionTime + " ns");
        System.out.println("Best Individual: \n acc \t\t\t attribute set \n" + best.getFitness() + "\t\t\t" + best.toString());
        System.out.println(useModel.toString());

        System.out.println(dataset.selectedAttributes((ArrayList<Integer>) best.getIndexOfFeatures()));
        useModel.evaluator((ArrayList<Integer>) best.getIndexOfFeatures());
        return best.getIndexOfFeatures();

    }
    public String run2() {
        Statistics stat = new Statistics();
        long executionTime = System.nanoTime();
        GeneticAlgorithm ga = new GeneticAlgorithm(this.pop_size, 
                                                   this.mutationRate, 
                                                   this.crossoverRate,
                                                   this.tournamentSize,
                                                   this.seed, 
                                                   dataset.getnFeatures());
        
        double lossOfDiversity = Double.POSITIVE_INFINITY;
        int generation = 1;
        
       //Initialize population
        Population population = ga.initPopulation(lBound, hBound);

        //Evaluate population
        evaluator.evalPopulation(population);
        
        Individual best = population.getFittest(0);
        double fittest = best.getFitness();

        while (ga.isTerminationConditionMet(generation, this.maxGenerations) == false && ga.isTerminationConditionMet(this.nGenerationWithoutImprovement, fittest) == false) {
            //System.out.println(population);
            //System.out.println("Best solution: " + population.getFittest(0).toString());
            
            GAPerformances before = stat.computeSts(population.getIndividuals());
            double sPop = stat.similarity_percent(before.getFitnessDistribution(), this.pop_size);
            
            //System.out.println("psimilarity:\t" + sPop);
            //System.out.println(before);
            //System.out.println("AFTER SELECTION");

            Population parentSelection = ga.parentSelection(population);

            GAPerformances after = stat.computeSts(parentSelection.getIndividuals());
            
            //System.out.println(after);
            //System.out.println("REPRODUCTION RATE");
            
           // stat.getReproductionRate(before.getFitnessDistribution(), after.getFitnessDistribution()).forEach((k, v) -> System.out.println(k + "->" + v));
            
            lossOfDiversity = stat.getLossOfDiversity(before.getFitnessDistribution(), after.getFitnessDistribution(), population.getPopulationSize());
            
           // System.out.println("\nLOSS OF DIVERSITY\n" + lossOfDiversity);

            Population offsprings = ga.crossover(parentSelection);
            //ga.mutation(offsprings);

            ga.self_adaptive_mutation(population, sPop, 0.15, 0.01);
            
            evaluator.evalPopulation(offsprings);
            population = ga.survivorSelection(population, offsprings);

            //Increment the current generation
            generation++;
           // System.out.println("generation: \t" + generation);
            best = population.getFittest(0);
            fittest = best.getFitness();
        }
        executionTime = System.nanoTime() - executionTime;
       // System.out.println("time:\t" + executionTime + " ns");
       // System.out.println("Best Individual: \n acc \t\t\t attribute set \n" + best.getFitness() + "\t\t\t" + best.toString());
       // System.out.println(useModel.toString());

      //  System.out.println(dataset.selectedAttributes((ArrayList<Integer>) best.getIndexOfFeatures()));
      //  useModel.evaluator((ArrayList<Integer>) best.getIndexOfFeatures());
      //  return best.getIndexOfFeatures();
       return fittest + "," + best.getChromosomeLength() + "," + executionTime;
    }

    
    public void writeInstances(List<Integer> fs, String fileoutput) {
        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(new File(fileoutput)));
            writer.write(useModel.getFSInstances((ArrayList<Integer>) fs, fileoutput).toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SAGA_FS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        Options options = new Options();
//
//        Option inputFile = new Option("i", "input", true, "input fasta file path");
//        inputFile.setRequired(true);
//        options.addOption(inputFile);
//
//        Option cls = new Option("c", "classifier", true, "select classfier");
//        cls.setRequired(true);
//        options.addOption(cls);
//
//        CommandLineParser parser = new DefaultParser();
//        HelpFormatter formatter = new HelpFormatter();
//        CommandLine cmd;
//
//        try {
//            cmd = parser.parse(options, args);
//        } catch (ParseException e) {
//            System.out.println(e.getMessage());
//            formatter.printHelp("utility-name", options);
//
//            System.exit(1);
//            return;
//        }
//
//        String filename = cmd.getOptionValue("input");
//        String classifier = cmd.getOptionValue("classifier");
//
//        //load dataset
////        String filename = "/home/beltran/Seq2Mol/MODAMP/tests/camp/CAMP_dataset_G.arff";
////        String classifier = "LIBLINEAR";
////        String filename = "/home/beltran/ProyectoGabriel/Jamming1Set_arff/1GVPA_1ZG4A_2NPHA_2P9HA_2ZD1A_3LZMA-ProtDCal_RemoveUseless/1GVPA_1ZG4A_2NPHA_2P9HA_2ZD1A_3LZMA-ProtDCal_RemoveUseless_corrSpearman98_IG.arff";
////        //String filename = "/home/beltran/ProyectoGabriel/datosresultadosautoweka/jamming1set_arff/1GVPA_1ZG4A_2NPHA_2P9HA_2ZD1A_3LZMA-ProtDCal_RemoveUseless.arff";
//        // String filename = "/home/beltran/FS_project_17/database/Fernades/fernandes_datase_wid.arff";
//        String output = filename.substring(0, filename.indexOf(".")) + "_ACCRBF_liblinear.arff";
//
//        SAGA_FS ga = new SAGA_FS(filename, classifier);
//        List<Integer> run = ga.run();
//        // Integer[] features = {257, 4, 261, 137, 10, 141, 13, 272, 146, 148, 22, 150, 25, 28, 32, 34, 35, 39, 42, 171, 44, 45, 174, 46, 48, 50, 53, 182, 188, 61, 191, 65, 205, 77, 206, 207, 208, 211, 83, 86, 88, 90, 92, 97, 233, 235, 115, 116, 117, 251, 125};
//
//        //Integer [] features ={0, 1, 130, 6, 7, 135, 73, 12, 206, 142, 17, 82, 20, 212, 24, 90, 92, 159, 161, 33, 34, 103, 109, 116, 52, 119, 56, 121, 185, 126};
////Integer [] features ={161, 193, 66, 198, 6, 7, 72, 74, 77, 206, 16, 112, 48, 176, 113, 178, 83, 117, 122, 27, 124, 127, 159};
//        //Integer[] features = {0, 640, 257, 131, 260, 264, 521, 650, 529, 658, 280, 153, 155, 33, 161, 677, 39, 554, 428, 172, 556, 431, 688, 690, 183, 698, 188, 192, 194, 326, 582, 199, 584, 586, 78, 207, 464, 595, 214, 602, 90, 220, 478, 351, 223, 352, 615, 231, 616, 104, 618, 620, 367, 369, 371, 244, 246, 122, 378, 634, 123, 380, 124, 382, 638};
//        //ArrayList<Integer> run = new ArrayList<>(Arrays.asList(features));
//        ga.writeInstances(run, output);
//
//    }
    
//    public static void main(String[] args) {
//        String filename="/home/beltran/weka-3-8-1/data/iris.arff";
//        String output = filename.substring(0, filename.indexOf(".")) + "_SAGA_LIBLINEAROPT_MCC.arff";
//        String Ext="_SAGA_BB_MCC";
//        String [] opt ={"--inputFile=" + filename,
//                        "--classifier=TEST", "--ub=50",
//                        "--popSize=4",
//                        "--maxGen=500",
//                        "--ngwi=10",
//                        "--pm=0.05" ,
//                        "--pc=0.7",
//                        "--s=" + System.currentTimeMillis()};
//        //for(int i=0; i<30; i++){
//            SAGA_FS ga = new SAGA_FS(opt);
//        List<Integer> run = ga.run();
//        //Integer[] features={133, 134, 264, 16, 147, 20, 277, 22, 153, 31, 164, 168, 297, 43, 49, 306, 50, 51, 179, 308, 314, 189, 318, 319, 191, 320, 321, 325, 328, 329, 208, 209, 212, 84, 213, 215, 89, 218, 219, 95, 224, 100, 229, 105, 233, 108, 114, 119, 124};
//        //List<Integer> run =new ArrayList<>(Arrays.asList(features)); //run = ga.run();
//        //output +=Ext+ ".arff";
//        //ga.writeInstances(run, output);
//        //}
//    }

    private Individual getBestSolution(Individual best, Individual fittest) {
        if(fittest.getFitness()>best.getFitness())
            return fittest;
        return best;
    }

    public long getSeed() {
        return seed;
    }

    public long getExecutionTime() {
        double nanosecond_to_miliseconds = 1e-6;
        return (long) (executionTime*nanosecond_to_miliseconds);
    }

    public double getBestFiness() {
        return bestFiness;
    }
    
    
    

}
