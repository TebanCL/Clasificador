
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
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("Status");
        String textWOStopword = (String) tuple.getValueByField("textWOStopword");
        ArrayList<Double> locations = (ArrayList<Double>) tuple.getValueByField("locations");
        
        String[] text2BStemmed = textWOStopword.split(" ");
        String stemmedText = "";
        
        Stemmer stemmer = new Stemmer();
        
        for(String s: text2BStemmed){
            stemmedText += stemmer.stemm(s) + " ";
        }
        
        this.collector.emit(new Values(status, stemmedText, locations));
    }

    @Override
    public void cleanup() {
    //
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "stemmedText", "locations"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
