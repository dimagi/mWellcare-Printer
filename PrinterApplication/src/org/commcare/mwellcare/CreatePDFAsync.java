package org.commcare.mwellcare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.commcare.mwellcare.components.CustomDialog;
import org.commcare.mwellcare.projectconfigs.Constants;
import org.commcare.mwellcare.util.Util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class is to generate the PDF from the given values
 * @author rreddy.avula
 *
 */
public class CreatePDFAsync extends AsyncTask<Void, Void, Void>{
    private Activity mActivity;
    private Bundle mBundle;
    private CustomDialog mCustomDialog;
    private PdfPrintDocumentAdapter mPdfAdapter;
    private Font font8Normal ;
    private Font font8Bold ;
    private Font font8BoldUnderline ;
    private Font font8Italic ;

    public CreatePDFAsync(Activity activity) {
        mActivity = activity;
        mBundle = mActivity.getIntent().getExtras();
        initFonts();
    }
    private void initFonts() {
        try {
            font8Normal = new Font(BaseFont.createFont("assets/fonts/calibri.ttf", "UTF-8",BaseFont.EMBEDDED), 10,Font.NORMAL);
            font8Bold = new Font(BaseFont.createFont("assets/fonts/calibrib.ttf", "UTF-8",BaseFont.EMBEDDED), 10,Font.BOLD);
            font8BoldUnderline = new Font(BaseFont.createFont("assets/fonts/calibri.ttf", "UTF-8",BaseFont.EMBEDDED),10,Font.BOLD|Font.UNDERLINE);
            font8Italic = new Font(BaseFont.createFont("assets/fonts/calibrii.ttf", "UTF-8",BaseFont.EMBEDDED), 10,Font.ITALIC);
        } catch (Exception e) {
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCustomDialog = new CustomDialog(mActivity);
        mCustomDialog.show();
        mCustomDialog.setText(mActivity.getResources().getString(R.string.generating_pdf));
        
    }

    @Override
    protected Void doInBackground(Void... params) {
        generatePDF();
        return null;
    }
   
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mCustomDialog.dismiss();

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) mActivity
                .getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = mActivity.getString(R.string.app_name);

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        mPdfAdapter = new PdfPrintDocumentAdapter(mActivity);
        printManager.print(mBundle.getString(Constants.PATIENT_ID)!=null ? mBundle.getString(Constants.PATIENT_ID)
                +"_"+Util.getUniquePrintJobIDFromSP(mActivity):jobName
                , mPdfAdapter, null);
        
    }
    public PdfPrintDocumentAdapter getmPdfAdapter() {
        return mPdfAdapter;
    }
    /**
     * This method start creating PDF
     */
    private void generatePDF() {
        try {
            String path = getPDFConfigurePath();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            HeaderFooter event = new HeaderFooter();
            writer.setBoxSize("art", new Rectangle(36, 15, 559, 788));
            writer.setPageEvent(event);
            
            document.open();
            addPatientPersonalDetails(document);
            
            addPatientHistoryText(document);
            addPatientHistryContent(document);
            addPatientHistryContentSecondTable(document);
            addPatientHistryContentThridTable(document);
            
            addPatientHistryContentFourthTable(document);
            addRecommendedDrugs(document);
            addEnofText(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    private class HeaderFooter extends PdfPageEventHelper {
        private static final String HEADER_DATE = "dd MMMM yyyy";

        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            //header
            SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE);
            String currentDate = sdf.format(new Date());
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("CHC:Gownipalli", font8Normal),
                    rect.getLeft()+32, rect.getTop()+25, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Date:"+currentDate, font8Normal),
                    rect.getLeft()+45, rect.getTop()+12, 0);
            //Footer
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(Constants.SITE_COMMCARE,font8Normal),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom()-5, 0);
        }
    }
    
    /**
     * This method gives the configuration path of PDF
     * @return
     */
    private String getPDFConfigurePath() {
        String pdfPath = Util.getPdfNameFromSP(mActivity);
        if(pdfPath == null){//default case
            File fileName = new File(Environment.getExternalStorageDirectory(),
                    mBundle.getString(Constants.PATIENT_ID)+"_"+
                    mBundle.getString(Constants.PATIENT_NAME)+".pdf");
            if(!fileName.exists())
                try {
                    fileName.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return fileName.toString();
        }else{//create user configured path
            
            if(pdfPath.contains("/")){
                File fileName = new File(pdfPath, mBundle.getString(Constants.PATIENT_ID)+"_"+
                        mBundle.getString(Constants.PATIENT_NAME)+".pdf");
                if(!fileName.exists())
                    try {
                        fileName.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return fileName.toString();
                
            }
            
        }
        return "/printout.pdf";//default name if any of the aboce cases not matching
        
    }
    /**
     * This method creates patient fourth table
     * @param document
     */
    private void addPatientHistryContentFourthTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getPDFCell(Constants.DATE));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell(Constants.TOBOCCO));
        table.addCell(getPDFCell(mBundle.getString(Constants.TOBOCCO_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.TOBOCCO_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.TOBOCCO_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.TOBOCCO_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.TOBOCCO_5)));
        
        table.addCell(getPDFCell(Constants.ALCHOL));
        table.addCell(getPDFCell(mBundle.getString(Constants.ALCHOL_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.ALCHOL_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.ALCHOL_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.ALCHOL_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.ALCHOL_5)));
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method creates patient Third table
     * @param document
     */
    private void addPatientHistryContentThridTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getBoldPDFCell(Constants.DATE));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell(Constants.DEPRESSON_CAT));
        table.addCell(getPDFCell(mBundle.getString(Constants.DEPRESSON_CAT_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DEPRESSON_CAT_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DEPRESSON_CAT_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DEPRESSON_CAT_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DEPRESSON_CAT_5)));
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * This method creates patiend second table
     * @param document
     */
    private void addPatientHistryContentSecondTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getBoldPDFCell(Constants.DATE));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell(Constants.FBS));
        table.addCell(getPDFCell(mBundle.getString(Constants.FBS_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.FBS_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.FBS_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.FBS_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.FBS_5)));

        table.addCell(getPDFCell(Constants.PP));
        table.addCell(getPDFCell(mBundle.getString(Constants.PP_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.PP_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.PP_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.PP_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.PP_5)));
        
        table.addCell(getPDFCell(Constants.RBS));
        table.addCell(getPDFCell(mBundle.getString(Constants.RBS_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.RBS_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.RBS_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.RBS_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.RBS_5)));
        
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * This method is for the end of document summary
     * @param document
     */
    private void addEnofText(Document document) {
        
        try {
            Paragraph para = new Paragraph();
            para.add(new Chunk(Constants.LIFE_STYLE_MODIFICATIONS, font8Bold));
            para.add(new Chunk(Constants.AS_ADVISED, font8Normal));
            document.add(para);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    /**
     * THis method adds UI for recommended drugs
     * @param document
     */
    private void addRecommendedDrugs(Document document) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);

        table.addCell(getBoldPDFCell(Constants.REC_DRUG_PAN));
        table.addCell(getBoldPDFCell(Constants.MOD_WITH_COMMENTS));

        table.addCell(getPDFCell(mBundle.getString(Constants.DRUG_PAN)));
        table.addCell(getPDFCell(""));
        
        PdfPCell italicCell = getItalicPDFCell(Constants.PREFERABLE_PRESCRIBE);
        italicCell.setColspan(2);
        table.addCell(italicCell);
        //In future this might be useful, if we split the string

//        table.addCell(getPDFCell("Atorvastatin (20 mg)\nRosuvastatin (10 mg) "));
//        table.addCell(getPDFCell(""));
//
//        table.addCell(getPDFCell("Add Metformin 500mg with clinical discretion after excluding contraindications. "));
//        table.addCell(getPDFCell("Modifications"));
//
//        table.addCell(getPDFCell("Fluoxetine 20 mg/day"));
//        table.addCell(getPDFCell(""));

        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    /**
     * THis method shows UI for patient history
     * @param document
     */
    private void addPatientHistryContent(Document document) {


        PdfPTable mainTable = new PdfPTable(2);
        mainTable.getDefaultCell().setBorder(0);
        mainTable.setWidthPercentage(100f);
        PdfPTable table = new PdfPTable(6);
        table.addCell(getBoldPDFCell(Constants.DATE));
        table.addCell(getPDFCell(mBundle.getString(Constants.DATE_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DATE_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DATE_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DATE_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DATE_5)));

        table.addCell(getPDFCell(Constants.SBP));
        table.addCell(getPDFCell(mBundle.getString(Constants.SBP_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.SBP_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.SBP_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.SBP_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.SBP_5)));

        table.addCell(getPDFCell(Constants.DBP));
        table.addCell(getPDFCell(mBundle.getString(Constants.DBP_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DBP_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DBP_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DBP_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.DBP_5)));

        table.addCell(getPDFCell(Constants.WEIGHT));
        table.addCell(getPDFCell(mBundle.getString(Constants.WEIGHT_1)));
        table.addCell(getPDFCell(mBundle.getString(Constants.WEIGHT_2)));
        table.addCell(getPDFCell(mBundle.getString(Constants.WEIGHT_3)));
        table.addCell(getPDFCell(mBundle.getString(Constants.WEIGHT_4)));
        table.addCell(getPDFCell(mBundle.getString(Constants.WEIGHT_5)));

        table.addCell(getPDFCell(Constants.BMI));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        float[] columnWidths = new float[] {70f, 30f};
        try {
            mainTable.setWidths(columnWidths);
            mainTable.addCell(table);

            table.getDefaultCell().setBorder(0);
            Paragraph para = new Paragraph();
            para.add(new Phrase(Constants.CURRENT_DRUGS_TEXT, font8Bold));
            para.add(new Phrase(mBundle.getString(Constants.CURRENT_DGURS_KEY)+" ", font8Normal));

            PdfPCell currentDurgs =  new PdfPCell(para);
            currentDurgs.setBorder(0);
            currentDurgs.setLeading(0f, 2f);
            currentDurgs.setHorizontalAlignment(Element.ALIGN_CENTER);

            mainTable.addCell(currentDurgs);
            document.add(mainTable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    /**
     * THis method adds patient history
     * @param document
     */
    private void addPatientHistoryText(Document document) {

        Phrase patHistory = new Phrase();
        patHistory.add(
                new Chunk(Constants.PATIENT_HISTORY, font8BoldUnderline));
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(patHistory);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    /**
     * THis method adds patient personal details
     * @param document
     */
    private void addPatientPersonalDetails(Document document) {
        try {
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            //            table.setSpacingBefore(0f);
            //            table.setSpacingAfter(0f);
            
            table.addCell(getpatinetNameAddressPDFCell(mBundle.getString(Constants.PATIENT_NAME), mBundle.getString(Constants.ADDRESS)));

            table.addCell(getPDFCell(mBundle.getString(Constants.GENDER)+"\n\n"+mBundle.getString(Constants.AGE)));

            table.addCell(getPDFCell(Constants.PATIENT_ID_TEXT+mBundle.getString(Constants.PATIENT_ID)
                    +"\n\n"+Constants.PHONE_TEXT+mBundle.getString(Constants.PHONE)));
            PdfPCell diagnosisPdfCell = getPDFCell(Constants.DIAGNOSIS_TEXT+mBundle.getString(Constants.DIAGNOSIS));
            diagnosisPdfCell.setColspan(3);
            table.addCell(diagnosisPdfCell);
//            table.addCell("");
//            table.addCell("");

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    /**
     * This method creates a pdf cell for patient name and address
     * @param name
     * @param address
     * @return
     */
    private PdfPCell getpatinetNameAddressPDFCell(String name, String address){
        Paragraph para = new Paragraph();
        para.add(new Chunk(name == null?" ":name,font8Bold));
        para.add(new Chunk("\n\n"+(address == null?" ":address),font8Normal));
        PdfPCell c1 = new PdfPCell(para);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    /**
     * This method creates pdf cell for the given data
     * @param data
     * @return
     */
    private PdfPCell getPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data == null?" ":data ,font8Normal));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    /**
     * This method creates Bold pdf cell for given data
     * @param data
     * @return
     */
    private PdfPCell getBoldPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data == null?" ":data,font8Bold));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    /**
     * This method creates italic pdf cell for given data
     * @param data
     * @return
     */
    private PdfPCell getItalicPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data == null?" ":data,font8Italic));
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        return c1;
    }

}