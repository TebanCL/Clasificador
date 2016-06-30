/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.Labeling;
import cl.usach.diinf.dene.Object.MalletUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;

/**
 *
 * @author Teban
 */
public class Labeler implements IRichBolt{

    private OutputCollector collector;
    private Classifier classifier;
    
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        try {
            classifier = new MalletUtility().readClassifier();            
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        String stemmedText = (String) tuple.getValueByField("stemmedText");
        ArrayList<Double> locations = (ArrayList<Double>) tuple.getValueByField("locations");
        String category = "";
        Instance instance = new Instance(stemmedText, "", ""+(new Date().getTime()), "NULL");
        Labeling labeling = classifier.classify(instance).getLabeling();
        category = ""+labeling.getLabelAtRank(0);
        this.collector.emit(new Values(status, category, locations));
        
    }

    @Override
    public void cleanup() {
    //
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "category", "locations"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
