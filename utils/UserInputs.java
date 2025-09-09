package musicPlayer.utils;

import java.util.Scanner;

public class UserInputs {

    Scanner scanner;

    public UserInputs(){
        this.scanner = new Scanner(System.in);
    }

    public String getStringInput(String inputMessage){
        System.out.print(inputMessage);
        return scanner.nextLine();
    }

    public void waitForEnterInput(){
        System.out.print("\n" + "Press Enter to continue: ");
        scanner.nextLine();
    }
}
