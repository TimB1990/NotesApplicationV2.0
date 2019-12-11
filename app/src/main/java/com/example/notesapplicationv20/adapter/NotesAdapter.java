package com.example.notesapplicationv20.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplicationv20.R;
import com.example.notesapplicationv20.ReadNoteActivity;
import com.example.notesapplicationv20.database.ListedNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

   private static int selected_position = 0;
   private Context mContext;

   private final LayoutInflater mInflater;
   private List<ListedNote> mListedNotes;

   public NotesAdapter(Context context){
      this.mContext = context;
      mInflater = LayoutInflater.from(context);
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View itemView = mInflater.inflate(R.layout.note_item, parent, false);
      return new ViewHolder(itemView);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      ListedNote lNote = mListedNotes.get(position);

      TextView id = holder.id;
      id.setText(Integer.toString(lNote.getId()));

      TextView subject = holder.subject;
      subject.setText(lNote.getSubject());

      TextView title = holder.title;
      title.setText(lNote.getTitle());

      TextView description = holder.description;
      description.setText(lNote.getDescription());

      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

      TextView createdAt = holder.createdAt;
      createdAt.setText(sdf.format(lNote.getCreatedAt()));

      TextView lastUpdate = holder.lastUpdate;
      lastUpdate.setText(sdf.format(lNote.getLastUpdate()));

      holder.itemView.setBackgroundColor(selected_position == position ? Color.GREEN : Color.TRANSPARENT);

   }

   public void setListedNotes(List<ListedNote> newListedNotes){
      this.mListedNotes = newListedNotes;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      if(mListedNotes != null){
         return mListedNotes.size();
      }
      else{
         return 0;
      }
   }

   public class ViewHolder extends RecyclerView.ViewHolder{

      public TextView id, subject, title, description, createdAt, lastUpdate;

      public ViewHolder(View itemView){
         super(itemView);

         id = itemView.findViewById(R.id.note_id);
         subject = itemView.findViewById(R.id.note_subject_text);
         title = itemView.findViewById(R.id.note_title_text);
         description = itemView.findViewById(R.id.note_description_text);
         createdAt = itemView.findViewById(R.id.note_created_text);
         lastUpdate = itemView.findViewById(R.id.note_updated_text);

         // onclick listener to read note
         itemView.setOnClickListener(v -> {
           Intent intent = new Intent(mContext, ReadNoteActivity.class);
           // TODO Logic for put message extra holding id
            intent.putExtra("noteId", Integer.parseInt(id.getText().toString()));
            mContext.startActivity(intent);
         });
      }

   }
}