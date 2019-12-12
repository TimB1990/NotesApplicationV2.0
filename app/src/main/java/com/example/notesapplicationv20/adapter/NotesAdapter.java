package com.example.notesapplicationv20.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notesapplicationv20.R;
import com.example.notesapplicationv20.database.ListedNote;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class NotesAdapter extends SelectableAdapter<NotesAdapter.ViewHolder>{


   private final LayoutInflater mInflater;
   private List<ListedNote> mListedNotes;

   public NotesAdapter(Context context) {
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

      holder.id.setText(Integer.toString(lNote.getId()));
      holder.subject.setText(lNote.getSubject());
      holder.title.setText(lNote.getTitle());
      holder.description.setText(lNote.getDescription());
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      holder.createdAt.setText(sdf.format(lNote.getCreatedAt()));
      holder.lastUpdate.setText(sdf.format(lNote.getLastUpdate()));

   }

   // set listed notes
   public void setListedNotes(List<ListedNote> newListedNotes) {
      this.mListedNotes = newListedNotes;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      if (mListedNotes != null) {
         return mListedNotes.size();
      } else {
         return 0;
      }
   }

   public class ViewHolder extends RecyclerView.ViewHolder{

      public TextView id, subject, title, description, createdAt, lastUpdate;
      public LinearLayout itemLayout;


      public ViewHolder(View itemView) {
         super(itemView);

         itemLayout = itemView.findViewById(R.id.item_layout);
         id = itemView.findViewById(R.id.note_id);
         subject = itemView.findViewById(R.id.note_subject_text);
         title = itemView.findViewById(R.id.note_title_text);
         description = itemView.findViewById(R.id.note_description_text);
         createdAt = itemView.findViewById(R.id.note_created_text);
         lastUpdate = itemView.findViewById(R.id.note_updated_text);

      }
   }
}
