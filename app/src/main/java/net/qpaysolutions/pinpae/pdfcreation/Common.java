package net.qpaysolutions.pinpae.pdfcreation;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Common {
    public static String getAppPath(Context context) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator
                + "app_pdf"
                + File.separator
        );
        if (!dir.exists())
            dir.mkdir();
        return dir.getPath() + File.separator;
    }
}
