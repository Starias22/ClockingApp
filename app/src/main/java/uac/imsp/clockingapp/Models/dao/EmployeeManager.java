package uac.imsp.clockingapp.Models.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

import uac.imsp.clockingapp.Models.dbAdapter.EmployeeSQLite;
import uac.imsp.clockingapp.Models.entity.Day;
import uac.imsp.clockingapp.Models.entity.Employee;
import uac.imsp.clockingapp.Models.entity.Planning;
import uac.imsp.clockingapp.Models.entity.Service;

public class EmployeeManager {
    public final static int   CAN_NOT_LOGIN=15;
    private SQLiteDatabase Database=null;
    private final EmployeeSQLite employeeSQLite;
    //private Context context;



    public EmployeeManager(Context context) {
        employeeSQLite= new EmployeeSQLite(context);
    }
    public SQLiteDatabase open() {
        if(Database==null)
         Database=employeeSQLite.getWritableDatabase();
        return Database;
    }

    public void close(){
        if (Database!=null && Database.isOpen())
            Database.close();
        }

public int connectUser(Employee employee, String password){
        int n;
        String str;
        int number;

        String query="SELECT matricule,password FROM employe WHERE username=?";

        String [] selectArgs={employee.getUsername()};

        Cursor cursor = Database.rawQuery(query,selectArgs);
                 cursor.moveToFirst();
                 n=cursor.getCount();
                 number=cursor.getInt(0);
                 str=cursor.getString(1);
                 cursor.close();
        if(n==1 && str.equals(password)) {
            employee.setRegistrationNumber(number);

            //employee=new Employee(number);
            setInformations(employee);
            return 0;
        }

        return CAN_NOT_LOGIN;

}




    //create,delete,update

    public void create (Employee employee){
      // open();
        SQLiteStatement statement;
        String query = "INSERT INTO employe (matricule,nom,prenom,sexe,birthdate," +
                "couriel,qr_code,photo,username,password,type) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";

                statement=Database.compileStatement(query);

        statement.bindLong(1,employee.getRegistrationNumber());

        statement.bindString(2,employee.getLastname());
        statement.bindString(3,employee.getFirstname());
        statement.bindString (4, Character.toString((char) employee.getGender()));
        statement.bindString(5,employee.getBirthdate());
        statement.bindString(6,employee.getMailAddress());
        statement.bindBlob(7, employee.getQRCode());
         statement.bindBlob(8,employee.getPicture());
        statement.bindString(9,employee.getUsername());
        statement.bindString(10,employee.getPassword());
        statement.bindString(11,employee.getType());

        statement.executeInsert();

    }
//On peut modifier le courier ou la photo de l'employé


    //Pour modifier le courier de l'employé
    public void update (Employee employee, String mailAddress){

        String query="UPDATE employe SET couriel =? WHERE matricule=?";
        SQLiteStatement statement  ;
                statement=Database.compileStatement(query);
        statement.bindString(1, mailAddress);
        statement.bindLong(2, employee.getRegistrationNumber());
    }


    public void changeGrade (Employee employee, String type){

        String query="UPDATE employe SET type =? WHERE matricule=?";
        SQLiteStatement statement  ;
        statement=Database.compileStatement(query);
        statement.bindString(1, type);
        statement.bindLong(2, employee.getRegistrationNumber());
    }

    //Pour modifier la photo de l'employé
    public void update (Employee employee, byte[] picture){
        String query="UPDATE employe SET photo =? WHERE matricule=?";
        SQLiteStatement statement ;
        statement=Database.compileStatement(query);
        statement.bindBlob(1, picture);
        statement.bindLong(2, employee.getRegistrationNumber());
        statement.executeUpdateDelete();
    }

