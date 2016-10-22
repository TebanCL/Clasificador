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
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;

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
      
      builder.setSpout("TwitterSpout", new cl.usach.diinf.dene.Test.FakeStatusSpout(), 1);
      
      builder.setBolt("LanguageFilter", new LanguageFilter(), 10).shuffleGrouping("TwitterSpout");
      builder.setBolt("QueryFilter", new QueryFilter(), 2).shuffleGrouping("LanguageFilter");
      builder.setBolt("TextNormalizer", new TextNormalizer(), 2).shuffleGrouping("QueryFilter");
      builder.setBolt("LocationRecognizer", new LocationRecognizer(), 2).shuffleGrouping("TextNormalizer");
      builder.setBolt("StopwordRemover", new StopwordRemover(), 1).shuffleGrouping("LocationRecognizer");  
      builder.setBolt("TextStemmer", new TextStemmer(), 1).shuffleGrouping("StopwordRemover");      
      builder.setBolt("Labeler", new Labeler(), 1).shuffleGrouping("TextStemmer");         
      builder.setBolt("Persistence", new Persistence(), 1).shuffleGrouping("Labeler");
      
        Config conf = new Config();
        conf.setDebug(false);
        
        conf.setMaxTaskParallelism(1);
        LocalCluster cluster = new LocalCluster();
        
        long startTimeMs = System.currentTimeMillis( );
        cluster.submitTopology("Deteccion-Necesidades"+(new Date().getTime()), conf, builder.createTopology());

        Thread.sleep(360000);
        
        cluster.shutdown();
        long taskTimeMs  = System.currentTimeMillis( ) - startTimeMs;
        System.out.println("Tiempo de ejecución: "+taskTimeMs);
    }
    
    
}
