package uac.imsp.clockingapp.Controller.control;

import android.content.Context;

import uac.imsp.clockingapp.Controller.util.IClockInOutController;
import uac.imsp.clockingapp.Models.dao.ClockingManager;
import uac.imsp.clockingapp.Models.entity.Day;
import uac.imsp.clockingapp.Models.dao.DayManager;
import uac.imsp.clockingapp.Models.entity.Employee;
import uac.imsp.clockingapp.Models.dao.EmployeeManager;
import uac.imsp.clockingapp.View.util.IClockInOutView;

public class ClockingInOutController
        implements IClockInOutController {

private  IClockInOutView clockInOutView;
    public ClockingInOutController(IClockInOutView clockInOutView) {

        this.clockInOutView=clockInOutView;
        clockInOutView.onLoad( "Scannez votre code QR pour pointer");



    }

        @Override
    public void onClocking(int number) {
        //Take  time into account later
            Employee employee;
            Day day;
            EmployeeManager employeeManager;
            ClockingManager clockingManager;
            DayManager dayManager;

            employee = new Employee(number);
            //connection to  database , employee table
            employeeManager = new EmployeeManager((Context) clockInOutView);
            //open employee connection
            employeeManager.open();
            day=new Day();
            dayManager=new DayManager((Context) clockInOutView);
            dayManager.open();
            //A day is created and has an id
            dayManager.create(day);


            if (!employeeManager.exists(employee))
                //employee does not exist
                clockInOutView.onClockingError("Employé non retrouvé");

            else //The employee exists
            {

                //check if the employee shouldWorkToday or not
                //case not
                if (employeeManager.shouldNotWorkToday(employee))
                    clockInOutView.onClockingError("Vous n'êtes pas censé travailler aujourd'hui");
                else {

                    //Connection to clocking table
                    clockingManager = new ClockingManager((Context) clockInOutView);
                    //open  clocking connection
                    clockingManager.open();
                    if (clockingManager.hasNotClockedIn(employee, day)) {


                        clockingManager.clockIn(employee, day);

                        clockInOutView.onClockingSuccessful("Entrée marquée avec succès");
                    } else if (clockingManager.hasNotClockedOut(employee, day)) {
                        clockingManager.clockOut(employee, day);
                        clockInOutView.onClockingSuccessful("Sortie marquée avec succès");
                    } else {
                        clockInOutView.onClockingError("Vous avez déjà marqué votre entrée-sortie de la journée");
                    }
                    //close  clocking connection
                    clockingManager.close();

                }
            }
            //close employee management connection
            //employeeManager.close();

        }

}
