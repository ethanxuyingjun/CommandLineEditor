package com.company.command.impl;

import com.company.command.ICommand;
import com.company.recevier.ITextLineFile;
import com.company.recevier.impl.TextLineFile;

public class PrintLineCommand extends AbstractCommand {


    public PrintLineCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.printLineNumber(cmdModel.getAddress());
    }
}
