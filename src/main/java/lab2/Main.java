package lab2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;

public class Main {
//Точность, с которой вычисляем В
    private static final double PRECISION = 0.0001;


    public static void main(String[] args) {
	//Массив с интервалами между ошибками, данные взяты с таблицы №1 в лабораторной работе
	//int[] errorIntervals = new int[] {9, 12, 11, 4, 7, 2, 5, 8, 5, 7, 1, 6, 1, 9, 4, 1, 3, 3, 6, 1, 11, 33, 7, 91, 2, 1};
        int[] errorIntervals = uploadErrorIntervalsFromFile("source.txt");
	//Нижняя граница В, которую берём для первого приближения при решении уравнения.
	//Так как нам нужно подобрать В, т.е. общее кол-во ошибок, то за нижнюю границу можем взять то число ошибок,
	//которое уже у нас есть, число зафиксированных ошибок
        int lowerB = errorIntervals.length;
	//Верхняя граница В, при подстановке в уравнение необходимо чтобы знаки у результата уравнения при lowerB и upperB
	//имели разные знаки. Т.е. "F(lowerB) > 0 и F(upperB) < 0", или наоборот. Значение 100 в нашем случае подходит.
	//int upperB = 100;
        int upperB = lowerB * 2;

        double b, k, x, t;
	//Экземпляры класса уравнения, которое решается численным методом (делением пополам).
	//lowerBorder - решение уравнения при нижней границе В, upperBorder - при верхней границе В.
        Lab2Equation lowerBorder = new Lab2Equation(errorIntervals, lowerB);
        Lab2Equation upperBorder = new Lab2Equation(errorIntervals, upperB);
	    
	b = findBWithDividingByHalf(errorIntervals, lowerBorder, upperBorder);
        k = getK(errorIntervals, b);
        x = 1 / (k * (b - errorIntervals.length));
        t = getT(errorIntervals, b, k);

        System.out.printf("В = %f, К = %f, Хn+1 = %f, tk = %f", b, k, x, t);
	    
    }

    /**
     * Нахождение корня уравнения, т.е. В, при котором результат будет приблизительно равен 0.
     * Если длина отрезка (верхняя граница - нижняя граница) меньше заданной точности,
     * значит результат лежит в середине этого отрезка.
     * Пока данное условие не будет выполнено, вызываем функцию рекурсивно, деля отрезок пополам.
     * Если в середине отрезка (middlePoint) результат вычисления уравнения <0, то сдвигаем верхнюю границу.
     * Если >= 0, то сдвигаем нижнюю.
     * @param errorIntervals Массив с интервалами между ошибками.
     * @param lowerBorder Экземпляр класса уравнения, где за В взята нижняя граница.
     * @param upperBorder Экземпляр класса уравнения, где за В взята верхняя граница.
     * @return Приближенное значение В.
     */
    private static double findBWithDividingByHalf(int[] errorIntervals, Lab2Equation lowerBorder, Lab2Equation upperBorder){
        double middlePoint = (lowerBorder.getB() + upperBorder.getB()) / 2;
        Lab2Equation middle = new Lab2Equation(errorIntervals, middlePoint);

        if (upperBorder.getB() - lowerBorder.getB() < PRECISION){
            return middlePoint;
        }
        if (middle.getLeftMinusRightEquation() < 0){
            return findBWithDividingByHalf(errorIntervals, lowerBorder, middle);
        }
        else{
            return findBWithDividingByHalf(errorIntervals, middle, upperBorder);
        }	
    }
	
    private static double getK(int[] errorIntervals, double b){
        int n = errorIntervals.length;
        double result = 0;
        for (int i = 1; i <= n; i++) {
            result+= (b - i + 1) * errorIntervals[i - 1];
        }
        return n / result;
    }
    private static double getT(int[] errorIntervals, double b, double k){
        int length = errorIntervals.length;
        double result = 0;
        for (double i = 1; i <= b - length; i++) {
            result += 1 / i;
        }

        return result / k;
    }
}
