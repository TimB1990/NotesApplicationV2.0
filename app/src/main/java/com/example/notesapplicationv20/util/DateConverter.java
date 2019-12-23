package com.example.notesapplicationv20.util;

import androidx.room.TypeConverter;

import java.util.Date;

/** The DateConverter class is a helper class converts a Date from timestamp
 *  or converts a date to timestamp */
public class DateConverter {

   /** The fromTimestamp method converts a timestamp to date
    * @param value the long value of the timestamp
    * @return a new Date from timestamp */
   @TypeConverter
   public static Date fromTimestamp(Long value) {
      return value == null ? null : new Date(value);
   }

   /** The dateToTimestamp method converts a date to timestamp
    * @param date The date to be converted to timestamp
    * @return a long value by date.getTime() */
   @TypeConverter
   public static Long dateToTimestamp(Date date) {
      return date == null ? null : date.getTime();
   }
}
