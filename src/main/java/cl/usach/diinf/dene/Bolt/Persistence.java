
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import cl.usach.diinf.dene.Object.Marker;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.jongo.*;
import twitter4j.Status;

/**
 *
 * @author teban
 */
public class Persistence implements IRichBolt{

        DB db;
        Jongo jongo;
        MongoCollection markers;
        private int elementCounter = 0;
        private OutputCollector collector;
        private int inputCounter = 0;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        db = new MongoClient().getDB("DeNe-test");
        jongo = new Jongo(db);
        markers = jongo.getCollection("markers");        
    }

    @Override
    public void execute(Tuple tuple) {
        /* Guarda en BD un objeto con la latitud, longitud, categoria y contenido del tweet */
        Status status = (Status) tuple.getValueByField("status");
        String categoria = (String) tuple.getValueByField("category");
        Double latitud = (Double) tuple.getValueByField("latitud");
        Double longitud = (Double) tuple.getValueByField("longitud");
        inputCounter++;
        Marker m = new Marker();
        m.categoria = categoria;
        m.contenido = status.getText();
        //m.userID = String.valueOf(status.getUser().getId());
        m.latitud = latitud;
        m.longitud = longitud;
        m.generatedAt = new Date();
        elementCounter++;
        markers.save(m);
        this.collector.ack(tuple);
    }

    @Override
    public void cleanup() {    
        
       System.out.println("Elementos recibidos PERSISTENCE: "+inputCounter+"\nEstados emitidos PERSISTENCE: " + elementCounter);
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
