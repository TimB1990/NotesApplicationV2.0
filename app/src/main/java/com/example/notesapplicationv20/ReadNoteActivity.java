package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

public class ReadNoteActivity extends AppCompatActivity {

   private NotesViewModel notesVm;
   TextView readItemTitle, readItemId, readItemContent;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read_note);

      // define TextViews
      readItemTitle = findViewById(R.id.read_item_title);
      readItemId = findViewById(R.id.read_item_id);
      readItemContent = findViewById(R.id.read_item_content);
   }
}
