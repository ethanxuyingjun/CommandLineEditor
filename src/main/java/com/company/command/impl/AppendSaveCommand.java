package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class AppendSaveCommand extends AbstractCommand {

    public AppendSaveCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.saveContent(cmdModel.getAddress(), cmdModel.getParam(), false);
    }
}
