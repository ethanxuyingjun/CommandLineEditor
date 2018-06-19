package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class MoveLineCommand extends AbstractCommand {


    public MoveLineCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.moveContent(cmdModel.getAddress(), cmdModel.getParam());

    }
}
