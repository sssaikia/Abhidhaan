package sstudio.com.abhidhaan;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 1/13/2017.
 */

public class Dbassist extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/sstudio.com.abhidhaan/databases/";
    private static final String DB_NAME="xobdo.sqlite";
    private static final int DB_VERSION=1;
    public static final String TB_USER = "Axomi_Eng";
    private SQLiteDatabase myDB;
    private Context context;
    public String word="null", asmean,enmean;
    public Dbassist(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @Override
    public synchronized void close(){
        if(myDB!=null){
            myDB.close();
        }
        super.close();
    }

    public List<String> getAllUsers(String req,boolean langE){
        List<String> listUsers = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
            if (langE){
        try {
            c = db.rawQuery("SELECT meaning,WrdEng,meaningAsm " +
                    "from T_IdeaBase,T_Map_WrdENG_IdeaBase,T_WrdENG" +
                    " where T_IdeaBase.IdeaID=T_Map_WrdENG_IdeaBase.IdeaID" +
                    " and T_Map_WrdENG_IdeaBase.WrdEngID=T_WrdENG.WrdEngID" +
                    " and T_WrdENG.WrdEng like '"+req+"'", null);
            if(c == null) return null;

            String name, mean;
            c.moveToFirst();
            do {
                name = c.getString(0);
                //listUsers.add("Eng- "+name);
                mean=c.getString(2);
                //listUsers.add("Asm- "+mean);
                listUsers.add(name+"\n"+mean);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            listUsers.add("No results found.\nFor quick search switch to Smart mode.");
            Log.e("tle99", e.getMessage());
        }}else {
                try {
                    c = db.rawQuery("SELECT meaning,WrdAsm,meaningAsm" +
                            " from T_IdeaBase,T_Map_WrdASM_IdeaBase," +
                            "T_WrdASM where T_IdeaBase.IdeaID=T_Map_WrdASM_IdeaBase.IdeaID" +
                            " and T_Map_WrdASM_IdeaBase.WrdAsmID=T_WrdASM.WrdAsmID" +
                            " and T_WrdASM.WrdAsm like '"+req+"'", null);
                    if(c == null) return null;

                    String name, mean;
                    c.moveToFirst();
                    do {
                        name = c.getString(0);
                        //listUsers.add("Eng- "+name);
                        mean=c.getString(2);
                        //listUsers.add("Asm- "+mean);
                        listUsers.add(name+"\n"+mean);
                    } while (c.moveToNext());
                    c.close();
                } catch (Exception e) {
                    listUsers.add("No results found.\nEdit the word for suggestions. ");
                    Log.e("tle99", e.getMessage());
            }}


        db.close();

        return listUsers;
    }
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }

    }
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }
    }
    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e("tle99 - check", e.getMessage());
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null ? true : false;
    }
    public List<String> getUsers(String req,boolean langE){
        List<String> listUsers = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
if (langE){
        try {
            c = db.rawQuery("SELECT meaning,WrdEng,meaningAsm from" +
                    " T_IdeaBase,T_Map_WrdENG_IdeaBase,T_WrdENG" +
                    " where T_IdeaBase.IdeaID=T_Map_WrdENG_IdeaBase.IdeaID" +
                    " and T_Map_WrdENG_IdeaBase.WrdEngID=T_WrdENG.WrdEngID" +
                    " and T_WrdENG.WrdEng like '"+req+"%'" +
                    " ORDER BY WrdEng LIMIT 50", null);
            if(c == null) return null;


            c.moveToFirst();
            do {
                word = c.getString(1);
                listUsers.add(word);
                //Log.e("word set","in dbhelper");
               /* asmean=c.getString(2);
                listUsers.add("Asm- "+asmean);
                enmean=c.getString(0);
                listUsers.add("Eng- "+enmean+"\n\n");*/
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            listUsers.add("No results found.");
            Log.e("tle99", e.getMessage());
        }}else {
    try {
        c = db.rawQuery("SELECT meaning,WrdAsm,meaningAsm" +
                " from T_IdeaBase,T_Map_WrdASM_IdeaBase,T_WrdASM " +
                "where T_IdeaBase.IdeaID=T_Map_WrdASM_IdeaBase.IdeaID" +
                " and T_Map_WrdASM_IdeaBase.WrdAsmID=T_WrdASM.WrdAsmID" +
                " and T_WrdASM.WrdAsm like '"+req+"%'" +
                " ORDER BY WrdAsm LIMIT 50", null);
        if(c == null) return null;

        String name, mean,wordE;
        c.moveToFirst();
        do {
            name = c.getString(1);
            listUsers.add(name);
           /* mean=c.getString(2);
            listUsers.add("Asm- "+mean);
            wordE=c.getString(0);
            listUsers.add("Eng- "+wordE+"\n\n");*/
        } while (c.moveToNext());
        c.close();
    } catch (Exception e) {
        listUsers.add("No results found.");
        Log.e("tle99", e.getMessage());
        }}


        db.close();

        return listUsers;
    }


}
