
import java.sql.*;
import java.awt.*;
import java.awt.event.*; //user clicking buttons
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

//import swing1.s1.ListenForButton;

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
    
    //bellow is variables that store integer values derived from inputed data
    private String dayValue;
    private String monthValue;
    private String yearValue;
    private int companyId;
    private int siteId;
    private int wasteTypeId;
    private double weight;
    
    private static final String DB_URL = "jdbc:sqlite:dataWaste.db";

    public waste(){

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

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(p);
        this.setVisible(true);
        submit.requestFocus();
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
