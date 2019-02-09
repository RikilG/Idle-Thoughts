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

class FileIO {

    private Context c;
    private String filename;

    private final int PURPOSE_SHOW = 1;
    private final int PURPOSE_EDIT = 2;

    FileIO(Context c, String filename) {
        this.c = c;
        this.filename = filename;
    }

     void write(String content) {
        write(c, content, filename);
     }

     private void write(Context c,String content,String fileName) {
        try {
            FileOutputStream outputStream = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            Toast.makeText(c,"Text Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            //Toast.makeText(c,"Flie Name not given.Using today's date", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(c,"IO exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    String read(int purpose) {
        return read(c,filename,purpose);
    }

    private String read(Context c,/* TextView titleName,*/ String fileName, int purpose) {
        int count = 1;
        String temp;
        try {
            FileInputStream fileInputStream= c.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String lines;
            //stringBuilder.append("\n");
            while ((lines=bufferedReader.readLine())!=null) {
                if(purpose == PURPOSE_SHOW) {
                    temp = Integer.toString(count) + " : " + lines + "\n";
                    count += 1;
                }
                else {
                    temp = lines + "\n";
                }
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();
            //titleName.setText(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
