package ir.rastanco.mobilemarket.utility;

/**
 * Created by ParisaRashidhi on 29/03/2016.
 */
public class ColorUtility  {

    private static ColorUtility colorUtility;
    public static ColorUtility getInstance() {
        if(colorUtility == null){
            colorUtility = new ColorUtility();
        }
        return colorUtility;
    }

}
