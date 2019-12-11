package com.example.notesapplicationv20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notesapplicationv20.R;
import com.example.notesapplicationv20.database.ListedNote;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{


   private Context mContext;
   private static ClickListener clickListener;

   private final LayoutInflater mInflater;
   private List<ListedNote> mListedNotes;

   MyCallback myCallback;

   public interface MyCallback{
      void listenerMethod(String textViewValue);
   }

   public NotesAdapter(Context context) {
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

   public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {

      public TextView id, subject, title, description, createdAt, lastUpdate;
      public LinearLayout itemLayout;


      public ViewHolder(View itemView) {
         super(itemView);

         itemView.setOnClickListener(this);
         itemView.setOnLongClickListener(this);

         itemLayout = itemView.findViewById(R.id.item_layout);
         id = itemView.findViewById(R.id.note_id);
         subject = itemView.findViewById(R.id.note_subject_text);
         title = itemView.findViewById(R.id.note_title_text);
         description = itemView.findViewById(R.id.note_description_text);
         createdAt = itemView.findViewById(R.id.note_created_text);
         lastUpdate = itemView.findViewById(R.id.note_updated_text);
      }

      @Override
      public void onClick(View v) {
         clickListener.onItemClick(getAdapterPosition(), v);
      }

      @Override
      public boolean onLongClick(View v) {
         clickListener.onItemLongClick(getAdapterPosition(),v);
         return true;
      }
   }

   public void setOnItemClickListener(ClickListener clickListener){
      NotesAdapter.clickListener = clickListener;
   }

   public interface ClickListener{
      void onItemClick(int position, View v);
      void onItemLongClick(int position, View v);
   }
}
