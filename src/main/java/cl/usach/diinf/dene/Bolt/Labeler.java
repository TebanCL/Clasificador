/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cc.mallet.classify.Classifier;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Labeling;
import cl.usach.diinf.dene.Object.MalletUtility;
import cl.usach.diinf.dene.Object.Normalizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;

/**
 * @author Teban
 */
public class Labeler implements IRichBolt{
    
    private int elementCounter = 0;
    private int inputCounter = 0;
    private OutputCollector collector;
    private Classifier clasificador;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        try {
            clasificador = new MalletUtility().readClassifier();            
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        String text = (String) tuple.getValueByField("text");
        Double latitud = (Double) tuple.getValueByField("latitud");
        Double longitud = (Double) tuple.getValueByField("longitud");
        inputCounter++;
        String category = "";
        try {
            //text = new Normalizer().normalizeText(123, status.getText());
            category = this.getLabel(text);
        } catch (Exception ex) {
            Logger.getLogger(Labeler.class.getName()).log(Level.SEVERE, null, ex);
        }        
        elementCounter++;
        System.out.println("LABELER - Emitidos: "+elementCounter);
        this.collector.emit(new Values(status, category, latitud, longitud));
        
    }
    
    @Override
    public void cleanup() {
       System.out.println("Elementos recibidos ETIQUETADOR: "+inputCounter+"\nEstados emitidos ETIQUETADOR: " + elementCounter);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "category", "latitud", "longitud"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
    public String getLabel(String textContent) throws IOException{
        File file = this.instance2File(textContent);
        String result = this.label2String(clasificador, file);
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
    
    public String label2String(Classifier classifier, File file) throws IOException {

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

            /*for (int rank = 0; rank < labeling.numLocations(); rank++){
                System.out.print(labeling.getLabelAtRank(rank) + ":" +
                                 labeling.getValueAtRank(rank) + " ");
            }
            System.out.println();*/

        }
        r.close();
        return label;
    }
}
