/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saga_fs;

import GA.Evaluator;
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

/**
 *
 * @author beltran
 */
public class SaveSubsetDataset {

    private Dataset dataset;
    private UseModel useModel;

    public SaveSubsetDataset(String filename, String label) {
        dataset = new Dataset(filename);
        useModel = new UseModel(dataset, new Model(label,1));
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

//    public static void main(String[] args) {
//        String filename = "/home/beltran/FS_project_17/database/Xiao/Training/fastaFiles/Xiao_Blindset.arff";
//       // String filename = "/home/beltran/FS_project_17/database/CAMP_10/FASTA/FASTA/CAMP10_dataset_3AUG17.arff";
//        SaveSubsetDataset save = new SaveSubsetDataset(filename, "RFOPT");
//        File file = new File(filename);
//        String output = file.getName();
//        output = output.substring(0, output.indexOf(".")); //+ "_SAGA_BB_MCC.arff";
//        String Ext = "_SAGA_RF";
//        output += Ext + "5.arff";
//        System.out.println(output);
//
//
//       Integer[] features = {193, 2, 258, 66, 259, 132, 4, 133, 6, 265, 74, 11, 206, 142, 207, 79, 208, 80, 19, 83, 85, 149, 22, 23, 91, 93, 94, 223, 96, 163, 99, 35, 109, 48, 54, 55, 183, 56};
//
//
//
//
//
//
//
//
//        ArrayList<Integer> fs = new ArrayList<>(Arrays.asList(features));
//        save.writeInstances(fs, output);

//    }

}
