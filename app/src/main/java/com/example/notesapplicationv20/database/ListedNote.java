package com.example.notesapplicationv20.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notesapplicationv20.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

/** The ListedNote class implements Serializable and is equivalent to SingleNote except
 *  for the note's content that is only shown when a specific singleNote is to be called.
 *  */
@Entity(tableName = "notes")
@TypeConverters({DateConverter.class})
public class ListedNote implements Serializable {

   // variables
   @PrimaryKey(autoGenerate = true)
   @NonNull
   private int id;

   @ColumnInfo(name="subject")
   private String subject;

   @ColumnInfo(name="title")
   private String title;

   @ColumnInfo(name="description")
   private String description;

   @ColumnInfo(name="created_at")
   private Date createdAt;

   @ColumnInfo(name="last_update")
   private Date lastUpdate;

   //methods
   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Date getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
   }

   public Date getLastUpdate() {
      return lastUpdate;
   }

   public void setLastUpdate(Date lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

}
