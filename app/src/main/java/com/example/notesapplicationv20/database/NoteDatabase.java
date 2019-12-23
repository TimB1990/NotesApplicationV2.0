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

/** The NoteDatabase class is a subclass of RoomDatabase and contains the setup of Room */
public abstract class NoteDatabase extends RoomDatabase {

   /* create instance of NoteDatabase. The volatile keyword means that multiple threads can use and modify
   * an instance of this class at the same time without any problem. This means that an instance of this class
   * is processed asynchronously  */
   private static volatile NoteDatabase INSTANCE;
   public abstract NoteDao noteDao();
   private static final int NUMBER_OF_THREADS = 4;

   /* Define an databaseWriteExecutor so writing data to the database is done asynchronously
   *  This ExecutorService uses a fixedThreadPool of 4 threads that can run on top of each other.
   * */
   public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

   /** The getDatabase method will create or reuse a new database instance when requested.
    * @param context the context that will be taken as an argument by the Room.databaseBuilder() method.
    * @return an instance of this database class.
    * */
   public static NoteDatabase getDatabase(final Context context){
      if(INSTANCE == null){
         /* the synchronized keyword in java
          * is used to provide mutually exclusive access to a resource with multiple threads
          * we defined a singleton NoteDatabase to prevent having multiple instances of the database opened
          * at the same time
         */
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
