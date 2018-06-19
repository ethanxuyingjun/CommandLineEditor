package com.company.command.impl;

import com.company.recevier.ITextLineFile;

public class UpdateContentCommand extends AbstractCommand {

    public UpdateContentCommand(ITextLineFile tf) {
        super(tf);
    }

    @Override
    public void execute() {
        tf.updateContent(cmdModel.getTextContent());
    }
}
