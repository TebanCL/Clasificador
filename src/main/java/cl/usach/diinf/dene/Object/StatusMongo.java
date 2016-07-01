
package cl.usach.diinf.dene.Object;

import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author teban
 */
public class StatusMongo implements Serializable{

    public ObjectId _id;
    public long tweetID;
    public String tweetText;
    public Date timestamp;
}
