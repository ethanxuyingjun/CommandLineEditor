package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class AppendCommand extends AbstractCommand{

    public AppendCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.appendContent(cmdModel.getAddress());
    }
}
