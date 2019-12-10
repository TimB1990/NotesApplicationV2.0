package com.example.notesapplicationv20.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.notesapplicationv20.util.DateConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SingleNote.class}, version =1, exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class NoteDatabase extends RoomDatabase {

   private static volatile NoteDatabase INSTANCE;
   public abstract NoteDao noteDao();
   private static final int NUMBER_OF_THREADS = 4;
   public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

   public static NoteDatabase getDatabase(final Context context){
      if(INSTANCE == null){
         // the synchronized keyword in java
         // is used to provide mutually exclusive access to a resource with multiple threads
         // we defined a singleton NoteDatabase to prevent having multiple instances of the database opened
         // at the same time
         synchronized (NoteDatabase.class){
            if (INSTANCE == null){
               INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                       NoteDatabase.class, "notes_database").build();
            }
         }
      }

      return INSTANCE;
   }

}
