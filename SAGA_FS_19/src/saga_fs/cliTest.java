/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saga_fs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 * @author beltran
 */
public class cliTest {

    private Options options;

    public cliTest() {
        this.options = createOptions();
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

    public void setOption(String[] args) {
        //create the parser
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                formatter.printHelp("SAGA_FS", options);
            }
            System.out.println(cmd.getOptionValue("inputFile"));
        } catch (Exception exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            formatter.printHelp("SAGA_FS", options);
        }
    }

//    public static void main(String[] args) {
//        cliTest cli = new cliTest();
//        String a[] = {"--inputFile=algo.arff", "--classifier=SMO"};//{"-popsize", "100"};
//        cli.setOption(a);
//
//    }

}
