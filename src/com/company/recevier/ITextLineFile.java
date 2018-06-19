package com.company.recevier;

import com.company.command.model.CommandAddressModel;
import com.company.common.enumeration.CommandActionEnum;

import java.util.Set;

public interface ITextLineFile {

    void enterEditFile(String filename);

    //q or Q
    void quitEditFile(boolean isRemind);

    //w or W
    void saveContent(Set<CommandAddressModel> addrSet, String filename, boolean overwrite);

    //===========文本相关=================//

    void appendContent(Set<CommandAddressModel> addrSet);

    void insertContent(Set<CommandAddressModel> addrSet);

    void copyContent(Set<CommandAddressModel> addrSet);

    //添加文本内容
    void updateContent(String content);

    //退出文本输入
    void quitUpdate();

    //===========操作相关================//

    void printContent(Set<CommandAddressModel> addrSet);

    void printLineNumber(Set<CommandAddressModel> addrSet);

    void deleteContent(Set<CommandAddressModel> addrSet);

    void mergeContent(Set<CommandAddressModel> addrSet);

    void moveBlock(Set<CommandAddressModel> addrSet, Set<CommandAddressModel> paramSet);

    void copyBlock(Set<CommandAddressModel> addrSet, Set<CommandAddressModel> paramSet);

    void moveContent(Set<CommandAddressModel> addrSet, String param);

    void replaceContent(Set<CommandAddressModel> addrSet, String param);

    void setFlag(Set<CommandAddressModel> addrSet, String param);

    void undoOperation();

    void setDefaultFileName(String fileName);
}
