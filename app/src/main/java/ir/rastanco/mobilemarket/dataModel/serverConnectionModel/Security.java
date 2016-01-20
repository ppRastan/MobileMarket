package ir.rastanco.mobilemarket.dataModel.serverConnectionModel;

import android.util.Base64;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Created by ShaisteS on 1394/10/30.
 */
public class Security {

    private String MD5(String information)
    {
        String informationToHash =information;
        String generatedInformation = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(informationToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedInformation = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedInformation;
    }

    private String Base64(String information){
        return Base64.encodeToString(information.getBytes(),Base64.NO_WRAP); //0 or No_WRAP
    }

    public String encode(String user,String pass,String key){

        String hashKey=MD5(Base64(MD5(key)));
        String hashUserPass=MD5(hashKey+Base64(user+hashKey+pass)+hashKey);
        return hashUserPass;
    }
}
