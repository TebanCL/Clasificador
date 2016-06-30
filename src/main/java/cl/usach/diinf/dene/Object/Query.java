
package cl.usach.diinf.dene.Object;

/**
 * @author Teban
 */

import java.io.Serializable;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;

/**r
* Clase de marcadores para el mapeo de objetos JSON
*/
public class Query implements Serializable{

    public ObjectId _id;
    public ArrayList<String> terminos;
    public String estado; 
    public Date generatedAt;
    
    public Query(){
    	terminos = new ArrayList();
    }
    public void cleanUp(){
    	terminos.clear();
    }
    
    @Override
    public String toString(){
        if(this.terminos == null){
            return "none";
        }
        String q = "\n==== QUERY: "+_id.toHexString()+" ===="; 
        q+= "\nTerminos: "+this.terminos.toString();
        q+= "\nEstado: "+this.estado;
        q+= "\nFecha: "+this.generatedAt.toString();
        return q;
    }
    
    public String[] toStringArray(){
       if(this.terminos.isEmpty()){
           String[] result = new String[1];
           result[0] = "";
           return result;
       }
       String temp  = this.terminos.toString().replace("[", "");
       temp = temp.replace("]", "");  
       String[] tempArray = temp.split(",");
       String[] result = new String[tempArray.length];
       int index = 0;
       for(String s : tempArray){
           result[index++] = s.replaceFirst("^\\s*", "");
       }
       return result;
    }
}
