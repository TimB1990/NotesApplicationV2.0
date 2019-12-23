package com.example.notesapplicationv20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notesapplicationv20.R;
import com.example.notesapplicationv20.database.ListedNote;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** The NotesAdapter class extends SelectableAdapter that defines the logic to make
 *  the items of an recyclerView selectable. The class itself defines the logic
 *  for the recyclerView that is used to display listed notes.
 *  */
public class NotesAdapter extends SelectableAdapter<NotesAdapter.ViewHolder>{

   /* Create instance of Context */
   private Context context;

   /* Create instance of ViewHolder.ClickListener */
   private ViewHolder.ClickListener cl;

   /* Define the layoutInflater and the List-object containing all listed notes */
   private final LayoutInflater mInflater;
   private List<ListedNote> mListedNotes;


   /** Initializes context and ViewHolder.ClickListener in constructor
    * @param context The given context
    * @param cl The instance of ViewHolder.ClickListener to be set */
   public NotesAdapter(Context context, ViewHolder.ClickListener cl) {
      super();
      this.cl = cl;
      this.context = context;
      this.mInflater = LayoutInflater.from(context);
   }

   /** The onCreateViewHolder method is called when RecyclerView needs a new RecyclerView.ViewHolder of the given
    *  type to represent an item. This new ViewHolder should be constructed with a new View that can represent
    *  the items of the given type. You can either create a new View manually or inflate it from an XML layout file.
    *  @param parent ViewGroup: The ViewGroup into which the new View will be added after it is bound to an adapter position.
    *  @param viewType int: The view type of the new View.
    *  @return A new ViewHolder that holds a View of the given view type */
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      /* Take R.layout.note_item(.xml) as single item that holds information about
      *  the specific note that will be displayed in the viewHolder. attachToRoot is false
      *  because we are not responsible for adding the child view manually (item) */

      View itemView = mInflater.inflate(R.layout.note_item, parent, false);
      return new ViewHolder(itemView, cl);
   }

   /** Called by RecyclerView to display the data at the specified position.
    * This method should update the contents of the itemView to reflect the item at the given position.
    * @param holder ViewHolder:  The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
    * @param position int: The position of the item within the adapter's data set.
    * */
   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      /* get listed note from list by parsing position */
      final ListedNote lNote = mListedNotes.get(position);

      /* set the content of the views defined in the ViewHolder subclass */
      holder.id.setText(Integer.toString(lNote.getId()));
      holder.subject.setText(lNote.getSubject());
      holder.title.setText(lNote.getTitle());
      holder.description.setText(lNote.getDescription());
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      holder.createdAt.setText(sdf.format(lNote.getCreatedAt()));
      holder.lastUpdate.setText(sdf.format(lNote.getLastUpdate()));

      /* this code is to be used when using staggeredLayoutManager to align items properly */

//      final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//      if(lp instanceof StaggeredGridLayoutManager.LayoutParams){
//         StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
//         sglp.setFullSpan(lNote.isSelected());
//         holder.itemView.setLayoutParams(sglp);
//      }

      /* When the listed Note (cardView) is selected change its background color */
      holder.cardViewLayout.setBackgroundColor(
              isSelected(position) ? context.getResources().getColor(R.color.selected_overlay) :
                      context.getResources().getColor(R.color.item_standard)
      );
   }

   /** This method will set the listed notes inside this adapter and will notify
    * the data set being changed
    * @param newListedNotes List<ListedNotes>: A list containing the notes to be listed (ListedNote)
    * */
   public void setListedNotes(List<ListedNote> newListedNotes) {
      this.mListedNotes = newListedNotes;
      notifyDataSetChanged();
   }

   /** This method will get all the notes that are listed
    *  @return the list containing listed notes */
   public List<ListedNote> getListedNotes(){
      return this.mListedNotes;
   }

   /** this method implements getItemCount() from RecyclerView.Adapter and returns
    *  an Integer representing the item count
    *  @return mListedNotes.size() if the list that contains notes is not null
    *  otherwise 0 */
   @Override
   public int getItemCount() {
      if (mListedNotes != null) {
         return mListedNotes.size();
      } else {
         return 0;
      }
   }

   /** This method removes a single item from the list containing notes and notifies when
    *  an item is removed
    *  @param position the note's position to be removed */
   public void removeItem(int position){
      mListedNotes.remove(position);
      notifyItemRemoved(position);
   }

   /** This method handles the removal of multiple items
    *  @param positions A list with integer positions of listed items
    *  */
   public void removeItems(List<Integer> positions){
      Collections.sort(positions, new Comparator<Integer>(){
         @Override
         public int compare(Integer lhs, Integer rhs) {
            return rhs - lhs;
         }
      });

      /* split the list in ranges */
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

   /** This method removes a range of items
    * @param positionStart The start position
    * @param itemCount Amount of items being count */
   private void removeRange(int positionStart, int itemCount){
      for(int i = 0; i < itemCount; ++i){
         mListedNotes.remove(positionStart);
      }

      notifyItemRangeRemoved(positionStart,itemCount);
   }



   /** This inner class contains the logic of a listed item inside the RecyclerView
    *  Therefore this class extends RecyclerView.ViewHolder. This class implements
    *  View.OnClickListener and View.onLongClickListener so a single item can perform separate operations,
    *  i.e. being selected on long click and launching the ReadNoteActivity on a single click. */
   public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      /* Define the view elements for single item */
      TextView id, subject, title, description, createdAt, lastUpdate;
      View cardViewLayout;

      /* Define the ClickListener */
      private ClickListener listener;

      /** This constructor initializes the viewHolder of a single item
       *  @param itemView The view that represents the entire viewHolder
       *  @param listener The ClickListener that will be attached to this viewHolder*/
      public ViewHolder(View itemView, ClickListener listener) {
         super(itemView);

         /* Initialize subviews of the ViewHolder's itemView */
         id = itemView.findViewById(R.id.note_id);
         subject = itemView.findViewById(R.id.note_subject_text);
         title = itemView.findViewById(R.id.note_title_text);
         description = itemView.findViewById(R.id.note_description_text);
         createdAt = itemView.findViewById(R.id.note_created_text);
         lastUpdate = itemView.findViewById(R.id.note_updated_text);
         cardViewLayout = itemView.findViewById(R.id.card_view_layout);

         /* Initialize ClickListener listener */
         this.listener = listener;

         /* Attach the onClickListeners to the viewHolder's itemView
         *  the 'this' keywords refer to the implemented onClick() and onLongClick() methods */
         itemView.setOnClickListener(this);
         itemView.setOnLongClickListener(this);
      }

      /** The onClick method checks if the listener is present, if so the listener calls its
       *  onItemClicked method for the current layoutPosition of the item being clicked.
       *  This method implements the View.OnClickListener interface,
       *  listener is a reference to the ClickListener interface inside this class */
      @Override
      public void onClick(View v) {
         if(listener != null){
            listener.onItemClicked(getLayoutPosition());
         }
      }

      /** The onLongClick method checks if the listener is present, if so the listener calls its
       *  onLongItemClicked method for the current layoutPosition of the item being long clicked.
       *  This method implements the View.OnLongClickListener interface,
       *  listener is a reference to the ClickListener interface inside this class */
      @Override
      public boolean onLongClick(View v) {
         if(listener != null){
            return listener.onItemLongClicked(getLayoutPosition());
         }
         return true;
      }

      /** This inner interface contains the methods that will be implemented by MainActivity */
      public interface ClickListener{
         void onItemClicked(int position);
         boolean onItemLongClicked(int position);
      }
   }
}
