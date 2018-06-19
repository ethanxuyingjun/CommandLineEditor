package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class UndoCommand extends AbstractCommand{


    public UndoCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.undoOperation();

    }
}
