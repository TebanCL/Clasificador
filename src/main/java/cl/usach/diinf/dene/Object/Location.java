
package cl.usach.diinf.dene.Object;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author teban
 */
public class Location implements Serializable{
    private String name;
    private double latitud;
    private double longitud;
    
    private Date generatedAt;
    private String content;
    
    public Location(String n, double lat, double lon, Date gen, String c)
    {
        this.name = n;
        this.latitud = lat;
        this.longitud = lon;
        this.generatedAt = gen;
        this.content = c;
    }
    
    public Location(String n, double lat, double lon)
    {
        this.name = n;
        this.latitud = lat;
        this.longitud = lon;
        this.generatedAt = new Date();
        this.content = "";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the latitud
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * @return the generatedAt
     */
    public Date getGeneratedAt() {
        return generatedAt;
    }

    /**
     * @param generatedAt the generatedAt to set
     */
    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    
    
}
