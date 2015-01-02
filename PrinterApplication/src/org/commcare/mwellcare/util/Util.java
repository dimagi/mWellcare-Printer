package org.commcare.mwellcare.util;

import org.commcare.mwellcare.R;
import org.commcare.mwellcare.projectconfigs.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class Util {
    /**
     * This method is getPdf path {@link SharedPreferences}.
     * @param activity {@link Activity}
     * @return pdf path
     */
    public static String getPdfNameFromSP(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(Constants.PDF_CONFIGURATION, android.content.Context.MODE_PRIVATE);
        return preferences.getString(Constants.PDF_PATH, null);
    }

    /**
     * This method is used to save pdf path {@link SharedPreferences}.
     * @param activity {@link Activity}
     * @param pdfPath
     */
    public static void savePdfNameInSP(Context ctx, String pdfPath){
        SharedPreferences preferences = ctx.getSharedPreferences(Constants.PDF_CONFIGURATION, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PDF_PATH, pdfPath);

        editor.commit();
    }
    
    
    /**
     * This method is used to save printjobID path {@link SharedPreferences}.
     * @param activity {@link Activity}
     * @param printjobid
     */
    public static void saveUniquePrintJobIDSP(Context ctx, long printjobID){
        SharedPreferences preferences = ctx.getSharedPreferences(Constants.PDF_CONFIGURATION, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Constants.PRINTJOB_ID, printjobID);

        editor.commit();
    }
    /**
     * This method is get printjobid {@link SharedPreferences}.
     * @param activity {@link Activity}
     * @return printjobid
     */
    public static long getUniquePrintJobIDFromSP(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(Constants.PDF_CONFIGURATION, android.content.Context.MODE_PRIVATE);
        return preferences.getLong(Constants.PRINTJOB_ID, 0);
    }
    /**
     * Show the alert in the specified activity and message
     * @param CTX Context object
     * @param MSG String message that should be shown on the alert
     */
    public static void showAlert(Context ctx, String msg){
        final AlertDialog.Builder alert=new AlertDialog.Builder(ctx);
        alert.setTitle(ctx.getResources().getString(R.string.alert));
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setPositiveButton(ctx.getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog1, int which) {
            }
        });
        alert.show();
    }
}
