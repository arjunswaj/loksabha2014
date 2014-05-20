package com.asb.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

public class DataConsolidator {

  public static final String CONSOLIDATED_REPORT = "../0_Consolidated_Report.csv";
  public int recordNo = 0;

  public void migrateData() {
    File path = new File(".");
    File consolidatedReport = new File(CONSOLIDATED_REPORT);
    BufferedReader constituencyFile = null;
    BufferedWriter bwReportFile = null;
    Set<String> consList = new HashSet<String>();
    recordNo = 0;
    int fileCounter = 0;
    try {
      bwReportFile = new BufferedWriter(new FileWriter(consolidatedReport));
      // bwReportFile.write("ID,STATE,CONSTITUENCY,CANDIDATE,PARTY,VOTES\n");
      File[] files = path.listFiles();
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          if (fileName.endsWith(".csv")) {
            int endIndex = fileName.length() - 4;
            fileName = fileName.substring(fileName.indexOf(" "), endIndex);
            String[] splits = fileName.split(" - ");
            String state = splits[0].trim();
            String constituency = splits[1].trim();
            fileCounter += 1;
            System.out.println(fileCounter + ". Processing: " + fileName);
            consList.add(constituency.toUpperCase(Locale.US));
            try {
              constituencyFile = new BufferedReader(new FileReader(file));
              consolidateData(bwReportFile, constituencyFile, state,
                  constituency);
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              if (null != constituencyFile) {
                try {
                  constituencyFile.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != bwReportFile) {
        try {
          bwReportFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void consolidateData(BufferedWriter bwReportFile,
      BufferedReader constituencyFile, String state, String constituency)
      throws IOException {
    String row = constituencyFile.readLine();
    while (null != (row = constituencyFile.readLine())) {
      StringTokenizer st = new StringTokenizer(row, ",");
      String candidate = st.nextToken().trim();
      String party = st.nextToken().trim();
      String votes = st.nextToken().trim();
      recordNo += 1;
      bwReportFile.write(String.valueOf(recordNo));
      bwReportFile.write(",");
      bwReportFile.write(state.replaceAll("\"", "").toUpperCase(Locale.US));
      bwReportFile.write(",");
      bwReportFile.write(constituency.replaceAll("\"", "").toUpperCase(
          Locale.US));
      bwReportFile.write(",");
      bwReportFile.write(candidate.replaceAll("\"", "").toUpperCase(Locale.US));
      bwReportFile.write(",");
      bwReportFile.write(party.replaceAll("\"", "").toUpperCase(Locale.US));
      bwReportFile.write(",");
      bwReportFile.write(votes);
      bwReportFile.write("\n");
    }
  }
}
