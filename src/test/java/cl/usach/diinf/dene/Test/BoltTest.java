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
public class BoltTest{
    Reader reader;
    FakeStatusGenerator statusGenerator;
    
    public BoltTest(){
        reader = new Reader();
        statusGenerator = new FakeStatusGenerator();
    }
    
    //@Test
    public void testLanguage(){
        System.out.println("PRUEBA IDIOMA.");
        assert(Language(reader.getQuarterFile()) && Language(reader.getHalfFile()) && Language(reader.getFullFile()));
    }
    
    //@Test
    public void testLocation(){
        System.out.println("PRUEBA UBICACION.");
        assert(Location(reader.getQuarterFile()) && Location(reader.getHalfFile()) && Location(reader.getFullFile()));
    }
    
    //@Test
    public void testNormalizer(){
        System.out.println("PRUEBA NORMALIZADOR.");
        assert(Normalizer(reader.getQuarterFile()) && Normalizer(reader.getHalfFile()) && Normalizer(reader.getFullFile()));
    }
    
    //@Test
    public void testStopword(){
        System.out.println("PRUEBA STOPWORD.");
        assert(Stopword(reader.getQuarterFile()) && Stopword(reader.getHalfFile()) && Stopword(reader.getFullFile()));
    }
    
    public boolean Language(ArrayList<String> StatusList){
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        ArrayList<Status> statuses = new ArrayList();
        System.out.println(">> Preparando bolt de idioma. Tamaño total entrada: "+ reader.getFullFile().size()+". Utilizados: "+StatusList.size());
        for(String s: StatusList){
            Status st = statusGenerator.getStatus(s);
            statuses.add(st);
        }
        System.out.println(">>> Comenzando prueba:");
        inputNumber = statuses.size();
        start = System.nanoTime()/1000000;
        for(Status s : statuses){
            if(s.getLang().equals("es")){
                outputNumber++;
            }
        }
        end = System.nanoTime()/1000000;
        
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
    
    public boolean Location(ArrayList<String> StatusList){
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        System.out.println(">> Preparando bolt de ubicación. Tamaño total entrada: "+ reader.getFullFile().size()+". Utilizados: "+StatusList.size());
        
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
        ArrayList<Status> statuses = new ArrayList();
        for(String s: StatusList){
            statuses.add(statusGenerator.getStatus(s));
        }
        
        inputNumber = statuses.size();
        System.out.println(">>> Comenzando prueba:");
        start = System.nanoTime();
        for(Status s: statuses){
            String content = s.getText();
            content = content.toLowerCase();
            for(Location l : chileanLocations){
                if(content.contains(l.getName().toLowerCase())){
                    outputNumber++;
                }
            }
        }
        end = System.nanoTime();
        
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
    
    public boolean Stopword(ArrayList<String> StatusList){
        
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        StopwordDictionary stopworddictionary = new StopwordDictionary();
        List<String> stopwords = stopworddictionary.getStopwordsAsArrayList();
        
        ArrayList<Status> statuses = new ArrayList();
        System.out.println(">> Preparando bolt de stopword. Tamaño total entrada: "+ reader.getFullFile().size()+". Utilizados: "+StatusList.size());
        Normalizer textNormalizer = new Normalizer();
        for(String s: StatusList){
            Status st = statusGenerator.getStatus(textNormalizer.normalizeText(123,s));
            statuses.add(st);
        }
        
        inputNumber = statuses.size();
        System.out.println(">>> Comenzando prueba:");
        
        start = System.nanoTime();
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
        end = System.nanoTime();
        
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
    
    public boolean Normalizer(ArrayList<String> StatusList){
    
        long start;
        long end;
        int inputNumber;
        int outputNumber = 0;
        boolean result = false;
        
        ArrayList<Status> statuses = new ArrayList();
        System.out.println(">> Preparando bolt Normalizador. Tamaño total entrada: "+ reader.getFullFile().size()+". Utilizados: "+StatusList.size());
        for(String s: StatusList){
            Status st = statusGenerator.getStatus(s);
            statuses.add(st);
        }
        System.out.println(">>> Comenzando prueba:");
        
        Normalizer textNormalizer = new Normalizer();
        
        inputNumber = statuses.size();
        
        start = System.nanoTime();
        for(Status status: statuses){
            String normalizedText = textNormalizer.normalizeText(123, status.getText());
            outputNumber++;
        }
        end = System.nanoTime();
        
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
