package org.commcare.mwellcare;
/**
 * This PdfPrintDocumentAdapter class prepares the PDF document
 * and giving the pdf to Android print frame work
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;


public class PdfPrintDocumentAdapter extends PrintDocumentAdapter{
   
    private Activity mActivity ;
    private final String TAG = this.getClass().getName();
    
    public PdfPrintDocumentAdapter(Activity activity) {
        this.mActivity = activity;
    }
    @Override
    public void onStart() {
        super.onStart();
        Intent intent = mActivity.getIntent();
        Bundle bundle = intent.getExtras();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
            PrintAttributes newAttributes,
            CancellationSignal cancellationSignal,
            PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
        
     // Respond to cancellation request
        if(cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
            return;
        }

         // Return print information to print framework
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build();
            // Content layout reflow is complete
            callback.onLayoutFinished(info, true);
            
//            callback.onLayoutFailed("Page count calculation failed.");

    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination,
            CancellationSignal cancellationSignal, WriteResultCallback callback) {
        
        InputStream input = null;
        OutputStream output = null;

        try {

            input = new FileInputStream(Environment.getExternalStorageDirectory()+"/abc.pdf");
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                 output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (FileNotFoundException ee){
            Log.d(TAG, "FileNotFoundException"+ee);
        } catch (Exception e) {
            Log.d(TAG, "Exception"+e);
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onFinish() {
        super.onFinish();
        
    }
    

}
