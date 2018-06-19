package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class OverwriteSaveCommand extends AbstractCommand {


    public OverwriteSaveCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.saveContent(cmdModel.getAddress(),cmdModel.getParam(), true);
    }
}
