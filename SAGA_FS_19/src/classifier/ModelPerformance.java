/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

/**
 *
 * @author beltran
 */
public class ModelPerformance {

    private final double Acc; // Percentage of correctly classified instances 
    private final double MCC; //Mattew Correlation Coeficient
    private final double WPrecision;
    private final double wRecall;
    private final double wFmeasure;
    private final double wROCArea;

    public ModelPerformance(double Acc, double MCC, double WPrecision, double wRecall, double wFmeasure, double wROCArea) {
        this.Acc = Acc;
        this.MCC = MCC;
        this.WPrecision = WPrecision;
        this.wRecall = wRecall;
        this.wFmeasure = wFmeasure;
        this.wROCArea = wROCArea;
    }

    ModelPerformance(double tn, double fp, double fn, double tp, double acc, double tprate, double fprate, double prec, double recall, double fmeasure, double mcc, double auc, double prc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getAcc() {
        return Acc;
    }

    public double getMCC() {
        return MCC;
    }

    public double getWPrecision() {
        return WPrecision;
    }

    public double getwRecall() {
        return wRecall;
    }

    public double getwFmeasure() {
        return wFmeasure;
    }

    public double getwROCArea() {
        return wROCArea;
    }
    
    
    @Override
    public String toString(){
        return "Correctly Classified Instances \t" + this.Acc + "\n" 
                +"Weighted Avg. MCC \t" + this.MCC + "\n"
                +"Weighted Avg. Precision \t" + this.WPrecision + "\n"
                +"Weighted Avg. Recall \t" + this.wRecall + "\n"
                +"Weighted Avg. F-Measure \t" + this.wFmeasure + "\n"
                +"Weighted Avg. ROC Area \t" + this.wROCArea;
    }
    

}