    public Planning getPlanning(Employee employee){


        //open();
        //EmployeeManager e = new EmployeeManager(())
        Planning planning=null ;
        String query="SELECT heure_debut_officielle,heure_fin_officielle " +
                "FROM planning  JOIN employe ON id_planning=id_planning_ref " +
                "WHERE matricule=?";
        String[] selectArgs={String.valueOf(employee.getRegistrationNumber())};
        Cursor cursor = Database.rawQuery(query,selectArgs);
        if(cursor.moveToFirst())

        planning = new Planning(cursor.getString(0),
                                cursor.getString(1));
        cursor.close();
        return planning;
    }
    public Service getService(Employee employee){
        Service service ;
        String query="SELECT service.nom  " +
                "FROM employe JOIN service ON id_service=id_service_ref " +
                "WHERE matricule=?";
        String[] selectArgs={String.valueOf(employee.getRegistrationNumber())};
        Cursor cursor = Database.rawQuery(query,selectArgs);
        cursor.moveToFirst();
        service = new Service(cursor.getString(0));

        cursor.close();
        return service;
    }


    public void delete(Employee employee){
        String query = "DELETE FROM employe WHERE matricule=?";
        SQLiteStatement statement ;

        statement = Database.compileStatement(query);
        statement.bindLong(1,employee.getRegistrationNumber());
        statement.executeUpdateDelete();
    }


//Pour rechercher un employé

/*On peut rechercher par matricule,nom,prénom,sexe,date de naissance ou mail
//Cette méthode prend la donnée à rechercher et retourne
un tableau contenant les emplyés vérifiant le motif de recherche*/

    //c'est une méthode de classe
    public  Employee[] search(String data){
                String query="SELECT matricule, employe.nom,prenom,photo " +
                "FROM employe JOIN service ON id_service=id_service_ref " +
                "WHERE  matricule||employe.nom||prenom||service.nom " +
                "LIKE '%'||?||'%'";


                String [] selectArg={data};
        ArrayList <Employee> employeeSet= new ArrayList<>();
        Employee employee;
        Cursor cursor=Database.rawQuery(query,selectArg);
        //cursor.moveToFirst();

        while (cursor.moveToNext())
        {
            employee = new Employee(cursor.getInt(0));
            employee.setLastname(cursor.getString(1));
            employee.setFirstname(cursor.getString(2));
            employee.setPicture(cursor.getBlob(3));
            employeeSet.add(employee);
        }
        cursor.close();
        return employeeSet.toArray(new Employee[employeeSet.size()]);

    }


    public boolean exists(@NonNull Employee employee){
     int n;
       String query ="SELECT matricule FROM employe WHERE matricule=? OR username=? OR couriel=?";
       String [] selectArg={Integer.valueOf(employee.getRegistrationNumber()).toString(),
                             employee.getUsername(),
               employee.getMailAddress()
       };
        Cursor cursor = Database.rawQuery(query,selectArg);

             n=cursor.getCount();
             cursor.close();
        return n==1;


    }


    public void setInformations(Employee employee){
       // open();

        String query;
        String [] selectArgs;
        Cursor cursor;


            query = "SELECT nom,prenom,sexe,photo,type,couriel," +
                    "username,birthdate FROM employe WHERE matricule=?";
            selectArgs= new String[]{
                    Integer.valueOf(employee.getRegistrationNumber()).toString()
            };




         cursor =Database.rawQuery(query,selectArgs);
       if( cursor.moveToFirst()) {
           //employee.setRegistrationNumber();
           employee.setLastname(cursor.getString(0));
           employee.setFirstname(cursor.getString(1));
           employee.setGender(cursor.getString(2).charAt(0));

           employee.setPicture(cursor.getBlob(3));
           employee.setType(cursor.getString(4));
           employee.setMailAddress(cursor.getString(5));
           employee.setUsername(cursor.getString(6));
           employee.setBirthdate(cursor.getString(7));
          // employee.setRegistrationNumber(8);

       }
       cursor.close();
      // close();

    }




