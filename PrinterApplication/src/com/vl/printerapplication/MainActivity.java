
package com.vl.printerapplication;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.print.PrinterId;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
     // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);
        

        // Set job name, which will be displayed in the print queue
        String jobName = this.getString(R.string.app_name);
        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName, new PdfPrintDocumentAdapter(this), null);
        
        new PrintService() {
            
            @Override
            protected void onRequestCancelPrintJob(PrintJob printJob) {
                
            }
            
            @Override
            protected void onPrintJobQueued(PrintJob printJob) {
                
            }
            
            @Override
            protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
                return abc;
            }
        };
        
    
       


    }
    PrinterDiscoverySession abc = new PrinterDiscoverySession() {
        
        @Override
        public void onValidatePrinters(List<PrinterId> printerIds) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onStopPrinterStateTracking(PrinterId printerId) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onStopPrinterDiscovery() {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onStartPrinterStateTracking(PrinterId printerId) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onStartPrinterDiscovery(List<PrinterId> priorityList) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
            
        }
    };
}
