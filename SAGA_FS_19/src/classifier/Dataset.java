/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 *
 * @author beltran
 */
public class Dataset {

    private Instances dataset;
    private int nFeatures;
    private int nInstances;
    private int nClasses;
    private int indexClass;

    public Dataset(String filename) {
        ConverterUtils.DataSource source = null;
        try {
            if (filename.endsWith(".csv")) {
                CSVLoader csv = new CSVLoader();
                csv.setFile(new File(filename));
                this.dataset = csv.getDataSet();

            } else {
                source = new ConverterUtils.DataSource(filename);
                this.dataset = source.getDataSet();
            }

            //this.dataset =normalizeData(source.getDataSet());
            this.indexClass = this.dataset.numAttributes() - 1;
            this.dataset.setClassIndex(this.indexClass); //selecting index where are the classes or target varaibles
            this.nFeatures = this.dataset.numAttributes() - 1;
            this.nInstances = this.dataset.numInstances();
            this.nClasses = this.dataset.numClasses();

            if (this.dataset.attribute(this.indexClass).isNumeric()) {
                this.dataset = numeric2NominalClass(dataset);
            }

        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Data normalization is the process of rescaling one or more attributes to
     * the range of 0 to 1. This means that the largest value for each attribute
     * is 1 and the smalles value is 0. you can use other scales such as -1 to
     * 1, which is useful when using support vector machines.
     *
     * @param dataset
     * @return
     */
    private Instances normalizeData(Instances dataset) {
        try {
            //Normalize training data
            Normalize norm = new Normalize();
            norm.setScale(2.0);
            norm.setTranslation(-1.0);
            norm.setInputFormat(dataset);
            Instances normDataset = Filter.useFilter(dataset, norm);
            return normDataset;
        } catch (Exception ex) {
            System.err.println("Unable to normalize data");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    private Instances numeric2NominalClass(Instances data) {
        try {
            NumericToNominal convert = new NumericToNominal();
            convert.setAttributeIndicesArray(new int[]{data.numAttributes() - 1});
            convert.setInputFormat(data);
            return Filter.useFilter(data, convert);

        } catch (Exception ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Instances getDataset() {
        return dataset;
    }

    public int getnFeatures() {
        return nFeatures;
    }

    public int getnInstances() {
        return nInstances;
    }

    public int getnClasses() {
        return nClasses;
    }

    public int getIndexClass() {
        return indexClass;
    }

    public String selectedAttributes(ArrayList<Integer> g) {
        String selec = "Selected attributes: ";
        String names = "";

        for (Integer i : g) {
            selec += i + ",";
            names += dataset.attribute(i).name() + "\n";
        }
        selec = selec.substring(0, selec.length() - 1) + ":" + g.size();
        return selec + "\n" + names;
    }

    public String toString() {
        return "numAttributes: \t" + this.nFeatures + "\n"
                + "numInstances: \t" + this.nInstances + "\n"
                + "numClasses: \t" + this.nClasses;
    }

}
