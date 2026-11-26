import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;

public class SortBenchmark {
    private static final int[] TAMANOS = {100, 1_000, 10_000, 50_000, 100_000, 1_000_000, 5_000_000};
    private static final int LIMITE_CUADRATICOS = 100_000;
    private static final int REPETICIONES = 3;
    private static final long SEMILLA = 20261125L;

    private record Algoritmo(String nombre, Consumer<int[]> ordenar, boolean cuadratico) {}

    private static final Algoritmo[] ALGORITMOS = {
        new Algoritmo("Insertion Sort", SortBenchmark::insertionSort, true),
        new Algoritmo("Selection Sort", SortBenchmark::selectionSort, true),
        new Algoritmo("Bubble Sort", SortBenchmark::bubbleSort, true),
        new Algoritmo("Merge Sort", SortBenchmark::mergeSort, false),
        new Algoritmo("Heap Sort", SortBenchmark::heapSort, false),
        new Algoritmo("Quick Sort", SortBenchmark::quickSort, false)
    };

    static void insertionSort(int[] arreglo) {
        for (int i = 1; i < arreglo.length; i++) {
            int actual = arreglo[i];
            int j = i - 1;
            while (j >= 0 && arreglo[j] > actual) {
                arreglo[j + 1] = arreglo[j];
                j--;
            }
            arreglo[j + 1] = actual;
        }
    }

