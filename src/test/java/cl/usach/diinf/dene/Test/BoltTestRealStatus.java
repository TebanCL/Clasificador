/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.diinf.dene.Test;


import cl.usach.diinf.dene.Bolt.LocationRecognizer;
import cl.usach.diinf.dene.Object.Location;
import cl.usach.diinf.dene.Object.Normalizer;
import cl.usach.diinf.dene.Object.StopwordDictionary;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;

import org.junit.Test;

/**
 *
 * @author Teban
 */
public class BoltTestRealStatus{
    Reader reader;
    FakeStatusGenerator statusGenerator;
    
    public BoltTestRealStatus() throws UnsupportedEncodingException{
        statusGenerator = new FakeStatusGenerator();
        statusGenerator.prepareData();
        System.out.println("TAMAÑOS:");
        System.out.println(statusGenerator.first.size());
        System.out.println(statusGenerator.second.size());
        System.out.println(statusGenerator.third.size());
        System.out.println(statusGenerator.fourth.size());
    }
    
    /*@Test
    public void testLanguage(){
        assert(this.Language(statusGenerator.first) && this.Language(statusGenerator.second) && this.Language(statusGenerator.third) && this.Language(statusGenerator.fourth));
    }
    
    @Test
    public void testLocation(){
        assert(this.Location(statusGenerator.first) && this.Location(statusGenerator.second) && this.Location(statusGenerator.third) && this.Location(statusGenerator.fourth));
    }
    
    
    @Test
    public void testNormalizer(){
        assert(this.Normalizer(statusGenerator.first) && this.Normalizer(statusGenerator.second) && this.Normalizer(statusGenerator.third) && this.Normalizer(statusGenerator.fourth));
    }
    
    
    @Test
    public void testStopword(){
        assert(this.Stopword(statusGenerator.first) && this.Stopword(statusGenerator.second) && this.Stopword(statusGenerator.third) && this.Stopword(statusGenerator.fourth));
    }*/
    
    public boolean Language(ArrayList<Status> list){
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        ArrayList<Status> statuses = list;
        System.out.println(">> Preparando bolt de idioma. Tamaño total entrada: "+ list.size());
        
        System.out.println(">>> Comenzando prueba:");
        inputNumber = statuses.size();
        start = System.nanoTime()/1000;
        for(Status s : statuses){
            if(s.getLang().equals("es")){
                outputNumber++;
            }
        }
        end = System.nanoTime()/1000;
        
        long elapsedTime = end - start;
        int items = inputNumber - outputNumber;
        
        if(items >= 0){
            result = true;
        }
        
        System.out.println(">>>Prueba concluida - Resultados:");
        System.out.println("\tSe procesaron "+ inputNumber + " elementos.");
        System.out.println("\tSe emitieron "+ outputNumber + " elementos.");
        System.out.println("\tSe descartaron "+ items + " elementos.");
        System.out.println("\tTiempo empleado: " + elapsedTime + " milisegundos.");
        System.out.println(">>> Prueba Concluida.");
        
        return result;
    }
    
    public boolean Location(ArrayList<Status> list){
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        System.out.println(">> Preparando bolt de ubicación. Tamaño total entrada: "+ list.size());
        
        List<Location> chileanLocations = new ArrayList();
        try(BufferedReader br = new BufferedReader(new FileReader("C:/DeNe/CL.txt"))) {
            String line = br.readLine();
            while (line != null) {
                String[] lr = line.split(";");
                chileanLocations.add(new Location(lr[0],Double.parseDouble(lr[1]),Double.parseDouble(lr[2])));;
                line = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(LocationRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }      
        ArrayList<Status> statuses = list;
        
        inputNumber = statuses.size();
        System.out.println(">>> Comenzando prueba:");
        start = System.nanoTime()/1000;
        for(Status s: statuses){
            String content = s.getText();
            content = content.toLowerCase();
            for(Location l : chileanLocations){
                if(content.contains(l.getName().toLowerCase())){
                    outputNumber++;
                }
            }
        }
        end = System.nanoTime()/1000;
        
        long elapsedTime = end - start;
        int items = inputNumber - outputNumber;
        
        System.out.println(">>>Prueba concluida - Resultados:");
        System.out.println("\tSe procesaron "+ inputNumber + " elementos.");
        System.out.println("\tSe emitieron "+ outputNumber + " elementos.");
        System.out.println("\tSe descartaron "+ items + " elementos.");
        System.out.println("\tTiempo empleado: " + elapsedTime + " milisegundos.");
        System.out.println(">>> Prueba Concluida.");
        
        if(items >= 0){
            result = true;
        }
        
        return result;
    }
    
    public boolean Stopword(ArrayList<Status> list){
        
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        StopwordDictionary stopworddictionary = new StopwordDictionary();
        List<String> stopwords = stopworddictionary.getStopwordsAsArrayList();
        
        ArrayList<Status> statuses = list;
        System.out.println(">> Preparando bolt de stopword. Tamaño total entrada: "+ list.size());
        Normalizer textNormalizer = new Normalizer();
        
        inputNumber = statuses.size();
        System.out.println(">>> Comenzando prueba:");
        
        start = System.nanoTime()/1000;
        for(Status status: statuses){
            String textWOStopword = "";
            List<String> cS = new ArrayList();
            for(String s : status.getText().split(" "))
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
            outputNumber++;
        }
        end = System.nanoTime()/1000;
        
        long elapsedTime = end - start;
        int items = inputNumber - outputNumber;
        
        if(items >= 0){
            result = true;
        }
        
        System.out.println(">>>Prueba concluida - Resultados:");
        System.out.println("\tSe procesaron "+ inputNumber + " elementos.");
        System.out.println("\tSe emitieron "+ outputNumber + " elementos.");
        System.out.println("\tSe descartaron "+ items + " elementos.");
        System.out.println("\tTiempo empleado: " + elapsedTime + " milisegundos.");
        System.out.println(">>> Prueba Concluida."); 
        
        return result;
    }
    
    public boolean Normalizer(ArrayList<Status> list){
    
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        ArrayList<Status> statuses = list;
        System.out.println(">> Preparando bolt Normalizador. Tamaño total entrada: "+ list.size());
        
        System.out.println(">>> Comenzando prueba:");
        
        Normalizer textNormalizer = new Normalizer();
        
        inputNumber = statuses.size();
        
        start = System.nanoTime()/1000;
        for(Status status: statuses){
            String normalizedText = textNormalizer.normalizeText(123, status.getText());
            outputNumber++;
        }
        end = System.nanoTime()/1000;
        
        long elapsedTime = end - start;
        int items = inputNumber - outputNumber;
        
        if(items >= 0){
            result = true;
        }
        
        System.out.println(">>>Prueba concluida - Resultados:");
        System.out.println("\tSe procesaron "+ inputNumber + " elementos.");
        System.out.println("\tSe emitieron "+ outputNumber + " elementos.");
        System.out.println("\tSe descartaron "+ items + " elementos.");
        System.out.println("\tTiempo empleado: " + elapsedTime + " milisegundos.");
        System.out.println(">>> Prueba Concluida.");
        
        return result;
    }
}
