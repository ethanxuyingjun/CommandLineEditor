package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class SetFileNameCommand extends AbstractCommand {


    public SetFileNameCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.setDefaultFileName(cmdModel.getParam());
    }
}
