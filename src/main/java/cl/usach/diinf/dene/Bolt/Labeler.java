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
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;
import cl.usach.diinf.dene.Object.MalletUtility;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import twitter4j.Status;

/**
 *
 * @author Teban
 */
public class Labeler implements IRichBolt{
    
    private int elementCounter = 0;
    private int inputCounter = 0;
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
        String text = (String) tuple.getValueByField("text");
        Double latitud = (Double) tuple.getValueByField("latitud");
        Double longitud = (Double) tuple.getValueByField("longitud");
        String category = "";
        inputCounter++;
        Instance i = new Instance(text, "", "-"+(new Date().getTime()), "NULL");
        /*Pipe pipe = buildPipe();
        InstanceList iL = new InstanceList(pipe);
        iL.addThruPipe(i);*/
        
        /*Instance iter = classifier.getInstancePipe().instanceFrom(i);
        
        System.out.println(classifier.classify(iter).getLabeling().getLabelAtRank(0));
        */        
        //Labeling labeling = classifier.classify(instance).getLabeling();
        //category = ""+labeling.getLabelAtRank(0);
        elementCounter++;
        System.out.println("LABELER - Emitidos: "+elementCounter);
        this.collector.emit(new Values(status, "None", latitud, longitud));
        
    }

    public Pipe buildPipe(){//ArrayList<Instance> input){
        ArrayList pipeList = new ArrayList();
        pipeList.add(new Input2CharSequence("UTF-8"));
        pipeList.add(new CharSequence2TokenSequence());
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequence2FeatureSequence());
        pipeList.add(new Target2Label());
        pipeList.add(new FeatureSequence2FeatureVector());
        pipeList.add(new PrintInputAndTarget());
        return new SerialPipes(pipeList);
    }
    
    @Override
    public void cleanup() {
       System.out.println("Elementos recibidos ETIQUETADOR: "+inputCounter+"\nEstados emitidos ETIQUETADOR: " + elementCounter);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "category", "latitud", "longitud"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
