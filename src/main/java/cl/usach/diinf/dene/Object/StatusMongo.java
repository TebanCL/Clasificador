
package cl.usach.diinf.dene.Object;

import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import twitter4j.Status;

/**
 *
 * @author teban
 */
public class StatusMongo implements Serializable{

    public ObjectId _id;
    public long tweetID;
    public Date timestamp;
}
