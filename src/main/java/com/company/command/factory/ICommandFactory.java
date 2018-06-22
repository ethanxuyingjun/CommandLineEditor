package com.company.command.factory;

import com.company.command.ICommand;

public interface ICommandFactory {

    ICommand createCommand(String commandType);
}
