/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.usach.diinf.dene.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/**
 *
 * @author Teban
 */
public class FakeStatusGenerator {
    Random randomGenerator;
    ArrayList<Status> first;
    ArrayList<Status> second;
    ArrayList<Status> third;
    ArrayList<Status> fourth;
    
    public FakeStatusGenerator(){     
        randomGenerator = new Random();
    }
    public FakeStatusGenerator(int i){     
        randomGenerator = new Random();
    }
    
    public void prepareData() throws UnsupportedEncodingException{    
        first = this.getFullStatusList(1000);
        second = this.getFullStatusList(2000);
        third = this.getFullStatusList(4000);
        fourth = this.getFullStatusList(8000);
    };
    
    public ArrayList<Status> getFullStatusList(int howMany) throws UnsupportedEncodingException{
        String FileName="C:/tweets_json.JSON";
        ArrayList<Status> status = new ArrayList();
        try {
                System.out.println("Preparando lista...");
                status = ReadJSON(new File(FileName),"UTF-8", howMany);
            } catch (FileNotFoundException | ParseException e) {
                System.out.println("ARCHIVO NO ENCONTRADO!");;
            }
        return status;
    }
    
    public Status getStatus(String t){
        final String text = t;
        final Date date = new Date();
        final long id = randomGenerator.nextLong();
        final String lang = "es";
        
        return new Status() {
            @Override
            public Date getCreatedAt() {
                return date;
            }

            @Override
            public long getId() {
                return id;
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public String getSource() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isTruncated() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long getInReplyToStatusId() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long getInReplyToUserId() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getInReplyToScreenName() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public GeoLocation getGeoLocation() {
                return null;
            }

            @Override
            public Place getPlace() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isFavorited() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isRetweeted() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getFavoriteCount() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public User getUser() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isRetweet() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Status getRetweetedStatus() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long[] getContributors() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getRetweetCount() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isRetweetedByMe() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long getCurrentUserRetweetId() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isPossiblySensitive() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getLang() {
                return lang;
            }

            @Override
            public Scopes getScopes() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int compareTo(Status o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public RateLimitStatus getRateLimitStatus() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getAccessLevel() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public UserMentionEntity[] getUserMentionEntities() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public URLEntity[] getURLEntities() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public HashtagEntity[] getHashtagEntities() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public MediaEntity[] getMediaEntities() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SymbolEntity[] getSymbolEntities() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    
    }
    
    public static synchronized ArrayList<Status> ReadJSON(File MyFile,String Encoding, int howMany) throws FileNotFoundException, ParseException, UnsupportedEncodingException, OutOfMemoryError {
        ArrayList<JSONObject> json=new ArrayList<JSONObject>();
        int counter = 0;
        ArrayList<Status> statuses = new ArrayList();
        try{
            Scanner scn=new Scanner(MyFile,Encoding);
            while(scn.hasNext()){
                if(counter==howMany){
                    break;
                }
                JSONObject obj= (JSONObject) new JSONParser().parse(scn.nextLine());
                
                counter++;
                //writer.println((String)obj.get("id_str"));
                json.add(obj);
            }
            for(final JSONObject j: json){
                Status s = new Status() {
                    @Override
                    public Date getCreatedAt() {
                        return (Date)j.get("created_at");
                    }

                    @Override
                    public long getId() {
                        return (Long)j.get("id");
                    }

                    @Override
                    public String getText() {
                        return (String)j.get("text");
                    }

                    @Override
                    public String getSource() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isTruncated() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public long getInReplyToStatusId() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public long getInReplyToUserId() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public String getInReplyToScreenName() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public GeoLocation getGeoLocation() {
                        return null;
                    }

                    @Override
                    public Place getPlace() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isFavorited() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isRetweeted() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public int getFavoriteCount() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public User getUser() {
                        return null;
                    }

                    @Override
                    public boolean isRetweet() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public Status getRetweetedStatus() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public long[] getContributors() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public int getRetweetCount() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isRetweetedByMe() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public long getCurrentUserRetweetId() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isPossiblySensitive() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public String getLang() {
                        return (String)j.get("lang");
                    }

                    @Override
                    public Scopes getScopes() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public int compareTo(Status o) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public RateLimitStatus getRateLimitStatus() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public int getAccessLevel() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public UserMentionEntity[] getUserMentionEntities() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public URLEntity[] getURLEntities() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public HashtagEntity[] getHashtagEntities() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public MediaEntity[] getMediaEntities() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public SymbolEntity[] getSymbolEntities() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                statuses.add(s);
            }
        }
        catch(OutOfMemoryError e){}
        
        return statuses;
    }
}
