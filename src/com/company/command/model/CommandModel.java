package com.company.command.model;

import com.company.common.enumeration.CommandActionEnum;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandModel {

    public String getTextContent() {
        return textContent;
    }

    private String textContent;
    private Set addressSet = null;

    private Set paramSet = null;
    private String addressStr = null;
    private CommandActionEnum action = CommandActionEnum.UNKNOWN;
    private String param = null;

    public Set getAddress() {
        return addressSet;
    }

    public Set getParamSet() {
        return paramSet;
    }

    public void setAddress(String address) {
        if (address != null) {
            this.addressStr = address;
            CommandAddressManager manager = CommandAddressManager.newInstance();
            manager.getModels().clear();
            manager.parseAddress(address, manager.getModels(), true);
            this.addressSet = manager.getModels();
        }
    }

    public void setParamSet(String param) {
        if(param != null){
            CommandAddressManager manager = CommandAddressManager.newInstance();
            manager.getParamModels().clear();
            manager.parseAddress(param, manager.getParamModels(), false);
            this.paramSet = manager.getParamModels();
        }
    }



    public CommandActionEnum getAction() {
        return action;
    }

    public void setAction(String act) {
        for (int i = 0; i < CommandActionEnum.values().length; i++) {
            if(CommandActionEnum.values()[i].getName().equals(act)) {
                action = CommandActionEnum.values()[i];
                break;
            }
        }
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public CommandModel (String cmdLine, boolean isCommand) {

        if (isCommand) {
            parseCommandLine(cmdLine);
        } else {
            textContent = cmdLine;
        }
    }


    void parseCommandLine (String cmdLine) {
        Matcher matcher1 = Pattern.compile("\\?(.*?)\\?").matcher(cmdLine);  // pattern for ?str?
        Matcher matcher2 = Pattern.compile("/(.*?)/").matcher(cmdLine);    // pattern for /str/
        Matcher matcher3 =Pattern.compile("[=A-Za-z]").matcher(cmdLine);  //\.=A-Za-z  字母，等号，点
        if (cmdLine.startsWith("ed")) {
            setAction("ed");
            if (cmdLine.split(" ").length > 1) {
                setParam(cmdLine.split(" ")[1]);
            }
        } else if (cmdLine.equals(".")) {
            setAction(cmdLine);
        } else if (matcher1.find() && cmdLine.startsWith("?")) {
            setAddress("?"+matcher1.group(1)+"?");
            setAction(cmdLine.substring(this.addressStr.length() , this.addressStr.length() + 1));
            if (cmdLine.length() > this.addressStr.length()+1) {
                setParam(cmdLine.substring(this.addressStr.length()+1));
            }
        } else if(matcher2.find() && cmdLine.startsWith("/")) {
            setAddress("/"+matcher2.group(1)+"/");
            setAction(cmdLine.substring(this.addressStr.length(), this.addressStr.length() + 1));
            if (cmdLine.length() > this.addressStr.length()+1) {
                setParam(cmdLine.substring(this.addressStr.length()+1));
            }
        } else if(cmdLine.indexOf("'") != -1) {
            String replStr = cmdLine.replaceAll("'[a-zA-Z]","**");
            Matcher mat = Pattern.compile("[=A-Za-z]").matcher(replStr);
            if (mat.find()) {
                int index = mat.start();
                setAction(cmdLine.substring(index, index+1));
                if (index > 0) { //has address
                    setAddress(cmdLine.substring(0, index));
                }
                if (index < cmdLine.length() -1) {
                    setParam(cmdLine.substring(index + 1));
                }
            }
        } else if(matcher3.find()){
            int index = matcher3.start();
            setAction(cmdLine.substring(index, index+1));
            if (index > 0) { //has address
                setAddress(cmdLine.substring(0, index));
            }
            if (index < cmdLine.length() -1) {
                setParam(cmdLine.substring(index + 1));
            }
        }
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "CommandModel{" +
                "address='" + addressStr + '\'' +
                ", action='" + action.toString() + '\'' +
                ", param='" + param + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandModel that = (CommandModel) o;
        return Objects.equals(addressStr, that.addressStr) &&
                Objects.equals(action, that.action) &&
                Objects.equals(param, that.param);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressStr, action, param);
    }
}
