
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

    private OutputCollector collector;
    List<Location> chileanLocations = new ArrayList<>();
    ArrayList<Double> locations = new ArrayList();
    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {
        try(BufferedReader br = new BufferedReader(new FileReader("C:/DeNe/CL.txt"))) {
            String line = br.readLine();
            while (line != null) {
                String[] lr = line.split(";");
                this.chileanLocations.add(new Location(lr[0],Double.parseDouble(lr[1]),Double.parseDouble(lr[2])));;
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(LocationRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("status");
        String normalizedText = (String) tuple.getValueByField("normalizedText");
        double lat = 0.0, lon = 0.0;
        if(null==status.getGeoLocation()){
            //Caso No Geolocalizado
            String content = status.getText();
            content = content.toLowerCase();
            for(Location l : this.chileanLocations){
                if(content.contains(l.getName().toLowerCase())){
                    //Si habla de chile...
                        locations.add(l.getLatitud());
                        locations.add(l.getLongitud());
                    break;
                }
            }
            if(lat != 0.0 && lon != 0.0){
                //-----------------------------------
                this.collector.emit(new Values(status, normalizedText, locations));
            }
            //Si no habla de Chile, se olvida.
        }
        else{
            //Caso Geolocalizado
            if(this.isInChile(status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude())){
                //Dentro de chile y habla de chile?
                String content = status.getText();
                content = content.toLowerCase();
                for(Location l : this.chileanLocations){
                    if(content.contains(l.getName().toLowerCase())){
                        locations.add(l.getLatitud());
                        locations.add(l.getLongitud());
                        break;
                    }
                }
                this.collector.emit(new Values(status, normalizedText, locations));
            }
            else{
                //Dentro de chile y no habla de chile?
                System.out.println("Dentro de chile, pero no habla de algun lugar en particular");
                this.collector.emit(new Values(status, normalizedText, locations));
            }
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "normalizedText", "locations"));
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
