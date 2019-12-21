# README #

This README describes the use of a Java implementation of a genetic algorithm used to select features for machine-learning purposes. The work is originally published in the XXXXXX journal.

### What is this repository for? ###

* This ditro contains the java executables to select attributes for machine-learning purposes. The files are written en java, and use Weka methods.

* Version 1.0 (21/12/19)

### How do I get set up? ###

SAGAFS (Species Adaptive Genetic Algorithm for Feature Selection)<br><br>

The ¨dist¨ folder containts the executable version of the SAGAFS, to run this code just type as follows:<br><br>
Java –Xmx (amount of memory) -jar SAGA_FS.jar number of runs (e.g., java -jar SAGA_FS.jar  1) <br><br>
To configure the hyperparameters, the file parameter.conf (the details for each hyperparameter are  described in SAGA_Hyperparameters) should be edited. <br><br><br>
SAGA_FS outputs a folder with two types of files: the first type are files of type .arff, SAGA_FS generates a .arff file per run, this is the database given as input but represented solely by the selected features of the best found individual; the second file contains a report of the best individual found per run. <br><br>
The code to edit is a Netbeans 8.2 project. To add a new classifier for computing the indivuals´ fitness the class Model.java should be edited in the following way:<br><br><br>
Add a method with the classifier name: for instance, if one wants to add one from libsvm it can be done as follows:<br><br>
private Classifier camp_SVM() {<br>
&nbsp;&nbsp;&nbsp;&nbsp;try {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LibSVM clf = new LibSVM();<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;clf.setOptions(weka.core.Utils.splitOptions("-S 0 -K 1 -D 4 -G 0.1 -R 1.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -Z "));<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;scheme_option = Arrays.toString(clf.getOptions());<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return clf;<br>
&nbsp;&nbsp;&nbsp;&nbsp;} catch (Exception ex) {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return null;<br>
&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;&nbsp;}<br>
The key point here is to exactly copy configuration of the used ML algorithm in Weka to the method clf.setOption.<br><br>
After this, we just need to add the method defined to the switch with a name of our preference, for instance,<br><br>
case "LIBSVM_new":<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;model = camp_SVM();<br>
&nbsp;&nbsp;&nbsp;&nbsp;learning_scheme = "weka.classifiers.functions.LibLinear"; //name <br>
&nbsp;&nbsp;&nbsp;&nbsp;break;<br><br>
Now, in parameter.conf we can add LIBSVM_new as a classifier, the rest is transparent for the user.<br>

### Who do I talk to? ###

* cbrizuel@cicese.edu.mx
