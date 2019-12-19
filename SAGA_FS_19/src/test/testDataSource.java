/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author abeltran
 */
public class testDataSource {

    public static void main(String[] args) throws Exception {
        String filename = "/home/abeltran/repos/sagafs_2018/3-ExperimentOne/2-Data/CAMP_14/523665231/CAMP14_train_523665231.csv";
        //DataSource source = new DataSource(filename);
        CSVLoader csv = new CSVLoader();
        csv.setFile(new File(filename));
        Instances instances = csv.getDataSet();
    }

}
