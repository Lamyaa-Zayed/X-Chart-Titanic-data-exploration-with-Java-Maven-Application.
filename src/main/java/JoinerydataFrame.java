
import java.io.IOException;
import joinery.DataFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lamya
 */
public class JoinerydataFrame {
    public static void main(String args[]){
        try {

           /* DataFrame<Object>  df1= DataFrame.readCsv ("src/main/resources/data/vgsales.csv")
                    .retain("Year", "NA_Sales","EU_Sales","JP_Sales")
                    .describe ();
           System.out.println (df1.toString ());
            System.out.println ("=========================================================================================");*/
            DataFrame<Object>  df= DataFrame.readCsv ("vgsales.csv")
                    .retain("Year", "NA_Sales","EU_Sales","JP_Sales")
                    .groupBy(row ->row.get(1))
                    .mean ();
            df.iterrows ().forEachRemaining (System.out::println);

            System.out.println ("=========================================================================================");
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
