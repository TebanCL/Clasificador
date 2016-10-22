
package cl.usach.diinf.dene;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import cl.usach.diinf.dene.Bolt.*;
import cl.usach.diinf.dene.Object.FakeStatusSpout;
import cl.usach.diinf.dene.Spout.TwitterSpout;

public class main {
    
    public static void main(String[] args) throws Exception {

      TopologyBuilder builder = new TopologyBuilder();
      
      builder.setSpout("TwitterSpout", new TwitterSpout(), 1);
      
      builder.setBolt("LanguageFilter", new LanguageFilter(), 4).shuffleGrouping("TwitterSpout");
      builder.setBolt("QueryFilter", new QueryFilter(), 2).shuffleGrouping("LanguageFilter");
      builder.setBolt("TextNormalizer", new TextNormalizer(), 2).shuffleGrouping("QueryFilter");
      builder.setBolt("LocationRecognizer", new LocationRecognizer(), 2).shuffleGrouping("TextNormalizer");
      builder.setBolt("StopwordRemover", new StopwordRemover(), 1).shuffleGrouping("LocationRecognizer");  
      builder.setBolt("TextStemmer", new TextStemmer(), 1).shuffleGrouping("StopwordRemover");      
      builder.setBolt("Labeler", new Labeler(), 1).shuffleGrouping("TextStemmer");         
      builder.setBolt("Persistence", new Persistence(), 1).shuffleGrouping("Labeler");
      
      Config conf = new Config();
      conf.setDebug(false);

      /*
      CASO: Ejecutando en cluster.
      */
      if (args != null && args.length > 0) {
        conf.setNumWorkers(4);
        StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
      }
      /*
      CASO: Probando de manera local.
      */
      else {
        conf.setMaxTaskParallelism(4);
        LocalCluster cluster = new LocalCluster();
        
        final TopologyRunner topologyRunner = new TopologyRunner(cluster, conf, builder);
        
        /*Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("=== SHUTTING DOWN LOCAL CLUSTER ===");
                topologyRunner.ShutDown();
            }
        });*/
        
        //while(true){
            topologyRunner.Run();
        //}
        /*cluster.submitTopology("Deteccion-Necesidades", conf, builder.createTopology());
        Thread.sleep(40000);     
        /*cluster.shutdown();*/
      }
    }
}
