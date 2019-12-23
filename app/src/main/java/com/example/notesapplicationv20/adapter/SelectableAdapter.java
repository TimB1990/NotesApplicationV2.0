package com.example.notesapplicationv20.adapter;

import android.util.SparseBooleanArray;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** The SelectableAdapter class is subclassed by notes adapter.
 *  <VH extends RecyclerView.ViewHolder> means that it is assumed we have
 *   a class which requires a type to use across its class declaration.
 *   In this case it needs to be a subtype of RecyclerView.ViewHolder.
 *   Also this class needs to extend RecyclerView.Adapter, that should be one of the generic type we defined
 *   (VH) i.e. an RecyclerView.ViewHolder superclass.
 * @Extends generic type VH that should be some class subclass of RecyclerView.ViewHolder
 * @Extends RecyclerView.Adapter<VH>
 *   */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

   /* a SparseBooleanArray of selectedItems. A SpareBooleanArray maps integers to booleans
   *  Unlike a normal array of booleans there can be gaps in the indices.
   *  It is intended to be more memory efficient than using a HashMap to map Integers to Booleans
   *  both because it avoids auto-boxing keys and values and its data structure doesn't rely on
   *  an extra entry object for each mapping.
   * */

   private SparseBooleanArray selectedItems;

   public SelectableAdapter() {
      selectedItems = new SparseBooleanArray();
   }

   /**
    * Indicates if the item at position position is selected
    * @param position Position of the item to check
    * @return true if the item is selected, false otherwise
    */
   public boolean isSelected(int position) {
      return getSelectedItems().contains(position);
   }

   /**
    * Toggle the selection status of the item at a given position
    * @param position Position of the item to toggle the selection status for
    */
   public void toggleSelection(int position) {
      if (selectedItems.get(position, false)) {
         selectedItems.delete(position);
      } else {
         selectedItems.put(position, true);
      }
      notifyItemChanged(position);
   }

   /**
    * Clear the selection status for all items
    */
   public void clearSelection() {
      List<Integer> selection = getSelectedItems();
      selectedItems.clear();
      for (Integer i : selection) {
         notifyItemChanged(i);
      }
   }

   /**
    * Count the selected items
    * @return Selected items count
    */
   public int getSelectedItemCount() {
      return selectedItems.size();
   }

   /**
    * Indicates the list of selected items
    * @return List of selected items ids
    */
   public List<Integer> getSelectedItems() {
      List<Integer> items = new ArrayList<>(selectedItems.size());
      for (int i = 0; i < selectedItems.size(); ++i) {
         items.add(selectedItems.keyAt(i));
      }
      return items;
   }
}
