package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class CopyCommand extends AbstractCommand {

    public CopyCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.copyContent(cmdModel.getAddress());
    }
}
