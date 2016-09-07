
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
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;
import cc.mallet.types.Labeling;
import cl.usach.diinf.dene.Object.MalletUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 *
 * @author Teban
 */
public class LabelerTestFromCSV {
    private String text;
    private Classifier clasificador;
    Instance instancia;
    
    public LabelerTestFromCSV() throws IOException, ClassNotFoundException{
        text = "estamos bien en el refugio los treintaitres";
        try {
            clasificador = new MalletUtility().readClassifier();            
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("CRITICAL: CANNOT READ CLASSIFIER!!!");
        } 
        instancia = new Instance(text, "", ""+(new Date().getTime()), text);
       int i = 0; 
    }
    
    @Test
    public void labelTest() throws IOException{
        System.out.println("Etiqueta: "+this.getLabel(text));
        System.out.println("");
        assert(true);
    }
    
    public String getLabel(String textContent) throws IOException{
        File file = this.instance2File(textContent);
        String result = this.getLabel(clasificador, file);
        if(!this.deleteTemporalFile(file)){
            System.out.println("El archivo "+file.getName()+" no pudo ser eliminado. Eliminelo manualmente, por favor.");
        }
        return result;
    }
    
    public File instance2File(String instanceContent) throws FileNotFoundException, UnsupportedEncodingException{
        String temporalFileName = "instance_"+System.nanoTime()+"_Example.txt";
        File f = new File(temporalFileName);
        PrintWriter writer = new PrintWriter(f, "UTF-8");
        writer.println(System.nanoTime()+" unknownLabel "+instanceContent);
        writer.close();
        return f;
    }
    
    public boolean deleteTemporalFile(File temporalFile){
        return temporalFile.delete();
    }
    
    public String getLabel(Classifier classifier, File file) throws IOException {

        String label = "";        
        // Create a new iterator that will read raw instance data from                                     
        //  the lines of a file.                                                                           
        // Lines should be formatted as:                                                                   
        //                                                                                                 
        //   [name] [label] [data ... ]                                                                    
        //                                                                                                 
        //  in this case, "label" is ignored.                                                              

        FileReader r = new FileReader(file);
        CsvIterator reader =
            new CsvIterator(r,
                            "(\\w+)\\s+(\\w+)\\s+(.*)",
                            3, 2, 1);  // (data, label, name) field indices               

        
        
        
        // Create an iterator that will pass each instance through                                         
        //  the same pipe that was used to create the training data                                        
        //  for the classifier.                                                                            
        Iterator instances =
            classifier.getInstancePipe().newIteratorFrom(reader);

        // Classifier.classify() returns a Classification object                                           
        //  that includes the instance, the classifier, and the                                            
        //  classification results (the labeling). Here we only                                            
        //  care about the Labeling.   
        while (instances.hasNext()) {
            Labeling labeling = classifier.classify(instances.next()).getLabeling();
            label = labeling.getBestLabel().toString();
            //System.out.println(labeling.getBestLabel());
            // print the labels with their weights in descending order (ie best first)                     

            for (int rank = 0; rank < labeling.numLocations(); rank++){
                System.out.print(labeling.getLabelAtRank(rank) + ":" +
                                 labeling.getValueAtRank(rank) + " ");
            }
            System.out.println();

        }
        r.close();
        return label;
    }
    
    
    
    
}
