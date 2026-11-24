import java.util.ArrayDeque;
import java.util.Deque;

public class EvaluadorExpresiones {
    public static void main(String[] args) {
        String[] casosPrueba = {"(8 + 4) / 2 * 3", "5 / 0", "9 - 3 + 2 * 4"};
        for (String caso : casosPrueba) {
            System.out.println("Entrada: " + caso);
            String postfija = convertir(caso);
            if (postfija != null) {
                Integer resultado = evaluar(postfija);
                System.out.println("Expresion postfija: " + postfija);
                if (resultado != null) {
                    System.out.println("Resultado: " + resultado);
                }
            }
            System.out.println();
        }
    }

    public static String convertir(String infija) {
        if (infija == null || infija.trim().isEmpty()) {
            System.out.println("Error: Expresion vacia");
            return null;
        }

        Deque<Character> pila = new ArrayDeque<>();
        StringBuilder salida = new StringBuilder();
        boolean esperaNumero = true;
        for (int i = 0; i < infija.length(); i++) {
            char c = infija.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (Character.isDigit(c)) {
                if (!esperaNumero || (i + 1 < infija.length() && Character.isDigit(infija.charAt(i + 1)))) {
                    System.out.println("Error: Digito invalido o de mas de un digito");
                    return null;
                }
                if (salida.length() > 0) {
                    salida.append(' ');
                }
                salida.append(c);
                esperaNumero = false;
            } else if (c == '(') {
                if (!esperaNumero) {
                    System.out.println("Error: Falta operador antes de '('");
                    return null;
                }
                pila.push(c);
                esperaNumero = true;
            } else if (c == ')') {
                if (esperaNumero) {
                    System.out.println("Error: Parentesis de cierre invalido");
                    return null;
                }
                boolean encontrado = false;
                while (!pila.isEmpty()) {
                    char tope = pila.pop();
                    if (tope == '(') {
                        encontrado = true;
                        break;
                    }
                    salida.append(' ').append(tope);
                }
                if (!encontrado) {
                    System.out.println("Error: Parentesis desbalanceados");
                    return null;
                }
                esperaNumero = false;
            } else if (esOperador(c)) {
                if (esperaNumero) {
                    System.out.println("Error: Operador en posición invalida");
                    return null;
                }
                while (!pila.isEmpty() && pila.peek() != '(' && prioridad(pila.peek()) >= prioridad(c)) {
                    salida.append(' ').append(pila.pop());
                }
                pila.push(c);
                esperaNumero = true;
            } else {
                System.out.println("Error: Caracter no permitido '" + c);
                return null;
            }
        }

        if (esperaNumero) {
            System.out.println("Error: Expresión incompleta");
            return null;
        }
        while (!pila.isEmpty()) {
            char tope = pila.pop();
            if (tope == '(' || tope == ')') {
                System.out.println("Error: Parentesis desbalanceados");
                return null;
            }
            salida.append(' ').append(tope);
        }
        return salida.toString();
    }

    public static Integer evaluar(String postfija) {
        Deque<Integer> pila = new ArrayDeque<>();
        String[] partes = postfija.trim().split("\\s+");
        for (String parte : partes) {
            if (parte.length() == 1 && Character.isDigit(parte.charAt(0))) {
                pila.push(parte.charAt(0) - '0');
            } else if (parte.length() == 1 && esOperador(parte.charAt(0))) {
                if (pila.size() < 2) {
                    System.out.println("Error: Faltan operandos");
                    return null;
                }
                int b = pila.pop();
                int a = pila.pop();
                char op = parte.charAt(0);
                if (op == '/' && b == 0) {
                    System.out.println("Error: Division entre cero.");
                    return null;
                }
                pila.push(operar(a, b, op));
            } else {
                System.out.println("Error: parte invalida '" + parte);
                return null;
            }
        }

        if (pila.size() != 1) {
            System.out.println("Error: Expresion postfija invalida");
            return null;
        }

        return pila.pop();
    }

    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    private static int prioridad(char op) {
        if (op == '*' || op == '/') {
            return 2;
        }
        if (op == '+' || op == '-') {
            return 1;
        }
        return -1;
    }

    private static int operar(int a, int b, char op) {
        if (op == '+') {
            return a + b;
        }
        if (op == '-') {
            return a - b;
        }
        if (op == '*') {
            return a * b;
        }
        return a / b;
    }
}