package uac.imsp.clockingapp.View.util;


public interface IRegisterEmployeeView {
    void onRegisterEmployeeSuccess(String message);
    void onRegisterEmployeeError(String message);
    void sendEmail(String [] to, String subject, String message,
                   String qrCodeFileName);

}

