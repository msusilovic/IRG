package hr.fer.zemris.linearna.demo;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

import java.util.Scanner;

public class Drugi {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Unesite podatke o prvoj jednadžbi:");
        String prvi = sc.nextLine().strip();

        System.out.println("Unesite podatke o drugoj jednadžbi:");
        String drugi = sc.nextLine().strip();

        System.out.println("Unesite podatke o trećoj jednadžbi:");
        String treci = sc.nextLine().strip();

        String[] prviRed = prvi.split(" ");
        String[] drugiRed = drugi.split(" ");
        String[] treciRed = treci.split(" ");

        StringBuilder sb = new StringBuilder();
        sb.append(prviRed[0] + " " + prviRed[1] + " " + prviRed [2] + " |");
        sb.append(drugiRed[0] + " " + drugiRed[1] + " " + drugiRed [2] + " |");
        sb.append(treciRed[0] + " " + treciRed[1] + " " + treciRed [2]);

        IMatrix a = Matrix.parseSimple(sb.toString());

        sb = new StringBuilder();

        sb.append(prviRed[3] + " | " + drugiRed[3] + " | " + treciRed[3]);
        IMatrix r = Matrix.parseSimple(sb.toString());

        IMatrix v = (a.nInvert()).nMultiply(r);
        System.out.println("Rjesenje sustava je: ");
        System.out.println(v);

        sc.close();
    }
}
