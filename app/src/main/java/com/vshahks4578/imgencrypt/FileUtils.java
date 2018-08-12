package com.vshahks4578.imgencrypt;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

public class FileUtils {

    private static final String LOG_TAG = FileUtils.class.getSimpleName();

    public static File convert(File file, String fileDir) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        File temp = new File(fileDir, "conversion.png");
        try {
            FileOutputStream out = new FileOutputStream(temp);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "exception", e);
        }
        return temp;
    }

    public static String getExtension(File file) throws IOException{
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        String fileType = URLConnection.guessContentTypeFromStream(in);
        in.close();
        return fileType.toLowerCase();
    }
}
