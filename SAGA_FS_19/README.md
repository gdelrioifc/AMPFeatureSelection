# README #

This README describes the use of a Java implementation of a genetic algorithm used to select features for machine-learning purposes. The work is originally published in the XXXXXX journal.

### What is this repository for? ###

* This ditro contains the java executables to select attributes for machine-learning purposes. The files are written en java, and use Weka methods.

* Version 1.0 (21/12/19)

### How do I get set up? ###

SAGAFS (Species Adaptive Genetic Algorithm for Feature Selection)

The ¨dist¨ folder containts the executable version of the SAGAFS, to run this code just type as follows:<br>
Java –Xmx (amount of memory) -jar SAGA_FS.jar number of runs (e.g., java -jar SAGA_FS.jar  1) <br>
To configure the hyperparameters, the file parameter.conf (the details for each hyperparameter are  described in SAGA_Hyperparameters) should be edited. <br>
SAGA_FS outputs a folder with two types of files: the first type are files of type .arff, SAGA_FS generates a .arff file per run, this is the database given as input but represented solely by the selected features of the best found individual; the second file contains a report of the best individual found per run. <br>
The code to edit is a Netbeans 8.2 project. To add a new classifier for computing the indivuals´ fitness the class Model.java should be edited in the following way:<br>
Add a method with the classifier name: for instance, if one wants to add one from libsvm it can be done as follows:<br>
private Classifier camp_SVM() {<br>
        try {<br>
            LibSVM clf = new LibSVM();<br>
            clf.setOptions(weka.core.Utils.splitOptions("-S 0 -K 1 -D 4 -G 0.1 -R 1.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -Z "));<br>
            scheme_option = Arrays.toString(clf.getOptions());<br>
            return clf;<br>
        } catch (Exception ex) {<br>
            return null;<br>
        }<br>
    }<br>
The key point here is to exactly copy configuration of the used ML algorithm in Weka to the method clf.setOption.<br>
After this, we just need to add the method defined to the switch with a name of our preference, for instance,
case "LIBSVM_new":<br>
                model = camp_SVM();<br>
                learning_scheme = "weka.classifiers.functions.LibLinear"; //name <br>
                break;<br>
Now, in parameter.conf we can add LIBSVM_new as a classifier, the rest is transparent for the user.<br>

### Who do I talk to? ###

* cbrizuel@cicese.edu.mx
