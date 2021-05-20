/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lamya
 */

import tech.tablesaw.api.*;
import tech.tablesaw.selection.Selection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import tech.tablesaw.filtering.Filter;

public class MavenDataPrep {
    public MavenDataPrep(){
        
    }
    
public static void main(String[] args) throws IOException {
        /*
         * importing the file that we will be using
         */
        Table hrAnalytics = Table.read ().csv ("HR_comma_sep2.csv");
        //Getting the table structure
        System.out.println ("Printing the structure of the table  loaded from my local machine");
        Table localStructure = hrAnalytics.structure ();
        System.out.println (localStructure);
        //Knowing the size of the table that loaded. Knowing the columns and the rows
        System.out.println ("Getting the total number of columns and rows");
        String tableShape = hrAnalytics.shape ();
        System.out.println (tableShape);
        //Creating a new table as a subset of the old one
        Table latestHrAnalytics = hrAnalytics.select ("Name", "Satisfaction level", "Last Evaluation", "Left", "Promotion");
        System.out.println (latestHrAnalytics.columnNames ());
        //Printing all the column names
        System.out.println (hrAnalytics.columnNames ());
        //Retrieving a single column from the table
        DoubleColumn theSatisfaction = (DoubleColumn) hrAnalytics.column ("Satisfaction level");
        System.out.println("theSatisfaction:" + theSatisfaction);
        //Adding new columns to the  table you have loaded.
        int rowCount= hrAnalytics.rowCount ();
        double[] theIndexing = new double[rowCount];
        for(int i = 0;i < rowCount;i++) {
            theIndexing[i]=i;
        }
        DoubleColumn myIndexColumn = DoubleColumn.create ("theIndexes", theIndexing);
        hrAnalytics.insertColumn (0, myIndexColumn);
        //print the column names to see whether the column has been added.
        System.out.println (hrAnalytics.columnNames ());
        //removing some columns  from the table
        hrAnalytics.removeColumns ("theIndexes");
        //print the column names to see whether the column has been removed
        System.out.println (hrAnalytics.columnNames ());
        //Specifying the columns that you want to leave wich is also easier in case the columns to be removed are many
        //hrAnalytics.retainColumns("Me","you");
        //Sorting the table with specific Columns( ascending or descending manner)
        Table ascendingHr = hrAnalytics.sortAscendingOn ("Satisfaction level");
        ascendingHr.first (8);
        //Accessing the first 5 rows of the table
        System.out.println ("Printing the first rows of the table");
        Table tableHead = hrAnalytics.first (15);
        System.out.println (tableHead);
        //Accessing the last 5 rows of the table
        System.out.println ("Printing the last  rows of the table");
        Table tableTail = hrAnalytics.last (5);
        System.out.println (tableTail);
        // printing the structure of the table to identify the columns that we would like to remove
        System.out.println (hrAnalytics.columnNames ());
        //Creating a table from the loaded data set, a situation you want specific columns
        Table filteredTable = hrAnalytics.select ("Name", "Satisfaction level", "Last Evaluation", "Left", "Promotion");
        System.out.println (filteredTable.columnNames ());
        // Want table which fits a specific criteria
        //EXAMPLE: All employees whose last evaluation is equal to 0.75
        NumericColumn<?> LE = hrAnalytics.numberColumn ("Last Evaluation");
        Selection LEscore = LE.isEqualTo (0.75);
        Table EmployeesWithLE = hrAnalytics.where (LEscore);
        //Accessing the first 5 rows of the table
        System.out.println ("Printing the first rows of the table");
        Table tableHead2 = EmployeesWithLE.first (5);
        System.out.println (tableHead2);
        //Talk about : Selections, filters, most used filters, where, drop where and give a link to the filter API
        //Removing Columns with Missing data
        //hrAnalytics.removeColumnsWithMissingValues();
        //Removing Rows with Missing data based on specific columns. Using a for loop
        //Creating arrays to Hold columns
        List<String> nameList = new ArrayList<> ();
        List<Double> SatisfactionLevelList = new ArrayList<> ();
        List<Double> LastEvaluationList = new ArrayList<> ();
        List<Double> numProjectsList = new ArrayList<> ();
        List<Double> aveMonHoursList = new ArrayList<> ();
        List<Double> leftList = new ArrayList<> ();
        List<Double> promotionList = new ArrayList<> ();
        List<String> debtList = new ArrayList<> ();

        //To be used for interpolation, double arrays
        double[] SatisfactionLevel_raw = new double[50];
        double[] LastEvaluation_raw = new double[50];
        int index = 0;
        //Looping through the table to remove the rows with NANs
      Table  hrAnalyticsSorted=hrAnalytics.sortAscendingOn ("Satisfaction level","Last Evaluation");

        for (Row row : hrAnalyticsSorted) {
            //Extract everything in the row
            String theName = row.getString ("NAME");
            Double theSL = row.getDouble ("Satisfaction level");
            Double theLE = row.getDouble ("Last Evaluation");
            Double theProjectsTaken = row.getNumber ("No. of Projects Undertaken");
            Double theAMH = row.getNumber ("Av monthly hours");
            //Double theTM = row.getDouble("Time Spent");
            Double theNOA = row.getNumber ("No. of accidents");
            Double thePromo = row.getNumber ("Promotion");
            Double theLeft = row.getNumber ("Left");
            String theDept = row.getString ("Department");
            //Printing Everything
            System.out.println (theName);
            System.out.println ("The SL1 is" + theSL);
            System.out.println ("The LE is " + theLE);
            System.out.println ("The projects taken is " + theProjectsTaken);
            System.out.println ("The AVG is " + theAMH);
            System.out.println ("The department is  " + theDept + "\n");

            if (theName.length () != 0 && theDept.length () != 0 && theSL != 0 && theLE != 0 && theAMH != 0 && (theLeft == 0 || theLeft == 1)) {
                nameList.add (theName);
                SatisfactionLevelList.add (theSL);
                LastEvaluationList.add (theLE);
                aveMonHoursList.add (theAMH);
                leftList.add (theLeft);
                debtList.add (theDept);
            }
            //Adding the data for Interpolation
            SatisfactionLevel_raw[index] = theSL;
            LastEvaluation_raw[index] = theLE;
            index++;
        }
        //Creating columns to store the variables:
        String[] nameArr = nameList.toArray (new String[SatisfactionLevelList.size ()]);
        StringColumn name = StringColumn.create ("name", nameArr);
        Double[] SLArr = SatisfactionLevelList.toArray (new Double[SatisfactionLevelList.size ()]);
        DoubleColumn SL = DoubleColumn.create ("SE", SLArr);
        Double[] LEArr = LastEvaluationList.toArray (new Double[LastEvaluationList.size ()]);
        DoubleColumn LastE = DoubleColumn.create ("Last Eva", LEArr);
        Double[] aveHrsArr = aveMonHoursList.toArray (new Double[aveMonHoursList.size ()]);
        DoubleColumn aveHrs = DoubleColumn.create ("Average hours", aveHrsArr);
        Double[] leftListArr = leftList.toArray (new Double[leftList.size ()]);
        DoubleColumn theLeftList = DoubleColumn.create ("Left List", leftListArr);
        String[] debtArr = debtList.toArray (new String[debtList.size ()]);
        StringColumn deptcol = StringColumn.create ("dept", debtArr);
        //Adding the columns to the table
        Table droppedRows = Table.create ("DroppedRows", name, SL, LastE, aveHrs, theLeftList, deptcol);
        String tableShape2 = droppedRows.shape ();
        System.out.println ("extracted table size: " + tableShape2);
        System.out.println ("original table size: " + tableShape);
        Table tableDroppedRowsHead = droppedRows.first (18);
        System.out.println(tableDroppedRowsHead);


    }
}
