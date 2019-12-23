package com.example.notesapplicationv20.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.repository.NoteRepository;

import java.util.List;

/** The NotesViewModel class has the role to provide data to the UI and survive configuration changes
 *  by separating the UI from the activity, it extends the AndroidViewModel class. This class is called
 *  to communicate with the repository defining the queries to be executed. */
public class NotesViewModel extends AndroidViewModel {

   NoteRepository mRepository;
   LiveData<List<ListedNote>> listedNotes;

   /** The NotesViewModel constructor initializes the repository and will initialize the listedNotes class
    *  variable
    *  @param application The application to be used as an argument when creating a new NoteRepository instance
    *  */
   public NotesViewModel(Application application){
      super(application);
      mRepository = new NoteRepository(application);
      listedNotes = mRepository.getListedNotes();
   }

   /** Get listed Notes
    * @return A list with listed notes */
   public LiveData<List<ListedNote>> getListedNotes(){
      return listedNotes;
   }

   /** Insert a single note
    * @param singleNote The SingleNote instance that will be inserted */
   public void insertSingleNote(SingleNote singleNote){
      mRepository.insertSingleNote(singleNote);
   }

   /** Delete a single note by its ID
    * @param id The id of the SingleNote to be deleted */
   public void deleteSingleNoteById(int id){
      mRepository.deleteSingleNoteById(id);
   }

   /** Update a single note
    * @param singleNote The updated instance of SingeNote*/
   public void updateSingleNote(SingleNote singleNote){
      mRepository.updateSingleNote(singleNote);
   }

   /** Get a single note by its id
    * @param id The ID of the SingleNote instance to retrieve
    * @return The updated SingleNote instance */
   public LiveData<SingleNote> getNoteById(int id){
      LiveData<SingleNote> singleNote = mRepository.getNoteById(id);
      return singleNote;
   }
}
