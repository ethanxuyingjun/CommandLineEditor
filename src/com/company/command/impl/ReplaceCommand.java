package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class ReplaceCommand extends AbstractCommand {


    public ReplaceCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.replaceContent(cmdModel.getAddress(), cmdModel.getParam());

    }
}
