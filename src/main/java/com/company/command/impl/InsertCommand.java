package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class InsertCommand extends AbstractCommand {


    public InsertCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.insertContent(cmdModel.getAddress());
    }
}
