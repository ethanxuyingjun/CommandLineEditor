package com.company;

import com.company.command.ICommand;
import com.company.command.factory.ICommandFactory;
import com.company.command.factory.impl.CommandFactory;

import java.util.Scanner;

public class EDLineEditor {

    public void action () {
        ICommandFactory cmdFactory = CommandFactory.newInstance();
        System.out.println("================== Welcome to ED line editor program, please input your command ================");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String inputStr = scanner.nextLine();
            //System.out.println("Your input: "+inputStr);

            if (inputStr.toLowerCase().equals("exit")) {
                break;
            }

            ICommand cmd = cmdFactory.createCommand(inputStr);
            if (cmd != null) {
                cmd.execute();
            }
        }
        System.out.println("===================== Exit ED Line Editor Program! ==========================");
    }

    public static void main(String[] args) {
        EDLineEditor editor = new EDLineEditor();
        editor.action();
    }
}
