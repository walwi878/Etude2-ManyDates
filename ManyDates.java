import java.util.*;
import java.lang.Math;
import java.lang.String;
import java.util.regex.*;

// Etude-2 Many Dates
// Authors: Darcy Knox & William Wallace
// Date: April 2020

class ManyDates {

  /* Uses monthScore value to assign the 'most fitting' month column.
  * Then uses dayScore value to assign the 'most fitting' day column (using
  * the given day column), and sets the remaining column as years.
  * Once columns are set, the program checks for invalidities, then formats
  * valid dates for output. */

  // When accessing the daysInEachMonth array, the index is the given month - 1

  private static boolean isValidDate;
  private static String errorString;
  private static int[] daysInEachMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  // Returns a value representing percentage of valid months in a column.
  private static double monthScore(List<Integer> col) {
    double countValidMonths = 0;
    for (int i = 0; i < col.size(); i++) {
      if (col.get(i) <= 12 && col.get(i) > 0) {
        countValidMonths++;
      }
    }
    return countValidMonths/col.size();
  }

  /* Returns a value representing percentage of valid days in a column.
  * If the given month is valid, the day is only considered valid if it fits
  * within the daysInEachMonth for its month.
  * If the month isn't valid, we assume the day is valid if it's less than 31.
  * dayScore accounts for leap years by calling the leapYear mutator. */
  private static double dayScore(List<Integer> days, Integer[] months, List<Integer> years) {
    double countValidDays = 0;
    for (int i = 0; i < days.size(); i++) {
      if (months[i] > 0) {
        if (months[i] <= 12) {
          leapYear(years.get(i));
          if (days.get(i) <= daysInEachMonth[months[i] - 1]) {
            countValidDays++;
          }
        } else if (days.get(i) <= 31) {
          countValidDays++;
        }
      }
    }
    return countValidDays/days.size();
  }

  /* Mutator function that changes days in February if the year is a leap year,
  * or corrects it if it's not. */
  private static void leapYear(Integer year) {
    if (year % 4 == 0 && !(year % 100 == 0 && year % 400 != 0)) {
      daysInEachMonth[1] = 29;
    } else if (daysInEachMonth[1] == 29){
      daysInEachMonth[1] = 28;
    }
  }


