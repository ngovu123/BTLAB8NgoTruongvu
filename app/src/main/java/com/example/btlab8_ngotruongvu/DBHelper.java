package com.example.btlab8_ngotruongvu;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ql.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "nhanvien";
    private static final String COLUMN_ID_NV = "manv";
    private static final String COLUMN_TEN = "tennv";
    private static final String COLUMN_ID_phong = "maph";

    private static final String TABLE_NAME2 = "phongban";
    private static final String COLUMN_MOTA = "mota";
    private static final String COLUMN_NAME_PHONG = "tenph";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID_NV + " INTEGER PRIMARY KEY,"
                + COLUMN_TEN + " VARCHAR,"
                + COLUMN_ID_phong + " INTEGER" + ")";
        String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + "("
                + COLUMN_ID_phong + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_PHONG + " VARCHAR,"
                + COLUMN_MOTA + " VARCHAR" + ")";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE2);
        onCreate(db);
    }

    public void addPhong(PhongBan phong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_phong, phong.getMaph());
        values.put(COLUMN_NAME_PHONG, phong.getTenph());
        values.put(COLUMN_MOTA, phong.getMota());
        db.insert(TABLE_NAME2, null, values);
        db.close();
    }

    public void addNhanvien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_NV, nv.getManv());
        values.put(COLUMN_TEN, nv.getTennv());
        values.put(COLUMN_ID_phong, nv.getMaph());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID_NV + " = ?", new String[]{String.valueOf(nv.getManv())});
        db.close();
    }

    public ArrayList<NhanVien> getAllnv() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<NhanVien> list = new ArrayList<>();
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_NV);
                int nameIndex = cursor.getColumnIndex(COLUMN_TEN);
                int phongIndex = cursor.getColumnIndex(COLUMN_ID_phong);

                if (idIndex != -1 && nameIndex != -1 && phongIndex != -1) {
                    NhanVien nv = new NhanVien();
                    nv.setManv(cursor.getInt(idIndex));
                    nv.setTennv(cursor.getString(nameIndex));
                    nv.setMaph(cursor.getInt(phongIndex));
                    list.add(nv);
                }
            }
            cursor.close();
        }

        db.close();

        return list;
    }

    public ArrayList<PhongBan> getAllListPhong() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PhongBan> list = new ArrayList<>();
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_phong);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME_PHONG);
                int motaIndex = cursor.getColumnIndex(COLUMN_MOTA);

                if (idIndex != -1 && nameIndex != -1 && motaIndex != -1) {
                    PhongBan p = new PhongBan();
                    p.setMaph(cursor.getInt(idIndex));
                    p.setTenph(cursor.getString(nameIndex));
                    p.setMota(cursor.getString(motaIndex));
                    list.add(p);
                }
            }
            cursor.close();
        }

        db.close();

        return list;
    }

    public ArrayList<NhanVien> getEmployeesByPhongBan(int phongBanId) {
        ArrayList<NhanVien> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_phong + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(phongBanId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_NV);
                int nameIndex = cursor.getColumnIndex(COLUMN_TEN);
                int phongIndex = cursor.getColumnIndex(COLUMN_ID_phong);

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int phongId = cursor.getInt(phongIndex);

                // Kiểm tra nếu mã phòng ban của nhân viên trùng khớp với mã phòng ban được chọn
                if (phongId == phongBanId) {
                    NhanVien nhanVien = new NhanVien(id, name, phongId);
                    employeeList.add(nhanVien);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return employeeList;
    }


    public ArrayList<NhanVien> getEmployeesByNameAndPhongBan(String tenNhanVien, int phongBanId) {
        ArrayList<NhanVien> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TEN + " = '" + tenNhanVien + "' AND " + COLUMN_ID_phong + " = " + phongBanId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_NV);
                int nameIndex = cursor.getColumnIndex(COLUMN_TEN);
                int phongIndex = cursor.getColumnIndex(COLUMN_ID_phong);

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int phongId = cursor.getInt(phongIndex);
                NhanVien nhanVien = new NhanVien(id, name, phongId);
                employeeList.add(nhanVien);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return employeeList;
    }

    public ArrayList<NhanVien> getEmployeesByName(String tenNhanVien) {
        ArrayList<NhanVien> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TEN + " = '" + tenNhanVien + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_NV);
                int nameIndex = cursor.getColumnIndex(COLUMN_TEN);
                int phongIndex = cursor.getColumnIndex(COLUMN_ID_phong);

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int phongId = cursor.getInt(phongIndex);
                NhanVien nhanVien = new NhanVien(id, name, phongId);
                employeeList.add(nhanVien);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return employeeList;
    }



    public ArrayList<PhongBan> getAllPhongBan() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PhongBan> list = new ArrayList<>();
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID_phong);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME_PHONG);
                int motaIndex = cursor.getColumnIndex(COLUMN_MOTA);

                if (idIndex != -1 && nameIndex != -1 && motaIndex != -1) {
                    PhongBan p = new PhongBan();
                    p.setMaph(cursor.getInt(idIndex));
                    p.setTenph(cursor.getString(nameIndex));
                    p.setMota(cursor.getString(motaIndex));
                    list.add(p);
                }
            }
            cursor.close();
        }

        db.close();

        return list;
    }

}
