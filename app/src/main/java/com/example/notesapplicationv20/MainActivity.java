package com.example.notesapplicationv20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notesapplicationv20.adapter.NotesAdapter;
import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ViewHolder.ClickListener {

   public static final int NEW_NOTE_REQUEST_CODE = 1;

   private RecyclerView rvNotes;
   private NotesAdapter adapter;
   private ActionModeCallback actionModeCallback;
   private ActionMode actionMode;
   private NotesViewModel notesVm;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);


      /** set actionModeCallback */
      actionModeCallback = new ActionModeCallback();

      /** define recycler view, recycler-view-adapter and viewModel */
      rvNotes = findViewById(R.id.notes_recycler_view);
      adapter = new NotesAdapter(getApplicationContext(), this);
      notesVm = new ViewModelProvider(this).get(NotesViewModel.class);

      /** observe listedNotes to be set in adapter (async LiveData) */
      notesVm.getListedNotes().observe(this, listedNotes -> adapter.setListedNotes(listedNotes));

      /** set adapter, itemAnimator and new LinearLayoutManager to recycler-view */
      rvNotes.setAdapter(adapter);
      rvNotes.setItemAnimator(new DefaultItemAnimator());
      rvNotes.setLayoutManager(new LinearLayoutManager(this));
      //rvNotes.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

      /** define createButton and its OnClickActionListener */
      FloatingActionButton createButton = findViewById(R.id.create_note_button);
      createButton.setOnClickListener(view -> {
         /** pass intent and request-code to redirect user to WriteNoteActivity
          *  send over the requestCode in order to receive a result from WriteNoteActivity back */
         Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
         startActivityForResult(intent, NEW_NOTE_REQUEST_CODE);
      });
   }

   /** This method is defined to receive a result back from WriteNoteActivity */
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      /** Check if the requestCode we're responding to equals NEW_NOTE_REQUEST_CODE and if
       *  this request was successful */
      if (requestCode == NEW_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

         /** create a toast (small popup) that shows up the reply given by WriteNoteActivity
          *  for a short time */
         Toast.makeText(getApplicationContext(),
                 data.getStringExtra(
                         WriteNoteActivity.EXTRA_REPLY
                 ),
                 Toast.LENGTH_SHORT).show();
      }
   }

   /**
    * This method implements onItemClicked in interface ClickListener in NotesAdapter.ViewHolder
    * */
   @Override
   public void onItemClicked(int position) {
      /** if actionMode not null toggle on selection (single click) for the item clicked on current position
       *  by calling the toggleSelection method of this activity-class */
      if (actionMode != null) {
         toggleSelection(position);
      } else {
         /** otherwise create new Intent for ReadNoteActivity, get listed notes from adapter
          * and get note on current position */
         // adapter.removeItem(position);
         Intent intent = new Intent(MainActivity.this, ReadNoteActivity.class);
         List<ListedNote> listedNotes = adapter.getListedNotes();
         ListedNote lNote = listedNotes.get(position);

         /** get the id of current note and pass it along with the intent to ReadNoteActivity */
         int noteId = lNote.getId();
         intent.putExtra("noteId", noteId);
         startActivity(intent);
      }
   }

   /** This method implements onItemLongClicked in interface ClickListener in NotesAdapter.ViewHolder */
   @Override
   public boolean onItemLongClicked(int position) {

      /** if actionMode not null start ActionMode by calling instance of ActionModeCallback defined as
       *  an inner class in this activity-class */

      if (actionMode == null) {
         actionMode = startActionMode(actionModeCallback);
      }

      /** toggle on selection (on long click) for item on current position */
      toggleSelection(position);

      /** return true so the onLongClick operation isn't followed by a single click operation */
      return true;
   }

   /** this method calls the toggleSelection-method defined in the SelectableAdapter-class
    * extended by NotesAdapter-class. This method takes the item's position as an argument */
   private void toggleSelection(int position) {
      adapter.toggleSelection(position);
      /** Get the selectedItemCount from SelectableAdapter */
      int count = adapter.getSelectedItemCount();

      /** if count is 0 finish actionMode, otherwise set value of count as actionMode title
       *  to display the number of items being selected */
      if (count == 0) {
         actionMode.finish();
      } else {
         actionMode.setTitle(String.valueOf(count));
         actionMode.invalidate();
      }
   }

   /** This inner class is responsible for handling the actionMode,
    *  therefore it implements ActionMode.Callback */
   private class ActionModeCallback implements ActionMode.Callback {

      /** The onCreateActionMode overrides the onCreateActionMode method
       * in Callback.ActionMode and defines the logic when ActionMode is created */
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
         /** this method assigns an inflater to inflate the menu containing
          *  the menu-items to represent actions to be performed in action mode (for example delete)
          *  */
         mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
         return true;
      }

      /** In this case no specific preparations need to be done in order to perform any actions */
      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
         return false;
      }

      /**
       * This method handles the actions to be performed when a menu item is clicked in action mode
       * */
      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

         /** If the id of the menu item clicked is menu_remove set up an AlertDialog to verify
          *  the removal of the selected notes */
         if (item.getItemId() == R.id.menu_remove) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.alert_delete_message);
            builder.setIcon(R.drawable.ic_dialog_alert);

            /** set up 'yes' button and setup listener that will dismiss the dialog and calls
             *  a method to remove the selected notes, both from the selected items list and database,
             *  and finishes the selection mode */
            builder.setPositiveButton(R.string.alert_yes_button, (dialog, id) -> {
               dialog.dismiss();
               /** this method will be called to remove selected notes */
               deleteSelectedListItems(adapter, notesVm);
               mode.finish();
            });
            /** Set up 'no' button and setup listener that will simply dismiss the dialog and finishes
             *  the action mode i.e. nothing will happen */
            builder.setNegativeButton(R.string.alert_no_Button, (dialog, id) -> {
               dialog.dismiss();
               mode.finish();
            });

            /** Create the AlertDialog and have it showed. Understand that in this situation the listener logic
             *  is embedded at forehand. Only when the user clicks the button its logic will be executed.
             *  Therefore the 'yes' and 'no' buttons holding the listener need to be defined first */
            AlertDialog alert = builder.create();
            alert.show();
            return true;
         }
         return false;
      }

      /** This method is called when the user exits the action mode the selection is cleared
       *  and actionMode is set to null */
      @Override
      public void onDestroyActionMode(ActionMode mode) {
         adapter.clearSelection();
         actionMode = null;
      }

      /** This method performs the logic when the user verified to delete selected notes */
      void deleteSelectedListItems(NotesAdapter adapter, NotesViewModel notesViewModel) {

         /** Get an integer list containing the positions of selected items from adapter */
         List<Integer> selectedItemPositions = adapter.getSelectedItems();

         /** Get the list holding all listed notes from adapter */
         List<ListedNote> allNotesList = adapter.getListedNotes();

         /** Loop over the selectedItemPositions list */
         for (Integer selectedItemPosition : selectedItemPositions) {

            /** The selectedItemPositions-list is generated from the list containing all notes.
             *  Therefore the indices of both lists are corresponding. All ids of the selected notes
             *  can be retrieved by setting the selected item position as the index to be retrieved
             *  and by calling the getId() method */
            int id = allNotesList.get(selectedItemPosition).getId();

            /** Call the deleteSingleNoteById method from the viewModel and use id as an argument */
            notesViewModel.deleteSingleNoteById(id);
         }

         /** Remove the items listed in the adapter of the recyclerview */
         adapter.removeItems(adapter.getSelectedItems());
      }
   }
}


