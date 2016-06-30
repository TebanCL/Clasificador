
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import twitter4j.Status;

/**
 * 
 * @author Teban
 */
public class StopwordRemover implements IRichBolt {

    List<String> stopwords = new ArrayList();
            
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        
        String swS = "un,una,unas,unos,uno,sobre,todo,también,tras,otro,algun,alguno,algunos,algunas,ser,"+
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
        
        String[] stopwordList = swS.split(",");
        stopwords.addAll(Arrays.asList(stopwordList));
        
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("Status");
        String normalizedText = (String) tuple.getValueByField("normalizedText");
        ArrayList<Double> locations = (ArrayList<Double>) tuple.getValueByField("locations");
       
        String textWOStopword = "";
        List<String> cS = new ArrayList();
        for(String s : normalizedText.split(" "))
        {
            cS.add(s);
        }
        //cS.addAll(Arrays.asList(contenido.split(" ")));
        for(String s : cS){
            if(!stopwords.contains(s.toLowerCase()))
            {
                textWOStopword+=s+" ";
            }
        } 
        
        this.collector.emit(new Values(status, textWOStopword, locations));
        
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "textWOStopword", "locations"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
}
