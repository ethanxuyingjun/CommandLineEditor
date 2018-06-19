package com.company.recevier.impl;

import com.company.command.model.CommandAddressModel;
import com.company.common.enumeration.CommandActionEnum;
import com.company.recevier.ITextLineFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


public class TextLineFile implements ITextLineFile {

    private int currentLineNum = 0;
    private List<String> contentBuffer;
    private Stack historyStack;

    private String defaultFileName = null;

    private FileModeEnum currentFileMode = FileModeEnum.NOT_STARTED;
    private FileEditTypeEnum editType;

    private boolean contentChanged = false;
    private boolean contentSaved = false;
    private boolean preActionIsRemindQuit = false;
    private boolean undoAction  = false;
    private Map<String, String> flags;
    private Set<CommandAddressModel> cachedAddr;
    private String preReplaceParam;

    private static class SingletonHolder{
        public static TextLineFile instance = new TextLineFile();
    }
    private TextLineFile(){}

    public static TextLineFile newInstance(){
        return SingletonHolder.instance;
    }


    void setCurrentFileMode(FileModeEnum currentFileMode) {
        this.currentFileMode = currentFileMode;
        System.out.println("Now you are in "+ currentFileMode.toString().toLowerCase() + " mode.");
    }

    public void setEditType(FileEditTypeEnum editType) {
        this.editType = editType;
        System.out.println("Edit type is "+ editType.toString().toLowerCase() + ".");
    }

    public FileModeEnum getCurrentFileMode() {
        return currentFileMode;
    }

    //===================== implemented public method =========================//
    @Override
    public void enterEditFile(String filename) {
        //init default properties
        this.defaultFileName = filename;
        this.currentLineNum = 0;
        this.contentBuffer = new ArrayList<>();
        this.contentChanged = false;
        this.contentSaved = false;
        this.historyStack = new Stack();
        this.flags = new HashMap<>();

        setCurrentFileMode(FileModeEnum.COMMAND);
        handleEditFile();
        addBufferToHistory();
    }

    @Override
    public void saveContent(Set<CommandAddressModel> addrSet, String filename, boolean overwrite) {
        if(filename == null) {
            if (this.defaultFileName == null) {
                System.out.println("? -- Please provide file name first!");
                return;
            } else {
                filename = this.defaultFileName;
            }
        }
        handleSave(addrSet, filename.trim(), overwrite);

        if (contentChanged) {
            contentSaved = true;
            contentChanged = false;
        }

        List<String> copy = new ArrayList<>();
        copy.addAll(contentBuffer);
        historyStack.clear();
        historyStack.push(copy);
    }

    @Override
    public void quitEditFile(boolean isRemind) {
        if (isRemind && !contentSaved && !preActionIsRemindQuit) {
            System.out.println("? -- There are changed contents not saved to text file yet, please save it first or type 'q' again to quit without save");
            preActionIsRemindQuit = true;
            return;
        }
        preActionIsRemindQuit = false;
        setCurrentFileMode(FileModeEnum.NOT_STARTED);
        cleanCache();
    }

    @Override
    public void updateContent(String content) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(this.contentBuffer.size(), this.currentLineNum, this.cachedAddr, this.contentBuffer, flags);
        if(!lm.isValid()) {
            return;
        }

