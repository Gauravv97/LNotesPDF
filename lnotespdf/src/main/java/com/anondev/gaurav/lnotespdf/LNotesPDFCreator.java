package com.anondev.gaurav.lnotespdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LNotesPDFCreator {
    private Context mContext;
    private int pageWidth=720,pageHeight=1280;
    private int numberOfPages=0;
    private PdfDocument document;
    public LNotesPDFCreator(Context context) {
        mContext=context;
        document = new PdfDocument();
    }
    public LNotesPDFCreator(Context context,int Height,int Width) {
        mContext=context;
        pageWidth=Width;
        pageHeight=Height;
        document = new PdfDocument();
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void add(String Paths[]){
        for(int i=0;i<Paths.length;i++){
            add(Paths[i]);
        }
    }
    public void add(String Path){
        Bitmap bmp=BitmapFactory.decodeFile(Path);
        add(bmp);
        bmp.recycle();
    }
    public void add(Bitmap bitmap){
        if(bitmap==null){
            Log.e("LNotesPDF","null Bitmap");
            return;
        }
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight, numberOfPages+1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);
        int bWidth,bHeight ,pdfTop=0,pdfLeft=0;;
        if(bitmap.getHeight()*1.0/bitmap.getWidth()<pageHeight*1.0/pageWidth){
            bWidth=pageWidth;
            bHeight=(bitmap.getHeight()*pageHeight)/bitmap.getWidth();
            pdfTop=pageHeight/2-bHeight/2;
        }
        else {
            bHeight=pageHeight;
            bWidth=(bitmap.getWidth()*pageHeight)/bitmap.getHeight();
            pdfLeft=pageWidth/2-bWidth/2;
        }
        Bitmap tmp=Bitmap.createScaledBitmap(bitmap,bWidth,bHeight,true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(tmp, pdfLeft, pdfTop , null);
        tmp.recycle();
        document.finishPage(page);
        numberOfPages++;
    }
    public void add(Bitmap bitmaps[]){
        for(int i=0;i<bitmaps.length;i++){
            add(bitmaps[i]);
        }
    }
    public void add(View view){
        Bitmap bmp=getViewBitmap(view);
        add(bmp);
        bmp.recycle();
    }
    public void add(View views[]){
        for(int i=0;i<views.length;i++){
            add(views[i]);
        }
    }
    public void savePDF(String fullPath){
        File file=new File(fullPath);
        if(file==null){
            Log.e("LNotesPDF","The path "+fullPath+" can't be accessed make sure it's valid and permitted to access ");
            return;
        }
        if(file.exists()){
            Log.e("LNotesPDF","Saving Failed File already exists");
            return;
        }try {

            document.writeTo(new FileOutputStream(file));
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.e("LNotesPDF","Saving Failed During write");
        }
    }
    public void savePDF(Uri uri){
        savePDF(uri.getPath());
    }
    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap tmp = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return tmp;
    }
}
