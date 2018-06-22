package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class DeleteCommand extends AbstractCommand{

    public DeleteCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.deleteContent(cmdModel.getAddress());

    }
}
