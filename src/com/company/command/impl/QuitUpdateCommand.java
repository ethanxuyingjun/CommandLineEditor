package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class QuitUpdateCommand extends AbstractCommand{


    public QuitUpdateCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.quitUpdate();
    }
}
