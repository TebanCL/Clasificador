
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cl.usach.diinf.dene.Object.CurrentQueryChecker;
import cl.usach.diinf.dene.Object.QueryExpander;
import java.util.ArrayList;
import java.util.Map;
import twitter4j.Status;

/**
 * Realiza la búsqueda en la base de datos de nuevas consultas
 * Sólo emite estados que contengan palabras que están en los términos de
 * búsqueda.
 * Realiza expansión de la consulta.
 * De no haber términos pasan todos.
 * @author Teban
 */
public class QueryFilter implements IRichBolt{

    private OutputCollector collector;
    private CurrentQueryChecker cqc;
    private QueryExpander queryExpander;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        cqc = new CurrentQueryChecker();
        queryExpander = new QueryExpander();
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        CurrentQueryChecker cqc = new CurrentQueryChecker();
        /*Revisa la última query*/
        cqc.check();
        
        if(this.checkQueryMatch(queryExpander.expandQuery(cqc), status)){
            this.collector.emit(new Values(status));
        }
    }
    
    
    
    public boolean checkQueryMatch(String[] keywords, Status status){
        if(keywords.length == 0){
            return true;
        }
        for(String s: keywords){
            if(status.getText().toLowerCase().contains(s.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    public boolean checkQueryMatch(ArrayList<String> keywords, Status status){
        if(keywords.isEmpty()){
            return true;
        }
        for(String s: keywords){
            if(status.getText().toLowerCase().contains(s.toLowerCase())){
                return true;
            }
        }
        return false;
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
