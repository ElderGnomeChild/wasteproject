import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.JOptionPane;

// import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import javafx.css.CssParser.ParseError;

public class ReadDb {

    private static final String DB_URL = "jdbc:sqlite:dataWaste.db";

    public static void main(String... args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            System.out.println("What do you want to do?");
            System.out.print("Enter 'insert' or 'query': ");
            Scanner bepis = new Scanner(System.in);
            String option = bepis.nextLine();
            // String query = "query";

            if(option.equals("query")){
                System.out.print("Pickup Number: ");
                Scanner scanner = new Scanner(System.in);
                int code = scanner.nextInt();

                PreparedStatement statement = conn.prepareStatement(
                    // "select Pickup.weight, Pickup.date, Waste_Type.name as wname, Company.name as cname, Site.name as sname" +
                    // "from Pickup join Waste_Type on Waste_Type.id == Pickup.waste_type_id" + 
                    // "join Company on Company.id == Pickup.company_id join Site on Site.id == Pickup.site " +
                    // "where id == " + code

                    // "select weight, date from Pickup where id = " + code 

                    "select Pickup.date, Pickup.weight, Company.name as cname, Site.name as sname, Waste_Type.name as wname from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id where Pickup.id = " + code
                );

                ResultSet results = statement.executeQuery();
                String weight, date, wname, cname, sname;
                while (results.next()) {
                    weight = results.getString("weight");
                    date = results.getString("date");
                    wname = results.getString("wname");
                    cname = results.getString("cname");
                    sname = results.getString("sname");
                    System.out.println("weight: " + weight + "      date: " + date + "      Type: " + wname + "      Company: " + cname + "      Site: " + sname);
                    // System.out.println();
                }
            }

            else if(option.equals("insert")){
                System.out.println("Which table to insert to?");
                System.out.print("Enter 'pickup', 'company', 'site', or 'wastetype': ");

                Scanner scanner = new Scanner(System.in);
                String code = scanner.nextLine();

                if(code.equals("pickup")) {
                    PreparedStatement eyedee = conn.prepareStatement(
                      "select id from pickup order by id desc limit 1"  
                    );

                    ResultSet i = eyedee.executeQuery();

                    int id = i.getInt("id");   
                    id++;

                    System.out.println("Enter pickup id, weight, site id, waste type id, and company id");
                    // System.out.print("id: ");
                    // int id = scanner.nextInt();
                    System.out.print("weight: ");
                    double weight = scanner.nextDouble();
                    System.out.print("waste type (id): ");
                    int wid = scanner.nextInt();

                    System.out.print("year (YYYY): ");
                    // DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    int year = scanner.nextInt();

                    System.out.print("month (MM): ");
                    int month = scanner.nextInt();

                    System.out.print("day (DD): ");
                    int day = scanner.nextInt();
                    String doot = year+"-"+month+"-"+day;

                    System.out.print("company (id): ");
                    int cid = scanner.nextInt();
                    System.out.print("site (id): ");
                    int sid = scanner.nextInt();

                    

                    PreparedStatement statement = conn.prepareStatement(
                        "insert into pickup values(" + id + ", " + weight + ", " + "'" +doot+ "'" + ", " + sid + ", " + wid +", " + cid +")"
                    );

                    statement.execute();
                    System.out.println("done");
                } 

                if(code.equals("company")) {
                    PreparedStatement eyedee = conn.prepareStatement(
                      "select id from company order by id desc limit 1"  
                    );

                    ResultSet i = eyedee.executeQuery();

                    int id = i.getInt("id");   
                    id++;
                }

                else{
                    System.out.println("did not detect valid input");
                }

            }

            else {System.out.println("did not detect");}
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "DIDNT WORK RIP", "it be like that sometimes", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


}