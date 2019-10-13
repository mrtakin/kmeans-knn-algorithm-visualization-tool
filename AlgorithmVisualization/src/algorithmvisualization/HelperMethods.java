package algorithmvisualization;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * This class keeps some of helper methods common used in project.
 * 
 * @author Murat AKIN
 */
public class HelperMethods {

    /**
     * It returns a random number between 0 and n. 
     * 
     * @see HelperMethods#rand(int, int) 
     */
    public static int rand(int n) {
        return rand(0, n);
    }

    /**
     * It returns a random number in given range. 
     */
    public static int rand(int a, int b) {
        Random r = new Random();
        return r.nextInt(b - a) + a;
    }

    /**
     * It checks given string is numeric or not.
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    /**
     * It changes presicion of given double number. (scaling)
     */
    public static double scaleDouble(double num, int scale){
        if(scale < 0) return num;
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
