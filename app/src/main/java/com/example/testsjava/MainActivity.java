package com.example.testsjava;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    DBHelper DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);
        DB = new DBHelper(this);

        buildDialog();

        getData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void getData() {
        Cursor res = DB.getdata();
        while(res.moveToNext()){
            String id = res.getString(0);
            String title = res.getString(1);
            String content = res.getString(2);
            String date = res.getString(3);
            addCard(title, content, date, id);
        }
    }

    private void buildEditDialog(String id, String oldTitle, String oldContent, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.edit_dialog, null);

        final EditText editTitle = view.findViewById(R.id.nameEdit);
        final EditText editContent = view.findViewById(R.id.contentEdit);
        
        final TextView cardTitle = v.findViewById(R.id.name);
        final TextView cardContent = v.findViewById(R.id.content);
        final TextView cardDate = v.findViewById(R.id.date);

        editTitle.setText(oldTitle);
        editContent.setText(oldContent);
        builder.setView(view);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                String date = df.format(c);
                String title = editTitle.getText().toString();
                String content = editContent.getText().toString();

                Boolean checkUpdateData = DB.updateuserdata(id, title, content, date);
                if(!checkUpdateData) {
                    Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                cardTitle.setText(title);
                cardContent.setText(content);
                cardDate.setText(date);
            }
        });
        builder.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                layout.removeView(v);
                DB.deletedata(id);
            }
        });
        builder.create().show();
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText editTitle = view.findViewById(R.id.nameEdit);
        final EditText editContent = view.findViewById(R.id.contentEdit);

        builder.setView(view);
        builder.setTitle("Tambah diary baru")
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                        String date = df.format(c);
                        String title = editTitle.getText().toString();
                        String content = editContent.getText().toString();
                        String id = DB.GenerateRandomUUID();

                        Boolean checkInsertData = DB.insertuserdata(title, content, date, id);

                        if(!checkInsertData) {
                            Toast.makeText(MainActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        addCard(title, content, date, id);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addCard(String title, String content, String date, String id) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView contentView = view.findViewById(R.id.content);
        TextView dateView = view.findViewById(R.id.date);
        TextView idView = view.findViewById(R.id.id);

        Button edit = view.findViewById(R.id.edit);

        nameView.setText(title);
        contentView.setText(content);
        dateView.setText(date);
        idView.setText(id);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildEditDialog(id, nameView.getText().toString(), contentView.getText().toString(), view);

            }
        });

        layout.addView(view);
    }
}
