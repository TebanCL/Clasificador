
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
            
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        
        StopwordDictionary stopworddictionary = new StopwordDictionary();
        this.stopwords = stopworddictionary.getStopwordsAsArrayList();
        
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("Status");
        String normalizedText = (String) tuple.getValueByField("normalizedText");
        ArrayList<Double> locations = (ArrayList<Double>) tuple.getValueByField("locations");
       
        String textWOStopword = "";
        List<String> cS = new ArrayList();
        for(String s : normalizedText.split(" "))
        {
            cS.add(s);
        }
        //cS.addAll(Arrays.asList(contenido.split(" ")));
        for(String s : cS){
            if(!stopwords.contains(s.toLowerCase()))
            {
                textWOStopword+=s+" ";
            }
        } 
        
        this.collector.emit(new Values(status, textWOStopword, locations));
        
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "textWOStopword", "locations"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
