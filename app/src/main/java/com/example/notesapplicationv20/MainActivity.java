package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.notesapplicationv20.adapter.NotesAdapter;
import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

   private static final int NEW_NOTE_REQUEST_CODE = 1;

   private RecyclerView rvNotes;
   private NotesAdapter adapter;
   private NotesViewModel notesVm;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      rvNotes = findViewById(R.id.notes_recycler_view);

      adapter = new NotesAdapter(this);
      adapter.setOnItemClickListener(new NotesAdapter.ClickListener() {
         @Override
         public void onItemClick(int position, View v) {
            String text = "onItemClick: " + position;
            Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
         }

         @Override
         public void onItemLongClick(int position, View v) {
            String text = "onItemLongClick: " + position;
            Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
         }
      });

      notesVm = new ViewModelProvider(this).get(NotesViewModel.class);
      notesVm.getListedNotes().observe(this, new Observer<List<ListedNote>>(){
         @Override
         public void onChanged(List<ListedNote> listedNotes) {
            adapter.setListedNotes(listedNotes);
         }
      });

      rvNotes.setAdapter(adapter);
      rvNotes.setLayoutManager(new LinearLayoutManager(this));

      // create button logic
      Button createButton = findViewById(R.id.create_note_button);
      createButton.setOnClickListener(view -> {
         Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
         startActivityForResult(intent, NEW_NOTE_REQUEST_CODE);
      });
   }

   public void onActivityResult(int requestCode, int resultCode, Intent data){
      super.onActivityResult(requestCode, resultCode, data);

      if(requestCode == NEW_NOTE_REQUEST_CODE && resultCode == RESULT_OK){
         Toast.makeText(getApplicationContext(),
                 data.getStringExtra(
                         WriteNoteActivity.EXTRA_REPLY
                 ),
                 Toast.LENGTH_SHORT).show();
      }
   }
}
