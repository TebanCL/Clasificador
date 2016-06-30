
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;
import twitter4j.Status;
/**
 * Este bolt sólo emite estados donde le lenguaje, detectado por Twitter, es Español (es).
 * @author Teban
 */
public class LanguageFilter implements IRichBolt {
    
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        if(status.getLang().equals("es"))
        {
            this.collector.emit(new Values(status));
        }
    }

    @Override
    public void cleanup() {
    //
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
