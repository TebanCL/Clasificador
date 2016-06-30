
package cl.usach.diinf.dene.Object;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

/**
 *
 * @author teban
 * Esta clase busca en la base de datos si se produjo un cambio
 * en los terminos de búsqueda de la aplicación.
 * El uso es: Hacer check(), revisar si cambió con hasChanged() y
 * si es asi, recuperar los terminos como un Arreglo de strings con
 * getTerms().
 */
public class CurrentQueryChecker implements Serializable{
    
    private ArrayList<String> terminos = new ArrayList();
    private Date queryTimestamp = new Date();
    private boolean hasChanged = false;
    private String dataBaseName = "DeNe-test";
    
    private DB db;
    private Jongo jongo;
    
    public CurrentQueryChecker(){
        db = new MongoClient().getDB(dataBaseName);
        jongo = new Jongo(db);
        this.terminos = new ArrayList();
    }    
    public CurrentQueryChecker(ArrayList terminos){
        db = new MongoClient().getDB(dataBaseName);
        jongo = new Jongo(db);
        this.terminos = terminos;
    }
    
    /**
     * Busca si hay nuevas queries en la base de datos que no sean
     * aquellas que ya se tienen.
     * @return boolean Positivo si hay nuevas o falso en caso contrario.
     */
    private boolean areThereNewQueries(){
        MongoCollection queries = jongo.getCollection("queries");
        if(queries.count() == 0){
            System.out.println("No se encontraron consultas.");
            return false;
        }
        MongoCursor<Query> currentQueries = queries.find().as(Query.class);
        /*
            Si es el actual y no es el mismo que estoy revisando ahora... 
        */
        int actualCounter = 0;
        while(currentQueries.hasNext()){
            Query iter = (Query) currentQueries.next();
            if(iter.estado.equals("actual")){
                actualCounter++;
            }
            if(iter.estado.equals("actual") && !iter.terminos.equals(terminos)){
                this.terminos = new ArrayList(iter.terminos);
                this.queryTimestamp = iter.generatedAt;
                return true;
            }
        }
        /*if(actualCounter == 0){
            if(this.terminos.isEmpty()){
                return false;
            }
            else{
                this.terminos = new ArrayList();
                return true;
            }
        }*/
        return false;
    }
    
    /**
     * Retorna si es que ha cambiado o no el estado de la consulta actual.
     * @return boolean
     */
    public boolean getStatus(){
        return this.hasChanged;
    }
    
    /**
     * Retorna los términos de la clase como un arreglo de strings.
     * @return String[]
     */
    public String[] getTerms(){
        String[] ret = new String[this.terminos.size()];
        for(int i = 0; i < this.terminos.size();i++){
            ret[i] = this.terminos.get(i);
        }
        return ret;
    }
    
    public ArrayList<String> getTermsList(){
        return this.terminos;
    }
   
    /**
     * Realiza la consulta.
     */
    public void check() {
        this.hasChanged = areThereNewQueries();
    }
    
    public Date getLastQueryDate(){
        return this.queryTimestamp;
    }
    
}
