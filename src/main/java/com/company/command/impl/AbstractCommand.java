package com.company.command.impl;

import com.company.command.ICommand;
import com.company.command.model.CommandModel;
import com.company.recevier.ITextLineFile;

public abstract class AbstractCommand implements ICommand {

    ITextLineFile tf;

    CommandModel cmdModel;

    public AbstractCommand(ITextLineFile tf) {
        this.tf = tf;
    }

    @Override
    public void setModel (CommandModel cm) {
        cmdModel = cm;
    }

    @Override
    public abstract void execute();
}
