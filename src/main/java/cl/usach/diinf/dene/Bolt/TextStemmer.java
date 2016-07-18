
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cl.usach.diinf.dene.Object.Stemmer;
import java.util.ArrayList;
import java.util.Map;
import twitter4j.Status;

/**
 *
 * @author Teban
 */
public class TextStemmer implements IRichBolt{

    private OutputCollector collector;
    private int elementCounter = 0;
    private int inputCounter = 0;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        String text = (String) tuple.getValueByField("text");
        Double latitud = (Double) tuple.getValueByField("latitud");
        Double longitud = (Double) tuple.getValueByField("longitud");
        inputCounter++;
        String[] text2BStemmed = text.split(" ");
        String stemmedText = "";
        
        Stemmer stemmer = new Stemmer();
        
        for(String s: text2BStemmed){
            stemmedText += stemmer.stemm(s) + " ";
        }
        elementCounter++;
        System.out.println("STEMMER - Emitidos: "+elementCounter);
        this.collector.emit(new Values(status, stemmedText, latitud, longitud));
    }

    @Override
    public void cleanup() {
    
       System.out.println("Elementos recibidos STEMMER: "+inputCounter+"\nEstados emitidos STEMMER: " + elementCounter);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "text", "latitud", "longitud"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
