package com.vl.printerapplication;
/**
 * This PdfPrintDocumentAdapter class prepares the PDF document
 * and giving the pdf to Android print frame work
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfPrintDocumentAdapter extends PrintDocumentAdapter{
   
    private MainActivity mActivity ;
    private final String TAG = this.getClass().getName();
    private static Font catFont = new Font(Font.TIMES_ROMAN, 18,
            Font.BOLD);
        private static Font subFont = new Font(Font.TIMES_ROMAN, 16,
            Font.BOLD);
        private static Font smallBold = new Font(Font.TIMES_ROMAN, 12,
            Font.BOLD);
    public PdfPrintDocumentAdapter(MainActivity activity) {
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
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(mActivity.getFilesDir()+"/abc.pdf"));
            document.open();
            addPatientPersonalDetails(document);
            document.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        InputStream input = null;
        OutputStream output = null;

        try {

            input = mActivity.getAssets().open("pay.pdf");
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
    private void addPatientPersonalDetails(Document document) {
        try {
            document.add(new Paragraph(""));
            
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    
    

}
