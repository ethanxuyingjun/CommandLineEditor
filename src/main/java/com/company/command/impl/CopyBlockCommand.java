package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class CopyBlockCommand extends AbstractCommand{


    public CopyBlockCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.copyBlock(cmdModel.getAddress(), cmdModel.getParamSet());
    }
}