    static void selectionSort(int[] arreglo) {
        for (int i = 0; i < arreglo.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (arreglo[j] < arreglo[menor]) {
                    menor = j;
                }
            }
            if (menor != i) {
                intercambiar(arreglo, i, menor);
            }
        }
    }

    static void bubbleSort(int[] arreglo) {
        for (int fin = arreglo.length - 1; fin > 0; fin--) {
            boolean huboIntercambio = false;
            for (int j = 0; j < fin; j++) {
                if (arreglo[j] > arreglo[j + 1]) {
                    intercambiar(arreglo, j, j + 1);
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) {
                return;
            }
        }
    }

    static void mergeSort(int[] arreglo) {
        int[] auxiliar = new int[arreglo.length];
        mergeSort(arreglo, auxiliar, 0, arreglo.length);
    }

    private static void mergeSort(int[] arreglo, int[] auxiliar, int inicio, int fin) {
        if (fin - inicio <= 1) {
            return;
        }
        int medio = inicio + (fin - inicio) / 2;
        mergeSort(arreglo, auxiliar, inicio, medio);
        mergeSort(arreglo, auxiliar, medio, fin);

        int izquierda = inicio;
        int derecha = medio;
        for (int destino = inicio; destino < fin; destino++) {
            if (izquierda < medio && (derecha == fin || arreglo[izquierda] <= arreglo[derecha])) {
                auxiliar[destino] = arreglo[izquierda++];
            } else {
                auxiliar[destino] = arreglo[derecha++];
            }
        }
        System.arraycopy(auxiliar, inicio, arreglo, inicio, fin - inicio);
    }

    static void heapSort(int[] arreglo) {
        for (int padre = arreglo.length / 2 - 1; padre >= 0; padre--) {
            bajar(arreglo, padre, arreglo.length);
        }
        for (int fin = arreglo.length - 1; fin > 0; fin--) {
            intercambiar(arreglo, 0, fin);
            bajar(arreglo, 0, fin);
        }
    }

    private static void bajar(int[] arreglo, int padre, int tamanoHeap) {
        while (padre < tamanoHeap / 2) {
            int hijoMayor = 2 * padre + 1;
            int hijoDerecho = hijoMayor + 1;
            if (hijoDerecho < tamanoHeap && arreglo[hijoDerecho] > arreglo[hijoMayor]) {
                hijoMayor = hijoDerecho;
            }
            if (arreglo[padre] >= arreglo[hijoMayor]) {
                return;
            }
            intercambiar(arreglo, padre, hijoMayor);
            padre = hijoMayor;
        }
    }

    static void quickSort(int[] arreglo) {
        quickSort(arreglo, 0, arreglo.length - 1);
    }

    private static void quickSort(int[] arreglo, int inicio, int fin) {
        while (inicio < fin) {
            int pivote = arreglo[inicio + (fin - inicio) / 2];
            int izquierda = inicio;
            int derecha = fin;

            while (izquierda <= derecha) {
                while (arreglo[izquierda] < pivote) {
                    izquierda++;
                }
                while (arreglo[derecha] > pivote) {
                    derecha--;
                }
                if (izquierda <= derecha) {
                    intercambiar(arreglo, izquierda, derecha);
                    izquierda++;
                    derecha--;
                }
            }

            if (derecha - inicio < fin - izquierda) {
                quickSort(arreglo, inicio, derecha);
                inicio = izquierda;
            } else {
                quickSort(arreglo, izquierda, fin);
                fin = derecha;
            }
        }
    }

    private static void intercambiar(int[] arreglo, int i, int j) {
        int temporal = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temporal;
    }

    private static int[] generarArreglo(int tamano, Random random) {
        int[] arreglo = new int[tamano];
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = random.nextInt();
        }
        return arreglo;
    }

    private static void validar(Algoritmo algoritmo, int[] resultado, int[] esperado) {
        if (!Arrays.equals(resultado, esperado)) {
            throw new IllegalStateException(algoritmo.nombre() + " falló con n=" + resultado.length);
        }
    }

    private static void probarAlgoritmos() {
        int[][] casos = {
            {}, {4}, {2, 1}, {1, 2, 3, 4}, {5, 4, 3, 2, 1},
            {7, 7, 7, 7}, {0, -3, 2, -3, 0, Integer.MIN_VALUE, Integer.MAX_VALUE}
        };
        for (int[] caso : casos) {
            int[] esperado = caso.clone();
            Arrays.sort(esperado);
            for (Algoritmo algoritmo : ALGORITMOS) {
                int[] resultado = caso.clone();
                algoritmo.ordenar().accept(resultado);
                validar(algoritmo, resultado, esperado);
            }
        }
    }

    private static void calentarJvm() {
        Random random = new Random(SEMILLA);
        for (int i = 0; i < 24; i++) {
            int[] original = generarArreglo(2_048, random);
            int[] esperado = original.clone();
            Arrays.sort(esperado);
            for (Algoritmo algoritmo : ALGORITMOS) {
                int[] copia = original.clone();
                algoritmo.ordenar().accept(copia);
                validar(algoritmo, copia, esperado);
            }
        }
    }

    public static void main(String[] args) {
        boolean soloPruebas = args.length == 1 && args[0].equals("--check");
        if (args.length > 0 && !soloPruebas) {
            throw new IllegalArgumentException("Uso: java SortBenchmark [--check]");
        }
        probarAlgoritmos();
        if (soloPruebas) {
            System.out.println("Los seis algoritmos pasaron las pruebas de ordenamiento.");
            return;
        }

        calentarJvm();
        PrintWriter csv = new PrintWriter(System.out, true);
        csv.println("tamano,repeticion,algoritmo,tiempo_ms");
        for (int tamano : TAMANOS) {
            for (int repeticion = 1; repeticion <= REPETICIONES; repeticion++) {
                Random random = new Random(SEMILLA + 31L * tamano + repeticion);
                int[] original = generarArreglo(tamano, random);
                int[] esperado = original.clone();
                Arrays.sort(esperado);

                for (int i = 0; i < ALGORITMOS.length; i++) {
                    Algoritmo algoritmo = ALGORITMOS[(i + repeticion - 1) % ALGORITMOS.length];
                    if (algoritmo.cuadratico() && tamano > LIMITE_CUADRATICOS) {
                        continue;
                    }
                    int[] copia = original.clone();
                    long inicio = System.nanoTime();
                    algoritmo.ordenar().accept(copia);
                    long nanosegundos = System.nanoTime() - inicio;

                    validar(algoritmo, copia, esperado);
                    csv.printf(Locale.ROOT, "%d,%d,%s,%.6f%n",
                            tamano, repeticion, algoritmo.nombre(), nanosegundos / 1_000_000.0);
                }
                System.err.printf("n=%,d, repetición %d/%d completada%n", tamano, repeticion, REPETICIONES);
            }
        }
    }
}
