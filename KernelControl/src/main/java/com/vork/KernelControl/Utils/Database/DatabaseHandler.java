/*
 * This file is part of KernelControl.
 *
 *     KernelControl is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KernelControl is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KernelControl.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.vork.KernelControl.Utils.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vork.KernelControl.Utils.Database.DatabaseObjects.KernelInterface;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "interfaceManager";

    //Tables
    private static final String TABLE_KERNEL_INTERFACE = "interface";
    private static final String TABLE_INTERFACE_PROFILE = "interfaceProfile";
    private static final String TABLE_PROFILE = "profiles";

    //Interface Column Names
        //Interface
    private static final String KEY_INTERFACE_ID = "id";
    private static final String KEY_INTERFACE_NAME = "name";
    private static final String KEY_INTERFACE_DESCRIPTION = "description";
    private static final String KEY_INTERFACE_PATH = "path";
    private static final String KEY_INTERFACE_SAVED_VALUE = "savedValue";
    private static final String KEY_INTERFACE_SOB = "setOnBoot";
    private static final String[] COLUMNS_TO_READ = new String[]{
            KEY_INTERFACE_ID, KEY_INTERFACE_NAME, KEY_INTERFACE_DESCRIPTION, KEY_INTERFACE_PATH,
            KEY_INTERFACE_SAVED_VALUE, KEY_INTERFACE_SOB};
        //Interface Profile
    private static final String KEY_INTERPROFILE_INTID = "interfaceId";
    private static final String KEY_INTERPROFILE_PROID = "profileId";
        //Profiles
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createInterfaceTable = "CREATE TABLE " + TABLE_KERNEL_INTERFACE + "(" +
                KEY_INTERFACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_INTERFACE_NAME + " TEXT," +
                KEY_INTERFACE_DESCRIPTION + " TEXT," +
                KEY_INTERFACE_PATH + " TEXT not null," +
                KEY_INTERFACE_SAVED_VALUE + " TEXT," +
                KEY_INTERFACE_SOB + " INTEGER" + ")";
        String createProfileTable = "CREATE TABLE " + TABLE_PROFILE + "(" +
                KEY_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PROFILE_NAME + " TEXT not null" + ")";
        String createInterfaceProfileTable = "CREATE TABLE " + TABLE_INTERFACE_PROFILE + "(" +
                KEY_INTERPROFILE_INTID + " INTEGER not null," +
                KEY_INTERPROFILE_PROID + " INTEGER not null," +
                "FOREIGN KEY(" + KEY_INTERPROFILE_INTID + ") REFERENCES " + TABLE_KERNEL_INTERFACE + "(" + KEY_INTERFACE_ID + ")," +
                "FOREIGN KEY(" + KEY_INTERPROFILE_PROID + ") REFERENCES " + TABLE_PROFILE + "(" + KEY_PROFILE_ID + ")" +
                ")";

        db.execSQL(createInterfaceTable);
        //TODO profiles will be enabled at a later point
//        db.execSQL(createProfileTable);
//        db.execSQL(createInterfaceProfileTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERFACE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERNEL_INTERFACE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);

        // Create tables again
        onCreate(db);
    }

    /**
     *
     * @param kernelInterface
     * @return the KernelInterface with the correct id
     */
    public KernelInterface addKernelInterface(KernelInterface kernelInterface) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INTERFACE_NAME, kernelInterface.getName());
        values.put(KEY_INTERFACE_DESCRIPTION, kernelInterface.getDescription());
        values.put(KEY_INTERFACE_PATH, kernelInterface.getPath());
        values.put(KEY_INTERFACE_SAVED_VALUE, kernelInterface.getValue());
        values.put(KEY_INTERFACE_SOB, kernelInterface.getSetOnBoot());

        if (db != null) {
            db.insert(TABLE_KERNEL_INTERFACE, null, values);
            db.close();
        }

        db = this.getReadableDatabase();
        Cursor cursorGetId = null;
        if (db != null) {
            cursorGetId = db.rawQuery("SELECT MAX(" + KEY_INTERFACE_ID + ") FROM " + TABLE_KERNEL_INTERFACE, null);
        }

        int id = -1;
        if(cursorGetId != null) {
            cursorGetId.moveToFirst();
            id = cursorGetId.getInt(0);
        }

        kernelInterface.setId(id);

        return kernelInterface;
    }

    public KernelInterface getKernelInterface(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        KernelInterface toRet = null;

        if (db != null) {
            Cursor cursor = db.query(TABLE_KERNEL_INTERFACE,
                    COLUMNS_TO_READ,
                    KEY_INTERFACE_ID + "=?",
                    new String[] {String.valueOf(id)},
                    null,
                    null,
                    null,
                    null);

            if(cursor != null) {
                cursor.moveToFirst();
                toRet = new KernelInterface(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));
                String savedValue = cursor.getString(4);

                if (savedValue != null) {
                    if (!savedValue.equals("")) {
                        toRet.setValue(savedValue);
                    }
                }
                toRet.setSetOnBoot(cursor.getInt(5) == 1);
            }
        }

        return toRet;
    }

    public List<KernelInterface> getAllKernelInterfaces() {
        List<KernelInterface> toRet = new ArrayList<KernelInterface>();

        String selectQuery = "SELECT * FROM " + TABLE_KERNEL_INTERFACE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(selectQuery, null);
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    KernelInterface kernelInterface = new KernelInterface(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3));

                    String savedValue = cursor.getString(4);

                    if (savedValue != null) {
                        if (!savedValue.equals("")) {
                            kernelInterface.setValue(savedValue);
                        }
                    }
                    kernelInterface.setSetOnBoot(cursor.getInt(5) == 1);

                    toRet.add(kernelInterface);
                } while (cursor.moveToNext());
            }
        }

        return toRet;
    }

    public int getKernelInterfaceCount() {
        String countQuery = "SELECT  * FROM " + TABLE_KERNEL_INTERFACE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(countQuery, null);
            cursor.close();
        }

        // return count
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public void updateKernelInterface(KernelInterface kernelInterface) {
        SQLiteDatabase db = this.getWritableDatabase();

        int id = kernelInterface.getId();

        ContentValues values = new ContentValues();
        values.put(KEY_INTERFACE_NAME, kernelInterface.getName());
        values.put(KEY_INTERFACE_DESCRIPTION, kernelInterface.getDescription());
        values.put(KEY_INTERFACE_PATH, kernelInterface.getPath());
        values.put(KEY_INTERFACE_SAVED_VALUE, kernelInterface.getValue());
        values.put(KEY_INTERFACE_SOB, kernelInterface.getSetOnBoot());

        if (db != null) {
            db.update(TABLE_KERNEL_INTERFACE, values, KEY_INTERFACE_ID + " = ?",
                    new String[] { String.valueOf(id) });
            db.close();
        }
    }

    public void deleteKernelInterface(KernelInterface kernelInterface) {
        SQLiteDatabase db = this.getWritableDatabase();

        int id = kernelInterface.getId();

        if (db != null) {
            db.delete(TABLE_KERNEL_INTERFACE, KEY_INTERFACE_ID + " = ?",
                    new String[] { String.valueOf(id) });
            db.close();
        }

    }

    /*
    public Profile addProfile(Profile profile) {

    }

    public Profile getProfile(String name) {

    }

    public Profile getProfile(int id) {

    }

    public List<Profile> getAllProfiles() {

    }

    public int updateProfile(Profile profile) {

    }

    public void deleteProfile(Profile profile) {

    }
    */
}
