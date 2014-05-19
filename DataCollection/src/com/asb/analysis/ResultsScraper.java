package com.asb.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class ResultsScraper {

  public static final String SITE = "http://eciresults.nic.in/Constituencywise";
  public static final String HTM_AC = ".htm?ac=";
  public Map<String, Integer> stateSeatsMap = new LinkedHashMap<String, Integer>();

  public int cNo = 0;
  
  public ResultsScraper() {
    stateSeatsMap.put("S01", 42);
    stateSeatsMap.put("S02", 2);
    stateSeatsMap.put("S03", 14);
    stateSeatsMap.put("S04", 40);
    
    stateSeatsMap.put("S05", 2);
    stateSeatsMap.put("S06", 26);
    stateSeatsMap.put("S07", 10);
    stateSeatsMap.put("S08", 4);
    
    stateSeatsMap.put("S09", 6);
    stateSeatsMap.put("S10", 28);
    stateSeatsMap.put("S11", 20);
    stateSeatsMap.put("S12", 29);
    
    stateSeatsMap.put("S13", 48);
    stateSeatsMap.put("S14", 2);
    stateSeatsMap.put("S15", 2);
    stateSeatsMap.put("S16", 1);
    
    stateSeatsMap.put("S17", 1);
    stateSeatsMap.put("S18", 21);
    stateSeatsMap.put("S19", 13);    
    stateSeatsMap.put("S20", 25);
    
    stateSeatsMap.put("S21", 1);
    stateSeatsMap.put("S22", 39);
    stateSeatsMap.put("S23", 2);
    stateSeatsMap.put("S24", 80);
    
    stateSeatsMap.put("S25", 42);    
    stateSeatsMap.put("S26", 11);
    stateSeatsMap.put("S27", 14);
    stateSeatsMap.put("S28", 5);
    
    stateSeatsMap.put("U01", 1);
    stateSeatsMap.put("U02", 1);
    stateSeatsMap.put("U03", 1);
    stateSeatsMap.put("U04", 1);
    
    stateSeatsMap.put("U05", 7);
    stateSeatsMap.put("U06", 1);
    stateSeatsMap.put("U07", 1);
  }

  public void scrapeData() {
    cNo = 0;
    for (String state : stateSeatsMap.keySet()) {
      int constituencies = stateSeatsMap.get(state);
      for (int constituencyNumber = 1; constituencyNumber <= constituencies; constituencyNumber += 1) {
        String url = SITE + state + constituencyNumber + HTM_AC
            + constituencyNumber;
        extractDataToFile(fetchWebpage(url));
      }
    }
  }

  private Source fetchWebpage(String url) {
    Source source = null;
    try {
      source = new Source(new URL(url));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return source;
  }

  private void extractDataToFile(Source source) {
    File resultFile = null;
    BufferedWriter bwResultFile = null;

    try {

      Element result = source.getAllElements("div id=\"div1\"").get(0);

      Element table = (Element) result.getAllElements("table").get(0);
      List<Element> rows = table.getAllElements("tr");
      Element constituency = rows.get(0);
      String constituencyName = constituency.getTextExtractor().toString();
      cNo += 1;
      System.out.println(cNo + " " + constituencyName);
      resultFile = new File(cNo + " " + constituencyName + ".csv");
      bwResultFile = new BufferedWriter(new FileWriter(resultFile));
      bwResultFile.write("Candidate,Party,Votes\n");
      int rowSize = rows.size();
      for (int index = 3; index < rowSize; index += 1) {
        Element row = rows.get(index);
        List<Element> cols = row.getAllElements("td");
        bwResultFile.write(cols.get(0).getTextExtractor().toString());
        bwResultFile.write(",");
        bwResultFile.write(cols.get(1).getTextExtractor().toString());
        bwResultFile.write(",");
        bwResultFile.write(cols.get(2).getTextExtractor().toString());
        bwResultFile.write("\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != bwResultFile) {
        try {
          bwResultFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
