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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.commcare.mwellcare.components.CustomDialog;
import org.commcare.mwellcare.projectconfigs.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Toast;


public class PdfPrintDocumentAdapter extends PrintDocumentAdapter{
   
    private Activity mActivity ;
    private final String TAG = this.getClass().getName();
    public static final String ODK_INTENT_DATA = "odk_intent_data";
    private CustomDialog mDialog;
    private CustomTimerTask mTimerTask;
    
    public PdfPrintDocumentAdapter(Activity activity) {
        this.mActivity = activity;
        mDialog = new CustomDialog(mActivity);
    }
    @Override
    public void onStart() {
        super.onStart();
        Intent intent = mActivity.getIntent();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
            PrintAttributes newAttributes,
            CancellationSignal cancellationSignal,
            PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
        
     // Respond to cancellation request
        if(cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
            Toast.makeText(mActivity, R.string.error_while_printing, Toast.LENGTH_SHORT).show();
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
            Bundle bundle = mActivity.getIntent().getExtras();
            String filePath = Environment.getExternalStorageDirectory()+"/MWellCare/"+
                    bundle.getString(Constants.PATIENT_ID)+"_"+bundle.getString(Constants.PATIENT_NAME)+".pdf";
            if(Constants.LOG)Log.d(TAG, "File Path is::"+filePath);

            input = new FileInputStream(filePath);
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                 output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (FileNotFoundException ee){
            if(Constants.LOG)Log.d(TAG, "FileNotFoundException"+ee);
        } catch (Exception e) {
            if(Constants.LOG)Log.d(TAG, "Exception"+e);
        } finally {
            try {
                if(input!=null)
                    input.close();
                if(output!=null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onFinish() {
        super.onFinish();
        PrintManager printManager = (PrintManager) mActivity
                .getSystemService(Context.PRINT_SERVICE);
        List<PrintJob> printJobs = printManager.getPrintJobs();
        
        if(Constants.LOG)Log.d(TAG,"Total Jobs::"+printJobs.size());
        String PatientID = mActivity.getIntent().getExtras().getString(Constants.PATIENT_ID);
        for(int i = 0;i<printJobs.size();i++){
            PrintJob job = printJobs.get(i);
            if(job.getInfo().getLabel().equals(PatientID)){
                if(!job.isCompleted()){
//                    Toast.makeText(mActivity, i+"::"+job.isQueued(), Toast.LENGTH_SHORT).show();
                    if(!mDialog.isShowing())
                        mDialog.show();
                    Timer t = new Timer();
                    mTimerTask = new CustomTimerTask(job);
                    t.scheduleAtFixedRate(mTimerTask, 0, 2000);
                }else{
                    job.cancel();
                }
            }
        }
    }
    private class CustomTimerTask extends TimerTask{
        private PrintJob job;
        public CustomTimerTask(PrintJob job) {
            this.job = job;
        }
        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    if(job.isQueued()){
                        if(!mDialog.isShowing())
                            mDialog.show();
                        mDialog.setText(job.getInfo().getLabel()+"PrintJob Queued...");
//                        Toast.makeText(mActivity, job.getInfo().getLabel()+" PrintJob Queued...", Toast.LENGTH_SHORT).show();
                    }else if(job.isStarted()){
                        if(!mDialog.isShowing())
                            mDialog.show();
                        mDialog.setText(job.getInfo().getLabel()+"PrintJob Started...");
//                        Toast.makeText(mActivity, job.getInfo().getLabel()+" PrintJob Started...", Toast.LENGTH_SHORT).show();
                    }else if(job.isBlocked()){
                      mDialog.setText(job.getInfo().getLabel()+"PrintJob Blocked and restarting...");
//                      Toast.makeText(mActivity, job.getInfo().getLabel()+" PrintJob Blocked and restarting...", Toast.LENGTH_SHORT).show();
                      job.restart();
                    }else if(job.isFailed()){
                        mDialog.setText(job.getInfo().getLabel()+"PrintJob failed and restarting...");
//                        Toast.makeText(mActivity, job.getInfo().getLabel()+" PrintJob failed and restarting...", Toast.LENGTH_SHORT).show();
                        job.restart();
                    }else if(job.isCompleted()){
                        job.cancel();
//                        mDialog.setText("PrintJob Completed...");
                        Toast.makeText(mActivity, job.getInfo().getLabel()+" Priting Completed", Toast.LENGTH_SHORT).show();
                        CustomTimerTask.this.cancel();
                        
                        Intent intent = new Intent();
                        mActivity.setResult(Activity.RESULT_OK, intent);
                        intent.putExtra(ODK_INTENT_DATA, true);
                        if(mDialog.isShowing())    
                            mDialog.dismiss();
                        mActivity.finish();
                    }/*else if(mDialog.getDialogText().equals("")){
                        CustomTimerTask.this.cancel();
                        Intent intent = new Intent();
                        mActivity.setResult(Activity.RESULT_OK, intent);
                        intent.putExtra(ODK_INTENT_DATA, false);
                        if(mDialog.isShowing())    
                            mDialog.dismiss();
                        mActivity.finish();
                    }*/
                }
            });
        }
    }
    /**
     * This cancels the all print jobs currently irrespective of 
     * their status
     */
    public void cancelTimer() {
        if(mTimerTask!=null){
            mTimerTask.cancel();
            if(mDialog.isShowing())    
                mDialog.dismiss();
            PrintManager printManager = (PrintManager) mActivity
                    .getSystemService(Context.PRINT_SERVICE);
            List<PrintJob> printJobs = printManager.getPrintJobs();
            String PatientID = mActivity.getIntent().getExtras().getString(Constants.PATIENT_ID);
            for(int i = 0;i<printJobs.size();i++){
                PrintJob job = printJobs.get(i);
                if(!job.isCompleted()){
                    job.cancel();
                }
            }
        }
        
    } 
}
