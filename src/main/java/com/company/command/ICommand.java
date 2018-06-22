package com.company.command;

import com.company.command.model.CommandModel;

public interface ICommand {

    void execute();

    void setModel(CommandModel cm);
}