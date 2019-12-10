package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class WriteNoteActivity extends AppCompatActivity {

   public static final String EXTRA_REPLY = "com.example.notesapplicationv20.REPLY";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_write_note);

      NotesViewModel notesVm = new ViewModelProvider(this).get(NotesViewModel.class);

      Spinner createNoteSubjectSpinner = findViewById(R.id.create_note_subject_spinner);
      EditText createNoteTitleInput = findViewById(R.id.create_note_title_input);
      EditText createNoteDescInput = findViewById(R.id.create_note_desc_input);
      EditText createNoteContentInput = findViewById(R.id.create_note_content_input);

      final Button createNoteButton = findViewById(R.id.create_note_submit);
      createNoteButton.setOnClickListener(view -> {

         // the retrieval of the text should be in this onclick listener because its value is empty
         // when the view is created

         String[]values = new String[4];
         values[0] = createNoteSubjectSpinner.getSelectedItem().toString();
         values[1] = createNoteTitleInput.getText().toString();
         values[2] = createNoteDescInput.getText().toString();
         values[3] = createNoteContentInput.getText().toString();

         boolean go = false;
         Intent replyIntent = new Intent();
         for(String value: values){
            if(value.isEmpty()){
               setResult(RESULT_CANCELED, replyIntent);
               Toast.makeText(getApplicationContext(),"All fields are required!", Toast.LENGTH_LONG).show();
               return;
            }
            else{
               go = true;
            }
         }
         if(go){

            // Calendar.getInstance().getTime()
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeZone(TimeZone.getDefault());

            SingleNote singleNote = new SingleNote(values[0],
                    values[1],
                    values[2],
                    values[3],
                    calendar.getTime(),
                    calendar.getTime()
            );

            notesVm.insertSingleNote(singleNote);

            replyIntent.putExtra(EXTRA_REPLY, "note: " + values[1] + "was added!");
            setResult(RESULT_OK, replyIntent);

         }
         finish();
      });
   }
}
