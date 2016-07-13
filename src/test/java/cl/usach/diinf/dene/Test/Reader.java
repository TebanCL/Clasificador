
package cl.usach.diinf.dene.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Teban
 */
public class Reader {
    private ArrayList<String> fullInstanceList;
    private ArrayList<String> halfOfInstances;
    private ArrayList<String> quarterOfInstances;
    private Boolean halfRunnable;
    private Boolean quarterRunnable;
    
    public Reader(){
        fullInstanceList = this.readInstanceList();
        halfOfInstances = new ArrayList();
        quarterOfInstances = new ArrayList();
        if( this.prepareTestCases() ){
            this.halfRunnable = true;
            this.quarterRunnable = true;
        }
        else{
            this.halfRunnable = false;
            this.quarterRunnable = false;
        }
    }
    
    private ArrayList<String> readInstanceList(){
        ArrayList<String> input = new ArrayList();
        String directory = "C:/DeNe/test/testLibrary.csv";
        BufferedReader br = null;
        try {
            String sCurrentLine;

            br = new BufferedReader(new FileReader(directory));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] splittedInput = sCurrentLine.split(";");
                if(splittedInput.length == 3){
                    input.add(splittedInput[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                    if (br != null)br.close();
            } catch (IOException ex) {
                    ex.printStackTrace();
            }
        }
        return input;
    }
    
    private boolean prepareTestCases(){
        int halfCounter = this.fullInstanceList.size()/2;
        int quarterCounter = this.fullInstanceList.size()/4;
        for(int i = 0; i < halfCounter; i++){
            String element = this.fullInstanceList.get((int)(Math.random())*halfCounter);
            this.halfOfInstances.add(element);
        }        
        for(int i = 0; i < quarterCounter; i++){
             String element = this.fullInstanceList.get((int)(Math.random())*quarterCounter);
             this.quarterOfInstances.add(element);
        }        
        
        return (this.quarterOfInstances.size() == (this.fullInstanceList.size()/4)) 
                && 
                (this.halfOfInstances.size() == (this.fullInstanceList.size()/2));
    }
    
    public ArrayList<String> getFullFile(){
        return this.fullInstanceList;
    }
    
    public ArrayList<String> getHalfFile(){
        if(!this.halfRunnable){
            return new ArrayList();
        }
        else{
            return this.halfOfInstances;
        }
    } 
    
    public ArrayList<String> getQuarterFile(){
        if(!this.quarterRunnable){
            return new ArrayList();
        }
        else
        {
        return this.quarterOfInstances;
        }
    } 
}
