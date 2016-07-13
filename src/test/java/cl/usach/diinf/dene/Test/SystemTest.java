/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.diinf.dene.Test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import cl.usach.diinf.dene.Bolt.Labeler;
import cl.usach.diinf.dene.Bolt.LanguageFilter;
import cl.usach.diinf.dene.Bolt.LocationRecognizer;
import cl.usach.diinf.dene.Bolt.Persistence;
import cl.usach.diinf.dene.Bolt.QueryFilter;
import cl.usach.diinf.dene.Bolt.StopwordRemover;
import cl.usach.diinf.dene.Bolt.TextNormalizer;
import cl.usach.diinf.dene.Bolt.TextStemmer;

import org.junit.Test;

/**
 *
 * @author Teban
 */
public class SystemTest {
 
    public SystemTest(){}
    
    //@Test
    public void TestTopology() throws InterruptedException{
      
      TopologyBuilder builder = new TopologyBuilder();
      
      builder.setSpout("TwitterSpout", new cl.usach.diinf.dene.Test.TwitterTestSpout(), 1);
      
      builder.setBolt("LanguageFilter", new LanguageFilter(), 4).shuffleGrouping("TwitterSpout");
      builder.setBolt("QueryFilter", new QueryFilter(), 4).shuffleGrouping("LanguageFilter");
      builder.setBolt("TextNormalizer", new TextNormalizer(), 4).shuffleGrouping("QueryFilter");
      builder.setBolt("LocationRecognizer", new LocationRecognizer(), 4).shuffleGrouping("TextNormalizer");
      builder.setBolt("StopwordRemover", new StopwordRemover(), 2).shuffleGrouping("LocationRecognizer");  
      builder.setBolt("TextStemmer", new TextStemmer(), 2).shuffleGrouping("StopwordRemover");      
      builder.setBolt("Labeler", new Labeler(), 2).shuffleGrouping("TextStemmer");         
      builder.setBolt("Persistence", new Persistence(), 1).shuffleGrouping("Labeler");
      
        Config conf = new Config();
        conf.setDebug(false);
        
        conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        
        cluster.submitTopology("Deteccion-Necesidades", conf, builder.createTopology());
        
        Thread.sleep(10000);
        
        cluster.shutdown();
    }
    
    
}
