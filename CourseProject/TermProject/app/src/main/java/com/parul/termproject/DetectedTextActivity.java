package com.parul.termproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parul.termproject.databinding.ActivityDetectedTextBinding;
import com.parul.termproject.databinding.ActivityDetectedTextBindingImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetectedTextActivity extends AppCompatActivity {
    TextView detectedText;
    String finalText="";
    DatabaseReference mentry;
    String collegeName="";
    String name="",roll="";
    int ci=0;
    private ProgressDialog progressBar;
    List<String>finaldata=new ArrayList<>();
    ActivityDetectedTextBinding activityDetectedTextBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetectedTextBinding= DataBindingUtil.setContentView(this, R.layout.activity_detected_text);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mentry = database.getReference("records");
        progressBar = new ProgressDialog(DetectedTextActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please Wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("list");
       for(int i=0;i<myList.size();i++)
       {
           List<String>temp= Arrays.asList(myList.get(i).split(" "));
           for(int j=0;j<temp.size();j++){
               List<String>temp1= Arrays.asList(temp.get(j).split("\\r?\\n"));

               for(int k=0;k<temp1.size();k++){
                   finaldata.add(temp1.get(k));
               }

           }
       }


        for (int i=0;i<finaldata.size();i++)
        {
            Log.e("datavalue",i+finaldata.get(i));
            if(finaldata.get(i).contains("1")&&finaldata.get(i).length()!=6&&!finaldata.get(i).contains("/")&&(finaldata.get(i).length()==7||finaldata.get(i).length()==5)){
                roll=finaldata.get(i);
                break;
            }
        }
        Log.e("sizee",""+String.valueOf(finaldata.size()));
     //  Log.e("list",finalText);

        activityDetectedTextBinding.college.setText("NATIONAL INSTITUTE OF TECHNOLOGY, HAMIRPUR");

        activityDetectedTextBinding.rollNo.setText("Roll No: "+roll.toLowerCase());
        mentry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean ok = false;
                for (DataSnapshot record : snapshot.getChildren()) {
                    Entry rec = record.getValue(Entry.class);
                    Log.i ("Roll NO;", rec.getRollNo());
                    if (rec.getRollNo().equals(roll.toLowerCase())) {
                        progressBar.dismiss();
                        activityDetectedTextBinding.name.setText("Name: "+rec.getName());
                        activityDetectedTextBinding.category.setText("Category: "+rec.getCategory());
                        activityDetectedTextBinding.branch.setText("Department: "+rec.getDeptt());
                        ok = true;
                        break;
                    }
                }
                if (ok == false) {
                    Toast.makeText(DetectedTextActivity.this, "Roll No Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetectedTextActivity.this, "Some Error Occured"+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("erorr",error.getDetails());
            }
        });






    }
}