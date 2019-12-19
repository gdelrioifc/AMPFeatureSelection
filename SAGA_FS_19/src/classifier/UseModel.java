/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author beltran
 */
public class UseModel {

    private Dataset dataset;
    private Model model;
    private Remove rm;

    public UseModel(Dataset dataset, Model model) {
        this.dataset = dataset;
        this.model = model;
        this.rm = new Remove();
    }

    public ModelPerformance getEval(ArrayList<Integer> g) {
        if (g.size() <= this.dataset.getnFeatures()) {
            ArrayList<Integer> vec = (ArrayList<Integer>) g.clone(); //copy of array g to vec
            vec.add(dataset.getIndexClass()); //add index of class to vector vec
            int indexFeature[] = vec.stream().mapToInt(i -> i).toArray();
            Instances datasetMod = delFeatures(indexFeature);
            return model.fit(datasetMod, false);

        } else {
            System.err.print("Error: the vector size is bigger than the total number of features");
            return null;
        }
    }

    public void evaluator(ArrayList<Integer> g) {
        if (g.size() <= this.dataset.getnFeatures()) {
            ArrayList<Integer> vec = (ArrayList<Integer>) g.clone(); //copy of array g to vec
            vec.add(dataset.getIndexClass()); //add index of class to vector vec
            int indexFeature[] = vec.stream().mapToInt(i -> i).toArray();
            Instances datasetMod = delFeatures(indexFeature);
            model.fit(datasetMod, true);

        } else {
            System.err.print("Error: the vector size is bigger than the total number of features");
        }
    }

    public Instances getFSInstances(ArrayList<Integer> g, String filename) {
        if (g.size() <= this.dataset.getnFeatures()) {
            ArrayList<Integer> vec = (ArrayList<Integer>) g.clone(); //copy of array g to vec
            vec.add(dataset.getIndexClass()); //add index of class to vector vec
            int indexFeature[] = vec.stream().mapToInt(i -> i).toArray();
            Instances datasetMod = delFeatures(indexFeature);
            datasetMod.setRelationName(filename);
            return datasetMod;

        } else {
            System.err.print("Error: the vector size is bigger than the total number of features");
            return null;
        }
    }

    private synchronized Instances delFeatures(int[] indexFeatures) {
        try {
            this.rm.setInvertSelection(true);
            this.rm.setAttributeIndicesArray(indexFeatures);
            this.rm.setInputFormat(this.dataset.getDataset());
            return Filter.useFilter(this.dataset.getDataset(), rm);
        } catch (Exception ex) {
            Logger.getLogger(UseModel.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error in removeAttributes");
            return null;
        }

    }

    public String toString() {
        return "Attribute subset Evaluator: \n"
                + dataset + "\n"
                + model.toString() + "\n";
    }

//    public static void main(String[] args) {
//        String filename = "/home/beltran/FS_project_17/database/Fernades/fernandes_datase_wid.arff";
//        Model model = new Model("RFOPT",1);
//        Dataset dataset = new Dataset(filename);
//        UseModel uModel = new UseModel(dataset, model);
//
//        Integer[] features = {37, 165, 263, 41, 10, 142, 83, 53, 21, 152, 56, 27, 92, 93};
//
//
//        ArrayList<Integer> fs = new ArrayList<>(Arrays.asList(features));
//
//        //  Collections.sort(fs);
//        //ModelPerformance fit = model.fit(dataset.getDataset());
//        uModel.evaluator(fs);
//        ModelPerformance fit = uModel.getEval(fs);
//        System.out.println("ACC:\t" + fit.getAcc());
//        System.out.println("MCC:\t" + fit.getMCC());
//        System.out.println("wPrecision\t" + fit.getWPrecision());
//        System.out.println("F-score\t" + fit.getwFmeasure());
//        System.out.println("recall:\t" + fit.getwRecall());
//        System.out.println("ROC:\t " + fit.getwROCArea());
//        System.out.println("subsetSize" + fs.size());
//        System.out.println(dataset);
//        Evaluator eval = new Evaluator(uModel, 8);
//        // System.out.println( fit.getwFmeasure() + 0.001 *(1 - (1/(8*1.0))));
//        System.out.println(eval.calcFitness(fit, fs.size()));
//
//    }

}
