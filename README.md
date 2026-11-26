# Comparaci�n de algoritmos de ordenamiento

Pr�ctica de An�lisis de Algoritmos. El programa implementa seis sorts en Java y mide cu�nto tardan en ordenar arreglos de enteros en orden ascendente. El archivo [Comparacion_de_Sorts.xlsx](Comparacion_de_Sorts.xlsx) incluye las 108 mediciones, las medianas y el an�lisis.

## Ejecutar

Con un JDK 17 o m�s reciente, desde la ra�z del repositorio:

```sh
javac -encoding UTF-8 -d out SortBenchmark.java
java -cp out SortBenchmark --check
java -cp out SortBenchmark > mediciones.csv
```

El �ltimo comando guarda las nuevas mediciones en CSV. El progreso se muestra en la terminal. La prueba puede tardar m�s de un minuto, principalmente por Bubble Sort con 100 000 elementos.

## C�mo se midi�

- Cada tama�o tiene tres repeticiones. En cada repetici�n se genera un arreglo de enteros pseudoaleatorios con una semilla reproducible. Cada sort recibe una copia del mismo arreglo y el orden de ejecuci�n rota entre repeticiones.
- Se calienta la JVM antes de medir. El reloj mide solo la llamada al sort e incluye el arreglo auxiliar que reserva Merge Sort. Se excluyen la generaci�n de datos, las copias de entrada y la verificaci�n.
- Se compara cada salida completa con una referencia obtenida con `Arrays.sort`. La mediana es el valor central de las tres mediciones, expresadas en milisegundos.
- Los seis sorts comparten cinco tama�os, desde 100 hasta 100 000 elementos. Merge, Heap y Quick Sort tambi�n se miden con 1 y 5 millones. Para los tres cuadr�ticos, esos dos tama�os quedan sin medici�n porque el costo crece aproximadamente con el cuadrado de la entrada.
- Entorno de la corrida registrada: Intel Core Ultra 7 258V, Windows 11 y OpenJDK Temurin 17.0.18.

## Complejidad de las implementaciones

| Algoritmo | Mejor caso | Caso promedio | Peor caso | Espacio auxiliar |
| --- | --- | --- | --- | --- |
| Insertion Sort | O(n) | O(n�) | O(n�) | O(1) |
| Selection Sort | O(n�) | O(n�) | O(n�) | O(1) |
| Bubble Sort con salida temprana | O(n) | O(n�) | O(n�) | O(1) |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Heap Sort | O(n) con valores iguales | O(n log n) | O(n log n) | O(1) |
| Quick Sort con pivote central | O(n log n) | O(n log n) | O(n�) | O(log n) |

Heap Sort representa el mont�culo en el mismo arreglo. Con �ndices desde cero, los hijos del nodo `i` est�n en `2*i + 1` y `2*i + 2`. Quick Sort elige el valor de la posici�n central como pivote, recurre sobre la partici�n menor y procesa la mayor mediante un ciclo. Esa elecci�n mantiene la pila en O(log n) incluso si las particiones son desbalanceadas.

## Resultados

Al duplicar la entrada de 50 000 a 100 000 elementos, las medianas de Insertion, Selection y Bubble Sort crecieron **4,09x**, **3,96x** y **4,10x**, respectivamente. Para los mismos tama�os, Merge, Heap y Quick Sort crecieron entre **1,73x y 2,22x**. Los tiempos observados son coherentes con el crecimiento esperado para estas entradas aleatorias.

Con 100 000 elementos, Quick Sort registr� **9,07 ms** y Bubble Sort **17,50 s**. Con 5 millones, Quick Sort registr� **569,13 ms**, Merge Sort **712,06 ms** y Heap Sort **951,25 ms**. Las cifras corresponden a esta corrida en este equipo.
