package com.example.notesapplicationv20.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.database.NoteDao;
import com.example.notesapplicationv20.database.NoteDatabase;
import com.example.notesapplicationv20.database.SingleNote;

import java.util.List;

public class NoteRepository {

   // this repository class abstracts access to multiple data sources
   // A repository class provides a clean API for data access to the rest of the application
   // the repository manages queries and allows you to use multiple backends. In the most common
   // example it implements the logic for deciding whether to fetch data from a network or use results
   // cashed in a local database.

   private NoteDao noteDao;
   private LiveData<List<ListedNote>> listedNotes;

   public NoteRepository(Application application){
      NoteDatabase db = NoteDatabase.getDatabase(application);
      noteDao = db.noteDao();
      listedNotes = noteDao.listAllNotes();
   }

   // Room executes all queries on a separate thread.
   // Observed LiveData will notify the observer when the data has changed.
   public LiveData<List<ListedNote>> getListedNotes(){
      return listedNotes;
   }

   // You must call these on a non-UI thread or your app will throw an exception. Room ensures
   // that you're not doing any long running operations on the main thread, blocking the UI.

   public void insertSingleNote(SingleNote note){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.insertSingleNote(note);
      });
   }

   public void deleteSingleNote(SingleNote note){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.deleteSingleNote(note);
      });
   }

   public void updateSingleNote(SingleNote note){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.updateSingleNote(note);
      });
   }

   public void deleteMultipleNotes(List<SingleNote> notes){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.deleteMultipleNotes(notes);
      });
   }

   //TODO getNoteById from dao

}
