/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

/**
 *
 * @author Rolf Jagerman
 */
public class Item {
    
    public long id;
    
    public long buyPrice;
    
    public long sellPrice;
    
    public long buyVolume;
    
    public long sellVolume;
    
    public long rarity;
    
    public long restrictionLevel;
    
    public long type;
    
    public String name;
    
    public long getDirectRevenue() {
        return (long)(0.85 * buyPrice);
    }
    
    public long getSellRevenue() {
        return (long)(0.85 * sellPrice);
    }
    
}
