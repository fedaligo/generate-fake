package com.htp;

import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@NoArgsConstructor
public class Data {
    private final static List<String> ENGLISH_SYMBOLS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9");
    private final static List<String> BELARUSSIAN_SYMBOLS = Arrays.asList("А", "Б", "В", "Г", "Д", "Дж", "Дз", "Е", "Ё",
            "Ж", "З", "І", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ў", "Ф", "Х", "Ц", "Ч", "Ш", "Ы", "Ь",
            "Э", "Ю", "Я", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    private final static List<String> RUSSIAN_SYMBOLS = Arrays.asList("А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И",
            "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Ы", "Ь", "Ъ", "Э", "Ю",
            "Я", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    private final static String RESULT_PATH = "D:/1/itransition/3/result.csv";
    private final static String RESOURCE_PATH = "src/main/resources/data";
    private final static int MAX_COUNT_OF_ROWS_IN_MEMORY = 100000;
    private final static int IS_MISTAKE = 1;
    private final static Scanner SC = new Scanner(System.in);

    public void getFakeInfoByParameters(String localeTag, int countOfStrings, double countOfMistakes) {
        deleteInfoFromFile(RESULT_PATH);
        String fileName = RESOURCE_PATH + "/" + localeTag + ".yml";
        List<String[]> allInfoFromFile = getAllInfoFromFile(fileName);
        List<String> generatedString = new ArrayList<>();
        int countOfRepeatsOfOneBox = 0;
        for (int p = 0; p < countOfStrings; p++) {
            String stringWithMistakes = generateStringWithMistakes(getGeneratedString(allInfoFromFile)
                    , countOfMistakes, localeTag);
            generatedString.add(stringWithMistakes);
            countOfRepeatsOfOneBox++;
            if (countOfRepeatsOfOneBox == MAX_COUNT_OF_ROWS_IN_MEMORY) {
                writeRaw(generatedString);
                countOfRepeatsOfOneBox = 0;
                generatedString = new ArrayList<>();
            }
            if (countOfStrings < MAX_COUNT_OF_ROWS_IN_MEMORY && countOfRepeatsOfOneBox == countOfStrings) {
                writeRaw(generatedString);
            }
        }
    }

    public String getNameOfFileByLocale(String localTag) throws MySimpleException {
        if (!checkFileName(localTag).equals("")) {
            return localTag;
        } else {
            throw new MySimpleException("Locale Tag is not correct!");
        }
    }

    private String checkFileName(String localTag) {
        File dir = new File(RESOURCE_PATH);
        String[] arrFileNames = dir.list();
        for (int i = 0; i < arrFileNames.length; i++) {
            if (arrFileNames[i].replaceAll(".yml", "").equals(localTag)) {
                return localTag;
            }
        }
        return "";
    }

    private List<String[]> getAllInfoFromFile(String fileName) {
        byte[] all = new byte[0];
        try {
            all = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = new String(all);
        String[] mass = text.split("%");
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < mass.length; i++) {
            list.add(mass[i].split(", "));
        }
        return list;
    }

    private List<String> getGeneratedString(List<String[]> list) {
        List<String> listWithGeneratedStrings = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //System.out.println();
            listWithGeneratedStrings.add(list.get(i)[getRandomInt(list.get(i).length)]);
        }
        return listWithGeneratedStrings;
    }

    private String generateStringWithMistakes(List<String> listWithGeneratedStrings, double countOfMistakes,
                                              String localTag) {
        StringBuilder builder = new StringBuilder();
        List<String> myList = new ArrayList<>();
        if (countOfMistakes == 0) myList = listWithGeneratedStrings;
        if (countOfMistakes % IS_MISTAKE == 0) {
            for (int i = 0; i < countOfMistakes; i++) {
                myList = loadStringWithMistakeIntoList(listWithGeneratedStrings, countOfMistakes, localTag);
            }
        } else {
            myList = loadStringWithMistakeIntoList(listWithGeneratedStrings, countOfMistakes, localTag);
        }
        for (int i = 0; i < myList.size(); i++) builder.append(myList.get(i) + ";");
        return builder.toString();
    }

    private List<String> loadStringWithMistakeIntoList(List<String> listWithGeneratedStrings, double countOfMistakes,
                                                       String localTag) {
        int randomInt = getRandomInt(listWithGeneratedStrings.size());
        String stringWithMistakes;
        if (countOfMistakes % IS_MISTAKE == 0) {
            stringWithMistakes = makeMistakes(listWithGeneratedStrings.get(randomInt), IS_MISTAKE, localTag);
        } else {
            stringWithMistakes = makeMistakes(listWithGeneratedStrings.get(randomInt), isNeedMistake(countOfMistakes),
                    localTag);
        }
        listWithGeneratedStrings.set(randomInt, stringWithMistakes);
        return listWithGeneratedStrings;
    }

    private int isNeedMistake(double countOfMistakes) {
        int count = 0;
        if (Math.random() <= countOfMistakes) count = 1;
        return count;
    }

    private String makeMistakes(String stringWithMistakes, double countOfMistakes, String localTag) {
        if (countOfMistakes == 1) {
            switch (getRandomInt(3)) {
                case 0:
                    stringWithMistakes = deleteSymbol(stringWithMistakes, localTag);
                    break;
                case 1:
                    stringWithMistakes = changeSymbolInTheString(stringWithMistakes, localTag);
                    break;
                case 2:
                    stringWithMistakes = changePlaceOfSymbols(stringWithMistakes, localTag);
                    break;
            }
        }
        return stringWithMistakes;
    }

    private String deleteSymbol(String preparedString, String localTag) {
        if (isEmptyString(preparedString)) return changeSymbolInTheString(preparedString, localTag);
        int randomInt = getRandomInt(preparedString.length());
        return preparedString.replace(String.valueOf(preparedString.charAt(randomInt)), "");
    }

    private String changeSymbolInTheString(String preparedString, String localTag) {
        int randomSymbol = getRandomInt(getCorrectSymbols(localTag).size());
        if (isEmptyString(preparedString)) return getCorrectSymbols(localTag).get(randomSymbol);
        int randomInt = getRandomInt(preparedString.length());
        return preparedString.replace(String.valueOf(preparedString.charAt(randomInt)),
                getCorrectSymbols(localTag).get(randomSymbol));
    }

    private String changePlaceOfSymbols(String preparedString, String localTag) {
        if (isOneSymbol(preparedString)) return changeSymbolInTheString(preparedString, localTag);
        int randomInt = getRandomInt(preparedString.length());
        if (randomInt == 0) randomInt++;
        String firstSymbol = String.valueOf(preparedString.charAt(randomInt - 1));
        String secondSymbol = String.valueOf(preparedString.charAt(randomInt));
        String beforeChange = firstSymbol + secondSymbol;
        String afterChange = secondSymbol + firstSymbol;
        return preparedString.replace(beforeChange, afterChange.toUpperCase());
    }

    private boolean isEmptyString(String myEmptyString) {
        if (myEmptyString.length() == 0) return true;
        return false;
    }

    private boolean isOneSymbol(String myEmptyString) {
        if (myEmptyString.length() <= 1) return true;
        return false;
    }

    private List<String> getCorrectSymbols(String localeTag) {
        switch (localeTag) {
            case "be_BY":
                return BELARUSSIAN_SYMBOLS;
            case "en_US":
                return ENGLISH_SYMBOLS;
            case "ru_RU":
                return RUSSIAN_SYMBOLS;
        }
        return BELARUSSIAN_SYMBOLS;
    }

    private int getRandomInt(int max) {
        Random random = new Random();
        if (max == 3) {
            return getRandomIntForThreeValues();
        }
        return random.nextInt(max);
    }

    private int getRandomIntForThreeValues() {
        int value;
        double randomNext = Math.random();
        if (randomNext <= 0.33) {
            value = 0;
        } else {
            if (randomNext > 0.33 && randomNext <= 0.67) {
                value = 1;
            } else {
                value = 2;
            }
        }
        return value;
    }

    private void deleteInfoFromFile(String fileName) {
        File fl = new File(fileName);
        try {
            FileWriter pw = new FileWriter(fl);
            BufferedWriter bw = new BufferedWriter(pw);
            bw.write("");
            bw.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeRaw(List<String> records) {
        for (int i = 0; i < records.size(); i++) {
            System.out.println(records.get(i));
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(RESULT_PATH, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        write(records, writer);
    }

    private void write(List<String> records, Writer writer) {
        try {
            for (String record : records) {
                writer.write(record + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int checkCountOfString(String args) throws MySimpleException {
            if (args.matches("\\d+") && Integer.valueOf(args) > 0){
                return Integer.parseInt(args);
            } else {
                throw new MySimpleException("Count of Strings is not correct!");
            }
    }

    public double checkCountOfMistakes(String args) throws MySimpleException {
        if (args.matches("\\d+") && args.matches("\\d+") && Integer.valueOf(args) >= 0) {
            return Double.parseDouble(args);
        } else {
            throw new MySimpleException("Count of Mistakes is not correct!");
        }
    }

}
