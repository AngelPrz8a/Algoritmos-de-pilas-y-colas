import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Map;


public class Main {
    private static int contenedores_maximo;
    private static int pilas_maximo;
    private static Scanner scanner;
    private static ArrayList<LinkedList<String>> pilas;
    private static HashMap<Integer, String> contenedores_almacenados;
    private static HashMap<Integer, String> contenedores_retirados;
    
    public static void main(String[] args) {

        System.out.println("Cuantos contenedores pueden estar uno encima del otro: ");
        scanner = new Scanner(System.in);
        contenedores_maximo = scanner.nextInt();

        System.out.println("El limite de pilas que pueden haber: ");
        scanner = new Scanner(System.in);
        pilas_maximo = scanner.nextInt();

        //Crear las pilas
        System.out.println("-----------------");
        pilas= crear_pilas(pilas_maximo);
        listar_pilas();

        //Inicializar los contenedores
        contenedores_almacenados = new HashMap<>();
        contenedores_retirados = new HashMap<>();

        //Menu de opciones
        scanner = new Scanner(System.in);
        boolean continuar = true;
        while (continuar == true) {
            System.out.println("-----------------");
            System.out.println("Opciones");
            System.out.println("1 Agregar un producto");
            System.out.println("2 Listar pilas");
            System.out.println("3 Retirar un producto");
            System.out.println("4 Salir");
            System.out.print("Su elección es: ");

            //Capturar la elección
            scanner = new Scanner(System.in);
            int eleccion = scanner.nextInt();
            System.out.println("-----------------");

            //Según la elección realizar la siguiente actividad
            switch (eleccion) {
                case 1:
                    //capturar el nombre
                    System.out.print("Digite el nombre del nuevo producto: ");
                    scanner = new Scanner(System.in);
                    String nombre = scanner.nextLine();
                    agregar_producto(nombre);
                    break;
                case 2:
                    listar_pilas();
                    break;
                case 3:
                    listar(contenedores_almacenados, "almacenados");
                    //capturar el id
                    System.out.print("El indice del producto que desea retirar: ");
                    scanner = new Scanner(System.in);
                    int indice = scanner.nextInt();
                    System.out.println("-----------------");
                    retirar_producto(indice);
                    listar_pilas();
                    listar(contenedores_almacenados, "almacenados");
                    listar(contenedores_retirados, "retirados");
                    break;
                case 4:
                    continuar = false;
                    break;
                default:
                    System.out.println("La opción no está en la lista");
                    break;
            }
        }

    }
    //retirar_producto
    public static void retirar_producto(int indice){
        indice = indice-1;
        //Identificar la pila en donde está
        int id_ubicacion_pila = 0;
        int pila_ubicacion = 0;
        //recorrer pilas
        for (int i = 0; i < pilas.size(); i++) {
            //obtener el linkedlist de la pila actual
            LinkedList<String> pila = pilas.get(i);
            //recorrer pila
            for(int e=0; e<pila.size(); e++){
                if (contenedores_almacenados.containsKey(indice) && contenedores_almacenados.get(indice) == pila.get(e)) {
                    pila_ubicacion = i;
                    id_ubicacion_pila = e;
                    mover_contenedores(pila_ubicacion, id_ubicacion_pila, indice);
                    break;
                }
            }
        }
    }

    //Mover los contenedores encima si los hay
    public static void mover_contenedores(int pila_base, int id_ubicacion_pila, int indice){
        boolean finalizo = false;
        while (finalizo == false) {
            //obtener la pila descrita en donde está el producto a eliminar
            LinkedList<String> pila = pilas.get(pila_base);
            //Verificar si el ultimo elemento de la pila es el deseado a eliminar
            if (pila.getLast() == pila.get(id_ubicacion_pila)) {
                //agregar en contenedores retirados
                contenedores_retirados.put(contenedores_retirados.size(), contenedores_almacenados.get(indice));
                //eliminar de contenedores almacenados
                contenedores_almacenados.remove(indice);
                //eliminar de la pila actual
                pila.remove(id_ubicacion_pila);

                finalizo=true;
            }else{
                //Resaltar el ultimo producto de la pila
                String ultimo_producto = pila.getLast();
                //verificar que la pila actual sea menor al ultimo de las pilas, para mover los productos a la izquierda
                if (pila_base < (pilas.size()-1)) {
                    //contador para saber si la fila siguiente no ha superado el limite
                    int contador = 1;
                    LinkedList<String> pila_siguiente = pilas.get(pila_base+contador);
                    boolean verificar = false;
                    while (verificar==false) {
                        if (pila_siguiente.size() < contenedores_maximo) {
                            pila_siguiente.add(ultimo_producto);
                            verificar = true;
                        }else{
                            contador++;
                            pila_siguiente = pilas.get(pila_base+contador);
                        }
                    }
                    
                    pila.remove(ultimo_producto);

                    finalizo=true;
                    mover_contenedores(pila_base, id_ubicacion_pila, indice);
                }else{
                    int contador = 1;
                    LinkedList<String> pila_siguiente = pilas.get(pila_base-contador);
                    boolean verificar = false;
                    while (verificar==false) {
                        if (pila_siguiente.size() < contenedores_maximo) {
                            pila_siguiente.add(ultimo_producto);
                            verificar = true;
                        }else{
                            contador--;
                            pila_siguiente = pilas.get(pila_base-contador);
                        }
                    }
                    pila.remove(ultimo_producto);

                    finalizo=true;
                    mover_contenedores(pila_base, id_ubicacion_pila, indice);
                }
            }
        }
    }

    //Listar a los hashmap
    public static void listar(HashMap<Integer, String> hashMap, String nombre){
        System.out.println("Lista de los contenedores "+nombre);
        for(Map.Entry<Integer, String> entry: hashMap.entrySet()){
            if (entry.getValue()!=null) {                
                System.out.println("Indice: "+(entry.getKey()+1)+" producto: "+entry.getValue());
            }
        }
    }

    //agregar un producto a las pilas
    public static void agregar_producto(String nombre){
        boolean valor_agregado = false;
        //Recorrer pilas
        for (int i = 0; i < pilas.size(); i++) {
            //Obtener una pila
            LinkedList<String> pila = pilas.get(i);
            if (pila.size() < contenedores_maximo) {
                //evitar duplicar el valor en todas las pilas
                if (valor_agregado == false) {
                    //Agregar un producto
                    int indice = contenedores_almacenados.size();
                    while (contenedores_almacenados.containsKey(indice)) {
                        indice++;
                    }
                    contenedores_almacenados.put(indice, nombre);
                    pila.add(contenedores_almacenados.get(indice));
                    valor_agregado = true;
                    System.out.println("Se agrego '"+nombre+"' en la pila "+(i+1)+" en el indice "+indice);
                    break;
                }
            }else{
                continue;
            }
        }
        //Advertir si todas las pilas están llenas
        if (valor_agregado == false) {
            System.out.println("No hay más espacio en las pilas.");
        }
        //listar las pilas
        listar_pilas();
    }

    //Listar pilas
    public static void listar_pilas(){
        for (int i = 0; i < pilas.size(); i++) {
            int indice_para_imprimir = i+1;
            System.out.println("Pila #"+(indice_para_imprimir)+": "+pilas.get(i));
        }
    }

    //Crear pilas
    public static ArrayList<LinkedList<String>> crear_pilas(int limite){
        ArrayList<LinkedList<String>> pilas = new ArrayList<LinkedList<String>>();
        int contador = 1;
        while (contador <= limite) {
            LinkedList<String> pila = new LinkedList<String>();
            pilas.add(pila);
            contador++;
        }
        return pilas;
    }

}
