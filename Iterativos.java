import java.util.Scanner;
import java.util.function.Function;

public class Iterativos {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Seleccione el método");
            System.out.println("1 Bisección");
            System.out.println("2 Punto fijo");
            System.out.println("3 Newton-Raphson");

            System.out.print("Método a usar: ");
            String metodo = scanner.nextLine();

            //Primer método iterativo, Biseccion

            if (metodo.equals("1")) {
                System.out.println("Bisección");
                System.out.print("Ingrese una función. Asegurese de escribir los argumentos entre parentesis: ");
                String fx = scanner.nextLine();

                Function<Double, Double> fun = x -> evaluar(fx, x);

                while (true) {
                    int n = obt(scanner, "Ingrese el número de iteraciones que necesite: ");

                    System.out.print("Ingrese el primer valor de a,b (Asegurese de que \"a\"sea menor que \"b\")\n a= ");
                    double a = Double.parseDouble(scanner.nextLine());

                    System.out.print("b= ");
                    double b = Double.parseDouble(scanner.nextLine());

                    if (a > b) {
                        System.out.println("Asegurese de que \"a\"sea menor que \"b\"");
                        continue;
                    }

                    int i = 0;
                    double an = a, bn = b, cn = 0;

                    while (i < n) {
                        double fa = fun.apply(an);
                        double fb = fun.apply(bn);

                        if (fa * fb > 0) {
                            System.out.println("No es posible aplicar este método ya que: f(a)*f(b) > 0");
                            break;
                        } else {
                            cn = (an + bn) / 2.0;
                            double fc = fun.apply(cn);

                            if (fc * fa < 0) {
                                bn = cn;
                            } else {
                                an = cn;
                            }
                        }

                        i++;
                    }

                    System.out.println("Resultado:");
                    System.out.println("La iteración final fue: " + i);
                    System.out.println("Su raíz apróximada fue:  " + fx + " es x= " + cn +
                            " dentro del intervalo [" + a + "," + b + "]");
                    break;
                }

            //Segundo método iterativo, Punto fijo

            } else if (metodo.equals("2")) {
                System.out.println("Método de Punto fijo");

                System.out.print("Ingrese la expresión de la función g(x): ");
                String gx = scanner.nextLine();

                Function<Double, Double> g = x -> evaluar(gx, x);

                double x0 = obtenerDouble(scanner, "Aproximación inicial (x0): ");
                double tolerancia = obtenerDouble(scanner, "Tolerancia: ");
                int maxIter = obt(scanner, "Ingrese la cantidad de iteraciones que desea: ");

                try {
                    Pair<Double, Integer> solucion = puntoFijo(g, x0, tolerancia, maxIter);
                    System.out.println("\nLa solucion es: " + solucion.getFirst());
                    System.out.println("Número de iteraciones: " + solucion.getSecond());
                } catch (Exception e) {
                    System.out.println("\nError: " + e.getMessage());
                }

            // Tercer método iterativo, Newton-Raphson

            } else if (metodo.equals("3")) {
                System.out.println("Método de Newton-Raphson");

                System.out.print("Ingrese g(x) a calcular: ");
                String fx = scanner.nextLine();

                Function<Double, Double> f = x -> evaluar(fx, x);
                Function<Double, Double> fPrime = x -> derivada(f, x);

                double x0 = obtenerDouble(scanner, "Ingrese la primer aproximación x0: ");

                Pair<Double, Integer> result = newtonRaphson(f, fPrime, x0);

                System.out.println("Resultado:");
                System.out.println("Raíz aproximada: " + result.getFirst());
                System.out.println("Número de iteraciones: " + result.getSecond());

            } else {
                System.out.println("La eleccion fue mal hecha");
            }

            System.out.print("¿Quieres reiniciar el programa? (s/n): ");
            String reiniciar = scanner.nextLine();
            if (!reiniciar.toLowerCase().equals("s")) {
                break;
            }
        }
    }

    //En esta sección se encuentran las evaluaciones, operaciones y excepciones de cada uno de los metodos
    private static double evaluar(String expresion, double x) {
        // Evalua manualmente las funciones
        return analisis.evaluar(expresion, x);
    }

    private static double derivada(Function<Double, Double> f, double x) {
        // Calcula manualmente las derivadas
        double h = 1e-6;
        return (f.apply(x + h) - f.apply(x - h)) / (2 * h);
    }

    private static int obt(Scanner scanner, String string) {

        //Excepcion dentro del metodo de Bisección

        while (true) {
            System.out.print(string);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Porfavor ingresa solo numeros enteros aqui");
            }
        }
    }

    private static double obtenerDouble(Scanner scanner, String mensaje) {

        //  Excepcion dentro del metodo Punto Fijo

        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Porfavor ingrese solo números válidos aqui");
            }
        }
    }

    private static Pair<Double, Integer> puntoFijo(Function<Double, Double> g, double x0, double v, int maxIter) {
        int i = 0;
        double x = x0;

        while (i < maxIter) {
            double xAnterior = x;
            x = g.apply(xAnterior);

            // Determinamos la existencia de convergencia en el metodo Punto Fijo

            if (Math.abs(x - xAnterior) < v) {
                return new Pair<>(x, i + 1);
            }

            i++;
        }

        // Sin no existe convergencia en el ordenamiento elejido

        throw new RuntimeException("El ordenamiento g(x) elejido no converge en" + maxIter + " iteraciones.");
    }

    private static Pair<Double, Integer> newtonRaphson(Function<Double, Double> f, Function<Double, Double> fPrime, double x0) {
        int iteracion = 0;
        double x = x0;

        //Implementacion del metodo Newton-Raphson para encontrar las raices de la función

        while (Math.abs(f.apply(x)) > 1e-6 && iteracion < 100) {
            x = x - f.apply(x) / fPrime.apply(x);
            iteracion++;
        }

        return new Pair<>(x, iteracion);
    }

    static class Pair<A, B> {
        private A first;
        private B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

    static class analisis {
        public static double evaluar(String expresion, double x) {
            // Implementa la evaluación de funciones manualmente según sea necesario
            if (expresion.contains("x")) {
                // Reemplaza la variable x en la expresión
                expresion = expresion.replace("x", String.valueOf(x));
            }
            return evaluarExpresion(expresion);
        }

        private static double evaluarExpresion(String expresion) {
            // Implementa la evaluación de la expresión manualmente según sea necesario
            if (expresion.contains("sin")) {
                return Math.sin(evaluacion(expresion, "sin"));
            } else if (expresion.contains("cos")) {
                return Math.cos(evaluacion(expresion, "cos"));
            } else if (expresion.contains("tan")) {
                return Math.tan(evaluacion(expresion, "tan"));
            } else if (expresion.contains("^")) {
                String[] partes = expresion.split("\\^");
                double base = Double.parseDouble(partes[0]);
                double exponente = Double.parseDouble(partes[1]);
                return Math.pow(base, exponente);
            } else {
                // Si no hay funciones trigonométricas o potencias, asume que es un número
                return Double.parseDouble(expresion);
            }
        }

        private static double evaluacion(String t, String s) {
            // Obtiene el argumento de la función trigonométrica
            int inicio = t.indexOf(s) + s.length() + 1;
            int fin = t.indexOf(")", inicio);
            String argumento = t.substring(inicio, fin);
            return evaluarExpresion(argumento);
        }
    }
}
