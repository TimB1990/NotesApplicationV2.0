package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class UpdateNoteActivity extends AppCompatActivity{

   // public static final String EXTRA_REPLY = "com.example.notesapplicationv20.updatenoteactivity.REPLY";

   Spinner subjectSpinner;
   EditText updateTitle, updateDescription, updateContent;
   Button updateNoteBtn;
   private NotesViewModel model;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_note);

      SingleNote singleNote = (SingleNote)getIntent().getSerializableExtra("singleNote");
      model = new ViewModelProvider(this).get(NotesViewModel.class);

      subjectSpinner = findViewById(R.id.update_note_subject_spinner);
      updateTitle = findViewById(R.id.update_note_title_input);
      updateDescription = findViewById(R.id.update_note_desc_input);
      updateContent = findViewById(R.id.update_note_cnt_input);
      updateNoteBtn = findViewById(R.id.update_note_submit);

      setSpinnerText(subjectSpinner, singleNote.getSubject());
      updateTitle.setText(singleNote.getTitle());
      updateDescription.setText(singleNote.getDescription());
      // string builder instead of string?
      updateContent.setText(singleNote.getContent());

      updateNoteBtn.setOnClickListener(v -> {

         Intent backToMain = new Intent(this, MainActivity.class);

         String subject = subjectSpinner.getSelectedItem().toString();
         String title = updateTitle.getText().toString();
         String desc = updateDescription.getText().toString();
         String cnt = updateContent.getText().toString();
         Date createdAt = singleNote.getCreatedAt();

         String[]values = {subject, title, desc, cnt};
         Boolean go = false;

         for(String val: values){
            if(val.isEmpty()){
               Toast.makeText(getApplicationContext(),"All fields are required!", Toast.LENGTH_LONG).show();
               return;
            }
            else{
               go = true;
            }
         }

         if(go){
            // use the second constructor of single note with id, so room knows which id to update
            SingleNote updatedSingleNote = new SingleNote(singleNote.getId(),
                    values[0],
                    values[1],
                    values[2],
                    values[3],
                    createdAt,
                    Calendar.getInstance().getTime());

            model.updateSingleNote(updatedSingleNote);

            Toast.makeText(getApplicationContext(),
                    "note: '" + singleNote.getId() + "' have been updated!",
                    Toast.LENGTH_LONG).show();
            startActivity(backToMain);
         }
      });
   }

   private void setSpinnerText(Spinner spin, String text){
      for(int i = 0; i < spin.getAdapter().getCount(); i++){
         if(spin.getAdapter().getItem(i).toString().contains(text)){
            spin.setSelection(i);
         }
      }
   }
}