package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;
/** The ReadNoteActivity is responsible for allowing the user to read a note */
public class ReadNoteActivity extends AppCompatActivity {

   public static final int UPDATED_NOTE_CODE = 1;

   private NotesViewModel model;
   TextView readItemSubject, readItemTitle, readItemId, readItemContent;
   SingleNote singleNote;
   ImageButton editButton;

   /** The onCreate method defines logic when the Activity is created */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read_note);

      /* Get intent data passed by main activity */
      Intent intent = getIntent();
      int noteId = intent.getIntExtra("noteId",0);

      /* Define text views */
      readItemSubject = findViewById(R.id.read_item_subject);
      readItemTitle = findViewById(R.id.read_item_title);
      readItemId = findViewById(R.id.read_item_id);
      readItemContent = findViewById(R.id.read_item_content);
      editButton = findViewById(R.id.read_note_edit_btn);

      /* Initialize viewModel */
      model = new ViewModelProvider(this).get(NotesViewModel.class);

      /* Define an observer to retrieve singleNote asynchronously,
       * initialize singleNote class variable (using setSingleNote),
       * and set data of singleNote as text input in UI */
      final Observer<SingleNote> singleNoteObserver = singleNote -> {
         setSingleNote(singleNote);
         readItemSubject.setText(singleNote.getSubject());
         readItemId.setText(String.valueOf(singleNote.getId()));
         readItemTitle.setText(singleNote.getTitle());
         readItemContent.setText(singleNote.getContent());
      };

      /* call getNoteById function and use retrieved noteId from intent as its argument,
      *  notice the getNoteById method returns LiveData of SingleNote (LiveData<SingleNote> so
      *  the observe method needs to be called that takes the owner (this class) and the recently
      *  defined singleNoteObserver as arguments */
      model.getNoteById(noteId).observe(this, singleNoteObserver);

      /* Define the OnClickListener for the edit button that sends over a serializable singleNote
      *  via intent to the UpdateNoteActivity class */
      editButton.setOnClickListener(v -> {
         Intent intent1 = new Intent(ReadNoteActivity.this, UpdateNoteActivity.class);
         /* put serializable singleNote to be passed over to UpdateNoteActivity*/
         intent1.putExtra("singleNote", singleNote);
         startActivity(intent1);
      });
   }

   /** This method is defined to receive a result back from UpdateNoteActivity
    * @param requestCode The requestCode that is passed along
    * @param resultCode The resultCode to be RESULT_OK or RESULT_CANCELED
    * @param data The data that is passed along with the intent from UpdateNoteActivity
    * */
   public void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode, resultCode, data);

      /* if the requestCode equals UPDATED_NOTE_CODE (1) and resultCode equals RESULT_OK,
      *  create a toast that displays the data being send over as EXTRA_REPLY from UpdateNoteActivity */
      if(requestCode == UPDATED_NOTE_CODE && resultCode == RESULT_OK){
         Toast.makeText(getApplicationContext(),
                 data.getStringExtra(
                         WriteNoteActivity.EXTRA_REPLY
                 ),
                 Toast.LENGTH_SHORT).show();
      }
   }

   /** This method will initialize the global singleNote class variable
    *  @param singleNote The SingleNote instance to be set */
   private void setSingleNote(SingleNote singleNote){
      this.singleNote = singleNote;
   }
}

