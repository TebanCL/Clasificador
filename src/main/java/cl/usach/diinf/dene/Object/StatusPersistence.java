
package cl.usach.diinf.dene.Object;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.io.Serializable;
import java.util.Date;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import twitter4j.Status;

/**
 *
 * @author Teban
 */
public class StatusPersistence implements Serializable{
    DB db;
    Jongo jongo;
    MongoCollection status;
    
    public StatusPersistence(){        
        db = new MongoClient().getDB("DeNe-test");
        jongo = new Jongo(db);
        status = jongo.getCollection("status");        
    }
    
    public void saveStatus(Status s){
        if(s.getLang().equals("es")){
            StatusMongo statusMongo = new StatusMongo();
            statusMongo.tweetID = s.getId();        
            statusMongo.tweetText = s.getText();
            statusMongo.timestamp = new Date();
            status.save(statusMongo);
        }
    }
    
}