        switch (editType) {
            case APPEND:
                if (lm.isMultiLines()) {
                    System.out.println("Invalid address, only support single line append!");
                    break;
                }
                if (lm.getLineNum() < totalSize) {
                    contentBuffer.add(lm.getLineNum(), content);
                } else {
                    contentBuffer.add(content);
                }
                this.currentLineNum = lm.getLineNum() + 1;
                contentChanged = true;
                break;
            case INSERT:
                if (lm.isMultiLines()) {
                    System.out.println("Invalid address, only support single line insert!");
                    break;
                }
                if (lm.getLineNum() == 0) {
                    contentBuffer.add(content);
                    this.currentLineNum = lm.getLineNum() + 1;
                } else {
                    contentBuffer.add(lm.getLineNum() - 1, content);
                    this.currentLineNum = lm.getLineNum();
                }
                contentChanged = true;
                break;
            case COPY:
                if (lm.isMultiLines()) {
                    if (lm.getFrom() > totalSize || lm.getTo() > totalSize) {
                        System.out.println("Error! Current text lines is smaller than your input!, current lines: " + totalSize + ", but you want to replace from "+
                                lm.getFrom() + " to "+ lm.getTo());
                        break;
                    }
                    for(int i=lm.getFrom()-1; i<lm.getTo(); i++) {
                        contentBuffer.set(i, content);
                    }
                    currentLineNum = lm.getFrom();
                } else {
                    contentBuffer.set(lm.getLineNum()-1, content);
                    currentLineNum = lm.getLineNum();
                }
                contentChanged = true;
                break;
            default:
                System.out.println("no matched edit type found, content won't be updated!");
        }
        resetFlags();
        addBufferToHistory();
    }

    @Override
    public void quitUpdate() {
        //contentChanged = false;
        setCurrentFileMode(FileModeEnum.COMMAND);
    }

    @Override
    public void appendContent(Set<CommandAddressModel> addrSet) {
        setCurrentFileMode(FileModeEnum.TEXT_EDIT);
        setEditType(FileEditTypeEnum.APPEND);
        this.cachedAddr = addrSet;
    }

    @Override
    public void insertContent(Set<CommandAddressModel> addrSet) {
        setCurrentFileMode(FileModeEnum.TEXT_EDIT);
        setEditType(FileEditTypeEnum.INSERT);
        this.cachedAddr = addrSet;
    }

    @Override
    public void copyContent(Set<CommandAddressModel> addrSet) {
        setCurrentFileMode(FileModeEnum.TEXT_EDIT);
        setEditType(FileEditTypeEnum.COPY);
        this.cachedAddr = addrSet;
    }

    @Override
    public void printContent(Set<CommandAddressModel> addrSet) {
        LineModel lm = new LineModel(this.contentBuffer.size(), this.currentLineNum, addrSet, this.contentBuffer, flags);
        if (lm.isMultiLines() && lm.getFrom()!= -1 && lm.getTo() != -1) {
            this.printMultiLines(lm.getFrom(), lm.getTo());
        } else if (!lm.isMultiLines() && lm.getLineNum() != -1) {
            this.printSingleLine(lm.getLineNum());
        }
    }

    @Override
    public void printLineNumber(Set<CommandAddressModel> addrSet) {
        LineModel lm = new LineModel(this.contentBuffer.size(), this.currentLineNum, addrSet, this.contentBuffer, flags);
        if (!lm.isMultiLines()) {
            System.out.println("Do print line number command, No. : " + lm.getLineNum());
        } else {
            System.out.println("? -- Not support print multipal line number!");
        }
    }

    @Override
    public void deleteContent (Set<CommandAddressModel> addrSet) {
        int beforeDeleteSize = this.contentBuffer.size();
        LineModel lm = new LineModel(beforeDeleteSize, this.currentLineNum, addrSet, this.contentBuffer, flags);
        if (lm.isMultiLines() && lm.getFrom()!= -1 && lm.getTo() != -1) {
            if(lm.getTo() < beforeDeleteSize) {
                this.currentLineNum = lm.getFrom();
            } else {
                this.currentLineNum = lm.getFrom() - 1;
            }
            for (int i = lm.getFrom() -1 ; i< lm.getTo(); i++) {
                contentBuffer.remove(lm.getFrom() -1);
            }
        } else if (!lm.isMultiLines() && lm.getLineNum() != -1) {

            if (lm.getLineNum() < beforeDeleteSize) {
                this.currentLineNum = lm.getLineNum();
            } else {
                this.currentLineNum = lm.getLineNum() - 1;
            }
            contentBuffer.remove(lm.getLineNum() - 1);
        }
        resetFlags();
        addBufferToHistory();
    }

    @Override
    public void moveContent(Set<CommandAddressModel> addrSet, String param) {
        int totalSize = this.contentBuffer.size();
        int moveto;
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer,flags);
        if (lm.isMultiLines()) {
            System.out.println("Don't support multipal lines move!");
        } else {
            try {
                if(param!= null) {
                    int temp = lm.getLineNum() + parseInt(param);
                    moveto =  temp > totalSize ? totalSize : temp;
                } else {
                    moveto = totalSize;
                }
                String moveLine = this.contentBuffer.get(lm.getLineNum()-1);
                this.contentBuffer.remove(lm.getLineNum()-1);
                this.contentBuffer.add(moveto - 1, moveLine);
                this.currentLineNum = moveto - 1;
                printMultiLines(lm.getLineNum(), moveto);
                } catch (Exception ex) {
                System.out.println("Invalid parameter value: " + param);
            }
        }
        resetFlags();
        addBufferToHistory();
    }

    @Override
    public void mergeContent(Set<CommandAddressModel> addrSet) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer, flags);

        if(!lm.isValid()) {
            System.out.println("Invalid address value!");
            return;
        }
        if(lm.isMultiLines()) {
            StringBuilder mergeStr = new StringBuilder();
            //mergeStr.append(contentBuffer.get(lm.getLineNum()-1));
            for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
                mergeStr.append(contentBuffer.get(i));
            }
            contentBuffer.set(lm.getLineNum()-1, mergeStr.toString());
            this.currentLineNum = lm.getLineNum();
            addBufferToHistory();
        }
    }

    @Override
    public void moveBlock (Set<CommandAddressModel> addrSet, Set<CommandAddressModel> paramSet) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer,flags);
        LineModel paramLine = new LineModel(totalSize, this.currentLineNum, paramSet, this.contentBuffer,flags);

        List<String> moveList = new ArrayList<>();
        for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
            moveList.add(contentBuffer.get(lm.getFrom()-1));
            contentBuffer.remove(lm.getFrom()-1);
        }

        int moveSize = moveList.size();
        contentBuffer.addAll(paramLine.getLineNum()-1, moveList);
        this.currentLineNum = paramLine.getLineNum() + moveSize;
        resetFlags();
        addBufferToHistory();
    }

    @Override
    public void copyBlock (Set<CommandAddressModel> addrSet, Set<CommandAddressModel> paramSet) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer,flags);
        LineModel paramLine = new LineModel(totalSize, this.currentLineNum, paramSet, this.contentBuffer, flags);

        List<String> moveList = new ArrayList<>();
        for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
            moveList.add(contentBuffer.get(i));
        }

        int moveSize = moveList.size();
        contentBuffer.addAll(paramLine.getLineNum()-1, moveList);
        this.currentLineNum = paramLine.getLineNum() + moveSize;
        addBufferToHistory();
    }

    @Override
    public void replaceContent(Set<CommandAddressModel> addrSet, String param) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer, flags);
        if (param != null) {
            this.preReplaceParam = param;
        } else {
            param = this.preReplaceParam;
        }
        String[] params = param.split("/");

        if(params.length > 3) {
            String p3 = params[3];
            try {
                if(p3.equals("g")) {
                    if (lm.isMultiLines()) {
                        for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
                            String temp = contentBuffer.get(i);
                            contentBuffer.set(i,temp.replaceAll(params[1], params[2]));
                        }
                        this.currentLineNum = lm.getTo();
                    } else {
                        String temp = contentBuffer.get(lm.getLineNum()-1);
                        contentBuffer.set(lm.getLineNum()-1,temp.replaceAll(params[1], params[2]));
                        this.currentLineNum = lm.getLineNum();
                    }
                } else {
                    int count = parseInt(p3);
                    Pattern pattern = Pattern.compile(params[1]);
                    if (lm.isMultiLines()) {
                        for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
                            String temp = contentBuffer.get(i);
                            int startIndex = findMatchedIndex (pattern, temp, count);
                            String newStr = this.replaceStr(startIndex, temp, params[1], params[2]);
                           if (newStr != null) {
                               contentBuffer.set(i,newStr);
                           }
                        }
                        this.currentLineNum = lm.getTo();
                    } else {
                        String temp = contentBuffer.get(lm.getLineNum()-1);
                        int startIndex = findMatchedIndex (pattern, temp, count);
                        String newStr = this.replaceStr(startIndex, temp, params[1], params[2]);
                        if (newStr != null) {
                            contentBuffer.set(lm.getLineNum()-1,newStr);
                            this.currentLineNum = lm.getLineNum();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid parameter value: "+param);
            }
        } else {
            if (lm.isMultiLines()) {
                for (int i = lm.getFrom()-1; i < lm.getTo(); i++) {
                    String temp = contentBuffer.get(i);
                    contentBuffer.set(i,temp.replaceFirst(params[1], params[2]));
                }
                this.currentLineNum = lm.getTo();
            } else {
                String temp = contentBuffer.get(lm.getLineNum()-1);
                contentBuffer.set(lm.getLineNum()-1,temp.replaceFirst(params[1], params[2]));
                this.currentLineNum = lm.getLineNum();
            }
        }
        resetFlags();
        addBufferToHistory();
    }

    @Override
    public void setFlag(Set<CommandAddressModel> addrSet, String param) {
        int totalSize = this.contentBuffer.size();
        LineModel lm = new LineModel(totalSize, this.currentLineNum, addrSet, this.contentBuffer,flags);
        if(lm.isMultiLines()) {
            System.out.println("Invalid address, only support single line parameter");
        } else {
            this.flags.put(param, lm.getLineNum()+"");
            for(Map.Entry<String, String> entry: flags.entrySet())
            {
                System.out.println("Flag: "+ entry.getKey()+ " Line Number: "+entry.getValue());
            }
        }
    }

    @Override
    public void undoOperation() {
        if (this.historyStack != null && this.historyStack.size() > 1) {
            this.historyStack.pop();
            this.contentBuffer =  (List)this.historyStack.peek();
            this.currentLineNum = this.contentBuffer.size();
            resetFlags();
        } else {
            System.out.println("Nothing for undo operation");
        }
    }

    @Override
    public void setDefaultFileName(String fileName) {
        if (fileName == null) {
            if (this.defaultFileName == null) {
                System.out.println("? -- There is no file name set yet!");
            } else {
                System.out.println("Default file name is " + defaultFileName);
            }
        } else {
            defaultFileName = fileName;
        }
    }

    //===================== inner method =========================//

   private void addBufferToHistory () {
       List<String> copy = new ArrayList<>();
       copy.addAll(contentBuffer);
       historyStack.push(copy);
   }


   private void afterCommandExecuted(CommandActionEnum action) {
       if (currentFileMode == FileModeEnum.COMMAND && !undoAction) {
           List<String> copy = new ArrayList<>();
           copy.addAll(contentBuffer);
           historyStack.push(copy);
       }
       undoAction = false;
   }


    private void handleEditFile() {
        String filepath;
        if (this.defaultFileName != null) {
            filepath = defaultFileName+".txt";
            File file = new File(filepath);
            BufferedReader reader = null;
            try{
                if(!file.exists()){
                    file.createNewFile();
                } else {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    currentLineNum = 0;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        contentBuffer.add(tempString);
                        currentLineNum ++;
                        System.out.println("line " + currentLineNum + ": " + tempString);
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }

    private void handleSave (Set<CommandAddressModel> addrSet, String fileName, boolean overwrite) {
        // write
        FileWriter fw = null;
        PrintWriter writer = null;
        String path = fileName +".txt";
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(file, true);

            //remove exist content
            if(overwrite) {
                writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
            LineModel lm = new LineModel(this.contentBuffer.size(), this.currentLineNum, addrSet, this.contentBuffer, flags);
            if (lm.isMultiLines()) {
                for(int i=lm.getFrom() - 1; i<lm.getTo(); i++) {
                    fw.write("\n");
                    fw.write(contentBuffer.get(i));
                }
            } else {
                fw.write("\n");
                fw.write(contentBuffer.get(lm.getLineNum() -1));
            }
            fw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private void printSingleLine (int line) {
            System.out.println("Do print command, line: " + line +  " >>> " + contentBuffer.get(line-1));
    }

    private void printMultiLines (int from, int to) {
        System.out.println("Do print command, from: " + from +  " to: " + to);
        for (int i = from-1; i < to; i++) {
            System.out.println("line: " + (i+1) + " >>> " + contentBuffer.get(i));
        }
    }

    private int findMatchedIndex (Pattern pattern, String targetStr, int count) {
        Matcher slashMatcher = pattern.matcher(targetStr);
        int mIdx = 1;
        int startIndex = -1;
        while(slashMatcher.find()) {
            if (count == mIdx) { // found it
                startIndex = slashMatcher.start();
                break;
            }
            mIdx++;
        }
        return startIndex;
    }

    private String replaceStr (int startIndex, String target, String from, String to) {
        if(startIndex != -1) {
            StringBuilder sb = new StringBuilder();
            String preStr = target.substring(0, startIndex);
            String postStr = target.substring(startIndex + from.length());
            sb.append(preStr).append(to).append(postStr);
            return sb.toString();
        }
        return null;
    }

    private void resetFlags () {
        int totalSize = this.contentBuffer.size();
        for(Map.Entry<String, String> entry: flags.entrySet())
        {
            if (parseInt(entry.getValue()) > totalSize) {
                flags.remove(entry.getKey());
            }
        }
    }

    private void printAll () {
        System.out.println("print complete content: ");
        contentBuffer.forEach(System.out::println);
    }

    private void cleanCache () {
        contentBuffer.clear();
        historyStack.clear();
    }

}
