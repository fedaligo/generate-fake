package com.htp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {

    private static void writeRaw(List<String> records) throws IOException {
        File fl = new File("result.yml");
        FileWriter writer = new FileWriter(fl);
        System.out.print("Writing raw... ");
        write(records, writer);
    }

    private static void write(List<String> records, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        for (String record : records) {
            writer.write(record);
        }
        writer.flush();
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000f + " seconds");
    }

    public static void main(String[] args) throws IOException {
        deleteInfoFromFile("result.yml");
        byte[] all = Files.readAllBytes(Paths.get("1.txt"));
        String text = new String(all);
        String[] mass = text.split("\n");
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < mass.length; i++) {
            list.add(mass[i].split(", "));
        }
        List<String> myList = new ArrayList<>();
        for (int p = 0; p < 1000000; p++) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                int max = list.get(i).length;
                int randomValue = 0;
                Random random = new Random();
                randomValue = random.nextInt(max);
                if (i == 0) {
                    builder.append((p + 1) + " - ");
                }
                builder.append(list.get(i)[randomValue] + " ");
                if (i == list.size() - 1) {
                    builder.append("\n");
                }
            }
            myList.add(builder.toString());
        }
        writeRaw(myList);
    }

    private static void deleteInfoFromFile(String fileName) throws IOException {
        File fl = new File(fileName);
        FileWriter pw = null;
        try {
            pw = new FileWriter(fl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(pw);
        bw.write("");
        bw.close();
        pw.close();
    }
    private String getFakeInfoByParameters(String localeTag/*, int countOfStrings, int countOfMistakes*/) throws IOException {
        // Загрузка файла YAML из папки
        Data info = new Data();
        String[] arrLocaleTag = localeTag.split("_");
        Locale locale = new Locale(arrLocaleTag[0], arrLocaleTag[1]);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(info.getFileByLocale(locale)).getFile());

        // Создание нового ObjectMapper как YAMLFactory
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // Отображение сотрудника из файла YAML в класс Employee
        Data data = om.readValue(file, Data.class);

        // Распечатка информации
        //System.out.println(data.getAllInfoFromFile());
        return data.getAllInfoFromFile();
    }
}

