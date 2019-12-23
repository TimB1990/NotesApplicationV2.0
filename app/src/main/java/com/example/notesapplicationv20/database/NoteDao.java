package com.example.notesapplicationv20.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

/** The NoteDao interface contains the queries to retrieve from, and write data to room database */
@Dao
public interface NoteDao {

   /** Insert a single note */
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertSingleNote(SingleNote note);

   /** Update a single note*/
   @Update(onConflict = OnConflictStrategy.REPLACE)
   void updateSingleNote(SingleNote singleNote);

   /** List all Notes */
   @Query("select id, subject, title, description, created_at,last_update from notes")
   LiveData<List<ListedNote>> listAllNotes();

   /** Get Note by ID*/
   @Query("select * from notes where id = :id")
   LiveData<SingleNote> getNoteById(int id);

   /** Delete single note by ID */
   @Query("delete from notes where id = :id")
   void deleteSingleNoteById(int id);
}

