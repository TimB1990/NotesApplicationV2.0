package com.example.notesapplicationv20.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.database.NoteDao;
import com.example.notesapplicationv20.database.NoteDatabase;
import com.example.notesapplicationv20.database.SingleNote;

import java.util.List;

/** The NoteRepository class abstracts the access to multiple data sources which provides an
 *  clean API for data access to the rest of the application. In the most common example it implements
 *  the logic for deciding whether to fetch data from a network or use results cached in a database
 *  */
public class NoteRepository {

   private NoteDao noteDao;
   private LiveData<List<ListedNote>> listedNotes;

   /** This constructor initializes an database instance, the dao (data access object) and initializes
    * LiveData containing a List of ListedNote
    * @param application The application to be passed as an argument for the getDatabase method in NoteDatabase
    * */
   public NoteRepository(Application application){
      NoteDatabase db = NoteDatabase.getDatabase(application);
      noteDao = db.noteDao();
      listedNotes = noteDao.listAllNotes();
   }

   /* Room executes all queries on a separate thread, LiveData will notify
    * the observer when the data has changed.
    * */

   /** The getListedNotes returns the listed notes
    * @return a list with listed Notes */
   public LiveData<List<ListedNote>> getListedNotes(){
      return listedNotes;
   }

   /** The getNoteById method gets a SingleNote by its ID
    * @param id the ID of the SingleNote
    * @return a SingleNote instance */
   public LiveData<SingleNote> getNoteById(int id){
      LiveData<SingleNote> singleNote = noteDao.getNoteById(id);
      return singleNote;
   }

   /* You must call these on a non-UI thread or your app will throw an exception.
    * Room ensures
    * that you're not doing any long running operations on the main thread, blocking the UI. */

   /** The insertSingleNote method inserts a SingleNote is the database asynchronously
    * @param note the SingleNote instance to be inserted */
   public void insertSingleNote(SingleNote note){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.insertSingleNote(note);
      });
   }

   /** The deleteSingleNoteById method deletes a SingleNote by id
    *  @param id The ID of the SingleNote instance */
   public void deleteSingleNoteById(int id){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.deleteSingleNoteById(id);
      });
   }

   /** The updateSingleNote method updates a singe note
    *  @param note The updated version of the SingleNote instance */
   public void updateSingleNote(SingleNote note){
      NoteDatabase.databaseWriteExecutor.execute(()->{
         noteDao.updateSingleNote(note);
      });
   }
}
