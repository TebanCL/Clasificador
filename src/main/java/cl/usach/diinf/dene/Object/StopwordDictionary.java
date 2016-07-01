
package cl.usach.diinf.dene.Object;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Teban
 */
public class StopwordDictionary {
    private String swS = "un,una,unas,unos,uno,sobre,todo,también,tras,otro,algun,alguno,algunos,algunas,ser,"+
                "es,soy,eres,somos,sois,estoy,esta,estamos,estais,estan,como,en,para,atras,porque,por qué,estado,"+
                "estaba,ante,antes,siendo,ambos,poder,puede,puedo,podemos,podeis,pueden,fui,fue,fuimos,fueron,hacer,"+
                "hago,hace,hacemos,haceis,hacen,cada,fin,incluso,primero,desde,conseguir,consigo,consigue,consigues,"+
                "conseguimos,consiguen,ir,voy,va,vamos,vais,van,vaya,gueno,ha,tener,tengo,tiene,tenemos,teneis,tienen,"+
                "el,la,lo,las,los,su,aqui,mio,tuyo,ellos,ellas,nos,nosotros,vosotros,vosotras,si,dentro,solo,solamente,"+
                "saber,sabes,sabe,sabemos,sabeis,saben,ultimo,largo,bastante,haces,muchos,aquellos,aquellas,sus,entonces,"+
                "tiempo,verdad,verdadero,verdadera,cierto,ciertos,cierta,ciertas,intentar,intento,intenta,intentas,intentamos,"+
                "intentais,intentan,dos,bajo,arriba,encima,usar,uso,usas,usa,usamos,usais,usan,emplear,empleo,empleas,emplean,"+
                "ampleamos,empleais,valor,muy,era,eras,eramos,eran,modo,bien,cual,cuando,donde,mientras,quien,con,entre,sin,trabajo,"+
                "trabajar,trabajas,trabajamos,trabajais,trabajan,podria,podriamos,podrian,podriais,yo,aquel";
        
    public StopwordDictionary(){
        
    }
    
    public String getStopwords(){
        return this.swS;
    }
    
    public String[] getStopwordsAsArray(){
        return this.swS.split(",");
    }
    
    public ArrayList<String> getStopwordsAsArrayList(){
        ArrayList<String> output = new ArrayList();
        boolean addAll = output.addAll(Arrays.asList(this.getStopwordsAsArray()));
        return output;
    }
    
    public String removeStopwords(String s){
        String[] split = s.split(" ");
        String output = "";
        for(String i: split){
            if(!isStopword(i)){
                output+=i+" ";
            }
        }
        return output;
    }
    
    public boolean isStopword(String w){
        for(String s: this.getStopwordsAsArrayList()){
            if(s.equals(w)){
                return true;
            }
        }
        return false;
    }
}
