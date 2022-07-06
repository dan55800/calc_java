package com.dan55800;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static int convertFromRoman (@NotNull String in) throws IOException {
        int result;
        int Is = 0,  Vs = 0, Xs = 0;
        int i = 0;
        while (i<in.length()) {
            switch (in.charAt(i)) {
                case 'I' -> Is++;
                case 'V' -> Vs++;
                case 'X' -> Xs++;
            }
            i++;
        }
        // специфика римской СС с повторением буков
        if (Is > 3 || Vs > 1 || Xs > 3)
            throw new IOException("Неверный формат числа");
        // чтобы не было чего-то типа "IIX"
        if (in.indexOf('V')-in.indexOf('I')>1 || (in.indexOf('X')>in.indexOf('V') && in.indexOf('V') != -1)
            || in.indexOf('X')-in.indexOf('I')>1)
            throw new IOException("Неверный формат числа");
        if (in.indexOf('I') != -1) {
            // для 4 и 9
            if (in.indexOf('V')>in.indexOf('I')) return 4;
            if (in.indexOf('X')>in.indexOf('I')) return 9;
        }
        result = 10*Xs + 5*Vs + Is;
        return result;
    }

    public static @NotNull
    String convertToRoman (String in) throws IOException {
        StringBuilder result = new StringBuilder();
        int num = Integer.parseInt(in);
        if (num < 1) throw new IOException("Недопустимый результат");
        switch (num / 10) {
            case 4 -> result.append("XL");
            case 5, 6, 7, 8 -> {
                result.append("L");
                num -= 50;
            }
            case 9 -> result.append("XC");
            case 10 -> result.append("C");
        }
        for (int i = 0; i<num/10 && (num/10)<4; i++)
            result.append("X");
        num %= 10;
        switch (num) {
            case 4 -> {
                result.append("IV");
                return result.toString();
            }
            case 5, 6, 7, 8 -> {
                result.append("V");
                num -= 5;
            }
            case 9 -> {
                result.append("IX");
                return result.toString();
            }
        }
        for (int i = 0; i<num && num<3; i++)
            result.append("I");
        return result.toString();
    }

    public static String calc (@NotNull String input) throws IOException {
        String output = "";
        int i = 0;
        String nums = "0123456789";
        String romans = "IVX";
        String signs = "+-*/";
        String appropriateSymbols = nums+romans+signs;
        int arg1, arg2;
        char sign;
        // проверяем на наличие недопустимых символов вообще
        while (i<input.length() && appropriateSymbols.indexOf(input.charAt(i)) != -1) i++;
        if (i<input.length() && appropriateSymbols.indexOf(input.charAt(i)) == -1)
            throw new IOException("Не математическая операция: содержит недопустимые символы");
        // проверка наличия знаков операций
        if (input.indexOf('+') == -1 && input.indexOf('-') == -1  && input.indexOf('*') == -1
                && input.indexOf('/') == -1)
            throw new IOException("Не математическая операция: ни одного знака операции");
        i = 0;
        // если первый символ ВНЕЗАПНО не цифра
        if (signs.indexOf(input.charAt(i)) != -1)
            throw new IOException("Не математическая операция: знак операции не может стоять в начале");
        while (signs.indexOf(input.charAt(i)) == -1) i++;
        // считываем первый аргумент
        if (romans.indexOf(input.charAt(i-1)) != -1)
            arg1 = convertFromRoman(input.substring(0, i));
        else
            arg1 = Integer.parseInt(input.substring(0, i));
        if (arg1 < 1 || arg1 > 10)
            throw new IOException("Допускаются только числа от 1 до 10");
        // считываем знак операции
        sign = input.charAt(i);
        while (i<input.length() && signs.indexOf(input.charAt(i)) == -1) i++;
        // если знак операции встретился второй раз или входная строка заканчивается на него
        if (input.indexOf('+') != input.lastIndexOf('+') || input.indexOf('-') != input.lastIndexOf('-')
                || (input.indexOf('*') != input.lastIndexOf('*') || input.indexOf('/') != input.lastIndexOf('/')
                || signs.indexOf(input.charAt(input.length()-1)) != -1))
            throw new IOException("Допускается только одна операция");
        // если операнды в разных СС, выдать ошибку
        if ((romans.indexOf(input.charAt(input.indexOf(sign) + 1)) != -1) == (romans.indexOf(input.charAt(i - 1)) == -1))
            throw new IOException("Разные системы счисления");
        // считываем второй аргумент
        if (romans.indexOf(input.charAt(input.indexOf(sign)+1)) != -1)
            arg2 = convertFromRoman(input.substring(input.indexOf(sign) + 1));
        else
            arg2 = Integer.parseInt(input.substring(input.indexOf(sign) + 1));
        if (arg2 < 1 || arg2 > 10) {
            throw new IOException("Допускаются только числа от 1 до 10");
        }
        // вычисляем
        switch (sign) {
            case '+' -> output = String.valueOf(arg1 + arg2);
            case '-' -> output = String.valueOf(arg1 - arg2);
            case '*' -> output = String.valueOf(arg1 * arg2);
            case '/' -> output = String.valueOf(arg1 / arg2);
        }
        // если римские операнды, конвертируем
        if (romans.indexOf(input.charAt(i-1)) != -1) {
            output = convertToRoman(output);
        }
        return output;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Введите выражение");
        Scanner in = new Scanner(System.in);
        String expr = in.nextLine();
        System.out.print(calc(expr));
    }
}