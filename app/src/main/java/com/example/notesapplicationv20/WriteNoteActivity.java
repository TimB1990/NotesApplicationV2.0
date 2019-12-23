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

   /* set key-name of key: EXTRA_REPLY */
   public static final String EXTRA_REPLY =
           "com.example.notesapplicationv20.WriteNoteActivity.REPLY";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_write_note);

      /* Define new viewModel by setting up a ViewModelProvider for this activity that references
       *  the NotesViewModel class */
      NotesViewModel model = new ViewModelProvider(this).get(NotesViewModel.class);

      /* Define view elements for this activity */
      Spinner createNoteSubjectSpinner = findViewById(R.id.create_note_subject_spinner);
      EditText createNoteTitleInput = findViewById(R.id.create_note_title_input);
      EditText createNoteDescInput = findViewById(R.id.create_note_desc_input);
      EditText createNoteContentInput = findViewById(R.id.create_note_content_input);

      /* Define a submit note button and add an onclick listener */
      final Button submitNoteButton = findViewById(R.id.create_note_submit);
      submitNoteButton.setOnClickListener(view -> {

         /* Fetching user input should be defined inside this onclick listener because their values are empty
          *  when the view is created as long as the user hasn't provide them with input yet.
          *  The text values will be set in an array */

         String[]values = new String[4];
         values[0] = createNoteSubjectSpinner.getSelectedItem().toString();
         values[1] = createNoteTitleInput.getText().toString();
         values[2] = createNoteDescInput.getText().toString();
         values[3] = createNoteContentInput.getText().toString();

         /* Define a boolean with value: false that will be toggled to true when input validation succeeds */
         boolean go = false;

         /* Define a new intent*/
         Intent intent = new Intent();

         /* Loop over the array that contains all user input and check if given values are empty.
          *  Otherwise toggle boolean go to true to continue */
         for(String value: values){

            /* In case one or more values are empty call setResult and pass in RESULT_CANCELED (0) along
             *  with the intent */
            if(value.isEmpty()){
               setResult(RESULT_CANCELED, intent);
               /** When the validation fails show a toast that says: "All fields are required" */
               Toast.makeText(getApplicationContext(),"All fields are required!", Toast.LENGTH_LONG).show();
               return;
            }
            else{
               go = true;
            }
         }

         /* If input validation succeeds create a new SingleNote object and call the viewModel's
          *  insertSingleNote method and pass in single note */
         if(go){

            /* To create an instance of SingleNote this code uses the
             *  first constructor that does not require an id, because
             *  the id will be generated automatically by Room database */
            SingleNote singleNote = new SingleNote(values[0],
                    values[1],
                    values[2],
                    values[3],
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime()
            );

            model.insertSingleNote(singleNote);

            /* After the note is being inserted call the intent.putExtra method and pass in
             *  the key EXTRA_REPLY that contains a confirmation message as its value and call
             *  setResult to confirm resultCode: RESULT_OK along with the intent that is to be called
             *  by the onActivityResult method in MainActivity */
            intent.putExtra(EXTRA_REPLY, "note: " + values[1] + "was added!");
            setResult(RESULT_OK, intent);

         }
         finish();
      });
   }
}
