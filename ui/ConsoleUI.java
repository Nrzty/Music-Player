package musicPlayer.ui;

import musicPlayer.utils.UserInputs;

public class ConsoleUI {

    UserInputs userInputs;

    public ConsoleUI(){
        this.userInputs = new UserInputs();
    }

    public void displayOptions(){
        System.out.println(
                """
                             __          _              ___          \s
                          /\\ \\ \\_ __ ___| |_ _   _     /   \\_____   __
                         /  \\/ / '__|_  / __| | | |   / /\\ / _ \\ \\ / /
                        / /\\  /| |   / /| |_| |_| |  / /_//  __/\\ V /\s
                        \\_\\ \\/ |_|  /___|\\__|\\__, | /___,' \\___| \\_/ \s
                                             |___/                   \s
                        --------------------------------------------
                        1. Add Song
                        2. Remove Song
                        3. Show All Songs
                        4. Exit
                        --------------------------------------------
                        """
        );
    }

    public void startProgram(){
        boolean continueLooping = true;
        while(continueLooping){
            displayOptions();
            String inputOption = userInputs.getStringInput("Select an option: ");

            if (inputOption.equals("4")){
                continueLooping = false;
                System.out.println("Exiting program...");
            } else {
                switch (inputOption){
                    case "1":
                        // add song
                        break;
                    case "2":
                        // remove song
                        break;
                    case "3":
                        // show all songs
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }
}
