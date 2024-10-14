package es.florida.gestorFicheros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * Clase Modelo para gestionar archivos y directorios.
 */
public class Modelo {
	
	private File directorioTrabajo;

	/**
     * Constructor que inicializa el directorio de trabajo al directorio home del usuario.
     */
	public Modelo() {
		directorioTrabajo = new File(System.getProperty("user.home"));
	}
	
	/**
     * Lista los archivos y subdirectorios en formato de árbol.
     * 
     * @param dir El directorio a listar.
     * @return Una cadena que representa la estructura del directorio y sus archivos.
     */
    public static String listarArchivos(File dir) {
    	String directorioPadre = dir.getName() + "\n";
        return directorioPadre + listarArchivosRecursivo(dir, "");
    }

    /**
     * Método recursivo para listar los archivos y subdirectorios.
     * 
     * @param dir El directorio actual.
     * @param indent La indentación utilizada para estructurar la salida.
     * @return Una cadena con la lista de archivos y subdirectorios.
     */
    private static String listarArchivosRecursivo(File dir, String indent) {
        String resultado = "";
        File[] archivos = dir.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    resultado += indent + "|-- \\" + archivo.getName() + "\n";
                    resultado += listarArchivosRecursivo(archivo, indent + "|   ");
                } else {
                    String nombreArchivo = archivo.getName();
                    String tamanyo = String.format("%.2f KB", (double) archivo.length() / 1024);
                    String fechaModificacion = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(archivo.lastModified()));
                    resultado += indent + "|-- " + nombreArchivo + " (" + tamanyo + " - " + fechaModificacion + ")\n";
                }
            }
        }
        return resultado;
    }
    
    /**
     * Lista los archivos y cuenta coincidencias de una cadena en cada archivo.
     * 
     * @param dir El directorio a recorrer.
     * @param cadenaBuscada La cadena a buscar en los archivos.
     * @param caseSensitive Si es true, la búsqueda será sensible a mayúsculas y minúsculas.
     * @param ignoreAccents Si es true, se ignorarán los acentos en la búsqueda.
     * @return Una cadena con la lista de archivos y el número de coincidencias.
     */
    public static String contarCoincidencias(File dir, String cadenaBuscada, boolean caseSensitive, boolean ignoreAccents) {
        if (cadenaBuscada.length() == 0) {
            return "Cadena no válida";
        }

        if (!caseSensitive) {
            cadenaBuscada = cadenaBuscada.toLowerCase();
        }
        if (ignoreAccents) {
            cadenaBuscada = normalizarCadena(cadenaBuscada);
        }

        String directorioPadre = dir.getName() + "\n";
        return directorioPadre + contarCoincidenciasRecursivo(dir, cadenaBuscada, "", caseSensitive, ignoreAccents);
    }

    /**
     * Método recursivo para contar coincidencias en archivos.
     * 
     * @param dirActual El directorio actual.
     * @param cadenaBuscada La cadena a buscar.
     * @param indent La indentación utilizada para estructurar la salida.
     * @param caseSensitive Indica si la búsqueda es sensible a mayúsculas y minúsculas.
     * @param ignoreAccents Indica si se ignoran los acentos en la búsqueda.
     * @return Una cadena con la lista de archivos y el número de coincidencias.
     */
    private static String contarCoincidenciasRecursivo(File dirActual, String cadenaBuscada, String indent, boolean caseSensitive, boolean ignoreAccents) {
        String resultado = "";
        File[] archivos = dirActual.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    resultado += indent + "|-- \\" + archivo.getName() + "\n";
                    resultado += contarCoincidenciasRecursivo(archivo, cadenaBuscada, indent + "|   ", caseSensitive, ignoreAccents);
                } else {
                    int coincidencias = contarCoincidenciasEnArchivo(archivo, cadenaBuscada, caseSensitive, ignoreAccents);
                    resultado += indent + "|-- " + archivo.getName() + " (" + coincidencias + " coincidencias)\n";
                }
            }
        }
        return resultado;
    }

    /**
     * Cuenta las coincidencias de una cadena en un archivo.
     * 
     * @param archivo El archivo en el que se buscan las coincidencias.
     * @param cadenaBuscada La cadena a buscar.
     * @param caseSensitive Indica si la búsqueda es sensible a mayúsculas y minúsculas.
     * @param ignoreAccents Indica si se ignoran los acentos en la búsqueda.
     * @return El número de coincidencias encontradas.
     */
    private static int contarCoincidenciasEnArchivo(File archivo, String cadenaBuscada, boolean caseSensitive, boolean ignoreAccents) {
        int contador = 0;

        try {
            if (archivo.getName().endsWith(".pdf")) {
                contador = contarCoincidenciasEnPDF(archivo, cadenaBuscada, caseSensitive, ignoreAccents);
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (!caseSensitive) {
                        linea = linea.toLowerCase();
                    }
                    if (ignoreAccents) {
                        linea = normalizarCadena(linea);
                    }
                    contador += contarCoincidenciasEnLinea(linea, cadenaBuscada);
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + archivo.getName());
        }

        return contador;
    }

    /**
     * Cuenta las coincidencias de una cadena en un archivo PDF.
     * 
     * @param archivo El archivo PDF a analizar.
     * @param cadenaBuscada La cadena a buscar.
     * @param caseSensitive Indica si la búsqueda es sensible a mayúsculas y minúsculas.
     * @param ignoreAccents Indica si se ignoran los acentos en la búsqueda.
     * @return El número de coincidencias encontradas en el archivo PDF.
     */
    private static int contarCoincidenciasEnPDF(File archivo, String cadenaBuscada, boolean caseSensitive, boolean ignoreAccents) {
        int contador = 0;
        try {
            FileInputStream fis = new FileInputStream(archivo);
            PdfReader pdfReader = new PdfReader(fis);
            StringBuilder textoPDF = new StringBuilder();

            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                String textoPagina = PdfTextExtractor.getTextFromPage(pdfReader, i);
                textoPDF.append(textoPagina);
            }
            pdfReader.close();

            String texto = textoPDF.toString();
            if (!caseSensitive) {
                texto = texto.toLowerCase();
                cadenaBuscada = cadenaBuscada.toLowerCase();
            }
            if (ignoreAccents) {
                texto = normalizarCadena(texto);
                cadenaBuscada = normalizarCadena(cadenaBuscada);
            }

            contador = contarCoincidenciasEnLinea(texto, cadenaBuscada);

        } catch (IOException e) {
            System.out.println("No se puede leer el archivo PDF: " + archivo.getName());
        }
        return contador;
    }

    /**
     * Reemplaza las ocurrencias de una cadena en los archivos de un directorio.
     * 
     * @param dir El directorio a recorrer.
     * @param cadenaBuscada La cadena a reemplazar.
     * @param cadenaReemplazo La cadena con la que se reemplazará la buscada.
     * @return Una cadena con los nombres de los archivos modificados y el número de reemplazos.
     */
    public static String reemplazarCadenasEnArchivos(File dir, String cadenaBuscada, String cadenaReemplazo) {
        if (cadenaBuscada.length() == 0) {
            return "Cadena no válida";
        }

        String directorioPadre = dir.getName() + "\n";
        return directorioPadre + reemplazarCadenasRecursivo(dir, cadenaBuscada, cadenaReemplazo, "");
    }

    /**
     * Método recursivo para reemplazar cadenas en archivos.
     * 
     * @param dirActual El directorio actual.
     * @param cadenaBuscada La cadena a reemplazar.
     * @param cadenaReemplazo La cadena que reemplazará a la buscada.
     * @param indent La indentación utilizada para estructurar la salida.
     * @return Una cadena con los archivos procesados y el número de reemplazos.
     */
    private static String reemplazarCadenasRecursivo(File dirActual, String cadenaBuscada, String cadenaReemplazo, String indent) {
        String resultado = "";
        File[] archivos = dirActual.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    resultado += indent + "|-- \\" + archivo.getName() + "\n";
                    resultado += reemplazarCadenasRecursivo(archivo, cadenaBuscada, cadenaReemplazo, indent + "|   ");
                } else {
                    int reemplazos = reemplazarCadenaEnArchivo(archivo, cadenaBuscada, cadenaReemplazo);
                    resultado += indent + "|-- " + archivo.getName() + " (" + reemplazos + " reemplazos)\n";
                }
            }
        }
        return resultado;
    }

    /**
     * Reemplaza una cadena en un archivo de texto.
     * 
     * @param archivo El archivo en el que se realizará el reemplazo.
     * @param cadenaBuscada La cadena a reemplazar.
     * @param cadenaReemplazo La cadena con la que se reemplazará la buscada.
     * @return El número de reemplazos realizados.
     */
    private static int reemplazarCadenaEnArchivo(File archivo, String cadenaBuscada, String cadenaReemplazo) {
        int contadorReemplazos = 0;
        String contenidoModificado = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String lineaOriginal = linea;
                int coincidencias = contarCoincidenciasEnLinea(linea, cadenaBuscada);
                if (coincidencias > 0) {
                    contadorReemplazos += coincidencias;
                    lineaOriginal = lineaOriginal.replace(cadenaBuscada, cadenaReemplazo);
                }
                contenidoModificado += lineaOriginal + System.lineSeparator();
            }
            reader.close();
        } catch (IOException e) {
            return -1;
        }

        if (contadorReemplazos > 0) {
            String nuevoNombre = "MOD_" + archivo.getName();
            File nuevoArchivo = new File(archivo.getParent(), nuevoNombre);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nuevoArchivo))) {
                writer.write(contenidoModificado);
            } catch (IOException e) {
                return -1;
            }
        }

        return contadorReemplazos;
    }

    /**
     * Cuenta las coincidencias de una cadena en una línea de texto.
     * 
     * @param texto El texto en el que se buscarán las coincidencias.
     * @param cadenaBuscada La cadena a buscar.
     * @return El número de coincidencias encontradas.
     */
    private static int contarCoincidenciasEnLinea(String texto, String cadenaBuscada) {
        int contador = 0;
        int index = 0;

        while ((index = texto.indexOf(cadenaBuscada, index)) != -1) {
            contador++;
            index += cadenaBuscada.length();
        }

        return contador;
    }

    /**
     * Normaliza una cadena eliminando acentos.
     * 
     * @param cadena La cadena a normalizar.
     * @return La cadena sin acentos.
     */
    private static String normalizarCadena(String cadena) {
        String[] acentuados = new String[] {
            "á", "é", "í", "ó", "ú", "ü", "Á", "É", "Í", "Ó", "Ú", "Ü", "ï", "Í", "à", "è", "ì", "ò", "ù"
        };
        String[] sinAcentos = new String[] {
            "a", "e", "i", "o", "u", "u", "A", "E", "I", "O", "U", "U", "i", "I", "a", "e", "i", "o", "u"
        };

        for (int i = 0; i < acentuados.length; i++) {
            cadena = cadena.replace(acentuados[i], sinAcentos[i]);
        }

        return cadena;
    }
    
    /**
     * Devuelve el directorio de trabajo actual.
     * 
     * @return El directorio de trabajo.
     */
    public File getDirectorioTrabajo() {
        return directorioTrabajo;
    }

    /**
     * Establece el directorio de trabajo.
     * 
     * @param directorioTrabajo El nuevo directorio de trabajo.
     */
    public void setDirectorioTrabajo(File directorioTrabajo) {
        this.directorioTrabajo = directorioTrabajo;
    }
}
