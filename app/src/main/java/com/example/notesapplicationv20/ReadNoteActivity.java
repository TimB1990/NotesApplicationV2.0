package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notesapplicationv20.viewmodel.NotesViewModel;

public class ReadNoteActivity extends AppCompatActivity {

   private NotesViewModel notesVm;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read_note);

      Intent intent = getIntent();
      int noteId = intent.getIntExtra("noteId",0);
   }
}
