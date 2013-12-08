/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.json.simple.parser.ParseException;

/**
 * This thread gets the information from the internet and creates the objects
 * @author Rolf Jagerman
 */
public class PriceCheckerThread extends SwingWorker {
    
    private ChangeListener cl;
    
    private List<Recipe> profitableRecipes;
    
    private boolean running;
    
    private String status;
    
    private int pagesProcessed = 0;
    
    private Item ecto;
    
    public PriceCheckerThread(ChangeListener cl) {
        this.cl = cl;
        this.profitableRecipes = new LinkedList<Recipe>();
        this.running = false;
        this.status = "Idle";
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        
        running = true;
        
        try {
            
            // Get all disciplines
            this.status = "Loading disciplines...";
            TreeMap<Long, Discipline> disciplines = new TreeMap<Long, Discipline>();
            JsonCrawler disciplinesJson = HttpCrawler.getJson("http://www.gw2spidy.com/api/v0.9/json/disciplines");
            for(JsonCrawler disciplineJson : disciplinesJson.get("results").list()) {
                try {
                    Discipline discipline = new Discipline();
                    discipline.id = disciplineJson.get("id").longValue();
                    discipline.name = disciplineJson.get("name").string();
                    disciplines.put(discipline.id, discipline);
                } catch (JsonException ex) {
                }
            }
            
            // Get all items
            this.status = "Loading game items...";
            notifyListener();
            TreeMap<Long, Item> items = new TreeMap<Long, Item>();
            JsonCrawler itemsJson = HttpCrawler.getJson("http://www.gw2spidy.com/api/v0.9/json/all-items/all");
            for(JsonCrawler itemJson : itemsJson.get("results").list()) {
                try {
                    Item item = new Item();
                    item.id = itemJson.get("data_id").longValue();
                    item.buyPrice = itemJson.get("max_offer_unit_price").longValue();
                    item.sellPrice = itemJson.get("min_sale_unit_price").longValue();
                    item.buyVolume = itemJson.get("offer_availability").longValue();
                    item.sellVolume = itemJson.get("sale_availability").longValue();
                    item.rarity = itemJson.get("rarity").longValue();
                    item.restrictionLevel = itemJson.get("restriction_level").longValue();
                    item.type = itemJson.get("type_id").longValue();
                    item.name = itemJson.get("name").string();
                    items.put(item.id, item);
                } catch (JsonException ex) {
                }
            }
            
            // Set ecto item by ID
            ecto = items.get(19721l);
            
            // Loop over all recipes
            this.status = "Finding recipes...";
            notifyListener();
            int page = 1;
            long maxpage = 2;
            while(page < maxpage) {
                LinkedList<Recipe> recipes = new LinkedList<Recipe>();
                
                try {
                    JsonCrawler recipesJson = HttpCrawler.getJson("http://www.gw2spidy.com/api/v0.9/json/recipes/all/" + page);
                    maxpage = recipesJson.get("last_page").longValue();
                    for(JsonCrawler recipeJson : recipesJson.get("results").list()) {
                        try {

                            Recipe recipe = new Recipe();
                            JsonCrawler debug = recipeJson.get("data_id");
                            recipe.id = debug.longValue();
                            recipe.craftingCost = recipeJson.get("crafting_cost").longValue();
                            recipe.rating = recipeJson.get("rating").longValue();

                            Discipline discipline = disciplines.get(recipeJson.get("discipline_id").longValue());
                            if(discipline != null) {
                                recipe.discipline = discipline;
                            }

                            Item item = items.get(recipeJson.get("result_item_data_id").longValue());
                            if(item != null) {
                                recipe.item = item;
                            }
                            recipes.offer(recipe);
                        } catch(JsonException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch(ParseException ex) {
                } catch(JsonException ex) {
                } catch(IOException ex) {
                } finally {
                    page++;
                }

                int preSize = profitableRecipes.size();
                for(Recipe r : recipes) {
                    if(r.directProfit() > 0 && r.item.buyVolume > 0) {
                        if(!profitableRecipes.contains(r)) {
                            profitableRecipes.add(r);
                        }
                    }
                    if(r.ectoProfit(ecto) > 0 && r.item.buyVolume > 0) {
                        if(!profitableRecipes.contains(r)) {
                            profitableRecipes.add(r);
                        }
                    }
                }
                int postSize = profitableRecipes.size();
                if(preSize != postSize) {
                    notifyListener();
                }
                
            }
            pagesProcessed = page-1;
            
        } catch (JsonException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    @Override
    protected void done() {
        running = false;
        status = "Done (processed " + pagesProcessed + " pages)";
        notifyListener();
    }
    
    protected void notifyListener() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cl.stateChanged(new ChangeEvent(this));
            }
        });
    }
    
    public List<Recipe> getRecipes() {
        return profitableRecipes;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public String getStatus() {
        return status;
    }
    
    public Item getEcto() {
        return ecto;
    }
    
}
