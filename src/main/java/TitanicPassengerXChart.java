
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lamya
 */
public class TitanicPassengerXChart {
    public static void main(String[] args) {
        TitanicPassengerXChart TitanicPassengerXChart = new TitanicPassengerXChart ();
        List<TitanicPassengerData> allPassengers = TitanicPassengerXChart.getPassengersFromJsonFile ();
        TitanicPassengerXChart.graphPassengerAges (allPassengers);
        //System.in.read();
        
        TitanicPassengerXChart.graphPassengerClass (allPassengers);
    }
    // Read json file and return a list containing TitanicPassenger Objects
    public List<TitanicPassengerData> getPassengersFromJsonFile() {
        List<TitanicPassengerData> allPassengers = new ArrayList<TitanicPassengerData>();
        ObjectMapper objectMapper = new ObjectMapper ();
        objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream input = new FileInputStream ("titanic_csv.json")) {
            //Read JSON file
            allPassengers = objectMapper.readValue (input, new TypeReference<List<TitanicPassengerData>> () {
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return allPassengers;
    }

    public void graphPassengerAges(List<TitanicPassengerData> passengerList) {
        //filter to get an array of passenger ages
        List<Float> pAges = passengerList.stream ().map (TitanicPassengerData::getAge).limit (8).collect (Collectors.toList ());
        List<String> pNames = passengerList.stream ().map (TitanicPassengerData::getName).limit (8).collect (Collectors.toList ());
        String[] names = new String[pNames.size ()];
        Float ages[] = new Float[pAges.size ()];
        ages = pAges.toArray (ages);
        names = pNames.toArray (names);
        //Using XCart to graph the Ages
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder ().width (1024).height (768).title ("Age Histogram").xAxisTitle ("Names").yAxisTitle ("Age").build ();
        // Customize Chart
        chart.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart.getStyler ().setHasAnnotations (true);
        chart.getStyler ().setStacked (true);
        // Series
        chart.addSeries ("Passenger's Ages", pNames, pAges);
        // Show it
        new SwingWrapper (chart).displayChart ();
    }

    public void graphPassengerClass(List<TitanicPassengerData> passengerList) {
        //filter to get a map of passenger class and total number of passengers in each class
        Map<String, Long> result =
                passengerList.stream ().collect (
                        Collectors.groupingBy (
                                TitanicPassengerData::getPclass, Collectors.counting ()
                        )
                );
        // Create Chart
        PieChart chart = new PieChartBuilder ().width (800).height (600).title (getClass ().getSimpleName ()).build ();
        // Customize Chart
        Color[] sliceColors = new Color[]{new Color (180, 68, 50), new Color (130, 105, 120), new Color (80, 143, 160)};
        chart.getStyler ().setSeriesColors (sliceColors);
        // Series
        chart.addSeries ("First Class", result.get ("1"));
        chart.addSeries ("Second Class", result.get ("2"));
        chart.addSeries ("Third Class", result.get ("3"));
        // Show it
        new SwingWrapper (chart).displayChart ();
    }

}
