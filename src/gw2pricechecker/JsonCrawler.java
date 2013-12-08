/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The JSON crawler that allows easier traversal of the JSON tree
 * 
 * @author Rolf Jagerman
 */
public final class JsonCrawler {
    
    /**
     * The object that is attempted to crawl
     */
    protected Object json;
    
    /**
     * Creates a json crawler for given object
     * 
     * @param obj 
     */
    public JsonCrawler(Object jsonObject) {
        this.json = jsonObject;
    }
    
    /**
     * Gets the value for given key
     * @param key The key
     * @return The json crawler of key
     * @throws JsonException 
     */
    public JsonCrawler get(String key) throws JsonException {
        if(json instanceof JSONObject) {
            return new JsonCrawler(((JSONObject)json).get(key));
        } else if(json instanceof JSONArray) {
            return new JsonCrawler(((JSONArray)json).get(Integer.parseInt(key)));
        }
        throw new JsonException("Not a json array or object");
    }
    
    /**
     * Gets the value for given key
     * @param key The key
     * @return The json crawler of key
     * @throws JsonException 
     */
    public JsonCrawler get(int key) throws JsonException {
        if(json instanceof JSONObject) {
            return new JsonCrawler(((JSONObject)json).get(key));
        } else if(json instanceof JSONArray) {
            return new JsonCrawler(((JSONArray)json).get(key));
        }
        throw new JsonException("Not a json array or object");
    }
    
    /**
     * Converts the value to a list
     * @return The list
     * @throws JsonException 
     */
    public ArrayList<JsonCrawler> list() throws JsonException {
        if(json instanceof JSONArray) {
            ArrayList<JsonCrawler> list = new ArrayList<JsonCrawler>(((JSONArray)json).size());
            for(Object obj : ((JSONArray)json)) {
                list.add(new JsonCrawler(obj));
            }
            return list;
        }
        throw new JsonException("Invalid data type");
    }
    
    /**
     * Converts the value to an integer
     * @return The integer value
     * @throws JsonException 
     */
    public int integer() throws JsonException {
        if(json instanceof Integer) {
            return (Integer)json;
        } else if(json instanceof String) {
            return Integer.parseInt((String)json);
        } else if(json instanceof Long) {
            long l = ((Long)json).longValue();
            if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
            }
            return (int)l;
        } else {
            throw new JsonException("Invalid data type");
        }
    }
    
    /**
     * Converts the value to an integer
     * @return The integer value
     * @throws JsonException 
     */
    public long longValue() throws JsonException {
        if(json instanceof Long) {
            return (Long)json;
        } else if(json instanceof String) {
            return Long.parseLong((String)json);
        } else if(json instanceof Integer) {
            return ((Integer)json).intValue();
        } else {
            throw new JsonException("Invalid data type");
        }
    }
    
    /**
     * Converts the value to a string
     * @return The string value
     * @throws JsonException
     */
    public String string() throws JsonException {
        if(json instanceof String) {
            return (String)json;
        } else {
            throw new JsonException("Invalid data type");
        }
    }
    
}
