package saga_fs;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author beltran
 */
public class MAIN {

    private static int id = 0;

    public static int getID() {
        return id++;
    }

    private static String[] readConfFile() {
        String directory = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");
        String file = "parameter.conf";
        String filename = directory + sep + file;
        List<String> param = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            param = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
        }

        //param.add("--s=" + System.currentTimeMillis());
        String opt[] = new String[param.size()];
        opt = param.toArray(opt);
        return opt;
    }

    private static void saveMainResults(){
       
        
    }
    
    private static String createFolder(String foldername) {
        String userPath = System.getProperty("user.dir"); //"path  where the user is already working"
        
        //create file object
        File dir = new File(userPath 
                           + System.getProperty("file.separator")
                           + foldername);
        dir.mkdirs();
        return dir.getPath();
    }

    public static void main(String[] args) {
        String opt[];
        int nRun = 1;
        if (args.length == 1 && args[0].chars().allMatch( Character::isDigit )) {
            opt = readConfFile();
            nRun = Integer.parseInt(args[0]);
        } else {
            opt = args;
        }
        System.out.println(Arrays.toString(opt));

        String filename = opt[0];
        filename = filename.split("=")[1];
        File input = new File(filename);
        filename = input.getName();
        filename = filename.substring(0, filename.indexOf("."));
        String folder = createFolder(filename);
        if (!folder.equals("")) {
            FileWriter fstream = null;
            try {
                File log = new File(folder,filename + "_SAGA_runs.log");
                fstream = new FileWriter(log, true);
                 String header = "Run,"
                        +"Seed,"
                        +"Execution(ms),"
                        +"fitness,"
                        +"noFeatures,"
                        +"BestIndividual";
                fstream.write(header + "\n");
                for (int i = 0; i < nRun; i++) {
                    SAGA_FS ga = new SAGA_FS(opt);
                    System.out.println("Run \t" + i + "\nSeed: " + ga.getSeed() );
                    
                    List<Integer> run = ga.run();
                    fstream.write((i+1)+ ","
                                   +ga.getSeed() + ","
                                   + ga.getExecutionTime()+ ","
                                   + ga.getBestFiness() + ","
                                   + run.size()+ ","
                                   + run.toString() + "\n");
                    
                    File file = new File(folder, filename + "_SAGA_run" + getID() + ".arff");
                    System.out.println(file.getName());
                    System.out.println(file.getAbsoluteFile());
                    ga.writeInstances(run, file.getAbsolutePath());
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(MAIN.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fstream.close();
                } catch (IOException ex) {
                    Logger.getLogger(MAIN.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
