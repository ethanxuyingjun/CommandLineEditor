package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class PrintCommand extends AbstractCommand{

    public PrintCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.printContent(cmdModel.getAddress());
    }
}
