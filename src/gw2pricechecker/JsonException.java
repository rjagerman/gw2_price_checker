/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

/**
 * A JSON exception
 * 
 * @author Rolf Jagerman
 */
public class JsonException extends Exception {
    
    /**
     * Creates a JSON exception
     * 
     * @param message The message
     */
    public JsonException(String message) {
        super(message);
    }
    
}
