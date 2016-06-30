
package cl.usach.diinf.dene.Object;

import java.io.Serializable;


/**
 *
 * @author Teban
 */
public class Normalizer implements Serializable{    
        
    public Normalizer(){
    }
    
    /**
     * Normalize a given text.
     * level options:
     *  1 - URL
     *  2 - Hashtag
     *  3 - Emoticon
     *  12 - URL + Hashtag
     *  13 - URL + Emoticon
     *  23 - Hashtag + Emoticon
     *  123 - URL + Hastag + Emoticon
     * @param level which filters should be applied.
     * @param input input string.
     * @return normalized string.
     */
    public String normalizeText(int level, String input){
        String output;
        switch(level){
            case 1:
                output = this.removeURL(input);
                output = this.replaceUser(output);
                break;
            case 2:
                output = this.removeHS(input);
                output = this.replaceUser(output);
                break;
            case 3:
                output = this.removeEmoticon(input);
                output = this.replaceUser(output);
                break;
            case 12:
                output = this.removeURL(input);
                output = this.removeHS(output);
                output = this.replaceUser(output);
                break;
            case 13:
                output = this.removeURL(input);
                output = this.removeEmoticon(output);
                output = this.replaceUser(output);
                break;
            case 23:
                output = this.removeHS(input);
                output = this.removeEmoticon(output);
                output = this.replaceUser(output);
                break;
            case 123:
                output = this.removeURL(input);
                output = this.removeHS(output);                
                output = this.removeEmoticon(output);
                output = this.replaceUser(output);
                break;
            default:
                output = this.normalizeText(123, input);
                break;
            
        }
        return output.toLowerCase();
    }
    
    public String removeURL(String s){
        String[] sS = s.split(" ");
        String output = "";
        for(String sT : sS){
            if(sT.startsWith("http") || sT.startsWith("www.")){
                output+="URL ";
            }
            else{
                output+=sT+" ";
            }
                
        }
        return output;       
    }
    
    public String removeHS(String s){
        String[] sS = s.split(" ");
        String output = "";
        for(String sT : sS){
            if(!sT.startsWith("#")){
                output+= sT+" ";
            }
            else{
                output+= "HASHTAG ";
            }
        }
        return output; 
    }
    public String removeEmoticon(String s){
        
        /*String[] sS = s.split(" ");
        String output = "";
        for(String sT : sS){
            if(!this.containsOthers(sT)){
                output+= sT+" ";
            }
        }
        return output; */
        return s;
        
    }
    
    public String replaceUser(String s){
        String[] sS = s.split(" ");
        String output = "";
        for(String sT : sS){
            if(!sT.startsWith("@")){
                output+= sT+" ";
            }
            else{
                output+= "USUARIO ";
            }
        }
        return output;
    }
    
    public boolean containsOthers(String s){
        return !s.matches("^[^a-zA-Z\\d\\s]*$");
    }
    
    public String clean(String s){
        String[] sS = s.split(" ");
        String output = "";
        for(String sT : sS){
            
            output = sT.replaceAll(" ","");
            output = output.replaceAll("\"","");
            output = output.replaceAll("'","");
            output = output.replaceAll("@","");
            output = output.replaceAll(" rt ","");                
            output = output.replaceAll(" rt","");
            output = output.replaceAll("rt ","");
            output = output.replaceAll(":","");
            output = output.replaceAll(",","");
            output = output.replaceAll("\\.","");
            output = output.replaceAll("?","");
            output = output.replaceAll("!","");
            output = output.replaceAll("¿","");
            output = output.replaceAll("¡","");
            output = output.replaceAll("%","");
            output = output.replaceAll("(","");
            output = output.replaceAll(")","");
            output = output.replaceAll("#","");
            output = output.replaceAll("º","");
            output = output.replaceAll("{","");
            output = output.replaceAll("}","");
            output = output.replaceAll("[","");
            output = output.replaceAll("]","");
            output = output.replaceAll("|","");
            output = output.replaceAll("/","");
            output = output.replaceAll("&","");
            output = output.replaceAll("$","");
            output = output.replaceAll("=","");
            output = output.replaceAll("&gt;","");
            output = output.replaceAll("&lt;","");
            output = output.replaceAll("<","");
            output = output.replaceAll(">","");
        }
        return output; 
        
        
    }
}
