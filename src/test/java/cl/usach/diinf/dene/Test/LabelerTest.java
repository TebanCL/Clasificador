
package cl.usach.diinf.dene.Test;
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;
import cc.mallet.types.Labeling;
import cl.usach.diinf.dene.Object.MalletUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 *
 * @author Teban
 */
public class LabelerTest {
    private String text;
    private Classifier clasificador;
    Instance instancia;
    
    public LabelerTest() throws IOException, ClassNotFoundException{
        text = "estamos bien en el refugio los treintaitres";
        try {
            clasificador = new MalletUtility().readClassifier();            
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("CRITICAL: CANNOT READ CLASSIFIER!!!");
        } 
        instancia = new Instance(text, "", ""+(new Date().getTime()), text);
       int i = 0; 
    }
    
    //@Test
    public void labelTest() throws IOException{
        //System.out.println((String)instancia.getData());
        Pipe pipeline = this.buildPipe();
        InstanceList instances = new InstanceList(pipeline);
        instances.addThruPipe(instancia);
        printLabelings(clasificador, instances);
        int i = 0;
        assert(true);
    }
    
    public Pipe buildPipe(){
        ArrayList pipeList = new ArrayList();
        pipeList.add(new Input2CharSequence("UTF-8"));
        Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequence2FeatureSequence());
        pipeList.add(new Target2Label());
        pipeList.add(new FeatureSequence2FeatureVector());
        pipeList.add(new PrintInputAndTarget());
        return new SerialPipes(pipeList);
    }
    
    public void printLabelings(Classifier classifier, InstanceList iL) {     
        
        Iterator iter  = classifier.getInstancePipe().newIteratorFrom(iL.iterator());
        while (iter.hasNext()) {
            
            Labeling labeling = classifier.classify(iter.next()).getLabeling();
            int k = 3;
            // print the labels with their weights in descending order (ie best first)                     

            for (int rank = 0; rank < labeling.numLocations(); rank++){
                System.out.print(labeling.getLabelAtRank(rank) + ":" +
                                 labeling.getValueAtRank(rank) + " ");
            }

        }  
        
    }
    
    
}
