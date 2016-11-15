package com.example.nghia.coo;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by nghia on 15/11/2016.
 */

public class ShoppingObject {
    public String Name;
    public int id;
    public String Ration;
    public ArrayList<String> listimplement = new ArrayList<String>();
    public ShoppingObject() {
    }

    public ShoppingObject(int ID, String name, String ration, String convert) {
        Name = name;
        id=ID;
        Ration = ration;
        Scanner scanner = new Scanner(convert);
        ArrayList<String> tem = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            tem.add(line);
        }
        scanner.close();
        listimplement=tem;
    }

 /*   public ShoppingObject(int ID,String name, String ration, ArrayList<String> listimplement) {
        Name = name;
        id=ID;
        this.listimplement = listimplement;
        Ration = ration;
    }*/
}
