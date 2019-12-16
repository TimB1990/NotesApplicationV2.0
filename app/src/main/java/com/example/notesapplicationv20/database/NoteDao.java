package com.example.notesapplicationv20.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertSingleNote(SingleNote note);

   @Update(onConflict = OnConflictStrategy.REPLACE)
   void updateSingleNote(SingleNote singleNote);

   @Query("select id, subject, title, description, created_at,last_update from notes")
   LiveData<List<ListedNote>> listAllNotes();

   @Query("select * from notes where id = :id")
   LiveData<SingleNote> getNoteById(int id);

   @Query("delete from notes where id = :id")
   void deleteSingleNoteById(int id);
}

