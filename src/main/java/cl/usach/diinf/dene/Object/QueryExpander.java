
package cl.usach.diinf.dene.Object;

import cl.usach.diinf.dene.Bolt.TextNormalizer;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.Date;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

/**
 *
 * @author Teban
 */
public class QueryExpander {
    
    DB db;
    Jongo jongo;
    MongoCollection statusMongo;
    StopwordDictionary sD;
    
    public QueryExpander(){
        
        db = new MongoClient().getDB("DeNe-test");
        jongo = new Jongo(db);
        statusMongo = jongo.getCollection("status"); 
        sD = new StopwordDictionary();
    }
    
    /*Realiza la query expansion*/
    public ArrayList<String> expandQuery(CurrentQueryChecker cqc){
        if(cqc.getTermsList().isEmpty()){
            
            StopwordDictionary stopworddictionary = new StopwordDictionary();
            cqc.check();
            Date lastQueryDate = cqc.getLastQueryDate();
            
            MongoCursor<StatusMongo> mongoStatus = statusMongo.find().as(StatusMongo.class);
            
            ArrayList<ThrendingTopic> tt = new ArrayList();
            
            TextNormalizer textNormalizer = new TextNormalizer();
            Stemmer stemmer = new Stemmer();
            
            while(mongoStatus.hasNext()){
                StatusMongo sm = mongoStatus.next();
                if(sm.timestamp.getTime() > lastQueryDate.getTime()){
                    for(String s: sm.tweetText.split(" ")){
                        if(this.isInTheList(tt, s)){
                            tt.get(this.indexOf(tt, s)).increaseCounter();                                                 
                        }
                        else{
                            if(!sD.isStopword(s)){
                                ThrendingTopic newTT = new ThrendingTopic(s);
                                tt.add(newTT);
                            }                            
                        }
                    }
                }
            }
            
            ArrayList<String> terms  = cqc.getTermsList();
            
            int sizeOfArray = tt.size();
            
            if(sizeOfArray > 10){
                sizeOfArray = 10;
            }
            
            for(int i = 0; i < sizeOfArray; i++){
                terms.add(tt.get(i).getWord());
            }
            return terms;
        }
        else{
            return cqc.getTermsList();
        }
    }
    
    public boolean isInTheList(ArrayList<ThrendingTopic> tt, String word){
        for(ThrendingTopic t: tt){
            if(t.getWord().equals(word)){
                return true;
            }
        }
        return false;
    }
    
    public int indexOf(ArrayList<ThrendingTopic> tt, String w){
        int counter = 0;
        for(ThrendingTopic t: tt){
            if(t.getWord().equals(w)){
                break;
            }
            else{
                counter++;
            }
        }
        return counter;
    }
    
}
