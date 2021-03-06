package com.squarespace.cldr.dates;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;

import com.squarespace.cldr.CLDR;


/**
 * Common interface for date time formatters.
 */
public interface CalendarFormatter {
  
  enum FieldWidth {
    ABBREVIATED,
    SHORT,
    NARROW,
    WIDE
  }
  
  /**
   * Returns the bundle identifier associated with this formatter.
   */
  CLDR.Locale bundleId();

  /**
   * Fetch the localized name of the week.
   */
  String getWeekdayName(DayOfWeek dayOfWeek, FieldWidth width);
  
  /**
   * Fetch the localized name of the month.
   */
  String getMonthName(Month month, FieldWidth width);
  
  /**
   * Format a date time with the given options, writing the output to the buffer.
   */
  void format(ZonedDateTime datetime, CalendarFormatOptions options, StringBuilder buffer);

  /**
   * Format a single field of a given width.
   */
  void formatField(ZonedDateTime datetime, char field, int width, StringBuilder buffer);
  
  /**
   * Format a single field based on the first character in the pattern.
   */
  void formatField(ZonedDateTime datetime, String pattern, StringBuilder buffer);
  
  /**
   * Format a date time interval, guessing at the best skeleton to use based on the field
   * of greatest difference between the start and end date-time.  If the end date-time has
   * a different time zone than the start, this is corrected for comparison.
   * 
   * Greatest difference is calculated by comparing fields in the following order:
   * 
   *    year, month, date, day-of-week, am-pm, hour, hour-of-day, minute, and second
   */
  void format(
      ZonedDateTime start, ZonedDateTime end, DateTimeIntervalSkeleton skeleton, StringBuilder buffer);
  
}