    public void update(@NonNull Employee employee, @NonNull Service service){
        String query="UPDATE employe SET id_service_ref=? WHERE matricule=?";
        SQLiteStatement statement=Database.compileStatement(query);
               statement.bindLong(1,service.getId());
               statement.bindLong(2,employee.getRegistrationNumber());
               statement.executeUpdateDelete();

    }
    public void update(@NonNull Employee employee, Planning planning){
        String query="UPDATE employe SET id_planning_ref=? WHERE matricule=?";
        SQLiteStatement statement=Database.compileStatement(query);
       statement.bindLong(1,planning.getId());

        statement.bindLong(2,employee.getRegistrationNumber());
        statement.executeUpdateDelete();

    }
    //Retourne un tableau conteneant le matricule de tous les employés
    public String[] getAllEmployees(){
        ArrayList<String> employee= new ArrayList<>();

        String query="SELECT matricule FROM employe ORDER BY matricule ";
        Cursor cursor=Database.rawQuery(query,null);
        while (cursor.moveToNext())
            employee.add(cursor.getString(0));
        cursor.close();

        return employee.toArray(new String[employee.size()]);



    }
    public Hashtable<String,Double> getStatisticsByService(String start,String end){

        String service;
        int count;
        int total;
        double frequence;

        Hashtable<String,Double> row = new Hashtable<>();
        String query="SELECT nom, COUNT(*) FROM" +
                " (SELECT * FROM employe " +
                "JOIN pointage AS relation ON matricule = matricule_ref) " +
                " JOIN jour ON id_jour= relation.id_jour_ref " +
                "WHERE heure_entree IS NOT NULL AND date_jour BETWEEN ? AND ? "+
                "GROUP BY nom ";
        String [] selectArgs={start,end};
        total=getAllEmployees().length;
        Cursor cursor=Database.rawQuery(query,selectArgs);
        if(total!=0)
        {
            while (cursor.moveToNext()) {
                service = cursor.getString(0);
                count = cursor.getInt(2);
                frequence=count/total;
                row.put(service, frequence);
            }
        }
        cursor.close();
        return  row;
    }
   // presence report in a month for an employee (satursday and sunday aren't concerned)
public Hashtable<Day,Character> getPresenceReportForEmployee(
        Employee employee,int month)
        {

            String off=getPlanning(employee).getStartTime();
            String date;
            Day day;

            Hashtable<Day,Character> report = new Hashtable<>();
        Cursor cursor;

        String query="SELECT  date_jour  , heure_entree" +
                                        "  FROM (SELECT * FROM employe " +

                       "JOIN pointage AS relation ON matricule = matricule_ref) " +
                         " JOIN jour ON id_jour= R.id_jour_ref " +
                        "WHERE  matricule=? AND STRFTIME('%m',date_jour)=? " +
                       "AND STRFTIME('%Y',date_jour) =STRFTIME('%Y','NOW')";

    String [] selectArgs={
            String.valueOf(employee.getRegistrationNumber()),
            String.valueOf(month)
    };

     cursor=Database.rawQuery(query,selectArgs);

     while(cursor.moveToNext()){


         date=cursor.getString(0);//the date
         day=new Day(date);

       if((Integer.parseInt(date.split("-")[0]) <=
               Integer.parseInt(off.split("[-]")[0]))  ||
               (Integer.parseInt(date.split("-")[0]) ==
               Integer.parseInt(off.split("[-]")[0])&&
                       (Integer.parseInt(date.split("-")[1]) <=
                       Integer.parseInt(off.split("[-]")[1])) ))

           report.put(day,'P');
       else
           report.put(day,'R');


     }
     cursor.close();
return  report;
}

    public int  getDayNumberInWeek(String date){
        String str;
        String query="SELECT STRF('%w',?)";
        Cursor cursor = Database.rawQuery(query,
                new String[]{date}
        );
        cursor.moveToFirst();
        str=cursor.getString(0);
        cursor.close();
        return Integer.parseInt(str);

    }

    public boolean isNotSuperUser(Employee employee){
        boolean test;
        String query="SELECT type FROM employe WHERE matricule=?";
        String [] selectArgs= new String[]{String.valueOf(employee.getRegistrationNumber())};
       Cursor cursor=Database.rawQuery(query,selectArgs);
       test=cursor.moveToFirst() && !Objects.equals(cursor.getString(0), "Simple");
       cursor.close();
       return !test;


    }



}

