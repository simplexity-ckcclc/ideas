package com.ckcclc.anything.file;

import java.io.File;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 24/03/2018
 */
public class Test {

    public static void main(String[] args) {
//        System.out.println(FileUtils.getTempDirectoryPath());
//
//        String url = "tmp/aad.jpg";
//        System.out.println(FilenameUtils.getExtension(url));
//        System.out.println(FilenameUtils.getBaseName(url));

        String path = "/Users/ckcclc/tmp/test.txt";
        File file = new File(path);
        System.out.println(file.getName());
    }
}
