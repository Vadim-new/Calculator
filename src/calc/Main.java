package calc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("Введите выражение: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().toUpperCase();   //  считываем строку и переводим все в верхний регистр
            System.out.println(Main.calc(input));
        }

    }

    public static String calc(String input) throws IOException {
        int allOperations = 0;  //  кол-во операций в веденном выражении
        int operation = 0;      //  позиция операции в массиве
        String operand1 = "";   //  операнды в строковом формате
        String operand2 = "";
        int operand1_rim = 0;   //  формат операндов 0-арабский 1-римский
        int operand2_rim = 0;
        int number1 = 0;        //  операнды в числовом формате
        int number2 = 0;
        int res = 0;            //  результат вычислений в числовом формате
        String result = "";     //  результат вычислений в строковом формате

        Map<String, Integer> map = new HashMap<>();   //  коллекция соответствий арабских и римских цифр
        {
            map.put("I", 1);
            map.put("II", 2);
            map.put("III", 3);
            map.put("IV", 4);
            map.put("V", 5);
            map.put("VI", 6);
            map.put("VII", 7);
            map.put("VIII", 8);
            map.put("IX", 9);
            map.put("X", 10);
            map.put("XX", 20);
            map.put("XXX", 30);
            map.put("XL", 40);
            map.put("L", 50);
            map.put("LX", 60);
            map.put("LXX", 70);
            map.put("LXXX", 80);
            map.put("XC", 90);
            map.put("C", 100);
        }

        char[] operations = {'+', '-', '*', '/'};   //  массив возможных операций

        for (int i = 0; i < operations.length; i++) {  //  проходим по массиву операций и смотрим сколько их в полученном выражении
            if (input.indexOf(operations[i]) > 0) {
                if (input.substring(input.indexOf(operations[i]) + 1, input.length()).indexOf(operations[i]) > 0)
                    throw new IOException("Не верное количество операций!");

                allOperations++;         //  считаем кол-во операций в выражении
                operation = i;           //  запоминаем вид операции
                operand1 = input.substring(0, input.indexOf(operations[i]));                    //  запоминаем операнды
                operand2 = input.substring(input.indexOf(operations[i]) + 1, input.length());
            }
        }

        if (allOperations != 1) throw new IOException("Не верное количество операций!");

        if (map.get(operand1) != null) {           //  проверяем операнд1 по коллекции соответствий
            number1 = map.get(operand1);           //  если находим, запоминаем его числовое значение
            operand1_rim = 1;                      //  и ставим признак, что он в римском формате
        } else {                                   //  если не находим, то пытаемся получить целое число
            try {
                number1 = Integer.parseInt(operand1);
            } catch (NumberFormatException e) {
                throw new IOException("Не верный формат числа");
            }
        }

        if (map.get(operand2) != null) {            //  аналогично первому операнду
            number2 = map.get(operand2);
            operand2_rim = 1;
        } else {
            try {
                number2 = Integer.parseInt(operand2);
            } catch (NumberFormatException e) {
                throw new IOException("Не верный формат числа");
            }
        }

        if (operand1_rim != operand2_rim) throw new IOException("Нельзя использовать разные системы исчисления!");
        if (number1 > 10 || number2 > 10) throw new IOException( "Нельзя использовать числа больше 10!");
        if (operation == 3 && number2 == 0) throw new IOException( "Нельзя делить на ноль!");

        res = switch (operation) {          //  вычисляем результат
            case 0 -> number1 + number2;
            case 1 -> number1 - number2;
            case 2 -> number1 * number2;
            case 3 -> number1 / number2;
            default -> 0;
        };
        if (operand1_rim == 1 && res <= 0) throw new IOException( "Римское значение не может быть меньше 1");

        result = res + "";

        if (operand1_rim == 1) {                     //  если выражение было римскими цифрами, переводим обратно в римские цифры
            StringBuffer sb = new StringBuffer();
            int arabNumber = 0;
            String arabNumberString = "";
            int digitNumbers = 1;
            for (int i = result.length() - 1; i >= 0; i--) {
                if (!result.substring(i, i + 1).equals("0")) {
                    arabNumber = Integer.parseInt(result.substring(i, i + 1)) * digitNumbers;
                    for (String rimSymb : map.keySet()) {
                        if (map.get(rimSymb) == arabNumber) {
                            sb.insert(0, rimSymb);
                        }
                    }

                }
                digitNumbers = digitNumbers * 10;
            }
            result = sb.toString();
        }

        return result;
    }
}
