package org.commcare.mwellcare.service;

import java.util.ArrayList;
import java.util.List;

import android.print.PrinterId;
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
        Log.d(TAG,"onRequestCancelPrintJob");

    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {
        Log.d(TAG,"onPrintJobQueued");

    }
    @Override
    protected void onConnected() {
        Log.d(TAG,"onRequestCancelPrintJob");
        
    };
    @Override
    protected void onDisconnected() {
        Log.d(TAG,"onRequestCancelPrintJob");
        
    };
    PrinterDiscoverySession abc = new PrinterDiscoverySession() {

        @Override
        public void onValidatePrinters(List<PrinterId> printerIds) {
            Log.d(TAG,"onValidatePrinters");

        }

        @Override
        public void onStopPrinterStateTracking(PrinterId printerId) {
            Log.d(TAG,"onStopPrinterStateTracking");

        }

        @Override
        public void onStopPrinterDiscovery() {
            Log.d(TAG,"onStopPrinterDiscovery");

        }

        @Override
        public void onStartPrinterStateTracking(PrinterId printerId) {
            Log.d(TAG,"onStartPrinterStateTracking");

        }

        @Override
        public void onStartPrinterDiscovery(List<PrinterId> priorityList) {
            List<PrinterId> ddd = new ArrayList<PrinterId>();
//            ddd.add(generatePrinterId("Printer 1"));
//            removePrinters(ddd);
//            
//            PrinterCapabilitiesInfo capabilities =
//                    new PrinterCapabilitiesInfo.Builder(generatePrinterId("Printer 1"))
//            .addMediaSize(MediaSize.ISO_A1,true)
//                .setColorModes(PrintAttributes.COLOR_MODE_COLOR
//                        | PrintAttributes.COLOR_MODE_MONOCHROME,
//                        PrintAttributes.COLOR_MODE_COLOR)
//                        .addResolution(new Resolution("R1", "sdfsdf", 600, 600), true)
//                                .setMinMargins(new Margins(50, 50, 50, 50))
//                                        .build();
//            
//            PrinterInfo mFirstFakePrinter = new PrinterInfo.Builder(generatePrinterId("Printer 1"),
//                    "SHGH-21344", PrinterInfo.STATUS_IDLE)
//            .setCapabilities(capabilities).
//            build();
//            
//            List<PrinterInfo> dd = new ArrayList<PrinterInfo>();
//            dd.add(mFirstFakePrinter);
//            addPrinters(dd);
//            Log.d(TAG,"onStartPrinterDiscovery");

        }

        @Override
        public void onDestroy() {
            Log.d(TAG,"onDestroy");
            List<PrinterId> dd = new ArrayList<PrinterId>();
            dd.add(generatePrinterId("Printer 1"));
            removePrinters(dd);

        }
    };

}
