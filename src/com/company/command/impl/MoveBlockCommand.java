package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class MoveBlockCommand extends AbstractCommand {


    public MoveBlockCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.moveBlock(cmdModel.getAddress(), cmdModel.getParamSet());

    }
}
