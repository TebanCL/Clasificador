
package cl.usach.diinf.dene.Object;

import java.io.Serializable;

/**
 *
 * @author Teban
 */
public class InstanceUtil implements Serializable{
    
    String name;
    String label;
    String content;
    
    public InstanceUtil(String n, String l, String c){
        this.name = n;
        this.label = l;
        this.content = c;
    }
    
}
