    package com.example.bth8;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import java.util.ArrayList;
    import java.util.List;

    public class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "QuanLyNhanVien.db";
        private static final int DATABASE_VERSION = 1;

        // Tên bảng và các cột của bảng "PHONGBAN"
        private static final String TABLE_PHONGBAN = "PHONGBAN";
        private static final String COLUMN_MAPH = "MAPH";
        private static final String COLUMN_TENPH = "TENPH";
        private static final String COLUMN_MOTA = "MOTA";

        // Tên bảng và các cột của bảng "NHANVIEN"
        private static final String TABLE_NHANVIEN = "NHANVIEN";
        private static final String COLUMN_MANV = "MANV";
        private static final String COLUMN_TENNV = "TENNV";
        private static final String COLUMN_PHONG = "PHONG";

        private static final String CREATE_TABLE_PHONGBAN = "CREATE TABLE " + TABLE_PHONGBAN + " ("
                + COLUMN_MAPH + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TENPH + " TEXT,"
                + COLUMN_MOTA + " TEXT"
                + ")";

        // Câu lệnh tạo bảng "NHANVIEN" với cột kiểu INT tự tăng dần
        private static final String CREATE_TABLE_NHANVIEN = "CREATE TABLE " + TABLE_NHANVIEN + " ("
                + COLUMN_MANV + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TENNV + " TEXT,"
                + COLUMN_PHONG + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_PHONG + ") REFERENCES " + TABLE_PHONGBAN + "(" + COLUMN_MAPH + ")"
                + ")";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_PHONGBAN);
            String sql = "INSERT INTO " + TABLE_PHONGBAN + " (" + COLUMN_TENPH + ", " + COLUMN_MOTA + ") " +
                    "VALUES ('Hành Chính', 'Phòng Hành Chính')";
            db.execSQL(sql);
            sql = "INSERT INTO " + TABLE_PHONGBAN + " (" + COLUMN_TENPH + ", " + COLUMN_MOTA + ") " +
                    "VALUES ('Nhân sự', 'Phòng Nhân Sự')";
            db.execSQL(sql);

            db.execSQL(CREATE_TABLE_NHANVIEN);
            sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('Nguyễn Văn Hùng', '1')";
            db.execSQL(sql);
            sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('Nguyễn Văn Trường', '1')";
            db.execSQL(sql);
            sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('Nguyễn Thảo Nguyên', '2')";
            db.execSQL(sql);
            sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('Bùi Hoàng Huy', '2')";
            db.execSQL(sql);
        }
        public long insertDataNhanVien(String tenNV, int phong) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Câu lệnh SQL để chèn dữ liệu vào bảng "NHANVIEN"
            String sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('" + tenNV + "', " + phong + ")";

            long result = -1;
            try {
                db.execSQL(sql);
                result = 1; // Nếu không có lỗi, trả về 1 (thành công)
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return result;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONGBAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHANVIEN);
            onCreate(db);
        }
        public long insertDataNhanVien(String tenNV, String phong) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Câu lệnh SQL để chèn dữ liệu vào bảng "NHANVIEN"
            String sql = "INSERT INTO " + TABLE_NHANVIEN + " (" + COLUMN_TENNV + ", " + COLUMN_PHONG + ") " +
                    "VALUES ('" + tenNV + "', '" + phong + "')";

            // Thực hiện câu lệnh SQL để chèn dữ liệu
            long result = -1;
            try {
                db.execSQL(sql);
                result = 1; // Nếu không có lỗi, trả về 1 (thành công)
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return result;
        }

        public long insertDataPhongBan(String tenPhong, String moTa) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Câu lệnh SQL để chèn dữ liệu vào bảng "PHONGBAN"
            String sql = "INSERT INTO " + TABLE_PHONGBAN + " (" + COLUMN_TENPH + ", " + COLUMN_MOTA + ") " +
                    "VALUES ('" + tenPhong + "', '" + moTa + "')";

            // Thực hiện câu lệnh SQL để chèn dữ liệu
            long result = -1;
            try {
                db.execSQL(sql);
                result = 1; // Nếu không có lỗi, trả về 1 (thành công)
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return result;
        }

        public List<String> getAllPhongBan() {
            List<String> phongBanList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + COLUMN_TENPH + " FROM " + TABLE_PHONGBAN, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String tenPhong = cursor.getString(cursor.getColumnIndex(COLUMN_TENPH));
                    phongBanList.add(tenPhong);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return phongBanList;
        }
        public List<String> getAllNhanVien() {
            List<String> nhanVienList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + COLUMN_TENNV + " FROM " + TABLE_NHANVIEN, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String tenNhanVien = cursor.getString(cursor.getColumnIndex(COLUMN_TENNV));
                    nhanVienList.add(tenNhanVien);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return nhanVienList;
        }
        public List<String> getAllNhanVienByPhongBan(String phongBan) {
            List<String> nhanVienList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor;
            if ("ALL".equals(phongBan)) {
                // Lấy tất cả nhân viên nếu chọn phòng ban "ALL"
                cursor = db.rawQuery("SELECT " + COLUMN_TENNV + " FROM " + TABLE_NHANVIEN, null);
            } else {
                // Lấy nhân viên theo phòng ban
                cursor = db.rawQuery("SELECT " + COLUMN_TENNV + " FROM " + TABLE_NHANVIEN +
                        " WHERE " + COLUMN_PHONG + " = (SELECT " + COLUMN_MAPH + " FROM " + TABLE_PHONGBAN +
                        " WHERE " + COLUMN_TENPH + " = '" + phongBan + "')", null);
            }

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String tenNhanVien = cursor.getString(cursor.getColumnIndex(COLUMN_TENNV));
                    nhanVienList.add(tenNhanVien);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return nhanVienList;
        }
        public boolean deleteNhanVien(String tenNhanVien) {
            SQLiteDatabase db = this.getWritableDatabase();

            // Chuẩn bị điều kiện xoá
            String whereClause = COLUMN_TENNV + " = ?";
            String[] whereArgs = {tenNhanVien};

            try {
                // Thực hiện câu lệnh xoá
                int rowsDeleted = db.delete(TABLE_NHANVIEN, whereClause, whereArgs);

                // Kiểm tra xem có dòng nào bị xoá không
                if (rowsDeleted > 0) {
                    return true; // Xoá thành công
                } else {
                    return false; // Không có dòng nào bị xoá
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Xoá thất bại
            } finally {
                db.close();
            }
        }


    }
