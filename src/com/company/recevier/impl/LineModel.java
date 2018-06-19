package com.company.recevier.impl;

import com.company.command.model.CommandAddressModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class LineModel {
    private int lineNum = -1;
    private int from = -1;
    private int to = -1;
    private boolean isMultiLines = false;
    private boolean valid;

    /**
     * check the line is valid for usage
     * @return
     */
    public boolean isValid () {
        return valid;
    }

    /**
     *
     * @param totalLines
     * @param currentLine
     * @param addrSet
     * @param contentBuffer
     */
    public LineModel(int totalLines, int currentLine, Set<CommandAddressModel> addrSet, List<String> contentBuffer, Map<String, String> flags) {
        this.valid = true;
        int l, lf, lt;
        for(CommandAddressModel m: addrSet) {
            switch(m.getType()) {
                case CURRENT_LINE:
                    if(lineNum == -1) {
                        lineNum = currentLine;
                    }
                    break;
                case LAST_LINE:
                    if(m.getGoLines()!= -1) {
                        lineNum = totalLines - m.getGoLines();
                    } else {
                        lineNum = totalLines;
                    }
                    break;
                case LINE_NUMBER:
                    if (this.checkValidLineNumber(m.getGoLines(), totalLines)) {
                        lineNum = m.getGoLines();
                    }
                    break;
                case FORWARD_LINE:
                    l = lineNum != -1 ? lineNum - m.getGoLines() : currentLine - m.getGoLines();
                    if(this.checkValidLineNumber(l, totalLines)) {
                        lineNum = l;
                    }
                    break;
                case BACKWARD_LINE:
                    l = lineNum != -1 ? lineNum + m.getGoLines() : currentLine + m.getGoLines();
                    if(this.checkValidLineNumber(l, totalLines)) {
                        lineNum = l;
                    }
                    break;
                case FROM_TO:
                    isMultiLines = true;
                    lf = getLine(m.getFrom(), currentLine, totalLines, flags);
                    lt = getLine(m.getTo(), currentLine, totalLines, flags);
                    if (this.checkValidMultiLines(lf, lt, totalLines)) {
                        from = lf;
                        to = lt;
                    }
                    break;
                case FLAG_LINE:
                    isMultiLines = false;
                    for(Map.Entry<String, String> entry: flags.entrySet())
                    {
                        if(entry.getKey().equals(m.getFlagLine())) {
                           lineNum = parseInt(entry.getValue());
                           break;
                        }
                    }
                    break;
                case ALL_LINES:
                    isMultiLines = true;
                    from = 1;
                    to = totalLines;
                    break;
                case TOLAST:
                    isMultiLines = true;
                    if (this.checkValidMultiLines(currentLine, totalLines, totalLines)) {
                        from = currentLine;
                        to  = totalLines;
                    }
                    break;
                case FORWARD_SEARCH:
                    //search from current line to first line
                    for (int i = currentLine -1 ; i >= 0; i--) {
                        if (contentBuffer.get(i).indexOf(m.getSearchContent()) != -1) {
                            lineNum = i + 1;
                            break;
                        }
                    }
                    if (lineNum == -1) {
                        //continue search from bottom to current line
                        for (int i = totalLines -1 ; i >= currentLine; i--) {
                            if (contentBuffer.get(i).indexOf(m.getSearchContent()) != -1) {
                                lineNum = i + 1;
                                break;
                            }
                        }
                    }

                    if(lineNum == -1) {
                        valid = false;
                        System.out.println("? - No matched line with('"+ m.getSearchContent() +"') found!");
                    }
                    break;
                case BACKWARD_SEARCH:
                    //search from current line to last line
                    for (int i = currentLine; i < totalLines; i++) {
                        if (contentBuffer.get(i).indexOf(m.getSearchContent()) != -1) {
                            lineNum = i + 1;
                            break;
                        }
                    }
                    if (lineNum == -1) {
                        //continue search from bottom to current line
                        for (int i = 0 ; i < currentLine; i++) {
                            if (contentBuffer.get(i).indexOf(m.getSearchContent()) != -1) {
                                lineNum = i + 1;
                                break;
                            }
                        }
                    }
                    if(lineNum == -1) {
                        valid = false;
                        System.out.println("? - No matched line with('"+ m.getSearchContent() +"') found!");
                    }
                    break;
                default:
                    valid = false;
                    System.out.println("Invalid address type");
            }
        }
    }

    private boolean checkValidLineNumber (int line, int total) {
        if( line > 0 && line <= total) {
            return true;
        }
        System.out.println("? - No such line ( "+ line +" ) found!");
        valid = false;
        return false;
    }

    private boolean checkValidMultiLines (int from, int to, int total) {

        if(from < 0 || to < 0 || from > to || from > total || to > total) {
            System.out.println("? - No such lines( "+ from + " -> " + to+") found!");
            valid = false;
            return false;
        }
        return true;
    }

    private int getLine (String line, int currentLine, int total, Map<String, String> flags) {
        if(line.equals(".")){
            return currentLine;
        } else if (line.equals("$")) {
            return total;
        } else if(line.indexOf("'") !=-1) {
            String flag = line.substring(1,2);
            for(Map.Entry<String, String> entry: flags.entrySet())
            {
                if(entry.getKey().equals(flag)) {
                    return parseInt(entry.getValue());
                }
            }
            return -1;
        } else {
            return parseInt(line);
        }
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean isMultiLines() {
        return isMultiLines;
    }
}
