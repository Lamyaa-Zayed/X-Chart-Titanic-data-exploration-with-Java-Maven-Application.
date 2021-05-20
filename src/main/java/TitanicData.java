
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lamya
 */
public class TitanicData {
    Table titanicDataAnalysis;
    String dataPath = "titanic.csv";

    public TitanicData() {
    }

    public static void main(String[] args) {
        TitanicData tda = new TitanicData ();
        try {
            tda.titanicDataAnalysis = tda.loadDataFromCVS (tda.dataPath);
            //getting the Structure of the data
            String structure = tda.getDataInfoStructure (tda.titanicDataAnalysis);
            System.out.println (structure);
            //getting Data summery
            //System.in.read ();
            String summary = tda.getDataSummary (tda.titanicDataAnalysis);
            System.out.println (summary);
            //System.in.read ();
            // Adding date Column
            Table dataWithDate = tda.addDateColumnToData (tda.titanicDataAnalysis);
            System.out.println ("=====================================================================================");
            System.out.println (dataWithDate.structure ());
            //System.in.read ();
            //Sorting on the added Date Field
            Table sortedData = dataWithDate.sortAscendingOn ("Fake Date");
            System.out.println ("=====================================================================================");
            //getting the first 10 rows
            System.out.println ("Printing the first ten rows of the table");
            //System.in.read ();
            Table firstTenRows = sortedData.first (10);

            System.out.println (firstTenRows);
            //System.in.read ();
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Table mappedData = tda.mapTextColumnToNumber (tda.titanicDataAnalysis);
            Table firstFiveRows = mappedData.first (5);
            System.out.println ("=====================================================================================");
            System.out.println ("Printing the first five rows of the table");
            System.out.println (firstFiveRows);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  Load Data From CSV File
    public Table loadDataFromCVS(String path) throws IOException {
        Table titanicData = Table.read().csv(path);
        return titanicData;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// get the structure of the data
    public String getDataInfoStructure(Table data) {
        Table dataStructure = data.structure ();
        return dataStructure.toString ();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //get Data Summary
    public String getDataSummary(Table data) {
        Table summary = data.summary ();
        return summary.toString ();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Adding Columns
    public Table addDateColumnToData(Table data) {
        int rowCount = data.rowCount ();
        List<LocalDate> dateList = new ArrayList<LocalDate> ();
        for (int i = 0; i < rowCount; i++) {
            dateList.add (LocalDate.of (2021, 5, i % 31 == 0 ? 19 : i % 31));
        }
        DateColumn dateColumn = DateColumn.create ("Fake Date", dateList);
        data.insertColumn (data.columnCount (), dateColumn);
        return data;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // mapping text data to numeric values on the sex field female=1,male=0 and adding a column named gender
    public Table mapTextColumnToNumber(Table data) {
        NumberColumn mappedGenderColumn = null;
        StringColumn gender = (StringColumn) data.column ("Sex");
        List<Number> mappedGenderValues = new ArrayList<Number>();
        for (String v : gender) {
            if ((v != null) && (v.equals ("female"))) {
                mappedGenderValues.add (1);
            } else {
                mappedGenderValues.add (0);
            }
        }
        mappedGenderColumn = DoubleColumn.create ("gender", mappedGenderValues);
        data.addColumns (mappedGenderColumn);
        return data;
    }
}
