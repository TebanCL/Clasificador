
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
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        db = new MongoClient().getDB("DeNe-test");
        jongo = new Jongo(db);
        markers = jongo.getCollection("markers");        
    }

    @Override
    public void execute(Tuple tuple) {
        /* Guarda en BD un objeto con la latitud, longitud, categoria y contenido del tweet */
        Status status = (Status) tuple.getValueByField("status");
        String categoria = (String) tuple.getValueByField("category");
        ArrayList<Double> locations = (ArrayList<Double>) tuple.getValueByField("location");
        
        Marker m = new Marker();
        m.categoria = categoria;
        m.contenido = status.getText();
        m.userID = String.valueOf(status.getUser().getId());
        m.latitud = locations.get(0);
        m.longitud = locations.get(1);
        m.generatedAt = new Date();
        markers.save(m);
    }

    @Override
    public void cleanup() {    
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
