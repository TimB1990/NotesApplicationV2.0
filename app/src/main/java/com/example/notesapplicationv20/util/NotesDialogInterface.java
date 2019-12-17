package com.example.notesapplicationv20.util;

public class NotesDialogInterface {

   DialogReturn dialogReturn;

   public interface DialogReturn {

      void onDialogCompleted(boolean answer);
   }

   public void setListener(DialogReturn dialogReturn) {
      this.dialogReturn = dialogReturn;
   }

   public DialogReturn getListener() {
      return dialogReturn;

   }

}
