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

   public static final String EXTRA_REPLY =
           "com.example.notesApplicationV20.UpdateNoteActivity.REPLY";

   /* Define views and viewModel */
   Spinner subjectSpinner;
   EditText updateTitle, updateDescription, updateContent;
   private NotesViewModel model;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_note);

      /* Define serializable single note passed over by ReadNoteActivity */
      SingleNote singleNote = (SingleNote)getIntent().getSerializableExtra("singleNote");

      /* Define new viewModel by setting up a ViewModelProvider for this activity that references
       * the NotesViewModel class */
      model = new ViewModelProvider(this).get(NotesViewModel.class);

      /* Initialize the views that belong to this activity */
      subjectSpinner = findViewById(R.id.update_note_subject_spinner);
      updateTitle = findViewById(R.id.update_note_title_input);
      updateDescription = findViewById(R.id.update_note_desc_input);
      updateContent = findViewById(R.id.update_note_cnt_input);

      /* Call the setSpinnerText method and set spinner text to the subject
      as defined in SingleNote */
      setSpinnerText(subjectSpinner, singleNote.getSubject());

      /* Define the content of the text views along with singleNote's properties */
      updateTitle.setText(singleNote.getTitle());
      updateDescription.setText(singleNote.getDescription());
      updateContent.setText(singleNote.getContent());

      /* Define update note button and corresponding onclick listener */
      final Button updateNoteBtn = findViewById(R.id.update_note_submit);
      updateNoteBtn.setOnClickListener(v -> {

         /* Define string array that will contain given input */
         String[] values = new String[4];
         values[0] = subjectSpinner.getSelectedItem().toString();
         values[1] = updateTitle.getText().toString();
         values[2] = updateDescription.getText().toString();
         values[3] = updateContent.getText().toString();

         /* Extract createdAt from singleNote */
         Date createdAt = singleNote.getCreatedAt();

         /* Define a boolean with value: false that will be toggled to true when input validation succeeds */
         Boolean go = false;

         /* Define a new intent */
         Intent intent = new Intent();

         /* Loop over the array containing input values */
         for(String value: values){

            /* If any value is empty, set result code RESULT_CANCELED, pass the intent and create toast
            *  that notifies "all fields required", otherwise toggle boolean to true */
            if(value.isEmpty()){
               setResult(RESULT_CANCELED, intent);
               Toast.makeText(getApplicationContext(),"All fields are required!", Toast.LENGTH_LONG).show();
               return;
            }
            else{
               go = true;
            }
         }


         /* If input validation succeeds create a new SingleNote object and call the viewModel's
          *  updateSingleNote method and pass in single note */
         if(go){

            /* To create an instance of SingleNote this code uses the
             *  second constructor that requires an id, because
             *  Room database needs to know which note is to be updated */

            SingleNote updatedSingleNote = new SingleNote(singleNote.getId(),
                    values[0],
                    values[1],
                    values[2],
                    values[3],
                    createdAt,
                    Calendar.getInstance().getTime());

            model.updateSingleNote(updatedSingleNote);

            /* After the note is being inserted call the intent.putExtra method and pass in
             *  the key EXTRA_REPLY that contains a confirmation message as its value and call
             *  setResult to confirm resultCode: RESULT_OK along with the intent that is to be called
             *  by the onActivityResult method in ReadNoteActivity */

            intent.putExtra(EXTRA_REPLY, "note: " + singleNote.getId() + "was updated!");
            setResult(RESULT_OK, intent);
         }
         finish();
      });
   }


   /** this method sets the content of spinner along with given text.
    *  @param text the text describing the option within the spinner that should be shown.
    *  @param spinner the spinner object that is validated.
    *  */
   private void setSpinnerText(Spinner spinner, String text){
      for(int i = 0; i < spinner.getAdapter().getCount(); i++){
         if(spinner.getAdapter().getItem(i).toString().contains(text)){
            spinner.setSelection(i);
         }
      }
   }
}