package com.dkhs.cqssc;

import com.dkhs.cqssc.domain.Results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/16.
 */
public class FileData {
    public static String getDataFromFile(File file) throws Exception {
        if (file != null) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                sb.append(str);
            }
            bufferedReader.close();
            return sb.toString();
        }
        return null;
    }
    Results results = new Results();
    public static List<Results> builderDataList(String str) {
        List<Results> list = new ArrayList<Results>();
        String regex = "^\\d{11}.{5,}\\d{3}$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
           // results.set
           // list.add();
           // System.out.println(group);
        }
        return list;
    }

   /* @Test
    public void test() throws Exception {
        String dataFromFile = FileData.getDataFromFile(new File("d:/ssc_wa.txt"));
        // System.out.println(dataFromFile);
        System.out.println("******************************************");
        List<Results> builderDataList = FileData.builderDataList(dataFromFile);
        System.out.println(builderDataList.size());
    }*/
}
