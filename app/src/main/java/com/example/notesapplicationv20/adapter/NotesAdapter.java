package com.example.notesapplicationv20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.notesapplicationv20.R;
import com.example.notesapplicationv20.database.ListedNote;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesAdapter extends SelectableAdapter<NotesAdapter.ViewHolder>{

   private Context context;

   private ViewHolder.ClickListener cl;

   private final LayoutInflater mInflater;
   private List<ListedNote> mListedNotes;


   public NotesAdapter(Context context, ViewHolder.ClickListener cl) {
      super();
      this.cl = cl;
      this.context = context;
      this.mInflater = LayoutInflater.from(context);
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      // final int layout = viewType == TYPE_INACTIVE ? R.layout.note_item : R.layout.note_item_active;

      View itemView = mInflater.inflate(R.layout.note_item, parent, false);
      return new ViewHolder(itemView, cl);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      final ListedNote lNote = mListedNotes.get(position);

      holder.id.setText(Integer.toString(lNote.getId()));
      holder.subject.setText(lNote.getSubject());
      holder.title.setText(lNote.getTitle());
      holder.description.setText(lNote.getDescription());
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      holder.createdAt.setText(sdf.format(lNote.getCreatedAt()));
      holder.lastUpdate.setText(sdf.format(lNote.getLastUpdate()));

      // span the item if active, final can only be assigned once
      final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
      if(lp instanceof StaggeredGridLayoutManager.LayoutParams){
         StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
         sglp.setFullSpan(lNote.isSelected());
         holder.itemView.setLayoutParams(sglp);
      }

      // highlight the item if it is selected
      // holder.itemLayout.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
      holder.cardViewLayout.setBackgroundColor(
              isSelected(position) ? context.getResources().getColor(R.color.selected_overlay) :
                      context.getResources().getColor(R.color.item_standard)
      );
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

   public void removeItem(int position){
      mListedNotes.remove(position);
      notifyItemRemoved(position);
   }

   public void removeItems(List<Integer> positions){
      Collections.sort(positions, new Comparator<Integer>(){
         @Override
         public int compare(Integer lhs, Integer rhs) {
            return rhs - lhs;
         }
      });

      // split the list in ranges
      while (!positions.isEmpty()) {
         if (positions.size() == 1) {
            removeItem(positions.get(0));
            positions.remove(0);
         } else {
            int count = 1;
            while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
               ++count;
            }

            if (count == 1) {
               removeItem(positions.get(0));
            } else {
               removeRange(positions.get(count - 1), count);
            }

            for (int i = 0; i < count; ++i) {
               positions.remove(0);
            }
         }
      }
   }

   private void removeRange(int positionStart, int itemCount){
      for(int i = 0; i < itemCount; ++i){
         mListedNotes.remove(positionStart);
      }

      notifyItemRangeRemoved(positionStart,itemCount);
   }



   public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      private static final String TAG = ViewHolder.class.getSimpleName();
      TextView id, subject, title, description, createdAt, lastUpdate;
      View cardViewLayout;

      // part of listener
      private ClickListener listener;

      public ViewHolder(View itemView, ClickListener listener) {
         super(itemView);

         id = itemView.findViewById(R.id.note_id);
         subject = itemView.findViewById(R.id.note_subject_text);
         title = itemView.findViewById(R.id.note_title_text);
         description = itemView.findViewById(R.id.note_description_text);
         createdAt = itemView.findViewById(R.id.note_created_text);
         lastUpdate = itemView.findViewById(R.id.note_updated_text);
         cardViewLayout = itemView.findViewById(R.id.card_view_layout);

         this.listener = listener;

         itemView.setOnClickListener(this);
         itemView.setOnLongClickListener(this);
      }

      // logic for exposing listener passed to adapter
      @Override
      public void onClick(View v) {
         if(listener != null){
            listener.onItemClicked(getLayoutPosition());
         }
      }

      @Override
      public boolean onLongClick(View v) {
         if(listener != null){
            return listener.onItemLongClicked(getLayoutPosition());
         }

         return true;
      }

      public interface ClickListener{
         void onItemClicked(int position);
         boolean onItemLongClicked(int position);
      }

   }
}
