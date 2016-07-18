package cl.usach.diinf.dene.Spout;

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
import java.util.Date;

/**
 * Recolecta desde Twitter estados que se generan en tiempo real.
 * @author Teban
 */
public class TwitterSpout extends BaseRichSpout {
   private SpoutOutputCollector _collector;
   private LinkedBlockingQueue<Status> queue = null;
   private TwitterStream _twitterStream;
   private int elementCounter;
   private Long end;
   private Long init;
   private String consumerKey;
   private String consumerSecret;
   private String accessToken;
   private String accessTokenSecret;
   
   private StatusPersistence statusPersistence;
		
   public TwitterSpout() {
        this.initKeys();       
        
   }
				
   @Override
   public void open(Map conf, TopologyContext context,
      SpoutOutputCollector collector) {
       init = System.currentTimeMillis();
       end = init;
       elementCounter = 0;
        statusPersistence = new StatusPersistence();
        queue = new LinkedBlockingQueue<>(1000);
        _collector = collector;
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {      
                queue.offer(status); 
                end = System.currentTimeMillis();
                elementCounter++;
            }					
            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {}					
            @Override
            public void onTrackLimitationNotice(int i) {
                System.out.println("ATENCIÓN: ¡Límite alcanzado! No se recibirán estados por un tiempo.");
                System.out.println("EVENTOS: "+elementCounter);
            }					
            @Override
            public void onScrubGeo(long l, long l1) {}					
            @Override
            public void onException(Exception ex) {
            }					
            @Override
            public void onStallWarning(StallWarning arg0) {
            }
        };
				
        ConfigurationBuilder cb = new ConfigurationBuilder();
				
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);
					
        _twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        _twitterStream.addListener(listener);
	_twitterStream.sample();
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
            /*System.out.println("Estado:" + status.getText());
            System.out.println("Contador Actual: " + elementCounter);
            System.out.println("Tiempo (ms): " + (end - init));*/
           statusPersistence.saveStatus(status);
           _collector.emit(new Values(status));
        }        
   }
			
   @Override
   public void close() {
      _twitterStream.shutdown();
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
   
   private void initKeys(){
        this.consumerKey = "hckngBRu0t0FRQBFzhe3umvgY";
        this.consumerSecret = "hAn8cNktJmYwYQRzwZaDXGkKj8pkMGaVQ3FrV9O9Hw0SlIjtrD";
        this.accessToken = "361365793-b17furLZppeNh9pu1CHLnIZqje8RGJcuFTvvKkpG";
        this.accessTokenSecret = "Vd7O5rTMf2KcF6pILeQuVVZyoubmuUAs7yKEQUV0hLn1i";
   }
  
}