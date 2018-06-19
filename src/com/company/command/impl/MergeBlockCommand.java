package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class MergeBlockCommand extends AbstractCommand{


    public MergeBlockCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.mergeContent(cmdModel.getAddress());

    }
}
