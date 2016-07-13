
package cl.usach.diinf.dene.Test;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author Teban
 */
public class TwitterTestSpout extends BaseRichSpout {
    
    private SpoutOutputCollector _collector;
    private LinkedBlockingQueue<Status> queue = null;
    
    private String consumerKey;
    private String consumerSecret;
    private String accessTokenKey;
    private String accessTokenSecret;
    private ArrayList<Long> testTweets;

    @Override
    public void open(Map map, TopologyContext tc, SpoutOutputCollector soc) {
        queue = new LinkedBlockingQueue<>(1000);
        _collector = soc;
        this.initKeys();
        try {
            this.testTweets = this.readIds();
        } catch (IOException ex) {
            Logger.getLogger(TwitterTestSpout.class.getName()).log(Level.SEVERE, null, ex);
        }
        final Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        AccessToken accessToken = new AccessToken(accessTokenKey, accessTokenSecret);
        twitter.setOAuthAccessToken(accessToken);
        for(Long l: this.testTweets){
            System.out.println("Buscando Tweet ID: "+l);
            try {
                Status status = twitter.showStatus(l);
                if (status != null) { // 
                    queue.offer(status);
                }
            } catch (TwitterException e) {
                System.err.print("Failed to search tweets: " + e.getMessage());
            }
        }
    
    }

    @Override
    public void nextTuple() {
        Status status = queue.poll();       
        
        if (status == null) {
           Utils.sleep(50);
        } else {
            System.out.println("Emitiendo estado:"+ status.getText() +"Timestamp: "+ status.getCreatedAt());
           _collector.emit(new Values(status));
        }
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
       declarer.declare(new Fields("status"));
    }
    
    private void initKeys(){
        this.consumerKey = "hckngBRu0t0FRQBFzhe3umvgY";
        this.consumerSecret = "hAn8cNktJmYwYQRzwZaDXGkKj8pkMGaVQ3FrV9O9Hw0SlIjtrD";
        this.accessTokenKey = "361365793-b17furLZppeNh9pu1CHLnIZqje8RGJcuFTvvKkpG";
        this.accessTokenSecret = "Vd7O5rTMf2KcF6pILeQuVVZyoubmuUAs7yKEQUV0hLn1i";
   }
    
    private ArrayList<Long> readIds() throws FileNotFoundException, IOException{
        System.out.println("Leyendo ID.");
        ArrayList<Long> file = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader("c:/DeNe/test/TweetsID_FULL.txt"));
        try {            
            String line = br.readLine();

            while (line != null) {
                file.add(Long.parseLong(line));
                line = br.readLine();
            }
        } 
        catch(IOException e){
            System.out.println("FILE NOT FOUND");
        }
        finally {
            br.close();
        }
        System.out.println("IDs Listas");
        return file;
    }
}
