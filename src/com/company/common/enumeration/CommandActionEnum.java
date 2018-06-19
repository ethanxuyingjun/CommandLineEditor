package com.company.common.enumeration;

public enum CommandActionEnum {
    //default
    UNKNOWN("unknown"),

    //文本编辑指令
    ENTER_EDIT_FILE("ed"), QUIT_EDIT_FILE("Q"), QUIT_WITH_REMIND("q"),

    //文本输入指令
    APPEND("a"), INSERT("i"), COPY("c"), QUIT_UPDATE("."),

    //命令模式指令
    DELETE("d"), PRINT("p"), PRT_LINE_NUM("="), BACKWARD_MOVE("z"), SET_FILENAME("f"), SAVE_OVERWRITE("w"), SAVE_APPEND("W"),

    //进阶指令
    MOVE_BLOCK("m"), COPY_BLOCK("t"), MERGE_BLOCK("j"), REPLACE("s"),

    //高级指令
    SET_FLAG("k"), UNDO("u");

    public String getName() {
        return name;
    }

    private String name;

    private CommandActionEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
