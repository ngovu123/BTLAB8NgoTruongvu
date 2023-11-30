package com.example.btlab8_ngotruongvu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    private ListView listView;
    private Spinner spinner;
    private Button applyButton, allButton, closeButton;
    private ArrayAdapter<NhanVien> arrayAdapter;
    private int selectedEmployeePosition;
    private ArrayList<NhanVien> arrayList = new ArrayList<>(); // Khởi tạo arrayList ở đây

    private ArrayList<NhanVien> allEmployees = new ArrayList<>(); // Khởi tạo allEmployees ở đây

    ArrayList<PhongBan> arrayList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        listView = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);
        applyButton = findViewById(R.id.applyButton);
        allButton = findViewById(R.id.allButton);
        closeButton = findViewById(R.id.closeButton);
        registerForContextMenu(listView);

        PhongBan p = new PhongBan(1, "Phong Hanh Chinh");
        db.addPhong(p);
        arrayList2.add(p);
        p = new PhongBan(2, "Phong Ke Toan");
        db.addPhong(p);
        arrayList2.add(p);
        p = new PhongBan(3, "Phong Nhan Su");
        db.addPhong(p);
        arrayList2.add(p);
        NhanVien nv = new NhanVien(1, "Nguyễn Văn A", 1);
        arrayList.add(nv); // Sử dụng arrayList đã khai báo và khởi tạo
        db.addNhanvien(nv);
        nv = new NhanVien(2, "Nguyễn Kim D", 2);
        arrayList.add(nv); // Sử dụng arrayList đã khai báo và khởi tạo
        db.addNhanvien(nv);
        nv = new NhanVien(3, "Nguyễn Thị B", 1);
        arrayList.add(nv); // Sử dụng arrayList đã khai báo và khởi tạo
        db.addNhanvien(nv);
        nv = new NhanVien(4, "Nguyễn Văn C", 2);
        arrayList.add(nv); // Sử dụng arrayList đã khai báo và khởi tạo
        db.addNhanvien(nv);
    
        // Populate spinner with phong ban data
        arrayList2 = db.getAllPhongBan();
        ArrayAdapter<PhongBan> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arrayList2
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Set up onClickListeners
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhongBan selectedPhongBan = (PhongBan) spinner.getSelectedItem();
                locnv(selectedPhongBan);
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllEmployees();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEmployeePosition = i;
                return false;
            }
        });

        loadAllEmployees();
    }

    private void loadAllEmployees() {
        allEmployees.clear(); // Xóa dữ liệu cũ trước khi nạp dữ liệu mới
        allEmployees.addAll(db.getAllnv());
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allEmployees);
        listView.setAdapter(arrayAdapter);
    }

    private void locnv(PhongBan selectedPhongBan) {
        ArrayList<NhanVien> filteredList = db.getEmployeesByPhongBan(selectedPhongBan.getMaph());
        arrayAdapter.clear(); // Xóa dữ liệu hiện tại trong ArrayAdapter
        arrayAdapter.addAll(filteredList); // Thêm danh sách nhân viên đã lọc vào ArrayAdapter
        arrayAdapter.notifyDataSetChanged(); // Cập nhật ListView để hiển thị danh sách mới
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            Toast.makeText(this, "Thêm clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_edit) {
            Toast.makeText(this, "Sửa clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_delete) {
            showDeleteConfirmationDialog();
        }

        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá nhân viên này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NhanVien deletedEmployee = arrayAdapter.getItem(selectedEmployeePosition);
                db.deleteNhanVien(deletedEmployee);
                arrayAdapter.remove(deletedEmployee);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Nhân viên đã được xoá", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
