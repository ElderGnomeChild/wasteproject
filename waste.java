
import java.sql.*;
import java.awt.*;
import java.awt.event.*; //user clicking buttons
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

// import Pickup.java;

//import swing1.s1.ListenForButton;

/*
 aggregation query
 select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id group by waste_type.name;
 */

import java.awt.Dimension;
import java.awt.Toolkit;


public class waste extends JFrame{
  
  public static void main(String[] aargs){
    new waste();
  }
// All Jthings for north panel
  private JButton submit;
  private JComboBox waste_type;     
  private JComboBox company;    
  private JComboBox site;
  private JComboBox weightType;
  private JComboBox whichTable;
  private JSpinner day;
  private JSpinner year;
  private JLabel displayLabel; 
  private JTextField tf1; 
  
//Instance data for imput from NORTH PANEL 
  private String date;
  private String dayValue;
  private String monthValue;
  private String yearValue;
  private int companyId;
  private int siteId;
  private int wasteTypeId;
  private double weight;
  
  
//All Jthings for SOUTH PANEL
  private JComboBox sortBy_pickup;
  private JComboBox sortBy_aggro;
  private JComboBox upOrDown;
  private JButton generate_pickup_table;
  private JButton generate_aggregation_table; 
  private JSpinner end_day;
  private JSpinner start_day; 
  
  
//instance data for south panel
  private String sdate;
  private String edate;
  private String sday;
  private String smonth;
  private String syear;
  private String eday;
  private String emonth;
  private String eyear;
  private boolean ascdesc;
  
  
  private static final String DB_URL = "jdbc:sqlite:dataWaste.db";
  
  private PickupTableModel tableModel = new PickupTableModel();
  private JTable table = new JTable(tableModel);
  
  private AggroTableModel aggroModel = new AggroTableModel();
  private JTable aggroTable = new JTable(aggroModel);
  
//CONSTRUCTOR:
  public waste(){
// setting window parameters 
    Toolkit tk = Toolkit.getDefaultToolkit();      
    Dimension dim = tk.getScreenSize(); 
    this.setSize(dim.width, dim.height);
    this.setLocationRelativeTo(null);        
    this.setTitle("WasteManager");
    
    // ********************************************** NORTH PANEL ************************************************
    JPanel p = new JPanel(); //p is the north panel
    
    //submit button
    submit = new JButton("Insert Pickup");
    submit.setToolTipText("Adds a new row to the pickup table with parameters in text box and dropdown menus. Configure these correctly before clicking.");
    ListenForButton lfb = new ListenForButton();
    submit.addActionListener(lfb);
    p.add(submit);
    
    //weight input textbox
    tf1 = new JTextField("",20);
    tf1.setToolTipText("put in the weight");
    p.add(tf1);
    
    //weight tpyes combo box
    String[] weight_types = {"lbs", "tons"};
    this.weightType = new JComboBox(weight_types);
    p.add(weightType);
    
    
    
    try(Connection umbreon = DriverManager.getConnection(DB_URL)) { 
      
//generate new companies
      
      PreparedStatement num = umbreon.prepareStatement(
                                                       "select count(*) from company"  
                                                      );
      
      ResultSet i = num.executeQuery();
      int numberOfCompanies = i.getInt("count(*)"); 
      
      String[] companies = new String[numberOfCompanies];
      
      PreparedStatement getNames = umbreon.prepareStatement(
                                                            "select name from company order by id"    
                                                           );
      
      ResultSet names = getNames.executeQuery();
      String currentName;
      int count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        companies[count] = currentName;
        count++;
      }
//initialize company combobox
      this.company  = new JComboBox(companies);           
      company.setSize(40, 40);
      p.add(this.company);
      
//generate new sites
      num = umbreon.prepareStatement(
                                     "select count(*) from site"  
                                    );
      
      i = num.executeQuery();
      int numberOfSites = i.getInt("count(*)"); 
      
      String[] sites = new String[numberOfSites];
      
      PreparedStatement getSites = umbreon.prepareStatement(
                                                            "select name from site order by id"    
                                                           );
      
      names = getSites.executeQuery();
      count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        sites[count] = currentName;
        count++;
      }
//initialize sites combobox
      this.site  = new JComboBox(sites);
      site.setSize(40, 40);
      p.add(this.site);
      
//generate new waste types
      num = umbreon.prepareStatement(
                                     "select count(*) from waste_type"  
                                    );
      i = num.executeQuery();
      int numberOfWastes = i.getInt("count(*)"); 
      
