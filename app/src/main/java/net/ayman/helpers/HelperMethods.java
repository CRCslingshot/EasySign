package net.ayman.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperMethods {

    public static String getVideo(byte[] bytevideo) {
        String currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
        String date = currentDateandTime;
        String filename = "/rec" + date.replace(" ", "_").replace(":", "_") + ".mp4";
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Ayman");
            if (!myDir.exists())
                myDir.mkdir();
            File file = new File(myDir, filename);
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytevideo);
            out.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
