package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class SetFlagCommand extends AbstractCommand {


    public SetFlagCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.setFlag(cmdModel.getAddress(), cmdModel.getParam());

    }
}