      String[] waste_types = new String[numberOfWastes];
      
      PreparedStatement getWastes = umbreon.prepareStatement(
                                                             "select name from waste_type order by id"    
                                                            );
      
      names = getWastes.executeQuery();
      count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        waste_types[count] = currentName;
        count++;
      }
//initialize waste_types combo box
      this.waste_type = new JComboBox(waste_types);
      p.add(this.waste_type);
      
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
    
//date picker for data insertion
    Date todaysDate = new Date();
    year = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(year, "MM/dd/yy");
    year.setEditor(dateEditor);
    date = year.getValue().toString();
    p.add(year);
    
//*********************************** SOUTH PANEL ******************************************

// create ne panel (to add to the south of the north panel)
    JPanel s = new JPanel(); 
    
//pickup table button
    this.generate_pickup_table = new JButton("Generate Pickup Table");
    this.generate_pickup_table.setToolTipText("Generates a table displaying all pickups in the database between given dates and ordered by a specified parameter"); 
    ListenForButton lfpick = new ListenForButton();
    this.generate_pickup_table.addActionListener(lfpick); 
    s.add(generate_pickup_table);
    
// aggregation table button
    this.generate_aggregation_table = new JButton("Generate Aggregation Table");
    this.generate_aggregation_table.setToolTipText("Generates a table displaying aggregated data between given dates and ordered by a specified parameter"); 
    ListenForButton lfaggro = new ListenForButton();
    this.generate_aggregation_table.addActionListener(lfaggro); 
    s.add(generate_aggregation_table);
    
//sort by comboBox for pickup table
    String[] pickupSort = {"id", "weight", "date", "waste_type", "site"}; 
    this.sortBy_pickup = new JComboBox(pickupSort); 
    s.add(sortBy_pickup); 
    
//sort by comboBox for aggregation table
    String[] aggroSort = {"sum", "average", "waste_type"}; 
    this.sortBy_aggro = new JComboBox(aggroSort); 
    s.add(sortBy_aggro);
    
// combo box to descide ascending or descending order 
    String[] asc_desc = {"Ascending Order", "Descending Order"}; 
    this.upOrDown = new JComboBox(asc_desc); 
    s.add(upOrDown); 
    
//spinner for picking start day for query 
    start_day = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    JSpinner.DateEditor dateEditor1 = new JSpinner.DateEditor(start_day, "MM/dd/yy");
    start_day.setEditor(dateEditor1);
    date = year.getValue().toString();
    s.add(start_day);
    
//spinner for picking end day for query
    end_day = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    JSpinner.DateEditor dateEditor2 = new JSpinner.DateEditor(end_day, "MM/dd/yy");
    end_day.setEditor(dateEditor2);
    date = year.getValue().toString();
    s.add(end_day);

    
    //p.add(new JScrollPane(table), BorderLayout.SOUTH);
    //p.add(new JScrollPane(aggroTable), BorderLayout.SOUTH);
    
