package com.app.tp_4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "TP4";
    private static String UserName = "Username";
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    public static ArrayList<Item> items;
    private Intent intent;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //rotate
        if(savedInstanceState==null)
            items = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        customAdapter = new CustomAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        intent = new Intent(this,SecondActivity.class);
        activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getData().getBooleanExtra("edit",false)){
                        customAdapter.notifyItemChanged(result.getData()
                            .getIntExtra("position",0));
                    }
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_add){
            showDialogAddItem();
            return true;
        };
        return super.onOptionsItemSelected(item);
    }
    private void showDialogAddItem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_add_item);
        EditText editText = dialog.getWindow().findViewById(R.id.add_item_note);
        dialog.getWindow().findViewById(R.id.add_item_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")) {
                    items.add(new Item(editText.getText().toString(),
                        Calendar.getInstance().getTime()));
                    customAdapter.notifyItemInserted(items.size() - 1);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void showDialogDeleteItem(int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete note");
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setMessage("Delete note ?");
        dialog.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idBtn) {
                        items.remove(position);
                        customAdapter.notifyItemRemoved(position);
                    }
                });
        dialog.setNegativeButton(android.R.string.cancel, null);
        dialog.show();
    }

    class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        class CViewHolder extends RecyclerView.ViewHolder{
            private CardView Card;
            private ImageView ItemImage;
            private TextView ItemTitle;
            private TextView ItemText;
            public CViewHolder(@NonNull View itemView) {
                super(itemView);
                Card = itemView.findViewById(R.id.item_card);
                ItemImage = itemView.findViewById(R.id.item_img);
                ItemTitle = itemView.findViewById(R.id.item_title);
                ItemText = itemView.findViewById(R.id.item_text);
            }

            public CardView getCard() {
                return Card;
            }

            public ImageView getItemImage() {
                return ItemImage;
            }

            public TextView getItemTitle() {
                return ItemTitle;
            }

            public TextView getItemText() {
                return ItemText;
            }
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CViewHolder(getLayoutInflater().inflate(R.layout.view_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CViewHolder cViewHolder = (CViewHolder) holder;
            cViewHolder.getItemTitle().setText(UserName);
            cViewHolder.getItemText().setText(items.get(position).getNote());
            cViewHolder.getItemImage().setImageDrawable(getDrawable(R.drawable.user));

            cViewHolder.getCard().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDialogDeleteItem(holder.getAdapterPosition());
                    return false;
                }
            });

            cViewHolder.getCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("position",holder.getAdapterPosition());
                    activityResultLauncher.launch(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}