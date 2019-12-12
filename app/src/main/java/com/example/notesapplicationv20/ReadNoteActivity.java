package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

public class ReadNoteActivity extends AppCompatActivity {

   private NotesViewModel model;
   TextView readItemTitle, readItemId, readItemContent;
   SingleNote singleNote;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read_note);

      // get intent data passed over
      Intent intent = getIntent();
      int noteId = intent.getIntExtra("noteId",0);

      // define TextViews
      readItemTitle = findViewById(R.id.read_item_title);
      readItemId = findViewById(R.id.read_item_id);
      readItemContent = findViewById(R.id.read_item_content);

      // initialize viewModel
      model = new ViewModelProvider(this).get(NotesViewModel.class);

      final Observer<SingleNote> singleNoteObserver = new Observer<SingleNote>(){
         @Override
         public void onChanged(SingleNote singleNote) {
            // update the UI
            readItemId.setText(String.valueOf(singleNote.getId()));
            readItemTitle.setText(singleNote.getTitle());
            readItemContent.setText(singleNote.getContent());
         }
      };
      model.getNoteById(noteId).observe(this, singleNoteObserver);
   }
}