//Final window setting, adding panels
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    p.add(s, BorderLayout.SOUTH);   // adding south panel to north panel 
    this.add(p); 
    
    //displayPickups();
    //displayAggro();
    
    this.setVisible(true);
    submit.requestFocus();
  }
  
  
  
  //******************************************************************************** TABLE METHODS ******************************************************************************
  /*
   Void methods below for making tables pop up in the window
   */
  
  //all pickups
  private void displayPickups() {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      ResultSet pResults = s.executeQuery("select Pickup.id, Pickup.weight, Pickup.date, Site.name as sname, Waste_Type.name as wname, Company.name as cname from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id");
      while (pResults.next()) {
        int id = pResults.getInt(1);
        double weight = pResults.getDouble(2);
        String date = pResults.getString(3);
        String site = pResults.getString(4);
        String waste_type = pResults.getString(5);
        String company = pResults.getString(6);
        
        Pickup p = new Pickup(id, weight, date, site, waste_type, company);
        tableModel.addInstance(p);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }
  
  
  //pickups with start/end dates, ordered by either by date or by id, ascending or descending
  private void displayPickups(String startDate, String endDate, String order, Boolean descending) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      String desc;
      if(descending){desc = new String(" DESC");}
      else {desc = new String(" ASC");}
      ResultSet pResults = s.executeQuery("select Pickup.id, Pickup.weight, Pickup.date, Site.name as sname, Waste_Type.name as wname, Company.name as cname from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id where Pickup.date >= '" + startDate + "' and Pickup.date <= '" + endDate + "' order by " + order + desc);
      while (pResults.next()) {
        int id = pResults.getInt(1);
        double weight = pResults.getDouble(2);
        String date = pResults.getString(3);
        String site = pResults.getString(4);
        String waste_type = pResults.getString(5);
        String company = pResults.getString(6);
        
        Pickup p = new Pickup(id, weight, date, site, waste_type, company);
        System.out.println(id + weight + date + site + waste_type + company);
        
        tableModel.addInstance(p);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }
  
  private void displayAggro() {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      ResultSet aResults = s.executeQuery("select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id group by waste_type.name");
      while (aResults.next()) {
        String waste_type = aResults.getString(1);
        double sum = aResults.getDouble(2);
        double average = aResults.getDouble(3);
        
        Aggro a = new Aggro(waste_type, sum, average);
        // System.out.println("A" + a);
        aggroModel.addInstance(a);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }
  
  private void displayAggro(String startDate, String endDate, String order, Boolean descending) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      String desc;
      if(descending){desc = new String(" DESC");}
      else {desc = new String(" ASC");}
      ResultSet aResults = s.executeQuery("select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id where pickup.date >= '" + startDate + "' and pickup.date <= '" + endDate + "' group by waste_type.name order by " + order + desc);
      while (aResults.next()) {
        String waste_type = aResults.getString(1);
        double sum = aResults.getDouble(2);
        double average = aResults.getDouble(3);
        
        Aggro a = new Aggro(waste_type, sum, average);
        // System.out.println("A" + a);
        aggroModel.addInstance(a);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }
  private String monthfinder(String tempMonth){
   String month_num = "";  
        if (tempMonth.equals("Jan")){month_num = "01";}
        if (tempMonth.equals("Feb")){month_num = "02";} 
        if (tempMonth.equals("Mar")){month_num = "03";}  
        if (tempMonth.equals("Apr")){month_num = "04";}  
        if (tempMonth.equals("May")){month_num = "05";}
        if (tempMonth.equals("Jun")){month_num = "06";}
        if (tempMonth.equals("Jul")){month_num = "07";}
        if (tempMonth.equals("Aug")){month_num = "08";}  
        if (tempMonth.equals("Sep")){month_num = "09";}  
        if (tempMonth.equals("Oct")){month_num = "10";}  
        if (tempMonth.equals("Nov")){month_num = "11";}
        if (tempMonth.equals("Dec")){month_num = "12";} 
        return month_num; 
  }
  
  //SEPARATE CLASS
  private class ListenForButton implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == submit) {
        try {
          weight = Double.parseDouble(tf1.getText());
        }
        catch (NumberFormatException excep) {
          JOptionPane.showMessageDialog(waste.this, "input weight as a number", "Invalid Weight", JOptionPane.ERROR_MESSAGE);
          //System.exit(0);
        }
        if (weightType.getSelectedItem().toString().equals("tons")){
          weight *= 2000;
        }
        
        date = year.getValue().toString();
        //what.setText(date);  
        //get date data as integers
        String input, month, day, year, tempMonth; 
        input = date; 
        tempMonth = input.substring(4, 7);
        day = input.substring(8, 10); 
        year = input.substring(24, 28); 
        month = monthfinder(tempMonth); 
        dayValue = day;
        monthValue = month;
        yearValue = year;
        
        
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
          //company
          String cname = company.getSelectedItem().toString();
          PreparedStatement statement = conn.prepareStatement(
                                                              "select id from company where name = " + "'"+cname+"'");
          ResultSet results = statement.executeQuery();
          while(results.next()) {
            companyId = results.getInt("id");
          }
          System.out.println("cid: " + companyId);
          
          
          
          //site
          String sname = site.getSelectedItem().toString();
          statement = conn.prepareStatement(
                                            "select id from site where name = " + "'"+sname+"'");
          results = statement.executeQuery();
          while(results.next()) {
            siteId = results.getInt("id");
          }
          System.out.println("sid: " + siteId);
          
          
          //wastetype
          String wname = waste_type.getSelectedItem().toString();
          statement = conn.prepareStatement(
                                            "select id from waste_type where name = " + "'"+wname+"'");
          results = statement.executeQuery();
          while(results.next()) {
            wasteTypeId = results.getInt("id");
          }
          System.out.println("wid: " + wasteTypeId);
          
          String doot = yearValue+"-"+monthValue+"-"+dayValue;
          PreparedStatement eyedee = conn.prepareStatement(
                                                           "select id from pickup order by id desc limit 1"  
                                                          );
       
          ResultSet i = eyedee.executeQuery();
          
          int id = i.getInt("id");   
          id++;
          
          statement = conn.prepareStatement(
                                            "insert into pickup values(" + id + ", " + weight + ", " + "'" +doot+ "'" + ", " + siteId + ", " + wasteTypeId +", " + companyId +")"
                                           );
          
          statement.execute();
          System.out.println("done");
          
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }   
      }
      
      else if (e.getSource() == generate_pickup_table) {
        //start day data
        sdate = start_day.getValue().toString();
        String input1, month1, day1, year1, tempMonth1; 
        input1 = sdate; 
        tempMonth1 = input1.substring(4, 7);
        day1 = input1.substring(8, 10); 
        year1 = input1.substring(24, 28); 
        month1 = monthfinder(tempMonth1); 
        sday = day1;
        smonth = month1;
        syear = year1;
        
        //end day data
        edate = end_day.getValue().toString();
        String input2, month2, day2, year2, tempMonth2; 
        input2 = edate; 
        tempMonth2 = input2.substring(4, 7);
        day2 = input2.substring(8, 10); 
        year2 = input2.substring(24, 28); 
        month2 = monthfinder(tempMonth2); 
        eday = day1;
        emonth = month1;
        eyear = year1;
        
        String start = syear + "-" + smonth + "-" + sday; 
        String end = eyear + "-" + emonth + "-" + eday ;
         
        String ascendingOrDescending = upOrDown.getSelectedItem().toString();
        if (ascendingOrDescending.equals("Ascending Order")){
            ascdesc = false; 
        }
        else {
            ascdesc = true;  
        }
        
        String howToOrder = sortBy_pickup.getSelectedItem().toString(); 
        
        JDialog anything = new JDialog();
        anything.setTitle("anything");
        Toolkit tk = Toolkit.getDefaultToolkit();      
        Dimension dim = tk.getScreenSize(); 
        anything.setSize(dim.width, dim.height);
        anything.setModal(true);
        JPanel pp = new JPanel();
        displayPickups();
        //displayPickups(start, end, howToOrder, ascdesc);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
        
      }
      if (e.getSource() == generate_aggregation_table) {
      }
    }
  }
}
