package com.example.bth8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Spinner spinnerPhongBan;
    Button btnApply, btnAll, btnClose;
    ListView listViewNhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        spinnerPhongBan = findViewById(R.id.spinnerPhongBan);
        btnApply = findViewById(R.id.buttonApply);
        btnAll = findViewById(R.id.buttonAll);
        btnClose = findViewById(R.id.buttonClose);
        listViewNhanVien = findViewById(R.id.listViewNhanVien);

        // Lấy danh sách phòng ban từ bảng "PHONGBAN"
        List<String> phongBanList = dbHelper.getAllPhongBan();
        phongBanList.add(0, "ALL"); // Thêm phòng ban "ALL" vào đầu danh sách
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phongBanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhongBan.setAdapter(adapter);

        // Hiển thị toàn bộ danh sách nhân viên khi ứng dụng bắt đầu
        updateListView("ALL");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPhongBan = spinnerPhongBan.getSelectedItem().toString();
                updateListView(selectedPhongBan);
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị toàn bộ danh sách nhân viên khi ấn nút "All"
                updateListView("ALL");
            }
        });

        // Thêm sự kiện cho ListView để bật Context Menu khi giữ lâu trên một item
        listViewNhanVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                registerForContextMenu(listViewNhanVien);
                openContextMenu(listViewNhanVien);
                unregisterForContextMenu(listViewNhanVien);
                return true;
            }
        });
    }

    // ... (phần code trước đó không thay đổi)

    // Cập nhật ListView dựa trên phòng ban được chọn
    private void updateListView(String selectedPhongBan) {
        List<String> nhanVienList =        dbHelper.getAllNhanVienByPhongBan(selectedPhongBan);
        ArrayAdapter<String> nhanVienAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, nhanVienList);
        listViewNhanVien.setAdapter(nhanVienAdapter);
    }

    // ... (phần code trước đó không thay đổi)

    // Bật Context Menu khi giữ lâu trên item ListView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.item_menu, menu);
    }

    // Xử lý khi chọn một item trong Context Menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedNhanVien = listViewNhanVien.getItemAtPosition(info.position).toString();

        if (item.getItemId() == R.id.action_add) {
            // Xử lý khi chọn "Thêm"
            // ...
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            // Xử lý khi chọn "Sửa"
            // ...
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            // Xử lý khi chọn "Xoá"
            showConfirmationDialog(selectedNhanVien);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }


    // Hiển thị hộp thoại xác nhận xoá
    private void showConfirmationDialog(String selectedNhanVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Xác Nhận Xoá");
        builder.setMessage("Bạn có chắc muốn xoá nhân viên " + selectedNhanVien + "?");

        // Nút tích cực (Có)
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Xoá nhân viên từ CSDL
                boolean deleted = dbHelper.deleteNhanVien(selectedNhanVien);

                // Kiểm tra kết quả xoá và hiển thị thông báo
                if (deleted) {
                    Toast.makeText(MainActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();

                    // Cập nhật lại danh sách nhân viên
                    updateNhanVienList();
                } else {
                    Toast.makeText(MainActivity.this, "Xoá không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút tiêu cực (Không)
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Người dùng chọn "Không", không làm gì cả
            }
        });

        // Tạo và hiển thị hộp thoại
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateNhanVienList() {
        // Lấy danh sách nhân viên theo phòng ban hiện tại
        String selectedPhongBan = spinnerPhongBan.getSelectedItem().toString();
        List<String> nhanVienList = dbHelper.getAllNhanVienByPhongBan(selectedPhongBan);

        // Tạo Adapter và đặt lại cho ListView
        ArrayAdapter<String> nhanVienAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nhanVienList);
        listViewNhanVien.setAdapter(nhanVienAdapter);
    }

}

