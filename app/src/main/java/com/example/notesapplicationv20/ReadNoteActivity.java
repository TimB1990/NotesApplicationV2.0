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

public class ReadNoteActivity extends AppCompatActivity {

   public static final int UPDATED_NOTE_CODE = 1;

   private NotesViewModel model;
   TextView readItemSubject, readItemTitle, readItemId, readItemContent;
   SingleNote singleNote;
   ImageButton editButton;

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

      /**/
      final Observer<SingleNote> singleNoteObserver = new Observer<SingleNote>(){
         @Override
         public void onChanged(SingleNote singleNote) {
            // set SingleNote
            setSingleNote(singleNote);
            // update the UI
            readItemSubject.setText(singleNote.getSubject());
            readItemId.setText(String.valueOf(singleNote.getId()));
            readItemTitle.setText(singleNote.getTitle());
            readItemContent.setText(singleNote.getContent());
         }
      };

      model.getNoteById(noteId).observe(this, singleNoteObserver);

      editButton.setOnClickListener(v -> {
         Intent intent1 = new Intent(ReadNoteActivity.this, UpdateNoteActivity.class);
         /* put serializable singleNote to be passed over to UpdateNoteActivity*/
         intent1.putExtra("singleNote", singleNote);
         startActivity(intent1);
      });
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode, resultCode, data);

      if(requestCode == UPDATED_NOTE_CODE && resultCode == RESULT_OK){
         Toast.makeText(getApplicationContext(),
                 data.getStringExtra(
                         WriteNoteActivity.EXTRA_REPLY
                 ),
                 Toast.LENGTH_SHORT).show();
      }
   }

   private void setSingleNote(SingleNote singleNote){
      this.singleNote = singleNote;
   }
}

