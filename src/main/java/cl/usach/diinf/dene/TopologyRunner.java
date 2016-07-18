
package cl.usach.diinf.dene;

import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.Config;
import java.util.Date;

/**
 *
 * @author Teban
 */
public class TopologyRunner {
   
    LocalCluster cluster;
    Config conf;
    TopologyBuilder builder;
    
    public TopologyRunner(LocalCluster cluster, Config conf, TopologyBuilder builder){
        this.cluster = cluster;
        this.conf = conf;
        this.builder = builder;
    }
    
    public void Run() throws InterruptedException{
        cluster.submitTopology("Deteccion-Necesidades"+(new Date().getTime()), conf, builder.createTopology());
        Thread.sleep(10000);
    }
    
    public void ShutDown(){
        cluster.shutdown();
    }
    
}
