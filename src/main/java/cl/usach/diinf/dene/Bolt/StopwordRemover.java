
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cl.usach.diinf.dene.Object.StopwordDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import twitter4j.Status;

/**
 * 
 * @author Teban
 */
public class StopwordRemover implements IRichBolt {

    List<String> stopwords;
    private int inputCounter = 0;
    private OutputCollector collector;
    private int elementCounter = 0;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;        
        StopwordDictionary stopworddictionary = new StopwordDictionary();
        this.stopwords = stopworddictionary.getStopwordsAsArrayList();
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        String normalizedText = (String) tuple.getValueByField("text");
        Double latitud = (Double) tuple.getValueByField("latitud");
        Double longitud = (Double) tuple.getValueByField("longitud");
        inputCounter++;
        String text = "";
        ArrayList<String> cS = new ArrayList();
        for(String s : normalizedText.split(" "))
        {
            cS.add(s);
        }
        //cS.addAll(Arrays.asList(contenido.split(" ")));
        for(String s : cS){
            boolean flag = false;
            for(String t: stopwords){
                if(t.equals(t)){
                    flag = true;
                }
            }
            if(!flag){
                text+=" "+s;
            }
        } 
        elementCounter++;
        System.out.println("STOPWORD - Emitidos: "+elementCounter);
        this.collector.emit(new Values(status, text, latitud, longitud));
        
    }

    @Override
    public void cleanup() {
        
       System.out.println("Elementos recibidos STOPWORD: "+inputCounter+"\nEstados emitidos STOPWORD: " + elementCounter);
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
