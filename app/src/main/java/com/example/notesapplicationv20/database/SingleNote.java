package com.example.notesapplicationv20.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.notesapplicationv20.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "notes")
@TypeConverters({DateConverter.class})
public class SingleNote implements Serializable {

   @PrimaryKey(autoGenerate = true)
   @NonNull
   private int id;

   @ColumnInfo(name="subject")
   private String subject;

   @ColumnInfo(name="title")
   private String title;

   @ColumnInfo(name="description")
   private String description;

   @ColumnInfo(name="content")
   private String content;

   @ColumnInfo(name="created_at")
   private Date createdAt;

   @ColumnInfo(name="last_update")
   private Date lastUpdate;

   /** This is the first constructor for SingleNote which will be called when a new SingleNote instance is to be created
    *  and information about its id is required. This constructor will be used to create an updated SingleNote instance
    *  */
   public SingleNote(int id, String subject, String title, String description, String content, Date createdAt, Date lastUpdate){
      this.id = id;
      this.subject = subject;
      this.title = title;
      this.description = description;
      this.content = content;
      this.createdAt = createdAt;
      this.lastUpdate = lastUpdate;
   }

   /** This is the second constructor for SingleNote and does not require an id. This constructor will be
    * called when a new singleNote instance is to be created and its id needs to be generated.
    * The @Ignore- annotation prevents the room database to construct an entity without id column.
    * */
   @Ignore
   public SingleNote(String subject, String title, String description, String content, Date createdAt, Date lastUpdate){
      this.subject = subject;
      this.title = title;
      this.description = description;
      this.content = content;
      this.createdAt = createdAt;
      this.lastUpdate = lastUpdate;
   }

   // methods
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

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
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
