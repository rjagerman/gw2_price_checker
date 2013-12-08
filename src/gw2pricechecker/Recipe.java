/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

/**
 * A GW2 recipe
 * @author Rolf Jagerman
 */
public class Recipe implements Comparable {
    
    public static double ECTO_SALVAGE_CHANCE = 0.9;
    
    public long id;
    
    public long craftingCost;
    
    public long rating;
    
    public Item item;
    
    public Discipline discipline;
    
    public long directProfit() {
        if(item != null) {
            return item.getDirectRevenue() - craftingCost;
        } else {
            return -craftingCost;
        }
    }
    
    public long ectoProfit(Item ecto) {
        if (item != null &&(item.type == 0 || item.type == 15 || item.type == 18)
                && item.rarity >= 4 && item.restrictionLevel >= 68) {
            return (long)(ECTO_SALVAGE_CHANCE * ecto.getDirectRevenue()) - craftingCost;
        }
        return 0;
    }
    
    public long ectoSellProfit(Item ecto) {
        if(item != null && (item.type == 0 || item.type == 15 || item.type == 18)
                && item.rarity >= 4 && item.restrictionLevel >= 68) {
            return (long)(ECTO_SALVAGE_CHANCE * ecto.getDirectRevenue()) - craftingCost;
        }
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Recipe) {
            Recipe r = (Recipe)o;
            Long myDif = directProfit();
            Long rDif = r.directProfit();
            return myDif.compareTo(rDif);
        }
        return -1;
    }
    
    @Override
    public boolean equals(Object o) {
        return this.compareTo(o) == 0;
    }
    
}
