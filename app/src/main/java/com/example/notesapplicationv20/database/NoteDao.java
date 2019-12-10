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

   @Delete
   void deleteSingleNote(SingleNote note);

   @Delete
   void deleteMultipleNotes(List<SingleNote> notes);

   @Update
   void updateSingleNote(SingleNote note);

   @Query("select id, subject, title, description, created_at,last_update from notes")
   LiveData<List<ListedNote>> listAllNotes();

   @Query("select * from notes where id = :id")
   LiveData<SingleNote> getNoteById(int id);
}

/*
 @PrimaryKey(autoGenerate = true)
   private int id;

   @ColumnInfo(name="subject")
   private String subject;

   @ColumnInfo(name="title")
   private String title;

   @ColumnInfo(name="description")
   private String description;

   @ColumnInfo(name="created_at")
   private Date createdAt;

   @ColumnInfo(name="last_update")
   private Date lastUpdate;
* */
