package com.htp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.*;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Data {
    private List<String> firstName;
    private List<String> lastName;
    private List<String> index;
    private List<String> country;
    private List<String> cityPrefix;
    private List<String> city;
    private List<String> streetPrefix;
    private List<String> street;
    private List<String> housePrefix;
    private List<String> house;
    private List<String> flatPrefix;
    private List<String> flat;
    private List<String> numberPrefix;
    private List<String> firstNumber;
    private List<String> secondNumber;
    private List<String> thirdNumber;

    public static String getFileByLocale(Locale locale){
        Locale belarus = new Locale("be","BY");
        Locale usa = new Locale("en","US");
        Locale russia = new Locale("ru","RU");
        String fileName = "";
        if (checkTheSameLocale(locale,belarus)){
            fileName = "bel.yml";
        } else {
            if (checkTheSameLocale(locale,usa)){
                fileName = "en.yml";
            } else {
                fileName = "ru.yml";
            }
        }
        return fileName;
    }

    private static boolean checkTheSameLocale(Locale locale, Locale secondLocale){
        if (locale.getCountry().equals(secondLocale.getCountry()) && locale.getLanguage().equals(secondLocale.getLanguage())){
            return true;
        } else {
            return false;
        }
    }

    public String getAllInfoFromFile(){
        StringBuilder builder = new StringBuilder();
        builder.append(getRandomString(getFirstName()) + " ");
        builder.append(getRandomString(getLastName()) + "; ");
        builder.append(getRandomString(getIndex()) + ", ");
        builder.append(getRandomString(getCountry()) + ", ");
        builder.append(getRandomString(getCityPrefix()));
        builder.append(getRandomString(getCity()) + ", ");
        builder.append(getRandomString(getStreetPrefix()));
        builder.append(getRandomString(getStreet()) + ", ");
        builder.append(getRandomString(getHousePrefix()));
        builder.append(getRandomString(getHouse()) + ", ");
        builder.append(getRandomString(getFlatPrefix()));
        builder.append(getRandomString(getFlat()) + "; ");
        builder.append(getRandomString(getNumberPrefix()) + " ");
        builder.append(getRandomString(getFirstNumber()) + "-");
        builder.append(getRandomString(getSecondNumber()) + "-");
        builder.append(getRandomString(getThirdNumber()));
        return builder.toString();
    }

    private String getRandomString(List<String> arrayOfStrings){
        int max = arrayOfStrings.size();
        int randomValue = 0;
        Random random = new Random();
        randomValue = random.nextInt(max);
        if (max == 0) {
            return arrayOfStrings.get(0);
        }
        return arrayOfStrings.get(randomValue);
    }
}
