package com.example.notesapplicationv20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.notesapplicationv20.adapter.NotesAdapter;
import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ViewHolder.ClickListener{

   private static final int NEW_NOTE_REQUEST_CODE = 1;
   private static final String TAG = MainActivity.class.getSimpleName();

   private RecyclerView rvNotes;
   private NotesAdapter adapter;
   private ActionModeCallback actionModeCallback;
   private ActionMode actionMode;
   private NotesViewModel notesVm;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      actionModeCallback = new ActionModeCallback();
      rvNotes = findViewById(R.id.notes_recycler_view);
      adapter = new NotesAdapter(getApplicationContext(),this);
      notesVm = new ViewModelProvider(this).get(NotesViewModel.class);

      // here database is called to pass data in recycler view
      notesVm.getListedNotes().observe(this, new Observer<List<ListedNote>>(){
         @Override
         public void onChanged(List<ListedNote> listedNotes) {
            adapter.setListedNotes(listedNotes);
         }
      });

      rvNotes.setAdapter(adapter);
      rvNotes.setItemAnimator(new DefaultItemAnimator());
      rvNotes.setLayoutManager(new LinearLayoutManager(this));
      //rvNotes.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

      // create button logic
      Button createButton = findViewById(R.id.create_note_button);
      createButton.setOnClickListener(view -> {
         Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
         startActivityForResult(intent, NEW_NOTE_REQUEST_CODE);
      });
   }

   @Override
   public void onItemClicked(int position) {
      if(actionMode != null){
         toggleSelection(position);
      }
      else{
         // adapter.removeItem(position);
         Intent intent = new Intent(MainActivity.this, ReadNoteActivity.class);
         List<ListedNote> listedNotes = adapter.getListedNotes();
         ListedNote lNote = listedNotes.get(position);

         int noteId = lNote.getId();
         // String noteTitle = lNote.getTitle();

         intent.putExtra("noteId", noteId);
         //intent.putExtra("noteTitle", noteTitle);

         startActivity(intent);
      }
   }

   @Override
   public boolean onItemLongClicked(int position) {
      if(actionMode == null){
         actionMode = startActionMode(actionModeCallback);
      }

      toggleSelection(position);
      return true;
   }

   private void toggleSelection(int position){
      adapter.toggleSelection(position);
      int count = adapter.getSelectedItemCount();
      if(count == 0){
         actionMode.finish();
      }
      else{
         actionMode.setTitle(String.valueOf(count));
         actionMode.invalidate();
      }
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

   private class ActionModeCallback implements ActionMode.Callback{
      private final String TAG = ActionModeCallback.class.getSimpleName();

      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
         mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
         return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
         return false;
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
         switch(item.getItemId()){
            case R.id.menu_remove:
               adapter.removeItems(adapter.getSelectedItems());
               // TODO actual remove from database implementation
               Log.d(TAG, "menu_remove");
               mode.finish();
               return true;
            default:
               return false;
         }
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {
         adapter.clearSelection();
         actionMode = null;
      }
   }
}