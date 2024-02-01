import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;

public class HRSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hm_system";
    private static final String username = "root";
    private static final String password = "2002priyanshu";
    public static void main(String[] args) throws ClassNotFoundException, SQLException  {
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
        Connection connection = DriverManager.getConnection(url, username, password);
        Scanner scanner = new Scanner(System.in);
        
            while (true) {
                System.out.println();
                System.out.println("+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+");
                System.out.println("<<<< HOTEL RESERVATION SYSTEM >>>>");
                System.out.println("1. NEW RESERVATION ");
                System.out.println("2. VIEW RESERVATION ");
                System.out.println("3. GET ROOM NUMBER ");
                System.out.println("4. UPDATE RESERVATION ");
                System.out.println("5. DELETE RESERVATION ");
                System.out.println("0. EXIT ");
                System.out.print("CHOOSE AN OPTION: ");

                int input = scanner.nextInt();
                    switch (input) {
                        case 1:
                            newReservation(connection, scanner);
                            break;
                        case 2:
                            viewReservation(connection);
                            break;
                        case 3:
                            getRoomNo(connection, scanner);
                            break;
                        case 4:
                            updateReservation(connection, scanner);
                            break;
                        case 5:
                            deleteReservation(connection, scanner);
                            break;
                        case 0:
                            exit();
                            break;
                        default:
                            System.out.println("INVALID OPTION!!!!");
                            break;
                        }   
                    }
             }catch(SQLException e){
                System.out.println(e.getMessage());
                }
            }


    private static void newReservation(Connection connection, Scanner scanner) throws SQLException{
       System.out.print("ENTER GUEST NAME:- ");
       String guest_name = scanner.next();
       System.out.println();
       System.out.print("ENTER GUEST's PHONE NUMBER:- ");
       String phone_number = scanner.next();
       System.out.println();
       System.out.print("ENTER ROOM NUMBER:- ");
       int room_number = scanner.nextInt();

       String sql = "INSERT INTO reservations (guest_name, phone_number, room_number) VALUES('"+guest_name+"','"+phone_number+"','"+room_number+"');";
       try{
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0 ) {
             System.out.println("RESERVATION SUCCESSFULL");
            }else{
                System.out.println("RESERVATION UNSUCCESSFUL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection) throws SQLException{
        String sql = "SELECT reservation_id, guest_name, phone_number, room_number, reservation_date FROM reservations";
    
        try {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                String guest_name = resultSet.getString("guest_name");
                String phone_number = resultSet.getString("phone_number");
                int room_number = resultSet.getInt("room_number");
                Timestamp reservation_date = resultSet.getTimestamp("reservation_date");

                System.out.println("RESERVATION ID :- "+reservation_id);
                System.out.println("GUEST NAME :- "+guest_name);
                System.out.println("PHONE NUMBER :- "+phone_number);
                System.out.println("ROOM NUMBER :- "+room_number);
                System.out.println("RESERVATION DATE & TIME :- "+reservation_date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void getRoomNo(Connection connection, Scanner scanner) throws SQLException{
        System.out.print("ENTER RESERVATION ID :- ");
        int reservation_id = scanner.nextInt();
        System.out.println();
        System.out.print("ENTER GUEST NAME :- " );
        String guest_name = scanner.next();

        String sql = "SELECT room_number FROM reservations WHERE reservation_id = "+reservation_id+" AND guest_name = '"+guest_name+"'";

        try{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            int room_number = resultSet.getInt("room_number");
            System.out.println("ROOM NUMBER IS :- "+room_number);
        }
        else{
            System.out.println("NO RESERVATION FOUND FOR THE RESERVATION ID "+reservation_id+" AND GUEST NAME "+guest_name);
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection,Scanner scanner) throws SQLException{
        System.out.print("ENTER RESERVATION ID :- ");
        int reservation_id = scanner.nextInt();
        Statement statement = connection.createStatement();
        if(!reservatioExists(connection, reservation_id)){
            System.out.println("RESERVATION NOT FOUNT!!!!");
            return;
        }
        System.out.print("ENTER GUEST NAME :- ");
        String guest_name = scanner.next();
        System.out.println();
        System.out.print("ENTER GUEST's PHONE NUMBER :- ");
        String phone_number = scanner.next();
        System.out.println();
        System.out.print("ENTER ROOM NUMBER :- ");
        int room_number = scanner.nextInt();

        String sql = "UPDATE reservations SET guest_name = '"+guest_name+"'," + "room_number ="+room_number+","+"phone_number = '" +phone_number+"' "+"WHERE reservation_id = "+reservation_id;
        try{
        int rowsAffected = statement.executeUpdate(sql);
        
            if(rowsAffected>0){
                 System.out.println("RESERVATION UPDATE SUCCESSFUL");
            }else{
                System.out.println("RESERVATION UPDATE UNSUCCESSFUL!!!!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void deleteReservation(Connection connection,Scanner scanner) throws SQLException{
        System.out.print("ENTER RESERVATION ID :-");
        int reservation_id = scanner.nextInt();
        Statement statement = connection.createStatement();
        if (!reservatioExists(connection, reservation_id)) {
            System.out.println("RESERVATION NOT FOUND!!!!");
            return;            
        }
        String sql = "DELETE FROM reservations WHERE reservation_id = "+reservation_id;
        try{
            int rowsAffected = statement.executeUpdate(sql);

            if(rowsAffected>0){
                System.out.println("RESERVATION DELETED SUCCESSFUL");
            }else{
                System.out.println("RESERVATION DELETED USUCCESSFUL");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static boolean reservatioExists(Connection connection, int reservation_id) throws SQLException{
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservation_id;
        Statement statement = connection.createStatement();
        try{
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    private static void exit(){
        System.out.println("EXIT SUCCESSFUL");
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
}
