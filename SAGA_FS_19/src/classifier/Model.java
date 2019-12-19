/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibLINEAR;

import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
 *
 * @author beltran
 */
public class Model {

    private Classifier model;
    private Random rand;
    private final int FOLDS;
    private final long SEED;
    private String learning_scheme;
    private String scheme_option;

    public Model(String modelLabel, long seed) {
        learning_scheme = "Learning Scheme:\t";
        scheme_option = "Scheme Options: \t";
        switch (modelLabel) {
            case "CAMP_RF":
                model = camp_RF();
                learning_scheme = "weka.classifiers.trees.RandomForest";
                break;
            case "CAMP_SVM4":
                model = camp_SVM4();
                learning_scheme = "weka.classifiers.functions.libsvm";
                break;
            case "CAMP_SVM":
                model = camp_SVM();
                learning_scheme = "weka.classifiers.functions.LibLinear";
                break;
            case "ANFIS_SVM":
                model = anfis_SVM();
                learning_scheme = "weka.classifiers.functions.LibLinear";
                break;

        }
        this.SEED = seed;
        this.rand = new Random(seed);
        this.FOLDS = 10;

    }

    private Classifier camp_SVM4() {
        try {
            LibSVM clf = new LibSVM();
            clf.setOptions(weka.core.Utils.splitOptions("-S 0 -K 1 -D 4 -G 0.1 -R 1.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -Z "));
            scheme_option = Arrays.toString(clf.getOptions());
            return clf;
        } catch (Exception ex) {
            return null;
        }
    }

    private Classifier camp_RF() {
        try {
            RandomForest clf = new RandomForest();
            clf.setOptions(weka.core.Utils.splitOptions("-P 100 -I 150 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"));
            scheme_option = Arrays.toString(clf.getOptions());
            return clf;
        } catch (Exception ex) {
            return null;
        }
    }

    private Classifier camp_SVM() {
        try {
            LibLINEAR clf = new LibLINEAR();
            clf.setOptions(weka.core.Utils.splitOptions("-S 1 -C 1.0 -E 0.001 -B 1.0 -L 0.1 -I 1000"));
            scheme_option = Arrays.toString(clf.getOptions());
            return clf;
        } catch (Exception ex) {

            return null;
        }
    }

    private Classifier anfis_SVM() {
        try {
            LibLINEAR clf = new LibLINEAR();
            clf.setOptions(weka.core.Utils.splitOptions("-S 1 -C 0.01 -E 0.001 -B 1.0 -L 0.1 -I 1000"));
            scheme_option = Arrays.toString(clf.getOptions());
            return clf;
        } catch (Exception ex) {

            return null;
        }
    }

    public ModelPerformance fit(Instances dataset, boolean verbose) {

        try {
            rand = new Random(this.SEED);
            Evaluation eval = new Evaluation(dataset);
            eval.crossValidateModel(model, dataset, FOLDS, rand);
            if (verbose) {
                System.out.println(eval.toSummaryString("\nResults\n======\n", true));
                System.out.println(eval.toClassDetailsString("\n Details\n=========\n"));
                System.out.println(eval.toMatrixString("\nconfusion matrix\n=========\n"));
            }
            return new ModelPerformance(eval.pctCorrect(),
                    eval.weightedMatthewsCorrelation(),
                    eval.weightedPrecision(),
                    eval.weightedRecall(),
                    eval.weightedFMeasure(),
                    eval.weightedAreaUnderROC());

        } catch (Exception ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error in the evaluation model");

        }
        return null;

    }

    @Override
    public String toString() {
        return learning_scheme + "\n" + scheme_option + "\n"
                + "Number of folds for estimation: \t" + this.FOLDS;
    }

}
