/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Assembler {

    static public void readregisters() {
        Scanner x = null;
        try {
            x = new Scanner(new File("registers.txt"));
        } catch (Exception e) {
            System.out.println("cannot found");
        }
        while (x.hasNext()) {
            Registers r = new Registers();
            r.setName(x.next());
            r.setNumber(x.nextInt());
            re.add(r);
        }
    }

    static public void readcode() throws FileNotFoundException, IOException {
        FileInputStream fstream = new FileInputStream("controlsections.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

//Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            code ce = new code();
            if (strLine.startsWith(".")) {
                continue;

            } else {

                String sl = strLine.substring(0, 8).trim();
                //  System.out.println(sl);
                String sm = strLine.substring(10, 15).trim();
                String so = strLine.substring(18).trim();

                ce.setLabel(sl);
                //System.out.print(ce.getLabel() + "\t");
                ce.setMnemonic(sm);
                // System.out.print(ce.getMnemonic() + "\t");
                ce.setOperand(so);
                //   System.out.print(ce.getOperand() + "\n");
                // System.out.println(strLine);
                c.add(ce);
            }
        }
//Close the input stream
        br.close();

    }

    /*
    static public void readcode() {
        Scanner x = null;
        try {
            x = new Scanner(new File("code.txt"));
        } catch (Exception e) {
            System.out.println("cannot found");
        }
        while (x.hasNext()) {
            code cc = new code();
            cc.setLabel(x.next());
            cc.setMnemonic(x.next());
            cc.setOperand(x.next());
            c.add(cc);
        }
    }
     */
    static public void readfile() {
        Scanner x = null;
        try {
            x = new Scanner(new File("instructionset.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("cannot found");
        }
        while (x.hasNext()) {
            InstructionSet is = new InstructionSet();
            is.setMnemonic(x.next());
            is.setFormat(x.nextInt());
            is.setOpcode(x.next());
            list.add(is);
        }
        x.close();
    }

    /*
    static public void printing (){
        InstructionSet is = new InstructionSet();
         for (int i=0;i<list.size();i++ ){
             is = list.get(i);
             System.out.print(is.getMnemonic()+ "\t");
             System.out.print(is.getFormat()+ "\t" );
             System.out.println(is.getOpcode());
             
             
         }
         
     }

     */
    static public void printing() {
        code co = new code();
        System.out.printf("%-15s%-15s%-15s%-30s%-15s\n", "Location", "Label", "nmonic", "Operand", "Object code");
        for (int i = 0; i < c.size(); i++) {
            co = c.get(i);
            System.out.printf("%-15s%-15s%-15s%-30s%-15s\n", co.getLoc(), co.getLabel(), co.getMnemonic(), co.getOperand(), co.getObjcode());
        }

    }

    static public void printingEXTAB() {
        System.out.println("first control section");
        System.out.println("External definations");
        System.out.printf("%-15s%-15s\n", "Name", "Control section number");
        System.out.println("----------------------------------------");

        for (int w = 0; w < exdef.size(); w++) {
            EXTDEF d = new EXTDEF();
            d = exdef.get(w);
            System.out.printf("%-15s%-15s\n", d.getName(), d.getCsnumber());
        }
        System.out.println("----------------------------------------");

        System.out.println("External referances");
        System.out.printf("%-15s%-15s\n", "Name", "Control section number");
        System.out.println("----------------------------------------");
        for (int m = 0; m < exref.size(); m++) {
            EXTREF s = new EXTREF();
            s = exref.get(m);
            System.out.printf("%-15s%-15s\n", s.getName(), s.getCsnumber());
        }
        System.out.println("----------------------------------------");
    }

    // printing symbol table
    static public void symtablee() {
        symbolTable sym = new symbolTable();
        System.out.println("symboltable " + "\n" + "-------------------------------------------------------");
        for (int i = 0; i < symtab.size(); i++) {
            sym = symtab.get(i);
            System.out.print(sym.getSymbol() + "\t");
            System.out.print(sym.getC() + "\t");
            System.out.print(sym.getAddress() + "\n");

        }
        System.out.println("-------------------------------------------------------");

    }

    static public void printlttable() {
        LITTAB l = new LITTAB();
        System.out.println("----------------------");
        System.out.println("LITTAB");

        for (int i = 0; i < lt.size(); i++) {
            l = lt.get(i);
            System.out.print(l.getName() + "\t");
            System.out.print(l.getLenght() + "\t");
            System.out.print(l.getValue() + "\t");
            System.out.print(l.getAddress() + "\n");
        }
        System.out.println("----------------------");
    }

    public static void writeFile1() throws IOException {

        File fout = new File("out.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter HTD = new BufferedWriter(new OutputStreamWriter(fos));
        int section_number = 0;

        if (section_number == 0) {
            HTD.write("H");
            code cc = new code();
            cc = c.get(0);
            String unpadded = cc.getLabel();
            String padded = unpadded + "      ".substring(unpadded.length());
            HTD.write(padded);
            HTD.write("^");

            String unpadded2 = cc.getOperand();
            if (unpadded2.equals("--------")) {
                unpadded2 = "0";
            }

            String padded2 = leadingZeros(unpadded2, 6);
            HTD.write(padded2);
            HTD.write("^");

            String unpadded3 = Integer.toHexString(proglen1);
            String padded3 = leadingZeros(unpadded3, 6);
            HTD.write(padded3);
            HTD.newLine();

            HTD.write("D");
            HTD.write("^");

            for (int q = 0; q < exdef.size(); q++) {
                EXTDEF D = new EXTDEF();
                D = exdef.get(q);
                if (D.getCsnumber() == 1) {
                    HTD.write(D.getName());
                    HTD.write("^");
                    for (int r = 0; r < symtab.size(); r++) {
                        symbolTable s3 = new symbolTable();
                        s3 = symtab.get(r);
                        if ((s3.getSymbol()).equals(D.getName())) {
                            HTD.write(s3.getAddress());
                            HTD.write("^");
                        }
                    }
                }
            }
            HTD.newLine();
            HTD.write("R");
            HTD.write("^");

            for (int q = 0; q < exref.size(); q++) {
                EXTREF D = new EXTREF();
                D = exref.get(q);
                if (D.getCsnumber() == 1) {
                    HTD.write(D.getName());
                    HTD.write("^");
                }
            }
            HTD.newLine();

            int i = 1;
            int f;
            code d = new code();
            d = c.get(1);

            while (!(d.getMnemonic().equals("END"))) {
                HTD.write("T");
                HTD.write("^");
                String unpadded5 = d.getLoc();
                String padded5 = leadingZeros(unpadded5, 6);
                HTD.write(padded5);
                HTD.write("^");
                int count = 0;
                f = i;
                for (int j = i; j < c.size(); j++) {

                    d = c.get(j);
                    if (d.getObjcode().equals("--------")) {
                        continue;
                    }
                    int lg = (d.getObjcode()).length();
                    count += lg;
                    // System.out.println(count);
                    if (count == 60) {
                        i = j + 1;

                        break;

                    } else if (count > 60) {
                        count -= lg;
                        i = j;

                        break;
                    } else if (count < 60) {
                        i = j + 1;

                    }
                }

                String unpadded4 = Integer.toHexString(count / 2);
                // String padded4 = "00".substring(unpadded.length()) + unpadded4;
                String padded4 = leadingZeros(unpadded4, 2);
                HTD.write(padded4);
                HTD.write("^");

                for (int k = f; k < i; k++) {
                    code m = new code();
                    m = c.get(k);
                    if (m.getObjcode().equals("--------")) {
                        continue;
                    }
                    f++;
                    HTD.write(m.getObjcode());
                    HTD.write("^");

                }

                HTD.newLine();
            }

            for (int g = 1; g < c.size(); g++) {
                code l = new code();
                l = c.get(g);
                String j = l.getOperand();
                if ((l.getMnemonic().startsWith("+")) && !(Character.isDigit(j.charAt(1)))) {
                    HTD.write("M");
                    HTD.write("^");
                    int loc = Integer.parseInt(l.getLoc(), 16);
                    loc++;

                    String unpadded6 = Integer.toHexString(loc);
                    String padded6 = leadingZeros(unpadded6, 6);
                    HTD.write(padded6);
                    //System.out.println(unpadded6);
                    HTD.write("^");
                    HTD.write("05");
                    HTD.write("^+");
                    HTD.write(l.getOperand());
                    HTD.newLine();

                }

            }
            HTD.write("E");
            HTD.write("^");
            HTD.write(padded2);

            section_number++;

        } 

HTD.close();
   
    
    }

    public static String leadingZeros(String s, int length) {
        if (s.length() >= length) {
            return s;
        } else {
            return String.format("%0" + (length - s.length()) + "d%s", 0, s);
        }
    }
    public static ArrayList<symbolTable> symtab = new ArrayList();
    public static ArrayList<code> c = new ArrayList();
    public static ArrayList<InstructionSet> list = new ArrayList();
    public static ArrayList<Registers> re = new ArrayList();
    public static ArrayList<LITTAB> lt = new ArrayList();
    public static ArrayList<EXTDEF> exdef = new ArrayList();
    public static ArrayList<EXTREF> exref = new ArrayList();
    public static int proglen1;
    public static int proglen2;
    public static int proglen3;
    public static int section_number = 1;

    public static void main(String[] args) throws IOException, ScriptException {

        boolean baseflag = false;
        int format = 0;
        String basereg = null;
        Scanner x;
        readfile();
        readregisters();
        //  printing ();
        readcode();
        // set LOCCTR   
        code line = new code();
        code line2 = new code();
        line = c.get(0);
        line2 = c.get(1);
        if ((line.getMnemonic()).equals("START") && !((line.getOperand()).equals("--------"))) {
            line.setLoc(line.getOperand());
            line2.setLoc(line.getOperand());
            c.set(0, line);
            c.set(1, line2);

        } else {
            line.setLoc("0");
            line2.setLoc(line.getLoc());
            c.set(0, line);
            c.set(1, line2);
        }
        int counter = Integer.parseInt(line.getLoc(), 16);
        int start = counter;
        //line.setLoc(Integer.toHexString(counter));
        //c.set(1, line);
        //System.out.println(line.getLoc());
        //System.out.println(counter);
        int i = 1;

        line = c.get(1);
        //symbolTable symt = new symbolTable();
        //symt.setSymbol(line.getMnemonic());
        // symt.setSymbol(line.getLoc());
        // symtab.add(symt);
        int cc = 0;
        while (!((line.getMnemonic()).equals("END"))) {
            // System.out.println("i in the while loop " + i);

            line = c.get(i);
            if (i < c.size() - 1) {
                line2 = c.get(i + 1);
            }
            symbolTable sta = new symbolTable();
            if ((line.getMnemonic()).equals("EQU")) {
                if (line.getOperand().equals("*")) {
                    line.setLoc(Integer.toHexString(counter));
                } else {

                    line2.setLoc(Integer.toHexString(counter));
                    int neg = 0;
                    int pos = 0;
                    int h = 0;

                    String operand = line.getOperand();
                    String[] parts = operand.split(" ");
                    // System.out.println(Arrays.toString(parts));
                    int len = parts.length;
                    int j = 0;
                    ArrayList<String> afterreplace = new ArrayList();
                    int s = 0;
                    for (j = 0; j < len; j++) {
                        String par = parts[j];
                        if (Character.isAlphabetic(parts[0].charAt(0)) && !(parts[0].equals("+")) && !(parts[0].equals("-")) && h == 0) {
                            pos++;
                            h++;
                        }
                        if (Character.isAlphabetic(par.charAt(0))) {
                            for (int q = 0; q < symtab.size(); q++) {
                                symbolTable s3 = new symbolTable();
                                s3 = symtab.get(q);
                                if ((s3.getSymbol()).equals(par)) {
                                    //    System.out.println(s3.getSymbol());
                                    //    System.out.println(s3.getAddress());
                                    afterreplace.add(s3.getAddress());
                                    s++;
                                }
                            }
                        } else if (par.equals("+") || par.equals("-") || Character.isDigit(par.charAt(0))) {
                            if (par.equals("+")) {
                                pos++;
                                //    System.out.println("positive" + pos);
                            }

                            if (par.equals("-")) {
                                neg++;
                            }
                            afterreplace.add(par);
                        }

                    }
                    //  System.out.println("number of pluses = "+ pos);
                    // System.out.println("number of negative = "+ neg);
                    //System.out.println(Arrays.toString(afterreplace.toArray()));
                    // String ff = Arrays.toString(afterreplace.toArray());
                    // >> search for converting from array list to string 
                    if (pos == 0 || neg == 0) {
                        System.out.println("ERROR IN THE EXPRESSION");
                        System.exit(0);
                    } else {
                        //symtablee();

                        StringBuilder sb = new StringBuilder();
                        for (String st : afterreplace) {
                            sb.append(st);
                        }
                        // System.out.println(sb.toString());
                        ScriptEngineManager mgr = new ScriptEngineManager();
                        ScriptEngine engine = mgr.getEngineByName("JavaScript");
                        String foo = sb.toString();
                        //  System.out.println(engine.eval(foo));
                        int num = (int) engine.eval(foo);

                        line.setLoc(Integer.toHexString(num));
                        //   System.out.println("---->" + line.getLoc());
                        //   System.out.println("positive and negative" + pos + neg);
                        /*if(pos!=neg)
                {
               // sta.setSymbol(line.getLabel());
                sta.setAddress(line.getLoc());
                System.out.println("++"+line.getLoc());
                sta.setC('R');
                symtab.set(cc-1, sta);
                }
                else {
                   // sta.setSymbol(line.getLabel());
                sta.setAddress(line.getLoc());
                System.out.println("++"+line.getLoc());
                sta.setC('A');
                symtab.set(cc-1, sta);
                }
                         */
                        if (!((line.getLabel()).equals("         ")) && !((line.getLabel()).equals("--------"))) {
                            for (int q = 0; q < symtab.size(); q++) {
                                symbolTable s3 = new symbolTable();
                                s3 = symtab.get(q);
                                if (symtab.get(q).getSymbol().equals(line.getLabel())) {
                                    //  symtablee();
                                    //  System.out.println(" q = " + q);
                                    System.err.println("here double label in string of label " + line.getLabel());
                                    System.exit(0);
                                }

                            }
                            sta.setSymbol(line.getLabel());
                            sta.setAddress(line.getLoc());
                            if (pos == neg) {
                                sta.setC('A');
                            } else {
                                sta.setC('R');
                            }
                            symtab.add(sta);
                        }
                        // System.out.println("++ i = " + i);
                        i++;
                        //System.out.println("++ i = " + i);
                        continue;
                    }
                }
            } else if (line.getMnemonic().equals("CSECT")) {
                if (section_number == 1) {
                    proglen1 = counter - start;
                }
                if (section_number == 2) {
                    proglen2 = counter - start;
                }

                counter = 0;
                section_number++;

                EXTDEF d2 = new EXTDEF();
                line.setLoc(Integer.toHexString(counter));

                d2.setName(line.getLabel());
                d2.setCsnumber(section_number);
                exdef.add(d2);

            } else if ((line.getMnemonic()).equals("EXDEF")) {

                line.setLoc(Integer.toHexString(counter));
                line2.setLoc(Integer.toHexString(counter));
                String operand1 = line.getOperand();
                String[] parts1 = operand1.split(",");

                int len4 = parts1.length;

                for (int s1 = 0; s1 < len4; s1++) {
                    EXTDEF d1 = new EXTDEF();
                    d1.setName(parts1[s1]);
                    d1.setCsnumber(section_number);

                    exdef.add(d1);
                }
                parts1 = null;
            } else if ((line.getMnemonic()).equals("EXREF")) {

                line.setLoc(Integer.toHexString(counter));
                line2.setLoc(Integer.toHexString(counter));
                String operand2 = line.getOperand();
                String[] parts2 = operand2.split(",");
                int len2 = parts2.length;
                for (int s2 = 0; s2 < len2; s2++) {
                    EXTREF R2 = new EXTREF();
                    R2.setName(parts2[s2]);
                    R2.setCsnumber(section_number);
                    exref.add(R2);
                }
            } else if ((line.getMnemonic()).startsWith("+")) {
                counter += 4;
                line2.setLoc(Integer.toHexString(counter));

            } else if ((line.getMnemonic()).equals("WORD")) {
                counter += 3;
                line2.setLoc(Integer.toHexString(counter));

            } else if ((line.getMnemonic()).equals("RESB")) {
                counter = counter + Integer.parseInt(line.getOperand());
                line2.setLoc(Integer.toHexString(counter));
            } else if ((line.getMnemonic()).equals("RESW")) {
                counter += 3 * (Integer.parseInt(line.getOperand(), 10));
                line2.setLoc(Integer.toHexString(counter));
            } else if ((line.getMnemonic()).equals("BYTE")) {
                if ((line.getOperand()).startsWith("C")) {
                    int z = (line.getOperand()).length();
                    counter = counter + (z - 3);
                    line2.setLoc(Integer.toHexString(counter));

                } else if ((line.getOperand()).startsWith("X")) {
                    int z = ((line.getOperand()).length()) - 3;
                    counter = counter + (int) (Math.ceil(z / 2));
                    line2.setLoc(Integer.toHexString(counter));
                }
            } else if (((line.getMnemonic()).equals("BASE"))) {
                line2.setLoc(Integer.toHexString(counter));
                baseflag = true;
                basereg = line.getOperand();

            } else if (((line.getMnemonic()).equals("LTORG")) && !(lt.isEmpty())) {
                LITTAB z = new LITTAB();

                z = lt.get(0);
                z.setAddress(Integer.toHexString(counter));
                lt.set(0, z);
                counter = counter + z.getLenght();
                // System.out.println(lt.size());
                for (int h = 1; h < lt.size(); h++) {
                    z = lt.get(h);
                    z.setAddress(Integer.toHexString(counter));
                    counter = counter + z.getLenght();
                    lt.set(h, z);
                }
                line2.setLoc(Integer.toHexString(counter));
            } else if ((line.getMnemonic()).equals("ORG")) {
                int oldcounter = counter;
                if (Character.isDigit((line.getOperand()).charAt(0))) {
                    counter = Integer.parseInt(line.getOperand());
                } else if ((line.getOperand()).equals("--------")) {
                    counter = oldcounter;

                } else if ((line.getOperand()).contains("+") || (line.getOperand()).contains("-")) {
                    //////////////////////////////

                    String string = line.getOperand();;
                    String[] parts = string.split("+");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    for (i = 0; i < symtab.size(); i++) {
                        symbolTable s3 = new symbolTable();
                        s3 = symtab.get(i);
                        if ((s3.getSymbol()).equals(part1)) {
                            counter = (Integer.parseInt(s3.getAddress())) + (Integer.parseInt(part2));
                        }
                    }
                } else {
                    for (i = 0; i < symtab.size(); i++) {
                        symbolTable s4 = new symbolTable();
                        s4 = symtab.get(i);
                        if ((s4.getSymbol()).equals(line.getOperand())) {
                            counter = Integer.parseInt(s4.getAddress());
                        }
                    }
                }
            } else {
                if ((line.getMnemonic()).equals("LTORG")) {
                    break;
                }
                int flag = 0;
                for (int k = 0; k < list.size(); k++) {
                    InstructionSet I = new InstructionSet();
                    I = list.get(k);
                    if ((line.getMnemonic()).equals(I.getMnemonic())) {
                        int y = I.getFormat();
                        counter += y;
                        line2.setLoc(Integer.toHexString(counter));
                        flag = 1;
                        //System.out.println(line.getLoc());
                        //   System.out.println(counter);
                        if ((line.getOperand()).startsWith("=")) {
                            LITTAB l = new LITTAB();
                            l.setName(line.getOperand());
                            String s = line.getOperand().substring(1);
                            if (s.startsWith("X")) {
                                int le = ((line.getOperand().length()) - 4) / 2; //why 4
                                l.setLenght(le);
                                String st = s.substring(2, s.length() - 1);
                                l.setValue(st);
                            } else if (s.startsWith("C")) {
                                int le = s.length() - 3;
                                l.setLenght(le);
                                String st = s.substring(2, s.length() - 1);
                                String sb = " ";
                                for (i = 0; i < st.length(); i++) {
                                    char a = st.charAt(i);
                                    //System.out.println(a);
                                    String t = Integer.toHexString((int) a);
                                    char c = t.charAt(0);
                                    char d = t.charAt(1);
                                    sb = new StringBuilder(sb).insert(sb.length(), c).toString();
                                    sb = new StringBuilder(sb).insert(sb.length(), d).toString();

                                }
                                l.setValue(sb.substring(1));

                            }
                            boolean flagd = false;
                            if (!lt.isEmpty()) {
                                for (int p = 0; p < lt.size(); p++) {
                                    LITTAB j = new LITTAB();
                                    j = lt.get(p);
                                    if (j.getValue().equals(l.getValue())) {
                                        flagd = true;

                                    }

                                }
                            }
                            if (!flagd) {
                                lt.add(l);
                            }
                        }

                    }

                }

                if ((flag == 0) && !((line.getMnemonic().equals("END")))) {

                    System.out.println("ERROR ! instruction not found \t ");
                    System.out.println(line.getMnemonic());
                    System.exit(0);
                }
            }

            if (!((line.getLabel()).equals("         ")) && !((line.getLabel()).equals("--------"))) {
                for (int q = 0; q < symtab.size(); q++) {
                    symbolTable s3 = new symbolTable();
                    s3 = symtab.get(q);
                    if (symtab.get(q).getSymbol().equals(line.getLabel())) {
                        System.err.println("double label in string of label " + line.getLabel());
                        System.exit(0);
                    }

                }
                sta.setSymbol(line.getLabel());
                sta.setAddress(line.getLoc());
                sta.setC('R');
                symtab.add(sta);
            }

            i++;
            //System.out.println("location counter ==> "+ counter);
        }
        // printing();
        printingEXTAB();
        symtablee();
        //System.out.println();
        //printlttable();
        // System.out.println();
        for (int j = 0;
                j < symtab.size()
                - 1; j++) {
            symbolTable s1 = new symbolTable();
            s1 = symtab.get(j);
            for (int k = j + 1; k < symtab.size(); k++) {
                symbolTable s2 = new symbolTable();
                s2 = symtab.get(k);
                if ((s2.getSymbol()).equals(s1.getSymbol())) {
                    System.out.println("ERROR ! DOUBLE LABEL");
                    System.out.println(s2.getSymbol());
                    //System.exit(0);
                }
            }
        }

        for (i = 0;
                i < symtab.size();
                i++) {
            symbolTable s3 = new symbolTable();
            s3 = symtab.get(i);
            if ((s3.getSymbol()).equals(basereg)) {
                basereg = s3.getAddress();
            }
        }

        proglen3 = counter - start;

        // pass 2
        for (int z = 0; z < c.size(); z++) {

            code n = new code();
            code n2 = new code();
            if (z < c.size() - 1) {
                n2 = c.get(z + 1);
            }

            n = c.get(z);
            if (((n.getMnemonic()).equals("START")) || ((n.getMnemonic()).equals("END"))
                    || ((n.getMnemonic()).equals("RESW")) || ((n.getMnemonic()).equals("RESB"))
                    || ((n.getMnemonic()).equals("BASE")) || ((n.getMnemonic()).equals("LTORG"))
                    || ((n.getMnemonic()).equals("EQU")) || ((n.getMnemonic()).equals("CSECT"))
                    || ((n.getMnemonic()).equals("EXDEF")) || ((n.getMnemonic()).equals("EXREF"))) {

                n.setObjcode("--------");

            } else if ((n.getMnemonic()).equals("WORD")) {
                int w = Integer.parseInt(n.getOperand(), 10);
                n.setObjcode(Integer.toHexString(w));
            } else if ((n.getMnemonic()).equals("BYTE")) {
                if (n.getOperand().startsWith("X")) {
                    String st = n.getOperand();
                    st = st.substring(2, st.length() - 1);
                    n.setObjcode(st);
                } else {
                    String st = n.getOperand();
                    String sb = " ";
                    st = st.substring(2, st.length() - 1);
                    for (i = 0; i < st.length(); i++) {
                        char a = st.charAt(i);
                        // System.out.println(a);
                        //String sb = n.getObjcode();
                        // C'test'
                        //int length=sb.length();
                        String t = Integer.toHexString((int) a);
                        char c = t.charAt(0);
                        char d = t.charAt(1);

                        //sb = new StringBuilder(sb).insert(sb.length(), Integer.toHexString((int) a)).toString();
                        // System.out.println(t);
                        sb = new StringBuilder(sb).insert(sb.length(), c).toString();//Null pointer exceptiopn here 
                        sb = new StringBuilder(sb).insert(sb.length(), d).toString();

                    }
                    String tp = sb.substring(1);
                    // System.out.println(tp);
                    n.setObjcode(tp.toUpperCase());
                }
            } else {
                for (int r = 0; r < list.size(); r++) {
                    InstructionSet op = new InstructionSet();
                    op = list.get(r);
                    String test;
                    if (n.getMnemonic().startsWith("+")) {
                        test = (n.getMnemonic()).substring(1);
                    } else {
                        test = n.getMnemonic();
                    }
                    if ((test).equals(op.getMnemonic())) {
                        format = op.getFormat();
                        n.setObjcode(op.getOpcode().toUpperCase());
                    }
                }

                switch (format) {
                    case 2:
                        String st = n.getOperand();
                        if (st.contains(",")) {
                            String[] parts = st.split(",");
                            String p1 = parts[0];
                            String p2 = parts[1];
                            for (int y = 0; y < re.size(); y++) {
                                Registers reg = new Registers();
                                reg = re.get(y);
                                if ((reg.getName()).equals(p1)) {
                                    String sb = n.getObjcode();
                                    sb = new StringBuilder(sb).insert(sb.length(), reg.getNumber()).toString();
                                    // sb.concat(Integer.toHexString(reg.getNumber()));

                                    n.setObjcode(sb);
                                }
                            }
                            for (int y = 0; y < re.size(); y++) {
                                Registers reg = new Registers();
                                reg = re.get(y);
                                if ((reg.getName()).equals(p2)) {
                                    String sb = n.getObjcode();
                                    //sb.concat(p2);
                                    sb = new StringBuilder(sb).insert(sb.length(), reg.getNumber()).toString();

                                    n.setObjcode(sb);
                                }

                            }
                        } else {
                            String p1 = n.getOperand();
                            // System.out.println(n.getLabel() + "\t" + n.getMnemonic() + "\t" + n.getOperand());
                            for (int y = 0; y < re.size(); y++) {
                                Registers reg = new Registers();
                                reg = re.get(y);
                                if ((reg.getName()).equals(p1)) {
                                    String sb = n.getObjcode();
                                    //sb.concat(p1);
                                    int a = reg.getNumber();
                                    String q = Integer.toHexString(a);
                                    sb = new StringBuilder(sb).insert(sb.length(), q.charAt(0)).toString();
                                    //System.out.println(p1);
                                    n.setObjcode(sb);
                                }
                            }
                            String sb = n.getObjcode();
                            //sb.concat("0");
                            // System.out.println(sb);
                            sb = new StringBuilder(sb).insert(sb.length(), '0').toString();
                            n.setObjcode(sb);

                        }

                        break;

                    case 3:
                        int in = 1 << 5;
                        int im = 1 << 4;
                        int ix = 1 << 3;
                        int ba = 1 << 2;
                        int pc = 1 << 1;
                        int e = 1;
                        //System.out.println(n.getMnemonic());
                        int obj = (Integer.parseInt(n.getObjcode(), 16)) << 4;
                        if ((n.getOperand()).startsWith("@")) {
                            obj = obj | in;
                        } else if ((n.getOperand()).startsWith("#")) {
                            obj = obj | im;
                        } else {
                            obj = obj | in | im;
                        }

                        if ((n.getOperand()).endsWith(",X")) {
                            obj = obj | ix;
                        }
                        if ((n.getMnemonic()).startsWith("+")) {
                            obj = obj | e;

                            obj = obj << 20;
                            if ((n.getOperand()).startsWith("#")) {
                                String su = null;
                                String tran = n.getOperand();
                                tran = tran.substring(1);

                                if (Character.isAlphabetic(tran.charAt(0))) {
                                    for (i = 0; i < symtab.size(); i++) {
                                        symbolTable s3 = new symbolTable();
                                        s3 = symtab.get(i);
                                        if (tran.equals(s3.getSymbol())) {
                                            // System.out.println("s3 add" + (s3.getAddress()));
                                            if (s3.getAddress().length() > 4) {
                                                su = s3.getAddress().substring(4);
                                                // System.out.println("// su " + su);
                                                obj = obj | Integer.parseInt(su, 16);
                                                String unpadded = Integer.toHexString(obj);
                                                String padded = "00000000".substring(unpadded.length()) + unpadded;
                                                n.setObjcode(padded.toUpperCase());
                                            } else {
                                                obj = obj | Integer.parseInt(s3.getAddress(), 16);
                                                String unpadded = Integer.toHexString(obj);
                                                String padded = "00000000".substring(unpadded.length()) + unpadded;
                                                n.setObjcode(padded.toUpperCase());
                                            }

                                        }
                                    }
                                } else {
                                    //  System.out.println("******>" + tran);
                                    obj = obj | Integer.parseInt(tran);
                                    n.setObjcode(Integer.toHexString(obj).toUpperCase());
                                }
                            } else {
                                EXTREF ex = new EXTREF();
                                int found = 0;
                                for (int q = 0; q < exref.size(); q++) {
                                    ex = exref.get(q);
                                    if ((n.getOperand()).equals(ex.getName())) {
                                        String unpadded = Integer.toHexString(obj);
                                        String padded = "00000000".substring(unpadded.length()) + unpadded;
                                        n.setObjcode(padded.toUpperCase());
                                        found = 1;
                                        //System.out.println(n.getOperand());
                                        //System.out.println(found);
                                    }
                                }
                                if (found == 0) {
                                    for (i = 0; i < symtab.size(); i++) {
                                        symbolTable s3 = new symbolTable();
                                        s3 = symtab.get(i);
                                        if ((n.getOperand()).equals(s3.getSymbol())) {
                                            obj = obj | Integer.parseInt(s3.getAddress(), 16);
                                            String unpadded = Integer.toHexString(obj);
                                            String padded = "00000000".substring(unpadded.length()) + unpadded;
                                            n.setObjcode(padded.toUpperCase());

                                        }
                                    }
                                }
                                found = 0;
                            }

                        } else {

                            String sb = n.getOperand();
                            if (sb.equals("--------")) {
                                obj = obj << 12;
                                n.setObjcode(Integer.toHexString(obj).toUpperCase()); //RSUB
                            } else if (sb.startsWith("#")) {

                                if (Character.isDigit(sb.charAt(1))) {
                                    obj = obj << 12;
                                    sb = sb.substring(1);
                                    int ope = Integer.parseInt(sb, 16);
                                    obj = obj | ope;
                                    String unpadded = Integer.toHexString(obj);
                                    String padded = "000000".substring(unpadded.length()) + unpadded;
                                    n.setObjcode(padded.toUpperCase());
                                } else {
                                    //obj = obj << 12;
                                    sb = sb.substring(1);
                                    //look at sym table
                                    //make it in fn not to write it again    
                                    //copied from last else 
                                    for (i = 0; i < symtab.size(); i++) {
                                        symbolTable s3 = new symbolTable();
                                        s3 = symtab.get(i);
                                        int disp;
                                        if (sb.equals(s3.getSymbol())) {
                                            //  System.out.println(s3.getSymbol() + " " + s3.getAddress() + " " + n2.getMnemonic() + " " + n2.getLoc());
                                            int address = Integer.parseInt(s3.getAddress(), 16);
                                            int nextpc = Integer.parseInt(n2.getLoc(), 16);

                                            disp = address - nextpc;

                                            //  System.out.println("disp=" + Integer.toHexString(disp));
                                            if (disp >= -2048 && disp <= 2047) {
                                                obj = obj | pc;

                                            } else if (baseflag) {

                                                disp = Integer.parseInt(s3.getAddress()) - Integer.parseInt(basereg);
                                                if (disp >= 0 && disp <= 4095) {
                                                    obj = obj | ba;
                                                } else {
                                                    System.out.println("disp out of range");
                                                }

                                            } else {
                                                System.out.println("Base not initialized");
                                            }
                                            obj = obj << 12;
                                            obj = obj | (disp & 0x000FFF);
                                            obj = obj | 0x000000;
                                            String unpadded = Integer.toHexString(obj);
                                            String padded = "000000".substring(unpadded.length()) + unpadded;
                                            n.setObjcode(padded.toUpperCase());

                                        }
                                    }

                                }

                            } else if (sb.startsWith("@")) {
                                //obj = obj << 12;
                                sb = sb.substring(1);
                                //copied from last else 
                                for (i = 0; i < symtab.size(); i++) {
                                    symbolTable s3 = new symbolTable();
                                    s3 = symtab.get(i);
                                    int disp;
                                    if (sb.equals(s3.getSymbol())) {
                                        // System.out.println(s3.getSymbol() + " " + s3.getAddress() + " " + n2.getMnemonic() + " " + n2.getLoc());
                                        int address = Integer.parseInt(s3.getAddress(), 16);
                                        int nextpc = Integer.parseInt(n2.getLoc(), 16);

                                        disp = address - nextpc;

                                        //System.out.println("disp=" + Integer.toHexString(disp));
                                        if (disp >= -2048 && disp <= 2047) {
                                            obj = obj | pc;
                                        } else if (baseflag) {

                                            disp = Integer.parseInt(s3.getAddress()) - Integer.parseInt(basereg);
                                            if (disp >= 0 && disp <= 4095) {
                                                obj = obj | ba;
                                            } else {
                                                System.out.println("disp out of range");
                                            }

                                        } else {
                                            System.out.println("Base not initialized");
                                            System.exit(0);
                                        }
                                        obj = obj << 12;
                                        obj = obj | (disp & 0x000FFF);
                                        String unpadded = Integer.toHexString(obj);
                                        String padded = "000000".substring(unpadded.length()) + unpadded;
                                        n.setObjcode(padded.toUpperCase());
                                    }
                                }

                            } else if (sb.startsWith("=")) {
                                String s = " ";
                                int disp;
                                s = sb.substring(3, sb.length() - 1);
                                for (i = 0; i < lt.size(); i++) {
                                    LITTAB l = new LITTAB();
                                    l = lt.get(i);
                                    if (l.getName().equals(sb)) {
                                        // System.out.println(l.getAddress());
                                        int address = Integer.parseInt(l.getAddress(), 16);
                                        // System.out.println(address);
                                        int nextpc = Integer.parseInt(n2.getLoc(), 16);

                                        disp = address - nextpc;

                                        obj = obj << 12;
                                        obj = obj | (disp & 0x000FFF);
                                        String unpadded = Integer.toHexString(obj);
                                        String padded = "000000".substring(unpadded.length()) + unpadded;
                                        n.setObjcode(padded.toUpperCase());

                                    }

                                }

                            } else {
                                String d = n.getOperand();
                                if (n.getOperand().endsWith(",X")) {
                                    d = d.substring(0, d.length() - 2);
                                }
                                // System.out.println("\n" + d + "\n");
                                //put this in a fn
                                for (i = 0; i < symtab.size(); i++) {
                                    symbolTable s3 = new symbolTable();
                                    s3 = symtab.get(i);
                                    int disp;
                                    if (d.equals(s3.getSymbol())) {
                                        // System.out.println(s3.getSymbol() + " " + s3.getAddress() + " " + n2.getMnemonic() + " " + n2.getLoc());
                                        int address = Integer.parseInt(s3.getAddress(), 16);
                                        int nextpc = Integer.parseInt(n2.getLoc(), 16);

                                        disp = address - nextpc;

                                        // System.out.println("disp=" + Integer.toHexString(disp));
                                        if (disp >= -2048 && disp <= 2047) {
                                            obj = obj | pc;
                                        } else if (baseflag) {

                                            disp = Integer.parseInt(s3.getAddress()) - Integer.parseInt(basereg);
                                            if (disp >= 0 && disp <= 4095) {
                                                obj = obj | ba;
                                            } else {
                                                System.out.println("disp out of range");
                                            }

                                        } else {
                                            System.out.println("Base not initialized");
                                            System.exit(0);
                                        }
                                        obj = obj << 12;
                                        obj = obj | (disp & 0x000FFF);

                                        String unpadded = Integer.toHexString(obj);
                                        String padded = "000000".substring(unpadded.length()) + unpadded;

                                        n.setObjcode(padded.toUpperCase());
                                    }
                                }

                            }

                        }

                        break;
                }
                //    case 4:

                //     break;
                //     default:
                //    break;               
            }
        }

        printing();

        writeFile1();
    }

}
