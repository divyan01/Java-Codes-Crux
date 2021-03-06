package june14;

import java.util.Scanner;

public class A3Q16Q17 {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);

		System.out.println("Enter number ");
		int n = scn.nextInt();
		
		System.out.println("Enter precision ");
		int p = scn.nextInt();

		double sqrt = 0;
		double inc = 1;
		int mult = 1;
		double div = 1.0;
		int count = 0;

		while (count <= p) {
			while (sqrt * sqrt <= n) {
				System.out.println(Math.round(sqrt * mult) / div);
				sqrt = sqrt + inc;
			}

			System.out.println(Math.round(sqrt * mult) / div);
			sqrt = sqrt - inc;
			count++;
			inc = inc * 0.1;
			mult = mult * 10;
			div = div * 10.0;
		}

		System.out.println(Math.round(sqrt * mult) / div);
	}

}
