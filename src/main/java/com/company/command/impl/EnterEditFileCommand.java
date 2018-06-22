package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class EnterEditFileCommand extends AbstractCommand{


    public EnterEditFileCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.enterEditFile(cmdModel.getParam());
    }
}
