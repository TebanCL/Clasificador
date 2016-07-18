
package cl.usach.diinf.dene.Object;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import cl.usach.diinf.dene.Object.StatusPersistence;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Recolecta desde Twitter estados que se generan en tiempo real.
 * @author Teban
 */
public class FakeStatusSpout extends BaseRichSpout {
   private SpoutOutputCollector _collector;
   private LinkedBlockingQueue<Status> queue = null;
   
   private StatusPersistence statusPersistence;
		
   private int elementCounter = 0;
   
   public FakeStatusSpout() {
   }
				
   @Override
   public void open(Map conf, TopologyContext context,
      SpoutOutputCollector collector) {
       
        statusPersistence = new StatusPersistence();
        queue = new LinkedBlockingQueue<>(160000);
        _collector = collector;
        
        FakeStatusGenerator fsg = new FakeStatusGenerator();
       try {
           fsg.prepareData();
       } catch (UnsupportedEncodingException ex) {
           Logger.getLogger(FakeStatusSpout.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       for(Status s: fsg.All){
           queue.offer(s);
       }
   }
			
   @Override
   public void nextTuple() {
        //Mientras la cola no esté vacía.
        while(queue.isEmpty()){
            Utils.sleep(50);
        }
        Status status = queue.poll();       
        
        if (status == null) {
           Utils.sleep(50);
        } else {
            /*
                Guarde los ID y fecha de recepción del estado, 
                luego emite a la topología.
            */
           elementCounter++;
           statusPersistence.saveStatus(status);
           _collector.emit(new Values(status)); 
        }        
   }
			
   @Override
   public void close() {
       System.out.println("Estados emitidos SPOUT: " + elementCounter);
   }
			
   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
			
   @Override
   public void ack(Object id) {}
			
   @Override
   public void fail(Object id) {}
			
   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("status"));
   }
  
}