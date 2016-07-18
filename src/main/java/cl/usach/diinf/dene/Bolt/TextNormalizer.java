
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cl.usach.diinf.dene.Object.Normalizer;
import java.util.Map;
import twitter4j.Status;

/**
 * Normaliza el texto:
 *  - @user pasa a ser USUARIO para todo user
 *  - #hashtag pasa a ser HASHTAG para todo hashtag
 *  - Intenta eliminar emoticons.
 * @author Teban
 */
public class TextNormalizer implements IRichBolt{
    private int inputCounter = 0;
    private OutputCollector collector;
    private int elementCounter = 0;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        Normalizer textNormalizer = new Normalizer();
        inputCounter++;
        String normalizedText = textNormalizer.normalizeText(123, status.getText());
        elementCounter++;
        System.out.println("NORMALIZER - Emitidos: "+elementCounter);
        this.collector.emit(new Values(status, normalizedText));
    }

    @Override
    public void cleanup() {
    
       System.out.println("Elementos recibidos NORMALIZADOR: "+inputCounter+"\nEstados emitidos NORMALIZADOR: " + elementCounter);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "text"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
