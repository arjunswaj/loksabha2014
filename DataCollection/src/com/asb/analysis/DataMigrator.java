package com.asb.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class DataMigrator {

  public static final String CONSOLIDATED_REPORT = "Consolidated_Data.csv";
  public int recordNo = 0;
  
  public void migrateData() {
    File path = new File(".");
    File consolidatedReport = new File(CONSOLIDATED_REPORT);
    BufferedReader constituencyFile = null;
    BufferedWriter bwReportFile = null;
    recordNo = 0;
    try {
      bwReportFile = new BufferedWriter(new FileWriter(consolidatedReport));
      bwReportFile.write("ID,STATE,CONSTITUENCY,CANDIDATE,PARTY,VOTES\n");
      File[] files = path.listFiles();
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          if (fileName.endsWith(".csv") && !fileName.startsWith(CONSOLIDATED_REPORT)) {
            System.out.println("Processing: " + fileName);
            fileName = fileName.substring(fileName.indexOf(" "),
                fileName.indexOf("."));
            String[] splits = fileName.split(" - ");
            String state = splits[0].trim();
            String constituency = splits[1].trim();
            try {              
              constituencyFile = new BufferedReader(new FileReader(file));
              consolidateData(bwReportFile, constituencyFile, state, constituency);
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
      BufferedReader constituencyFile, String state, String constituency) throws IOException {
      String row = constituencyFile.readLine();
      while (null != (row = constituencyFile.readLine())) {
        StringTokenizer st = new StringTokenizer(row, ",");
        String candidate = st.nextToken();
        String party = st.nextToken();
        String votes = st.nextToken();
        recordNo += 1;
        bwReportFile.write(String.valueOf(recordNo));
        bwReportFile.write(",");
        bwReportFile.write(state);
        bwReportFile.write(",");
        bwReportFile.write(constituency);
        bwReportFile.write(",");
        bwReportFile.write(candidate);
        bwReportFile.write(",");
        bwReportFile.write(party);
        bwReportFile.write(",");
        bwReportFile.write(votes);
        bwReportFile.write("\n");
      }
  }
}
