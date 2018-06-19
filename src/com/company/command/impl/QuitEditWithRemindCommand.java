package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class QuitEditWithRemindCommand extends AbstractCommand {

    public QuitEditWithRemindCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.quitEditFile(true);
    }
}
