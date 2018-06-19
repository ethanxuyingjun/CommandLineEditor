package com.company.command.factory.impl;

import com.company.command.ICommand;
import com.company.command.factory.ICommandFactory;
import com.company.command.impl.*;
import com.company.common.enumeration.CommandActionEnum;
import com.company.command.model.CommandModel;
import com.company.recevier.impl.FileModeEnum;
import com.company.recevier.impl.TextLineFile;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements ICommandFactory {

    private static class SingletonHolder{
        public static CommandFactory instance = new CommandFactory();
    }
    private CommandFactory(){}

    public static CommandFactory newInstance(){
        return SingletonHolder.instance;
    }

    private Map commandMaps = new HashMap<CommandActionEnum, ICommand>();


    @Override
    public ICommand createCommand(String inputStr) {

        ICommand cmd = null;
        TextLineFile fileIns = TextLineFile.newInstance();

        if (fileIns.getCurrentFileMode() == FileModeEnum.NOT_STARTED && !inputStr.startsWith(CommandActionEnum.ENTER_EDIT_FILE.getName())) {
            System.out.println("you are not in edit mode, please type 'ed [file] or ed' to enter edit mode");
            return null;
        }

        //if current is in edit text mode, just update content
        boolean isCommandLine = fileIns.getCurrentFileMode() == FileModeEnum.TEXT_EDIT && !inputStr.equals(CommandActionEnum.QUIT_UPDATE.getName()) ? false : true;

        CommandModel model = new CommandModel(inputStr, isCommandLine);

        //if command already exist just reuse with new model
        if (commandMaps.containsKey(model.getAction())) {
            ((ICommand) commandMaps.get(model.getAction())).setModel(model);
            return (ICommand) commandMaps.get(model.getAction());
        }

        if (!isCommandLine) {
            cmd = new UpdateContentCommand(fileIns);
        } else {
            switch (model.getAction()) {
                case ENTER_EDIT_FILE:
                    cmd = new EnterEditFileCommand(fileIns);
                    break;
                case QUIT_EDIT_FILE:
                    cmd = new QuitEditFileCommand(fileIns);
                    break;
                case QUIT_WITH_REMIND:
                    cmd = new QuitEditWithRemindCommand(fileIns);
                    break;
                case APPEND:
                    cmd = new AppendCommand(fileIns);
                    break;
                case INSERT:
                    cmd = new InsertCommand(fileIns);
                    break;
                case COPY:
                    cmd = new CopyCommand(fileIns);
                    break;
                case DELETE:
                    cmd = new DeleteCommand(fileIns);
                    break;
                case PRINT:
                    cmd = new PrintCommand(fileIns);
                    break;
                case PRT_LINE_NUM:
                    cmd = new PrintLineCommand(fileIns);
                    break;
                case MERGE_BLOCK:
                    cmd = new MergeBlockCommand(fileIns);
                    break;
                case REPLACE:
                    cmd = new ReplaceCommand(fileIns);
                    break;
                case BACKWARD_MOVE:
                    cmd = new MoveLineCommand(fileIns);
                    break;
                case UNDO:
                    cmd = new UndoCommand(fileIns);
                    break;
                case SET_FLAG:
                    cmd = new SetFlagCommand(fileIns);
                    break;
                case SET_FILENAME:
                    cmd = new SetFileNameCommand(fileIns);
                    break;
                case MOVE_BLOCK:
                    model.setParamSet(model.getParam());
                    cmd = new MoveBlockCommand(fileIns);
                    break;
                case COPY_BLOCK:
                    model.setParamSet(model.getParam());
                    cmd = new CopyBlockCommand(fileIns);
                    break;
                case SAVE_APPEND:
                    cmd = new AppendSaveCommand(fileIns);
                    break;
                case SAVE_OVERWRITE:
                    cmd = new OverwriteSaveCommand(fileIns);
                    break;
                case QUIT_UPDATE:
                    cmd = new QuitUpdateCommand(fileIns);
                    break;
                default:
                    System.out.println("No Command Found!");
            }
        }
        cmd.setModel(model);
        commandMaps.put(model.getAction(),cmd);
        return cmd;
    }
}
