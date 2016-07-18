
package cl.usach.diinf.dene.Bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cl.usach.diinf.dene.Object.Location;
import twitter4j.Status;

/**
 * Por cada estado revisa:
 * - ¿Esta geolocalizado?
 * :: SI
 *    ¿Está en Chile?
 *    :: SI
 *       Emitir
 *    :: NO
 *       No Emitir
 * :: NO
 * -  ¿Habla de chile?
 *    :: SI
 *       Intentar obtener ubicación a partir del texto utilizando 
 *       una bolsa con todas las comunas y sus ubicaciones geográficas
 *       :: SI lo ubica
 *          Emitir
 *    :: NO
 *       No emitir
 * @author teban
 */
public class LocationRecognizer implements IRichBolt{
    private int inputCounter = 0;
    private int elementCounter = 0;
    private OutputCollector collector;
    List<Location> chileanLocations = new ArrayList<>();
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        this.collector = oc;
        try(BufferedReader br = new BufferedReader(new FileReader("C:/DeNe/CL.txt"))) {
            String line = br.readLine();
            while (line != null) {
                String[] lr = line.split(";");
                this.chileanLocations.add(new Location(lr[0],Double.parseDouble(lr[1]),Double.parseDouble(lr[2])));
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(LocationRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    @Override
    public void execute(Tuple tuple) {
        inputCounter++;
        //ArrayList<Double> locations = new ArrayList();
        Status status = (Status) tuple.getValueByField("status");
        String normalizedText = (String) tuple.getValueByField("text");
        if(normalizedText==null){
            normalizedText = "";
        }
        double lat = 0.0, lon = 0.0;
        try{
            if(status.getGeoLocation() == null){
            for(Location l: this.chileanLocations){
                String content = status.getText().toLowerCase();
                content = content.replace("á", "a");
                content = content.replace("é", "e");
                content = content.replace("í", "i");
                content = content.replace("ó", "o");
                content = content.replace("ú", "u");
                String locationName = l.getName().toLowerCase();
                if(content.contains(locationName)){ 
                    /*locations.add(l.getLatitud());
                    locations.add(l.getLongitud());
                    */
                    elementCounter++;
                    System.out.println("UBICACION - Emitidos: "+elementCounter);
                    this.collector.emit(new Values(status, normalizedText, l.getLatitud(), l.getLongitud()));
                }
                // SINO NO EMITE NADA
            }
            }
            else{
                /*locations.add(status.getGeoLocation().getLatitude());
                locations.add(status.getGeoLocation().getLongitude());*/
                elementCounter++;
                this.collector.emit(new Values(status, normalizedText, status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude()));
            }
        }
        catch(NullPointerException e){       
        }          
    }

    @Override
    public void cleanup() {
       System.out.println("Elementos recibidos UBICACION: "+inputCounter+"\nEstados emitidos UBICACION: " + elementCounter);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "text", "latitud", "longitud"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
    boolean isInChile(double lat, double lon){
        boolean flag1 = false, flag2 = false;
        if(lat > -109.45 && lat < -67.48333)
            flag1 = true;
        if(lon < -17.54528 && lon > -54.93333)
            flag2 = true;
        return flag1 && flag2;
    }
    
}
