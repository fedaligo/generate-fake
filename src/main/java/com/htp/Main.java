package com.htp;

public class Main {
    public static void main(String[] args) throws MySimpleException {
        Data data = new Data ();
        if(args.length == 3){
            data.getFakeInfoByParameters(data.getNameOfFileByLocale(args[0]),data.checkCountOfString(args[1]),
                    data.checkCountOfMistakes(args[2]));
        } else {
            data.getFakeInfoByParameters(data.getNameOfFileByLocale(args[0]),data.checkCountOfString(args[1]),0);
        }
    }
}

