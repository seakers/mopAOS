/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.history;

import aos.history.CreditHistory;
import aos.creditassigment.Credit;
import aos.history.OperatorQualityHistory;
import aos.history.OperatorSelectionHistory;
import aos.history.QualityRecord;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.moeaframework.core.Variation;
import org.moeaframework.core.operator.CompoundVariation;

/**
 * This class is responsible for saving the history of credits received by
 * operators, operator selection, and operator quality.
 *
 * @author nozomihitomi
 */
public class AOSHistoryIO {

    /**
     * Saves the credit history at the specified file. The file will be a a
     * dlm file with n rows to represent the n iterations. Each column will have
     * the credits received in the ith iteration by the mth operator. If no
     * credit was received a -1 will be stored to differentiate it from a 0
     * credit
     *
     * @param creditHistory The quality history to save
     * @param file the file to save to
     * @param separator the type of separator desired
     * @return true if the save is successful
     */
    public static boolean saveCreditHistory(CreditHistory creditHistory, File file, String separator) {
        Collection<Variation> operators = creditHistory.getOperators();
        try (FileWriter fw = new FileWriter(file)) {
            for (Variation oper : operators) {
                Collection<Credit> hist = creditHistory.getHistory(oper);
                if (hist.isEmpty()) {
                    continue;
                }
                int[] iters = new int[hist.size()];
                double[] vals = new double[hist.size()];
                Iterator<Credit> iter = hist.iterator();
                Credit reward = iter.next();
                iters[0] = reward.getIteration();
                vals[0] = reward.getValue();
                int index = 0;
                while (iter.hasNext()) {
                    index++;
                    Credit nextReward = iter.next();
                    int iteration = nextReward.getIteration();
                    double rewardVal = nextReward.getValue();
                    iters[index] = iteration;
                    vals[index] = rewardVal;
                }
                
                fw.append("iteration" + separator);
                for (int i = 0; i < index; i++) {
                    fw.append(Integer.toString(iters[i]) + separator);
                }
                fw.append(Integer.toString(iters[index]) + "\n");

                String operatorName;
                if(oper instanceof CompoundVariation){
                    operatorName = ((CompoundVariation)oper).getName();
                }else{
                    String[] str = oper.toString().split("operator.");
                    String[] splitName = str[str.length - 1].split("@");
                    operatorName =splitName[0];
                }
                fw.append(operatorName + separator);
                for (int i = 0; i < index; i++) {
                    fw.append(Double.toString(vals[i]) + separator);
                }
                fw.append(Double.toString(vals[index]) + "\n");

            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the credit history at the specified file as a java Object. The
     * file will contain an instance of CreditHistory
     *
     * @param creditHistory The quality history to save
     * @param file the file to save to
     */
    public static void saveCreditHistory(CreditHistory creditHistory, File file) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));) {
            os.writeObject(creditHistory);
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads the CreditHistory instance saved by using saveCreditHistory() from
     * the file.
     *
     * @param file the file to load
     * @return the CreditHistory instance saved by using saveCreditHistory()
     */
    public static CreditHistory loadCreditHistory(File file) {
        CreditHistory hist = null;
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            hist = (CreditHistory) is.readObject();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hist;
    }

    /**
     * Saves the quality history at the specified file. The file will be a
     * list of the operator qualities at every iteration in order from beginning
     * to end separated by the desired separator. Each row in the file will
     * contain the history of one operator, with the operator name at the
     * beginning of the row
     *
     * @param qualityHistory The quality history to save
     * @param file the file to save to
     * @param separator the type of separator desired
     * @return true if the save is successful
     */
    public static boolean saveQualityHistory(OperatorQualityHistory qualityHistory, File file, String separator) {
        try (FileWriter fw = new FileWriter(file)) {
            Iterator<Variation> operatorIter = qualityHistory.getOperators().iterator();
            while (operatorIter.hasNext()) {
                Variation operator = operatorIter.next();
                //First pass to get the iterations
                Iterator<QualityRecord> historyIter = qualityHistory.getHistory(operator).iterator();
                fw.append("iteration");
                while (historyIter.hasNext()) {
                    fw.append(Integer.toString(historyIter.next().getIteration()));
                    if (historyIter.hasNext()) {
                        fw.append(separator);
                    }
                }
                fw.append("\n");
                historyIter = qualityHistory.getHistory(operator).iterator();
                String operatorName;
                if(operator instanceof CompoundVariation){
                    operatorName = ((CompoundVariation)operator).getName();
                }else{
                    String[] str = operator.toString().split("operator.");
                    String[] splitName = str[str.length - 1].split("@");
                    operatorName =splitName[0];
                }
                fw.append(operatorName + separator);
                while (historyIter.hasNext()) {
                    fw.append(Double.toString(historyIter.next().getQuality()));
                    if (historyIter.hasNext()) {
                        fw.append(separator);
                    }
                }
                if (operatorIter.hasNext()) {
                    fw.append("\n");
                }
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the quality history at the specified file as a java Object. The
     * file will contain an instance of OperatorQualityHistory
     *
     * @param qualityHistory The quality history to save
     * @param file the file to save to
     */
    public static void saveQualityHistory(OperatorQualityHistory qualityHistory, File file) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));) {
            os.writeObject(qualityHistory);
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads the OperatorQualityHistory instance saved by using
     * saveQualityHistory() from the file.
     *
     * @param file the file to load 
     * @return the OperatorQualityHistory instance saved by using saveHistory()
     */
    public static OperatorQualityHistory loadQualityHistory(File file) {
        OperatorQualityHistory hist = null;
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            hist = (OperatorQualityHistory) is.readObject();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hist;
    }

    /**
     * Saves the selection history at the specified file. The file will be a
     * list of the operators selected in order from beginning to end separated
     * by the desired separator
     *
     * @param history The history to save
     * @param file file to save to
     * @param separator the type of separator desired
     * @return true if the save is successful
     */
    public static boolean saveSelectionHistory(OperatorSelectionHistory history, File file, String separator) {
        try (FileWriter fw = new FileWriter(file)) {
            ArrayList<Variation> orderedHistory = history.getOrderedHistory();
            ArrayList<Integer> orderedTime = history.getOrderedSelectionTime();

            for (int i = 0; i < orderedHistory.size(); i++) {
                fw.append(Integer.toString(orderedTime.get(i)));
                fw.append(separator);
                Variation operator = orderedHistory.get(i);
                String operatorName;
                if(operator instanceof CompoundVariation){
                    operatorName = ((CompoundVariation)operator).getName();
                }else{
                    String[] str = operator.toString().split("operator.");
                    String[] splitName = str[str.length - 1].split("@");
                    operatorName =splitName[0];
                }
                fw.append(operatorName);
                if (!orderedHistory.isEmpty()) {
                    fw.append("\n");
                }
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the number of times an operator is selected to the specified
     * file. The file will be a list of the operators selected in order
     * from beginning to end separated by the desired separator
     *
     * @param history The history to save
     * @param file the file to save to
     * @param separator the type of separator desired
     * @return true if the save is successful
     */
    public static boolean saveSelectionCount(OperatorSelectionHistory history, File file, String separator) {
        try (FileWriter fw = new FileWriter(file)) {
            Collection<Variation> operators = history.getOperators();
            for (Variation operator : operators) {
                String operatorName;
                if(operator instanceof CompoundVariation){
                    operatorName = ((CompoundVariation)operator).getName();
                }else{
                    String[] str = operator.toString().split("operator.");
                    String[] splitName = str[str.length - 1].split("@");
                    operatorName =splitName[0];
                }
                fw.append(operatorName + separator);
                fw.append(Integer.toString(history.getSelectionCount(operator)) + "\n");
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Saves the OperatorSelectionHistory at the specified file as a java
     * Object. The file will contain an instance of IOperatorSelectionHistory
     *
     * @param history The credit repository to save
     * @param file the file to save to
     */
    public static void saveSelectionHistory(OperatorSelectionHistory history, File file) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));) {
            os.writeObject(history);
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads the IOperatorSelectionHistory instance saved by using
     * saveSelectionHistory() from the specified file.
     *
     * @param file the file to load
     * @return the CreditRepository instance saved by using
     * saveSelectionHistory()
     */
    public static OperatorSelectionHistory loadSelectionHistory(File file) {
        OperatorSelectionHistory history = null;
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            history = (OperatorSelectionHistory) is.readObject();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return history;
    }

    /**
     * Saves the frequency of selection for each operator in the stored
     * selection to the desired file. Each entry is saved as the operator
     * name and the number of times that operator was selected, and is
     * separated by the desired separator.
     *
     * @param history The history to save
     * @param file the file to save to
     * @param separator the desired separator
     * @return True if save is successful, otherwise false
     */
    public static boolean saveSelectionFrequency(OperatorSelectionHistory history, File file, String separator) {
        try (FileWriter fw = new FileWriter(file)) {
            Iterator<Variation> iter = history.getOperators().iterator();
            while (iter.hasNext()) {
                Variation operator = iter.next();
                String operatorName;
                if(operator instanceof CompoundVariation){
                    operatorName = ((CompoundVariation)operator).getName();
                }else{
                    String[] str = operator.toString().split("operator.");
                    String[] splitName = str[str.length - 1].split("@");
                    operatorName =splitName[0];
                }
                fw.append(operatorName + separator + history.getSelectionCount(operator));
                if (iter.hasNext()) {
                    fw.append(separator);
                }
            }
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(AOSHistoryIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
