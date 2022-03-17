# Práctica 1

- Arganis Ramírez Francisco Alejandro
- Pérez Márquez José Alejandro
- Reyna Trejo Rodrigo Alvar

## Comparación entre ejecución secuencial y ejecución concurrente
Los siguientes resultados corresponden a la ejecución de los algoritmos implementados con un procesador Intel(R) Core(TM) i5-10300H CPU @ 2.50GHz de 8 unidades lógicas de procesamiento. Los tiempos de ejecución mostrados, en milisegundos, corresponden al promedio de 100 ejecuciones. La fracción de código en paralelo se calculó a partir de la ley de Amdahl.

### Prueba de primalidad
Se realizó la prueba de primalidad para el número 2147483647.
|Número de hilos|Tiempo de ejecución (ms)|Aceleración teórica|Aceleración obtenida|Porcentaje de código paralelo|
|:-:|:-:|:-:|:-:|:-:|
|1|0.22431|-|-|-|
|2|0.20036|2|1.1195|21.354|
|3|0.21828|3|1.0276|4.032|
|4|0.24878|4|0.9016|0|
|5|0.29792|5|0.7529|0|
|6|0.34417|6|0.6517|0|
|7|0.39320|7|0.5704|0|
|8|0.43968|8|0.5101|0|

En este caso, como se utilizó la optimización de revisar divisores solo hasta la raíz del número, para probar la primalidad de 2147483647 se deben revisar solo 46339 posibles divisores. A cada hilo le toca una cantidad relativamente pequeña de divisores que puede verificar rápidamente. Sin embargo, al incrementar la cantidad de hilos, se tiene que realizar más cambios de contexto. El aumento en los tiempos que se observa es resultado de que se pierde más tiempo en los cambios de contexto que lo que se gana con el trabajo en paralelo de los hilos.

### Promedio de una matriz
Se calculó el promedio para una matriz de 10000 x 10000
|Número de hilos|Tiempo de ejecución (ms)|Aceleración teórica|Aceleración obtenida|Porcentaje de código paralelo|
|:-:|:-:|:-:|:-:|:-:|
|1|316.84|-|-|-|
|2|155.50|2|2.0375|100|
|3|116.50|3|2.7196|94.846|
|4|102.96|4|3.0773|90.005|
|5|92.71|5|3.4175|88.424|
|6|86.20|6|3.6756|87.352|
|7|81.21|7|3.9014|86.763|
|8|80.81|8|3.9208|85.137|

A diferencia del caso anterior, donde se manejaban cantidades en el orden de 10^5, aquí cada hilo realiza una cantidad considerable de trabajo pues se tienen que recorrer las 10^8 entradas de la matriz. Así, paralelizar el trabajo de los hilos aporta más a la velocidad que lo que se pierde por los cambios de contexto. Sin embargo, se observa que el speedup no es lineal respecto al número de hilos, los cual se debe a que no todo el tiempo corresponde a la ejecución en paralelo, sino que solo una fracción del programa se beneficia de la paralelización.
