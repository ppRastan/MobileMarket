package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ShaisteS on 1394/10/20.
 * A Singleton Class for copy data stream
 */
public class Utils {

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){
            Log.v("Error happened in Utils class" , " !");
        }
    }
}
