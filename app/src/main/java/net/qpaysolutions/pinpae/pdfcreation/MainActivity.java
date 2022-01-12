package net.qpaysolutions.pinpae.pdfcreation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton create_pdf;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        create_pdf = findViewById(R.id.create_pdf);
        create_pdf.setOnClickListener(v -> {
            createPdf(Common.getAppPath(MainActivity.this) +"test_pdf.pdf");
        });
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }
        try {
            Document document = new Document();
            PdfWriter.getInstance(document,  new FileOutputStream(path));
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Gopal");
            document.addCreator("Gopal Thapa");
            BaseColor colorAgent = new BaseColor(0, 153, 204, 255);
            float fontsize = 20.0f;
            float valueFontSize = 26.0f;

            BaseFont baseFont = BaseFont.createFont("assests/font/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "order Details", Element.ALIGN_CENTER, titleFont);

            Font orderNumberFont = new Font(baseFont, valueFontSize, Font.NORMAL, colorAgent);
            addNewItem(document, "order No", Element.ALIGN_CENTER, orderNumberFont);

            Font orderNumberValueFont = new Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#7171717", Element.ALIGN_CENTER, orderNumberValueFont);

            addLineSpeactor(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberValueFont);
            addNewItem(document, "3/12/2020", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSpeactor(document);

            addNewItem(document, "Account Name", Element.ALIGN_LEFT, orderNumberValueFont);
            addNewItem(document, "Gopal Thapa", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSpeactor(document);

            addNewItem(document, "Product Details", Element.ALIGN_LEFT, orderNumberValueFont);
            addLineSpeactor(document);

            addNewItemWithLeftAndRight(document, "Pizza 20", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);
            addLineSpeactor(document);

            addNewItemWithLeftAndRight(document, "Pizza 21", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);
            addLineSpeactor(document);

            addNewItemWithLeftAndRight(document, "Pizza 22", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);
            addLineSpeactor(document);

            addLineSpace(document);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document, "Total", "24000.0", titleFont, orderNumberValueFont);


            document.close();
            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_LONG).show();

            printPDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter=new pdfDocumentAdapter(MainActivity.this,Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftRight, Font rightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftRight);
        Chunk chunkTextRight = new Chunk(textRight, rightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);


    }

    private void addLineSpeactor(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
}