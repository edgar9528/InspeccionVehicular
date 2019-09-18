package com.tdt.inspeccionVehicular.Class;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String colores="CREATE TABLE COLORES (COL_CVE_STR NVARCHAR(100),COL_DESC_STR NVARCHAR(255));";
        String lineas="CREATE TABLE LINEAS (LIN_CVE_N INT,LIN_DESC_STR NVARCHAR(255));";
        String pais="CREATE TABLE PAIS (PAIS_CVE_STR NVARCHAR(100), PAIS_DESC_STR NVARCHAR(255));";
        String retrabajo="CREATE TABLE RETRABAJO (RET_CVE_N INT, RET_CVE_STR NVARCHAR(255));";
        String turnos="CREATE TABLE TURNOS (TRN_CVE_N INT, TRN_DESC_STR NVARCHAR(255));";
        String danos="CREATE TABLE DANOS (DAN_CVE_N INT, DAN_DESC_STR NVARCHAR(255));";
        String tcajas="CREATE TABLE TCAJAS (TCA_CVE_N INT, TCA_DESC_STR NVARCHAR(255));";
        String marcas="CREATE TABLE MARCAS (MAR_CVE_N INT, MAR_DESC_STR NVARCHAR(255));";

        db.execSQL(colores);
        db.execSQL(lineas);
        db.execSQL(pais);
        db.execSQL(retrabajo);
        db.execSQL(turnos);
        db.execSQL(danos);
        db.execSQL(tcajas);
        db.execSQL(marcas);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS COLORES");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LINEAS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PAIS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS RETRABAJO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TURNOS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DANOS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TCAJAS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MARCAS");

        onCreate(sqLiteDatabase);

    }
}
