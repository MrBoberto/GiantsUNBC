import javax.swing.*;

public class main {


   // static Thread thread1 = new Thread("Box 1 thread");
    public static void main(String[] args) {
        int choice = Integer.parseInt(JOptionPane.showInputDialog("Enter player number"));


        if(choice == 0){
            new ServerGame(choice);
            new ClientGame(choice);
        } else {
            new ClientGame(choice);
            new ServerGame(choice);
        }






    }

}
