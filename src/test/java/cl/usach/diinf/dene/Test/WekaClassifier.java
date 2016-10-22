
package cl.usach.diinf.dene.Test;

import cl.usach.diinf.dene.Object.RawData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.junit.Test;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


/**
 *
 * @author Teban
 */
public class WekaClassifier {
    ArrayList<RawData> rawdata;
    BufferedReader br;
    
    
    public WekaClassifier(){
        rawdata = new ArrayList();
    }
   
    //@Test
    public void getInstances() throws IOException{
        //csv2Arff();
        Instances inst = new Instances(new BufferedReader(new FileReader("C:\\DeNe\\data.arff")));
        System.out.println(inst.toSummaryString());
    }
    
    public void csv2Arff() throws FileNotFoundException, IOException{
        String route = "c:\\data.csv";
        br = new BufferedReader(new FileReader(route));
        String line = "";
        while((line = br.readLine()) != null){
            String[] line2 = line.split(";");
            if(line2.length <= 2){
                rawdata.add(new RawData(line2[0].replace(",", "."), line2[1]));
            }
        }
        PrintWriter pw = new PrintWriter("c:\\DeNe\\data.csv","UTF-8");
        pw.write("@relation categoria\n");
        pw.write("@attribute texto string\n");
        pw.write("@attribute etiqueta string\n");
        pw.write("@data\n");
        for(RawData rd: rawdata){
            pw.write(rd.getContent()+","+rd.getLabel()+"\n");
        }
        pw.close();        
    }
    
}
