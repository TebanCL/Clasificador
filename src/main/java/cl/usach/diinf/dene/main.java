
package cl.usach.diinf.dene;

import cl.usach.diinf.dene.Spout.TwitterSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import cl.usach.diinf.dene.Bolt.*;

public class main {
    
    public static void main(String[] args) throws Exception {

      TopologyBuilder builder = new TopologyBuilder();
      
      builder.setSpout("TwitterSpout", new TwitterSpout(), 2);
      
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

      /*
      CASO: Ejecutando en cluster.
      */
      if (args != null && args.length > 0) {
        conf.setNumWorkers(3);
        StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
      }
      /*
      CASO: Probando de manera local.
      */
      else {
        conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        
        final TopologyRunner topologyRunner = new TopologyRunner(cluster, conf, builder);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("=== SHUTTING DOWN LOCAL CLUSTER ===");
                topologyRunner.ShutDown();
            }
        });
        
        while(true){
            topologyRunner.Run();
        }
        /*cluster.submitTopology("Deteccion-Necesidades", conf, builder.createTopology());
        Thread.sleep(10000);     
        cluster.shutdown();*/
      }
    }
}
