# Comparación de algoritmos de ordenamiento

Práctica de Análisis de Algoritmos. El programa implementa seis sorts en Java y mide cuánto tardan en ordenar arreglos de enteros en orden ascendente. El archivo [Comparacion_de_Sorts.xlsx](Comparacion_de_Sorts.xlsx) incluye las 108 mediciones, las medianas y el análisis.

## Ejecutar

Con un JDK 17 o más reciente, desde la raíz del repositorio:

```sh
javac -encoding UTF-8 -d out SortBenchmark.java
java -cp out SortBenchmark --check
java -cp out SortBenchmark > mediciones.csv
```

El último comando guarda las nuevas mediciones en CSV. El progreso se muestra en la terminal. La prueba puede tardar más de un minuto, principalmente por Bubble Sort con 100 000 elementos.

## Cómo se midió

- Cada tamaño tiene tres repeticiones. En cada repetición se genera un arreglo de enteros pseudoaleatorios con una semilla reproducible. Cada sort recibe una copia del mismo arreglo y el orden de ejecución rota entre repeticiones.
- Se ejecutó el programa hasta completar todas las pruebas. En cada repetición se anotó cuánto tardó cada algoritmo en ordenar su arreglo.
- Se comprobó que cada arreglo quedara correctamente ordenado. La mediana es el valor central de las tres mediciones, expresadas en milisegundos.
- Los seis sorts comparten cinco tamaños, desde 100 hasta 100 000 elementos. Merge, Heap y Quick Sort también se miden con 1 y 5 millones. Para los tres cuadráticos, esos dos tamaños quedan sin medición porque el costo crece aproximadamente con el cuadrado de la entrada.
- Entorno de la corrida registrada: Intel Core Ultra 7 258V, Windows 11 y OpenJDK Temurin 17.0.18.

## Complejidad de las implementaciones

| Algoritmo | Mejor caso | Caso promedio | Peor caso | Espacio auxiliar |
| --- | --- | --- | --- | --- |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |
| Bubble Sort con salida temprana | O(n) | O(n²) | O(n²) | O(1) |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Heap Sort | O(n) con valores iguales | O(n log n) | O(n log n) | O(1) |
| Quick Sort con pivote central | O(n log n) | O(n log n) | O(n²) | O(log n) |

Heap Sort representa el montículo en el mismo arreglo. Con índices desde cero, los hijos del nodo `i` están en `2*i + 1` y `2*i + 2`. Quick Sort elige el valor de la posición central como pivote, recurre sobre la partición menor y procesa la mayor mediante un ciclo. Esa elección mantiene la pila en O(log n) incluso si las particiones son desbalanceadas.

## Resultados

Al duplicar la entrada de 50 000 a 100 000 elementos, las medianas de Insertion, Selection y Bubble Sort crecieron **4,09×**, **3,96×** y **4,10×**, respectivamente. Para los mismos tamaños, Merge, Heap y Quick Sort crecieron entre **1,73× y 2,22×**. Los tiempos observados son coherentes con el crecimiento esperado para estas entradas aleatorias.

Con 100 000 elementos, Quick Sort registró **9,07 ms** y Bubble Sort **17,50 s**. Con 5 millones, Quick Sort registró **569,13 ms**, Merge Sort **712,06 ms** y Heap Sort **951,25 ms**. Las cifras corresponden a esta corrida en este equipo.
