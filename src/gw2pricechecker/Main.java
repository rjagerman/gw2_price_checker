/*
 * GW2 Price Checker
 * By Rolf Jagerman
 */
package gw2pricechecker;

import gw2pricechecker.view.MainView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Rolf Jagerman
 */
public class Main {

    /**
     * The loader that runs in the swing event handling thread to initiate the
     * GUI and run the application.
     */
    protected static class Loader implements Runnable {
        @Override
        public void run() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
            } catch (InstantiationException ex) {
            } catch (IllegalAccessException ex) {
            } catch (UnsupportedLookAndFeelException ex) {
            }
            MainView mainView = new MainView();
            mainView.setVisible(true);
        }
    }
    
    /**
     * Entry point of the application
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Loader loader = new Loader();
        SwingUtilities.invokeLater(loader);
    }
}
