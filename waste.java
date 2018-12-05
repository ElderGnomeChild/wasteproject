
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
	private JLabel what;
	private JButton submit;
	private JComboBox waste_type;     
    private JComboBox company;    
    private JComboBox site;
    private JComboBox weightType;
    private JSpinner day;
    private JSpinner year;
    private JButton submitButton = new JButton("Submit Pickup");
    private JLabel displayLabel; 
    private JTextField tf1;
    private String date;

    // private Pickup pickleRick = new Pickup(69, 4.20, "2018-09-23", "Carleson", "Glass", "ACE");
    private Aggro ag = new Aggro("rec", 4.20, 6.9);
    
    //bellow is variables that store integer values derived from inputed data
    private String dayValue;
    private String monthValue;
    private String yearValue;
    private int companyId;
    private int siteId;
    private int wasteTypeId;
    private double weight;
    
    private static final String DB_URL = "jdbc:sqlite:dataWaste.db";

    private PickupTableModel tableModel = new PickupTableModel();
    private JTable table = new JTable(tableModel);

    private AggroTableModel aggroModel = new AggroTableModel();
    private JTable aggroTable = new JTable(aggroModel);

    public waste(){
        // System.out.println(pickleRick);
        System.out.println(ag);
    	// super("Waste Data Management");
        
        this.setSize(600, 600);
//        Toolkit tk = Toolkit.getDefaultToolkit(); 
//        Dimension dim = tk.getScreenSize(); 
//        this.setSize(dim.width, dim.height);
        this.setLocationRelativeTo(null);        
        this.setTitle("WasteManager");
        
        JPanel p = new JPanel();
        what = new JLabel("");
        what.setToolTipText("what?");
		p.add(what);
		
		
        submit = new JButton("Insert Pickup");
        submit.setToolTipText("Adds a new row to the pickup table with parameters in text box and dropdown menus. Configure these correctly before clicking.");
        ListenForButton lfb = new ListenForButton();
        submit.addActionListener(lfb);
        p.add(submit);
        

        tf1 = new JTextField("",20);
        tf1.setToolTipText("put in the weight");
        p.add(tf1);

        String[] weight_types = {"lbs", "tons"};
        this.weightType = new JComboBox(weight_types);
        p.add(weightType);

        
        
        
        // String[] waste_types = {"Recycling", "Trash", "Cardboard", "C&D", "Food", "E-waste", "Glass", "Green"};
        // this.waste_type = new JComboBox(waste_types);
        // p.add(this.waste_type);
        
        

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
            this.waste_type = new JComboBox(waste_types);
            p.add(this.waste_type);


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }





        // String[] companies = {"ACE", "METech", "METech Recycling"};
        // this.company  = new JComboBox(companies);
        // company.setSize(40, 40);
        // p.add(this.company);
        
        
        // String[] sites = {"Westminster//RYC-Recycling", "Westminster//East", "Westminster//West", "Westminster//Hogle", "Garfield Home"}; 
        // this.site = new JComboBox(sites);
        // p.add(this.site); 


        
        
        Date todaysDate = new Date();
        year = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(year, "MM/dd/yy");
        year.setEditor(dateEditor);
        date = year.getValue().toString();
        p.add(year);
        
        // setupLayout();
        // addListeners();

        // p.add(new JScrollPane(table), BorderLayout.SOUTH);
        p.add(new JScrollPane(aggroTable), BorderLayout.SOUTH);

        

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(p);
        // setupTable();
        // displayPickups("2013-09-23","2018-12-31", "date", true);
        displayAggro("2013-01-01","2018-12-31", "sum", false);
        // displayPickups();
        // displayAggro();
        
        this.setVisible(true);
        submit.requestFocus();

        
    }
    
    private void setupPickups() {
        // add(new JScrollPane(table), BorderLayout.SOUTH);
        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        south.add(new JScrollPane(table), BorderLayout.SOUTH);
        this.add(south);
        // south.setLayout(new );
    }


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
            ResultSet pResults = s.executeQuery("select Pickup.id, Pickup.weight, Pickup.date, Site.name as sname, Waste_Type.name as wname, Company.name as cname from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id where Pickup.date > '" + startDate + "' and Pickup.date < '" + endDate + "' order by " + order + desc);
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
            ResultSet aResults = s.executeQuery("select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id where pickup.date > '" + startDate + "' and pickup.date < '" + endDate + "' group by waste_type.name order by " + order + desc);
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

    

    private class ListenForButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == submit) {
				try {
                    weight = Double.parseDouble(tf1.getText());
				}
				catch (NumberFormatException excep) {
					JOptionPane.showMessageDialog(waste.this, "input weight as a number", "Invalid Weight", JOptionPane.ERROR_MESSAGE);
//					System.exit(0);
                }
                if (weightType.getSelectedItem().toString().equals("tons")){
                    weight *= 2000;
                }

				date = year.getValue().toString();
//				what.setText(date);
//				
				//get date data as integers
				String input, month, day, year, tempMonth; 
				  input = date; 
				  tempMonth = input.substring(4, 7);
				  day = input.substring(8, 10); 
				  year = input.substring(24, 28); 
				  month = ""; 
				  
				  if (tempMonth.equals("Jan")){month = "01";}
				  if (tempMonth.equals("Feb")){month = "02";} 
				  if (tempMonth.equals("Mar")){month = "03";}  
				  if (tempMonth.equals("Apr")){month = "04";}  
				  if (tempMonth.equals("May")){month = "05";}
				  if (tempMonth.equals("Jun")){month = "06";}
				  if (tempMonth.equals("Jul")){month = "07";}
				  if (tempMonth.equals("Aug")){month = "08";}  
				  if (tempMonth.equals("Sep")){month = "09";}  
				  if (tempMonth.equals("Oct")){month = "10";}  
				  if (tempMonth.equals("Nov")){month = "11";}
				  if (tempMonth.equals("Dec")){month = "12";}  
				  
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
		}

	}
}
