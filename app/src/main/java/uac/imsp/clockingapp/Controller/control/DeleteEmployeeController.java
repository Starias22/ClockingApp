package uac.imsp.clockingapp.Controller.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.util.Hashtable;
import java.util.Objects;

import dao.EmployeeManager;
import dao.PlanningManager;
import entity.Day;
import entity.Employee;
import entity.Planning;
import entity.Service;
import uac.imsp.clockingapp.Controller.util.IDeleteEmployeeController;
import uac.imsp.clockingapp.View.util.IDeleteEmployeeView;

public class DeleteEmployeeController implements IDeleteEmployeeController {
    IDeleteEmployeeView deleteEmployeeView;
    private Employee employee;
    private EmployeeManager employeeManager;
    private final Context context;

    public DeleteEmployeeController(IDeleteEmployeeView deleteEmployeeView)


    {
        this.deleteEmployeeView=deleteEmployeeView;
        this.context=(Context) this.deleteEmployeeView;


    }
    public DeleteEmployeeController(IDeleteEmployeeView deleteEmployeeView,
                                    Context context){
        this.deleteEmployeeView=deleteEmployeeView;
        this.context=context;

    }
    @Override
    //get informations on load

    public  void onLoad(int number, @NonNull Hashtable <String,Object> informations) {


        Day day;
String birthdate;
        Service service;

        Planning planning;
        PlanningManager planningManager ;


        employee=new Employee(number);
        employeeManager=new EmployeeManager(context);
        employeeManager.open();
        employeeManager.setInformations(employee);
        service =employeeManager.getService(employee);
        employeeManager.close();

        planningManager = new PlanningManager(context);
        planningManager.open();
        planning=employeeManager.getPlanning(employee);
        planningManager.close();


        informations.put("number",String.valueOf(employee.getRegistrationNumber()));
        informations.put("lastname",employee.getLastname());
        informations.put("firstname",employee.getFirstname());

        try {

            informations.put("picture", getBitMapFromBytes(employee.getPicture()));
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        informations.put("email",employee.getMailAddress());
        informations.put("username",employee.getUsername());
        informations.put("gender",employee.getGender());
        try {
            day = new Day(employee.getBirthdate());
            birthdate=day.getFrenchFormat();

        }
        catch (NullPointerException e){
            birthdate="";
            informations.put("birthdate",birthdate);
            e.printStackTrace();

        }
        informations.put("birthdate",birthdate);
        informations.put("type",employee.getType());
        informations.put("service",service.getName());
        informations.put("start",Integer.parseInt(Objects.requireNonNull(planning.extractHours().get("start"))));
        informations.put("end",Integer.parseInt(Objects.requireNonNull(planning.extractHours().get("end"))));

    }



    @Override
    public void onDeleteEmployee() {

        deleteEmployeeView.askConfirmDelete("Oui","Non", "Confirmation",
                "Voulez vous vraiment supprimer l'employé ?");




    }

    @Override
    public void onConfirmResult(Boolean confirmed) {


        if(!confirmed)
            return;
        employeeManager=new EmployeeManager(context);
        employeeManager.open();
        employeeManager.delete(employee);



        employeeManager.close();

            deleteEmployeeView.onDeleteSuccessfull();

    }
    public static Bitmap getBitMapFromBytes(byte[] array){

        return BitmapFactory.decodeByteArray(array,0,array.length);

    }
}
