package org.commcare.mwellcare.service;

import java.util.ArrayList;
import java.util.List;

import org.commcare.mwellcare.projectconfigs.Constants;

import android.print.PrintAttributes;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintAttributes.Resolution;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.util.Log;

public class CustomPrintService extends PrintService {
    private final String TAG = this.getClass().getName();

    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        return abc ;
    }

    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {
        if(Constants.LOG)Log.d(TAG,"onRequestCancelPrintJob");

    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {
        if(Constants.LOG)Log.d(TAG,"onPrintJobQueued");

    }
    @Override
    protected void onConnected() {
        if(Constants.LOG)Log.d(TAG,"onRequestCancelPrintJob");
        
    };
    @Override
    protected void onDisconnected() {
        if(Constants.LOG)Log.d(TAG,"onRequestCancelPrintJob");
        
    };
    PrinterDiscoverySession abc = new PrinterDiscoverySession() {

        @Override
        public void onValidatePrinters(List<PrinterId> printerIds) {
            if(Constants.LOG)Log.d(TAG,"onValidatePrinters");

        }

        @Override
        public void onStopPrinterStateTracking(PrinterId printerId) {
            if(Constants.LOG)Log.d(TAG,"onStopPrinterStateTracking");

        }

        @Override
        public void onStopPrinterDiscovery() {
            if(Constants.LOG)Log.d(TAG,"onStopPrinterDiscovery");

        }

        @Override
        public void onStartPrinterStateTracking(PrinterId printerId) {
            if(Constants.LOG)Log.d(TAG,"onStartPrinterStateTracking");

        }

        @Override
        public void onStartPrinterDiscovery(List<PrinterId> priorityList) {
            Log.d(TAG,"onStartPrinterDiscovery");
//            List<PrinterId> printerIDS = new ArrayList<PrinterId>();
//            for(int i =0;i<priorityList.size();i++){
//                PrinterId printerID = priorityList.get(i);
//                printerIDS.add(printerID);
//                
//            }
//            removePrinters(printerIDS);
//            
//            List<PrinterInfo> printerInfoList=  getPrinterList(priorityList);
//            addPrinters(printerInfoList);
            

        }

        private List<PrinterInfo> getPrinterList(List<PrinterId> priorityList) {
            List<PrinterInfo> printersInfoList = new ArrayList<PrinterInfo>();
            
            for(int i =0;i<priorityList.size();i++){
                PrinterId printerID = priorityList.get(i);
                PrinterCapabilitiesInfo capabilities =
                        new PrinterCapabilitiesInfo.Builder(printerID)
                .addMediaSize(MediaSize.ISO_A1,true)
                    .setColorModes(PrintAttributes.COLOR_MODE_COLOR
                            | PrintAttributes.COLOR_MODE_MONOCHROME,
                            PrintAttributes.COLOR_MODE_COLOR)
                            .addResolution(new Resolution("R1", "sdfsdf", 600, 600), true)
                                    .setMinMargins(new Margins(50, 50, 50, 50))
                                            .build();
                
                PrinterInfo mFirstFakePrinter = new PrinterInfo.Builder(printerID,
                        "ABC"+i, PrinterInfo.STATUS_IDLE)
                .setCapabilities(capabilities).
                build();
                
               
                printersInfoList.add(mFirstFakePrinter);
            }
            
           
            return printersInfoList;
        }

        @Override
        public void onDestroy() {
            if(Constants.LOG)Log.d(TAG,"onDestroy");
//            List<PrinterId> dd = new ArrayList<PrinterId>();
//            dd.add(generatePrinterId("Printer 1"));
//            removePrinters(dd);

        }
    };

}