  public static void main(String[] args) {

    Scanner stdin = new Scanner(System.in);

    int n1, n2, n3;
    ArrayList<Integer> col1 = new ArrayList<Integer>();
    ArrayList<Integer> col2 = new ArrayList<Integer>();
    ArrayList<Integer> col3 = new ArrayList<Integer>();

    ArrayList<String> col1Strings = new ArrayList<String>();
    ArrayList<String> col2Strings = new ArrayList<String>();
    ArrayList<String> col3Strings = new ArrayList<String>();

    ArrayList<String> output = new ArrayList<String>();

    String line = "";
    String[] split;
    int numberOfLines;

    /* Splits each line of input by the / separator
    * Adds each number to an array by positions
    * - all days are in the same ArrayList
    * - all months are in the same ArrayList
    * - all years are in the same ArrayList */
    while (stdin.hasNextLine()) {

      line = stdin.nextLine();

      // Match the line of input against a regex
      boolean match = Pattern.compile("^[0-9]+/[0-9]+/[0-9]+$").matcher(line).matches();

      split = line.split("/"); // separate the numbers in the date

      /* If the pattern doesn't match, an error is printed and the input doesn't
      * get processed. */
      if (!match || split.length != 3) {
        if (line.compareTo(line.trim()) != 0) {
          output.add(line + " - INVALID: Input contains leading and/or trailing whitespace.");
        } else {
          output.add(line + " - INVALID: Input must be 3 numbers separated by '/'.");
        }
      } else {

        if ((split[0].length() > 4 || split[0].length() == 3)
          || (split[1].length() > 4 || split[1].length() == 3)
          || (split[2].length() > 4 || split[2].length() == 3)) {
          output.add(line + " - INVALID: Input numbers must be either 1, 2, or 4 digits long.");
        } else if (line.length() > 10) {
          output.add(line + " - INVALID: Excessive input length.");
        } else {

          col1Strings.add(split[0]);
          col2Strings.add(split[1]);
          col3Strings.add(split[2]);


          n1 = Integer.parseInt(split[0]); // first number
          n2 = Integer.parseInt(split[1]); // second number
          n3 = Integer.parseInt(split[2]); // third number


          col1.add(n1); // add the first number to the ArrayList of first numbers
          col2.add(n2); // add the second number to the ArrayList of second numbers
          col3.add(n3); // add the third number to the ArrayList of third numbers

          output.add("");

        }
      }
    }

    stdin.close();

    numberOfLines = col1.size();

    double col1MonthScore = monthScore(col1);
    double col2MonthScore = monthScore(col2);
    double col3MonthScore = monthScore(col3);

    /* Column 2 comes first in monthScores array so the format stays the same
    * as the input format if there are equal monthScore/dayScore values.
    * i.e. default case is d/m/y. */

    double[] monthScores = {col2MonthScore, col1MonthScore, col3MonthScore};

    // Gets the most likely month column based off monthScore values.
    int monthColIndex = 0;
    double maxScore = monthScores[monthColIndex];
    for (int i = 1; i < 3; i++) {
      if (monthScores[i] > maxScore) {
        monthColIndex = i;
        maxScore = monthScores[i];
      }
    }

    // Arrays that get the values of the columns once we know which is which.
    Integer[] days = new Integer[numberOfLines];
    Integer[] months = new Integer[numberOfLines];
    Integer[] years = new Integer[numberOfLines];

    String[] yearStrings = new String[numberOfLines];

    /* From knowing the likeliest month column, assign the days column to be
    * the likeliest days column out of the two remaining. */
    if (monthColIndex == 2) {
      months = col3.toArray(months);
      if (dayScore(col2, months, col1) > dayScore(col1, months, col2)) {
        days = col2.toArray(days);
        years = col1.toArray(years);
        yearStrings = col1Strings.toArray(yearStrings);
      } else {
        days = col1.toArray(days);
        years = col2.toArray(years);
        yearStrings = col2Strings.toArray(yearStrings);
      }
    } else if (monthColIndex == 0) {
      months = col2.toArray(months);
      if (dayScore(col3, months, col1) > dayScore(col1, months, col3)) {
        days = col3.toArray(days);
        years = col1.toArray(years);
        yearStrings = col1Strings.toArray(yearStrings);
      } else {
        days = col1.toArray(days);
        years = col3.toArray(years);
        yearStrings = col3Strings.toArray(yearStrings);
      }
    } else if (monthColIndex == 1) {
      months = col1.toArray(months);
      if (dayScore(col3, months, col2) > dayScore(col2, months, col3)) {
        days = col3.toArray(days);
        years = col2.toArray(years);
        yearStrings = col2Strings.toArray(yearStrings);
      } else {
        days = col2.toArray(days);
        years = col3.toArray(years);
        yearStrings = col3Strings.toArray(yearStrings);
      }
    }

    // Array to store the string form of each month
    String[] monthStrings = new String[numberOfLines];

    // Checks and formatting
    for (int i = 0; i < numberOfLines; i++) {

      errorString = "";
      isValidDate = true;

      // Converting month as number to its 3 letter string
      switch (months[i]){
        case 1:
          monthStrings[i] = "Jan";
          break;
        case 2:
          monthStrings[i] = "Feb";
          break;
        case 3:
          monthStrings[i] = "Mar";
          break;
        case 4:
          monthStrings[i] = "Apr";
          break;
        case 5:
          monthStrings[i] = "May";
          break;
        case 6:
          monthStrings[i] = "Jun";
          break;
        case 7:
          monthStrings[i] = "Jul";
          break;
        case 8:
          monthStrings[i] = "Aug";
          break;
        case 9:
          monthStrings[i] = "Sep";
          break;
        case 10:
          monthStrings[i] = "Oct";
          break;
        case 11:
          monthStrings[i] = "Nov";
          break;
        case 12:
          monthStrings[i] = "Dec";
          break;
        default:
          isValidDate = false;
          errorString = " - INVALID: Month out of range.";
          break;
      }

      if (isValidDate) {
        leapYear(years[i]); // changes days in Feb if it needs to

        /* Checks if month exceeds 12, and whether the day is within the days in
        * that particular month. */
        if (months[i] > 12 || months[i] < 1) {
          isValidDate = false;
          errorString = " - INVALID: Month out of range.";
        } else if (days[i] > 31 || days[i] < 1) {
          isValidDate = false;
          errorString = " - INVALID: Day out of range.";
        } else if (days[i] > daysInEachMonth[months[i] - 1]) {
          isValidDate = false;
          errorString = " - INVALID: Day out of range for given month.";
        } else {

          if (yearStrings[i].length() != 2 && yearStrings[i].length() != 4) {
            isValidDate = false;
            errorString = " - INVALID: Years must be 2 or 4 digits long.";
          }


          // adds 2000 to the year if between 0 and 49
          // adds 1900 to the year if between 50 and 99
          // invalid date if out of range 1753 - 3000 (both inclusive)
          if ((years[i] >= 100 && years[i] < 1753) || years[i] > 3000) {
              isValidDate = false;
              errorString = " - INVALID: Year out of range.";
          } else if (years[i] >= 0 && years[i] < 50 ) {
              years[i] += 2000;
          } else if (years[i] >= 50 && years[i] < 100) {
              years[i] += 1900;
          }
        }
      }

      int nextFreeIndex = output.indexOf("");

      // Output
      String dayString;
      if (!isValidDate) {
        output.set(nextFreeIndex, Integer.toString(days[i]) + "/" + Integer.toString(months[i]) + "/" + yearStrings[i] + errorString);
      } else {
        if (days[i] < 10) {
          dayString = "0" + Integer.toString(days[i]);
        } else {
          dayString = Integer.toString(days[i]);
        }
        output.set(nextFreeIndex, dayString + " " + monthStrings[i] + " " + Integer.toString(years[i]));
      }

    }

    for (int i = 0; i < output.size(); i++) {
      System.out.println(output.get(i));
    }

  }
}
