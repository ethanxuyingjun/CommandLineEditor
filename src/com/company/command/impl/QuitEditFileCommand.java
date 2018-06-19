package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class QuitEditFileCommand extends AbstractCommand{


    public QuitEditFileCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.quitEditFile(false);
    }
}
