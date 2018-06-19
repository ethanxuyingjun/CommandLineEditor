package com.company.command.model;

import com.company.common.enumeration.AddressTypeEnum;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class CommandAddressModel {

    //必选参数
    private final AddressTypeEnum type;
    //可选参数
    private  String searchContent;
    private  String from;
    private  String to;
    private  int goLines;
    private  String flagLine;

    private CommandAddressModel (Builder builder) {
        this.type = builder.type;
        this.searchContent = builder.searchContent;
        this.from = builder.from;
        this.to = builder.to;
        this.goLines = builder.goLines;
        this.flagLine = builder.flagLine;
    }

    public AddressTypeEnum getType() {
        return type;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getGoLines() {
        return goLines;
    }

    public String getFlagLine() {
        return flagLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandAddressModel that = (CommandAddressModel) o;
        return from == that.from &&
                to == that.to &&
                goLines == that.goLines &&
                type == that.type &&
                Objects.equals(searchContent, that.searchContent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, searchContent, from, to, goLines);
    }



    @Override
    public String toString() {
        return "CommandAddressModel{" +
                "type=" + type +
                ", searchContent='" + searchContent + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", goLines=" + goLines +
                '}';
    }

    public static class Builder {
        //必要参数
        private final AddressTypeEnum type;
        //可选参数
        private  String searchContent;
        private  String from;
        private  String to;
        private  String flagLine;
        private  int goLines;

        public Builder(AddressTypeEnum type) {
            this.type = type;
        }

        public Builder searchContent(String cont) {
            this.searchContent = cont;
            return this;
        }

        public Builder fromLine(String from) {
            this.from = from;
            return this;
        }

        public Builder toLine(String to) {
            this.to = to;
            return this;
        }

        public Builder goLines(int goLines) {
            this.goLines = goLines;
            return this;
        }

        public Builder flagLine(String flag) {
            this.flagLine = flag;
            return this;
        }

        public CommandAddressModel build() {
            return new CommandAddressModel(this);
        }
    }



}
