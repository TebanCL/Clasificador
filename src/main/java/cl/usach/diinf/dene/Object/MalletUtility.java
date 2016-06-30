
package cl.usach.diinf.dene.Object;

import cc.mallet.classify.Classifier;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.apache.commons.lang.SystemUtils;

/**
 * @author Teban
 */
public class MalletUtility implements Serializable{
    
    String directory = "";    

    public MalletUtility(){
        if(SystemUtils.IS_OS_WINDOWS){
            directory = "c:/DeNe/";
        }
        else{
            directory = "/opt/DeNe";
        }
    }
    
    public Classifier readClassifier() throws IOException, ClassNotFoundException{
        Classifier classifier = null;
        try{
            ObjectInputStream ois =
            new ObjectInputStream (new FileInputStream (directory+"classifier.dene"));
            classifier = (Classifier) ois.readObject();
            ois.close();            
        }
        catch(IOException e){
        }
        return classifier;
    }
    
}

