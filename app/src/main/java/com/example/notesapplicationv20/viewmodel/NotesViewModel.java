package com.example.notesapplicationv20.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notesapplicationv20.database.ListedNote;
import com.example.notesapplicationv20.database.SingleNote;
import com.example.notesapplicationv20.repository.NoteRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

   // a viewModel's role is to provide data to the UI and survive configuration changes, separating
   // your app's UI data from your activity and fragment.

   NoteRepository mRepository;
   LiveData<List<ListedNote>> listedNotes;

   public NotesViewModel(Application application){
      super(application);
      mRepository = new NoteRepository(application);
      listedNotes = mRepository.getListedNotes();
   }

   public LiveData<List<ListedNote>> getListedNotes(){
      return listedNotes;
   }

   public void insertSingleNote(SingleNote singleNote){
      mRepository.insertSingleNote(singleNote);
   }

   public void deleteSingleNote(SingleNote singleNote){
      mRepository.deleteSingleNote(singleNote);
   }

   public void deleteMultipleNotes(List<SingleNote> notes){
      mRepository.deleteMultipleNotes(notes);
   }

   public void updateSingleNote(SingleNote singleNote){
      mRepository.updateSingleNote(singleNote);
   }
}
