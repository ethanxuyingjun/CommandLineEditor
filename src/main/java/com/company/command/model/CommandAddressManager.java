package com.company.command.model;

import com.company.common.enumeration.AddressTypeEnum;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class CommandAddressManager {

    public Set<CommandAddressModel> getModels() {
        return models;
    }

    private static class SingletonHolder{
        public static CommandAddressManager instance = new CommandAddressManager();
    }
    private CommandAddressManager(){}

    public static CommandAddressManager newInstance(){
        return SingletonHolder.instance;
    }

    private Set<CommandAddressModel> models = new HashSet<>();

    public Set<CommandAddressModel> getParamModels() {
        return paramModels;
    }

    private Set<CommandAddressModel> paramModels = new HashSet<>();

    private CommandAddressModel paramAddress;

    public void parseAddress (String addrStr, Set<CommandAddressModel> set, boolean isRecursive) {

        CommandAddressModel model = null;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");   //整数判断

        if (addrStr.startsWith("?")) {
            String searchStr = addrStr.replaceAll("\\?", "");
            model = new CommandAddressModel.Builder(AddressTypeEnum.FORWARD_SEARCH).searchContent(searchStr).build();
        } else if (addrStr.startsWith("/")) {
            String searchStr = addrStr.replaceAll("/", "");
            model = new CommandAddressModel.Builder(AddressTypeEnum.BACKWARD_SEARCH).searchContent(searchStr).build();
        } else if(addrStr.indexOf(',') != -1) {
            if (addrStr.equals(",")) {
                model = new CommandAddressModel.Builder(AddressTypeEnum.ALL_LINES).build();
            } else {
                int commaIndex = addrStr.indexOf(',');
                int dollaIndex = addrStr.indexOf('$');
                int stopIndex = addrStr.indexOf('.');
                String from = "";
                String to = "";
                if (addrStr.indexOf("'") != -1) {
                    Matcher slashMatcher = Pattern.compile("'[a-zA-Z]").matcher(addrStr);
                    while(slashMatcher.find()) {
                        int startIndex = slashMatcher.start();
                        if (startIndex < commaIndex) {
                            from = slashMatcher.group();
                        } else if (startIndex > commaIndex) {
                            to = slashMatcher.group();
                            break;
                        }
                    }
                   slashMatcher = Pattern.compile("[0-9]").matcher(addrStr);
                    while(slashMatcher.find()) {
                        int startIndex = slashMatcher.start();
                        if (startIndex < commaIndex) {
                            from = slashMatcher.group();
                        } else if (startIndex > commaIndex) {
                            to = slashMatcher.group();
                            break;
                        }
                    }
                } else  if (dollaIndex != -1 && dollaIndex == commaIndex+1) {
                    String temp = addrStr.replaceAll("[^0-9]", "|");
                    String[] array = temp.split("\\|");
                    from = array[0];
                    to = "$";
                } else  if(stopIndex != -1 && (stopIndex < commaIndex || stopIndex ==commaIndex+1)) {
                    String temp = addrStr.replaceAll("[^0-9]", "|");
                    String[] array = temp.split("\\|");
                    if (stopIndex < commaIndex) {
                        from = ".";
                        to = array[0];
;                    } else {
                        from = array[0];
                        to = ".";
                    }
                } else {
                    String temp = addrStr.replaceAll("[^0-9]", "|");
                    String[] array = temp.split("\\|");
                    from = array[0];
                    to = array[1];
                }

                String s =   from + "," + to;
                if(addrStr.length() > s.length() && isRecursive) {
                    String nextAddress = addrStr.substring(s.length());
                    parseAddress(nextAddress, set, isRecursive);   //继续解析下一个地址信息
                }
                model = new CommandAddressModel.Builder(AddressTypeEnum.FROM_TO).fromLine(from).toLine(to).build();
          }
        }  else if (addrStr.startsWith("'")) {
            model = new CommandAddressModel.Builder(AddressTypeEnum.FLAG_LINE).flagLine(addrStr.substring(1,2)).build();
        } else if(addrStr.indexOf(';') != -1) {
            String[] s = addrStr.split(",");
            model = new CommandAddressModel.Builder(AddressTypeEnum.TOLAST).build();
        } else if(addrStr.indexOf('.') != -1) {
            model = new CommandAddressModel.Builder(AddressTypeEnum.CURRENT_LINE).build();
            if (addrStr.length() > 1) {  //多个地址组合
                String nextAddress = addrStr.replaceAll("\\.", "");
                if(isRecursive) {
                    parseAddress(nextAddress, set, isRecursive);   //继续解析下一个地址信息
                }
            }
        } else if(addrStr.indexOf('$') != -1) {
            model = new CommandAddressModel.Builder(AddressTypeEnum.LAST_LINE).build();
            if (addrStr.length() > 1) {  //多个地址组合
                String nextAddress = addrStr.replaceAll("\\$", "");
                if(isRecursive) {
                    parseAddress(nextAddress, set, isRecursive);   //继续解析下一个地址信息
                }
            }
        }  else if (pattern.matcher(addrStr).matches()) {  //是整数
            if (addrStr.startsWith(AddressTypeEnum.FORWARD_LINE.getLabel())) {
                int line = getLine(addrStr);
                model = new CommandAddressModel.Builder(AddressTypeEnum.FORWARD_LINE).goLines(line).build();
            } else if(addrStr.startsWith(AddressTypeEnum.BACKWARD_LINE.getLabel())) {
                int line = getLine(addrStr);
                model = new CommandAddressModel.Builder(AddressTypeEnum.BACKWARD_LINE).goLines(line).build();
            } else {
                model = new CommandAddressModel.Builder(AddressTypeEnum.LINE_NUMBER).goLines(parseInt(addrStr)).build();
            }
        }

        if (model == null) {
            System.out.println("Invalid address format =>" + addrStr);
        } else {
            set.add(model);
        }
    }

    private int getLine (String ad) {
        return parseInt(ad.substring(1, ad.length()));
    }

}
