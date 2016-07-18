
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
/**
 * Este bolt sólo emite estados donde le lenguaje, detectado por Twitter, es Español (es).
 * @author Teban
 */
public class LanguageFilter implements IRichBolt {
    private int elementCounter = 0;
    private int inputCounter = 0;
    private OutputCollector collector;
    private int counterToTime;
    

    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        this.counterToTime = 0;
    }

    @Override
    public void execute(Tuple tuple) {
        inputCounter++;
        Status status = (Status) tuple.getValueByField("status");
        if(status.getLang().equals("es"))
        {
            elementCounter++;
            System.out.println("IDIOMA - Emitidos: "+elementCounter);
            this.collector.emit(new Values(status));
        }
    }

    @Override
    public void cleanup() {
       System.out.println("Elementos recibidos IDIOMA: "+inputCounter+"\nEstados emitidos IDIOMA: " + elementCounter);
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
