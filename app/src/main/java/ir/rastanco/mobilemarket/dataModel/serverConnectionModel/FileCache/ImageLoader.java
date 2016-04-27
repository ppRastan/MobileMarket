package ir.rastanco.mobilemarket.dataModel.serverConnectionModel.FileCache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by ShaisteS on 1394/10/20.
 * This class Load image of product from server or memory and
 * when no find image in server or memory, show a default image
 */
public class ImageLoader {

    private final MemoryCache memoryCache=new MemoryCache();
    private final FileCache fileCache;
    private final Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private final ExecutorService executorService;
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }


    public void DisplayImage(String url, ImageView imageView)
    {
        //Drawable d=ResizeImage(R.drawable.loadingholder,rowView, Configuration.shopDisplaySizeForShow);
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            //imageView.setImageDrawable(d);
            queuePhoto(url, imageView);
            //imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;

        //from web
        try {
            Bitmap bitmap;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(70000);//before 30000,after see java.net.SocketTimeoutException change 70000
            conn.setReadTimeout(70000);//before 30000,after see java.net.SocketTimeoutException change 70000
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            //BitmapFactory.Options o = new BitmapFactory.Options();
            //o.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            //Find the correct scale value. It should be the power of 2.
            //int scale=1;
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            //o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.v("can not load image" , "!");}
        catch (OutOfMemoryError e) {
                memoryCache.clear();
        }
        return null;
    }
    //Task for the queue
    private class PhotoToLoad
    {
        public final String url;
        public final ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        final PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        final Bitmap bitmap;
        final PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)// {

                photoToLoad.imageView.setImageBitmap(bitmap);
          //  }
//            else {
//                //show default image when get image from url with error
//                //Drawable drawable = Utilities.getInstance().ResizeImage(R.drawable.loadingholder, context, displayWidth);
//                //photoToLoad.imageView.setImageDrawable(drawable);
//
//            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }



}