package com.rikilg.idlethoughts;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileIO {

    private Context c;
    private String filename;

    FileIO(Context c, String filename) {
        this.c = c;
        this.filename = filename;
    }

    public void write(Context c,String content,String fileName) {
        try {
            FileOutputStream outputStream = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            Toast.makeText(c,"Text Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            //Toast.makeText(c,"Flie Name not given.Using today's date", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(c,"IO exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void read(TextView textView) {
        read(c,textView,filename);
    }

    void read(Context c, TextView textView,/* TextView titleName,*/ String fileName) {
        try {
            FileInputStream fileInputStream= c.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String lines;
            //stringBuilder.append("\n");
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuilder.append(lines+"\n");
            }
            textView.setText(stringBuilder.toString());
            //titleName.setText(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void delete(Context c,String fileName){
        File file = new File(c.getFilesDir(),fileName);
        boolean status = file.delete();
        if(status){
            Toast.makeText(c.getApplicationContext(),"Successfully deleted "+fileName,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(c.getApplicationContext(),"Unable to delete "+fileName,Toast.LENGTH_LONG).show();
        }
    }

}
