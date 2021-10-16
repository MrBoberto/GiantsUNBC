import javax.swing.*;

public class main {


   // static Thread thread1 = new Thread("Box 1 thread");
    public static void main(String[] args) {

        Thread thread1 = new Thread(new Window(600,600));
        thread1.setName("Wow");
        thread1.start();




    }

}
