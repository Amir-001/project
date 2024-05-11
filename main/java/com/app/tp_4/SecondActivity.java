package com.app.tp_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {
    private TextView Note;
    private TextView Datetxt;
    private ImageView btn;
    private SimpleDateFormat simpleDateFormat;
    private Item item;
    private TextToSpeech textToSpeech;
    private int position;
    private boolean edit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Note = findViewById(R.id.note);
        Datetxt = findViewById(R.id.date);
        btn = findViewById(R.id.btn);

        position = getIntent().getExtras().getInt("position");
        item = MainActivity.items.get(position);
        Note.setText(item.getNote());
        simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Datetxt.setText(simpleDateFormat.format(item.getDate()));

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(item.getNote(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    protected void onPause() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_edit){
            showDialogEditItem();
            return true;
        };
        return super.onOptionsItemSelected(item);
    }
    private void showDialogEditItem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_edit_item);
        EditText editText = dialog.getWindow().findViewById(R.id.add_item_note);
        editText.setText(item.getNote());
        dialog.getWindow().findViewById(R.id.add_item_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals(item.getNote())) {
                    item.setNote(editText.getText().toString());
                    item.setDate(Calendar.getInstance().getTime());

                    Note.setText(item.getNote());
                    Datetxt.setText(simpleDateFormat.format(item.getDate()));
                    edit = true;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("edit",edit);
        intent.putExtra("position",position);
        setResult(1,intent);
        super.finish();
    }
}