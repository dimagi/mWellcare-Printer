package org.commcare.mwellcare;

import java.io.FileOutputStream;

import org.commcare.mwellcare.components.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class is to generate the pdf from the given values
 * @author rreddy.avula
 *
 */
public class CreatePDFAsync extends AsyncTask<Void, Void, Void>{
    private Activity mActivity;
    private Bundle mBundle;
    private Dialog mCustomDialog;
//    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
//            Font.BOLD);
//    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
//            Font.NORMAL, BaseColor.RED);
//    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
//            Font.BOLD);
//    private static Font font12Normal = new Font(Font.FontFamily.TIMES_ROMAN, 12,
//            Font.NORMAL);
//    private static Font font8Normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
//            Font.NORMAL);
//    private static Font font6Normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
//            Font.NORMAL);
//    private static Font font10Normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
//            Font.NORMAL);
//    public static final Font BOLD_UNDERLINED =
//            new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD | Font.UNDERLINE);
    
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
            font8Normal = new Font(BaseFont.createFont("assets/fonts/calibri.ttf", "UTF-8",BaseFont.EMBEDDED), 8,Font.NORMAL);
            font8Bold = new Font(BaseFont.createFont("assets/fonts/calibrib.ttf", "UTF-8",BaseFont.EMBEDDED), 8,Font.BOLD);
            font8BoldUnderline = new Font(BaseFont.createFont("assets/fonts/calibri.ttf", "UTF-8",BaseFont.EMBEDDED), 8,Font.BOLD|Font.UNDERLINE);
            font8Italic = new Font(BaseFont.createFont("assets/fonts/calibrii.ttf", "UTF-8",BaseFont.EMBEDDED), 8,Font.ITALIC);
        } catch (Exception e) {
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCustomDialog = new CustomDialog(mActivity);
        mCustomDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory()+"/abc.pdf"));
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
        printManager.print(jobName, new PdfPrintDocumentAdapter(mActivity), null);
    }
    private void addPatientHistryContentFourthTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getPDFCell("Date"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("Tobacco"));
        table.addCell(getPDFCell(mBundle.getString("tobacco_1")));
        table.addCell(getPDFCell(mBundle.getString("tobacco_2")));
        table.addCell(getPDFCell(mBundle.getString("tobacco_3")));
        table.addCell(getPDFCell(mBundle.getString("tobacco_4")));
        table.addCell(getPDFCell(mBundle.getString("tobacco_5")));
        
        table.addCell(getPDFCell("Alcohol"));
        table.addCell(getPDFCell(mBundle.getString("alch_1")));
        table.addCell(getPDFCell(mBundle.getString("alch_2")));
        table.addCell(getPDFCell(mBundle.getString("alch_3")));
        table.addCell(getPDFCell(mBundle.getString("alch_4")));
        table.addCell(getPDFCell(mBundle.getString("alch_5")));
        float[] columnWidths = new float[] {70f, 30f};
        try {
            table.setWidths(columnWidths);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private void addPatientHistryContentThridTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getBoldPDFCell("Date"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("Depression cat"));
        table.addCell(getPDFCell(mBundle.getString("depress_cat_1")));
        table.addCell(getPDFCell(mBundle.getString("depress_cat_2")));
        table.addCell(getPDFCell(mBundle.getString("depress_cat_3")));
        table.addCell(getPDFCell(mBundle.getString("depress_cat_4")));
        table.addCell(getPDFCell(mBundle.getString("depress_cat_5")));
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    private void addPatientHistryContentSecondTable(Document document) {
        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(70f);
        table.addCell(getBoldPDFCell("Date"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("FBS"));
        table.addCell(getPDFCell(mBundle.getString("fbs_1")));
        table.addCell(getPDFCell(mBundle.getString("fbs_2")));
        table.addCell(getPDFCell(mBundle.getString("fbs_3")));
        table.addCell(getPDFCell(mBundle.getString("fbs_4")));
        table.addCell(getPDFCell(mBundle.getString("fbs_5")));

        table.addCell(getPDFCell("PP"));
        table.addCell(getPDFCell(mBundle.getString("pp_1")));
        table.addCell(getPDFCell(mBundle.getString("pp_2")));
        table.addCell(getPDFCell(mBundle.getString("pp_3")));
        table.addCell(getPDFCell(mBundle.getString("pp_4")));
        table.addCell(getPDFCell(mBundle.getString("pp_5")));
        
        table.addCell(getPDFCell("RBS"));
        table.addCell(getPDFCell(mBundle.getString("rbs_1")));
        table.addCell(getPDFCell(mBundle.getString("rbs_2")));
        table.addCell(getPDFCell(mBundle.getString("rbs_3")));
        table.addCell(getPDFCell(mBundle.getString("rbs_4")));
        table.addCell(getPDFCell(mBundle.getString("rbs_5")));
        
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    private void addEnofText(Document document) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(50f);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(getItalicPDFCell("Preferable to prescribe the drug recommended by DSS but physician may select other class of drug if recommended drug is not available."));
        
//        Phrase patHistory = new Phrase();
//        patHistory.add(
//                new Chunk("Preferable to prescribe the drug recommended by DSS but physician may select other class of drug if recommended drug is not available.",font8Normal));
        try {
            document.add(table);
            Paragraph para = new Paragraph();
            para.add(new Chunk("Lifestyle modifications: ", font8Bold));
            para.add(new Chunk("As advised", font8Normal));
            document.add(para);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private void addRecommendedDrugs(Document document) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);

        table.addCell(getBoldPDFCell("Recommended Drug Plan"));
        table.addCell(getBoldPDFCell("Modifications With comments"));

        table.addCell(getPDFCell(mBundle.getString("drug_plan")));
        table.addCell(getPDFCell(""));

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
    private void addPatientHistryContent(Document document) {


        PdfPTable mainTable = new PdfPTable(2);
        mainTable.getDefaultCell().setBorder(0);
        mainTable.setWidthPercentage(100f);
        PdfPTable table = new PdfPTable(6);
        table.addCell(getBoldPDFCell("Date"));
        table.addCell(getPDFCell(mBundle.getString("date_1")));
        table.addCell(getPDFCell(mBundle.getString("date_2")));
        table.addCell(getPDFCell(mBundle.getString("date_3")));
        table.addCell(getPDFCell(mBundle.getString("date_4")));
        table.addCell(getPDFCell(mBundle.getString("date_5")));

        table.addCell(getPDFCell("SBP"));
        table.addCell(getPDFCell(mBundle.getString("sbp_1")));
        table.addCell(getPDFCell(mBundle.getString("sbp_2")));
        table.addCell(getPDFCell(mBundle.getString("sbp_3")));
        table.addCell(getPDFCell(mBundle.getString("sbp_4")));
        table.addCell(getPDFCell(mBundle.getString("sbp_5")));

        table.addCell(getPDFCell("DBP"));
        table.addCell(getPDFCell(mBundle.getString("dbp_1")));
        table.addCell(getPDFCell(mBundle.getString("dbp_2")));
        table.addCell(getPDFCell(mBundle.getString("dbp_3")));
        table.addCell(getPDFCell(mBundle.getString("dbp_4")));
        table.addCell(getPDFCell(mBundle.getString("dbp_5")));

        table.addCell(getPDFCell("Weight"));
        table.addCell(getPDFCell(mBundle.getString("weight_1")));
        table.addCell(getPDFCell(mBundle.getString("weight_2")));
        table.addCell(getPDFCell(mBundle.getString("weight_3")));
        table.addCell(getPDFCell(mBundle.getString("weight_4")));
        table.addCell(getPDFCell(mBundle.getString("weight_5")));

        table.addCell(getPDFCell("BMI"));
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

            PdfPCell currentDurgs =  new PdfPCell(new Phrase("Current Drugs\nACEi/ARB (1/2 dose)",font8Bold));
            currentDurgs.setBorder(0);
            currentDurgs.setLeading(0f, 2f);
            currentDurgs.setHorizontalAlignment(Element.ALIGN_CENTER);

            mainTable.addCell(currentDurgs);
            document.add(mainTable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private void addPatientHistoryText(Document document) {

        Phrase patHistory = new Phrase();
        patHistory.add(
                new Chunk("Patient History", font8BoldUnderline));
        try {
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(patHistory);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private void addPatientPersonalDetails(Document document) {
        try {
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            //            table.setSpacingBefore(0f);
            //            table.setSpacingAfter(0f);
            
            table.addCell(getpatinetNameAddressPDFCell(mBundle.getString("name"), mBundle.getString("address")));

            table.addCell(getPDFCell(mBundle.getString("gender")+"\n"+mBundle.getString("age")));

            table.addCell(getPDFCell("Patient ID: "+mBundle.getString("patient_id")
                    +"\n"+"Phone: "+mBundle.getString("phone")));

            table.addCell(getPDFCell("Diagnosis: "+mBundle.getString("diagnosis")));
            table.addCell("");
            table.addCell("");

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private PdfPCell getpatinetNameAddressPDFCell(String name, String address){
        Paragraph para = new Paragraph();
        para.add(new Chunk(name,font8Bold));
        para.add(new Chunk("\n\n"+address,font8Normal));
        PdfPCell c1 = new PdfPCell(para);
//        c1.setLeading(0f, 2f);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    private PdfPCell getPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data,font8Normal));
//        c1.setLeading(0f, 2f);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    private PdfPCell getBoldPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data,font8Bold));
//        c1.setLeading(0f, 2f);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        return c1;
    }
    private PdfPCell getItalicPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data,font8Italic));
        c1.setBorder(0);
        c1.setPaddingLeft(10);
        c1.setPaddingRight(10);
        c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        return c1;
    }

}