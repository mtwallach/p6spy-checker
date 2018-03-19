/**
 * Utility App to try and isolate long running queries from a p6spy output file.
 * 
 * @author astrachan
 */

package org.alfresco.support.expert;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class P6SpyChecker {

//	private static int BATCH = 4; // out of all the sorted (ascending) query times isolated, how many to go back
									// from the top of the list.

	public static void main(String[] args) throws IOException {


        int batchSize = 4;
        Options options = new Options();
        String p6spyfile;
        BufferedReader p6spyin1 = null;
        BufferedReader p6spyin2 = null;

	options.addOption("l", true, "name of log file to process");
        options.addOption("b", true, "number of log running queries to report");

        try {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);


	    String batchArg = cmd.getOptionValue("b");

	    if(batchArg != null) {
            batchSize = Integer.parseInt(batchArg);
        }

        p6spyfile = cmd.getOptionValue("l");
	     if (p6spyfile == null ) {
                System.err.println("Please provide a valid spy.log file to process");
                System.exit(-1);
         }



            System.out.println("P6Spy checker");
		System.out.println("-------------");

		//String p6spyfile = "C:\\Users\\astrachan\\Desktop\\alfresco_p6spy\\spy.log";
		
		File f = new File(p6spyfile);
		p6spyin1 = new BufferedReader(new FileReader(p6spyfile));
		p6spyin2 = new BufferedReader(new FileReader(p6spyfile));

		List<Integer> lList = new ArrayList<Integer>(); // get querytimes and order them
		List<P6SpyRow> qList = new ArrayList<P6SpyRow>(); 


			// phase one, get the querytimes and order them

			String line;
			String line2;
			
			while ((line = p6spyin1.readLine()) != null) {
				//if (line.startsWith("15")) { // <--- this is the first char from the epoch time
					String[] queryTime = line.split("\\|");
					lList.add(Integer.parseInt(queryTime[1]));
					P6SpyRow p6SpyData = new P6SpyRow(Integer.parseInt(queryTime[1]), line);
					qList.add(p6SpyData);
			//	}
			}
			
			Collections.sort(lList);
			Collections.sort(qList, new P6SpyRow());

			System.out.println(batchSize + " of the longest query times:");
			for (int i = 1; i <= batchSize; i++) {
				System.out.println("2 - Processing");
				System.out.println("QueryTime (ms): " + lList.get(lList.size() - i));
			}
			System.out.println("--------------------------------");

			// phase two, once we've got the query times, read in again via scanner and
			// (badly) isolate the query information itself for each one.

			int cntr = 0;
			/*
			 for(P6SpyRow a: qList)  { 
         			System.out.print(a.getTime() +"  : "+ a.getLine());
   			 }
			*/
			System.out.println("MTW 99: ArrayLenght is " + qList.size());
			for (int x = 0; x < batchSize; x++) {
				P6SpyRow a = qList.get(x);
				System.out.println(a.getTime() +"  : "+ a.getLine());
			}

		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			p6spyin1.close();
			p6spyin2.close();
		}
	}

    static class P6SpyRow implements Comparator<P6SpyRow>, Comparable<P6SpyRow> {
   	private String qLine;
   	private int qTime;
   	P6SpyRow() {
	}
   	P6SpyRow(int qt, String ql) {
		this.qTime = qt;
		this.qLine = ql;
   	}	
	public int getTime() {
		return this.qTime;
	}
	public String getLine() {
		return this.qLine;
	}
   // Overriding the compareTo method
   public int compareTo(P6SpyRow p6) {
      return (this.qLine).compareTo(p6.qLine);
   }

   // Overriding the compare method to sort the age 
   public int compare(P6SpyRow p6, P6SpyRow p61) {
      return p61.qTime - p6.qTime;
   }

	}
}
