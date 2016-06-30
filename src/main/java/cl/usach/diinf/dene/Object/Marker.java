
package cl.usach.diinf.dene.Object;

import java.io.Serializable;
import org.bson.types.ObjectId;
import java.util.Date;

/**
 *
 * @author Teban
 */

/**
* Clase de marcadores para el mapeo de objetos JSON
*/
public class Marker implements Serializable{

    public ObjectId _id;
    public String contenido;
    public String categoria;
    public Double latitud;
    public Double longitud;
    public String userID;	 
    public Date generatedAt;
    public Marker(){}
}