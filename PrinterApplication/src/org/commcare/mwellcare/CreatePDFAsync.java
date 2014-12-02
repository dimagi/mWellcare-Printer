package org.commcare.mwellcare;

import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PrintJob;
import android.print.PrintManager;

/**
 * This class is to generate the pdf from the given values
 * @author rreddy.avula
 *
 */
public class CreatePDFAsync extends AsyncTask<Void, Void, Void>{
    private Activity mActivity;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font font12Normal = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);
    private static Font font8Normal = new Font(Font.FontFamily.TIMES_ROMAN, 4,
            Font.NORMAL);
    private static Font font6Normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
            Font.NORMAL);
    private static Font font10Normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.NORMAL);
    public static final Font BOLD_UNDERLINED =
            new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD | Font.UNDERLINE);

    public CreatePDFAsync(Activity activity) {
        mActivity = activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) mActivity
                .getSystemService(Context.PRINT_SERVICE);

        // Set job name, which will be displayed in the print queue
        String jobName = mActivity.getString(R.string.app_name);

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName, new PdfPrintDocumentAdapter(mActivity), null);
    }

    private void addEnofText(Document document) {

        Phrase patHistory = new Phrase();
        patHistory.add(
                new Chunk("Preferable to prescribe the drug recommended by DSS but physician may select other class of drug if recommended drug is not available.",font8Normal));
        try {
            document.add(new Paragraph(" "));
            document.add(patHistory);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Lifestyle modifications: As advised",font8Normal));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private void addRecommendedDrugs(Document document) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(getPDFCell("Recommended Drug Plan"));
        table.addCell(getPDFCell("Modifications With comments"));

        table.addCell(getPDFCell("Pick one of the following:\nACEi/ARB (full dose):\nACEi: Enalapril (5 mg OD)\nACEi: Cardiopril (5 mg OD)\nACEi: Ramipril (5 mg OD)\nARB: LOSARTAN (50 mg OD)\nARB: Telmisartan (20-40 mg)"));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("Atorvastatin (20 mg)\nRosuvastatin (10 mg) "));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("Add Metformin 500mg with clinical discretion after excluding contraindications. "));
        table.addCell(getPDFCell("Modifications"));

        table.addCell(getPDFCell("Fluoxetine 20 mg/day"));
        table.addCell(getPDFCell(""));

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
        table.addCell(getPDFCell("Date"));
        table.addCell(getPDFCell("21/10/14"));
        table.addCell(getPDFCell("22/10/14"));
        table.addCell(getPDFCell("23/10/14"));
        table.addCell(getPDFCell("24/10/14"));
        table.addCell(getPDFCell("25/10/14"));

        table.addCell(getPDFCell("SBP"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("DBP"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

        table.addCell(getPDFCell("Weight"));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));
        table.addCell(getPDFCell(""));

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

            PdfPCell currentDurgs =  new PdfPCell(new Phrase("Current Drugs\nACEi/ARB (1/2 dose)",font8Normal));
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
                new Chunk("Patient History", BOLD_UNDERLINED));
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
            table.addCell(getPDFCell("Lakshmi Pramanik,\n4/24 srinivasapura, kolar, karnataka-111111"));

            table.addCell(getPDFCell("Female\n46 years"));

            table.addCell(getPDFCell("Patient ID:01133\nPhone:998833763"));

            table.addCell(getPDFCell("Diagnosis:Hypertension,Daibetes, COPD/Asthama, CKD, Liver Failure, CAD"));
            table.addCell("");
            table.addCell("");

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    private PdfPCell getPDFCell(String data){
        PdfPCell c1 = new PdfPCell(new Phrase(data,font8Normal));
        c1.setLeading(0f, 2f);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setPadding(10);
        return c1;

    }

}