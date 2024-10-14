package es.florida.gestorFicheros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * La clase Controlador gestiona los eventos de la interfaz gráfica y conecta la Vista con el Modelo.
 */
public class Controlador {

    private Vista vista;
    private Modelo modelo;

    /**
     * Constructor de la clase Controlador.
     * 
     * @param vista  La instancia de la vista que contiene la interfaz gráfica.
     * @param modelo La instancia del modelo que gestiona la lógica de la aplicación.
     */
    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        initEventHandlers();
    }

    /**
     * Método que inicializa los manejadores de eventos para los botones en la vista.
     */
    public void initEventHandlers() {

        /**
         * Manejador de eventos para el botón de seleccionar directorio.
         * Abre un JFileChooser que permite al usuario seleccionar un directorio de trabajo.
         */
        vista.getBtnSeleccionarDirectorio().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(modelo.getDirectorioTrabajo());

                // Configura el JFileChooser para que sólo permita seleccionar directorios.
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int resultado = fileChooser.showDialog(vista.getContentPane(), "Seleccionar carpeta");

                // Si el usuario selecciona un directorio, actualiza la vista y el modelo.
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    vista.getTxtDirectorio().setText(fileChooser.getSelectedFile().getAbsolutePath());
                    modelo.setDirectorioTrabajo(new File(fileChooser.getSelectedFile().getAbsolutePath()));
                }
            }
        });

        /**
         * Manejador de eventos para el botón de mostrar ficheros.
         * Muestra en el área de texto la estructura de archivos del directorio seleccionado.
         */
        vista.getBtnMostrarFicheros().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modelo.setDirectorioTrabajo(new File(vista.getTxtDirectorio().getText()));
                if (modelo.getDirectorioTrabajo().exists()) {
                    String contenido = Modelo.listarArchivos(modelo.getDirectorioTrabajo());
                    vista.getTextArea().setText(contenido);
                } else {
                    JOptionPane.showMessageDialog(vista.getContentPane(), "Este directorio no existe", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        /**
         * Manejador de eventos para el botón de buscar coincidencias.
         * Busca una cadena en los archivos del directorio y muestra los resultados en el área de texto.
         */
        vista.getBtnBuscarCoincidencias().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modelo.setDirectorioTrabajo(new File(vista.getTxtDirectorio().getText()));
                if (modelo.getDirectorioTrabajo().exists()) {
                    String contenido = Modelo.contarCoincidencias(
                        modelo.getDirectorioTrabajo(),
                        vista.getTxtBuscarCoincidencias().getText(),
                        vista.getChckbxCoincidirMayusMinus().isSelected(),
                        vista.getChckbxCoincidirAcentos().isSelected()
                    );
                    vista.getTextArea().setText(contenido);
                } else {
                    JOptionPane.showMessageDialog(vista.getContentPane(), "Este directorio no existe", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        /**
         * Manejador de eventos para el botón de reemplazar texto.
         * Reemplaza una cadena en los archivos del directorio y muestra los resultados en el área de texto.
         */
        vista.getBtnReemplazar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modelo.setDirectorioTrabajo(new File(vista.getTxtDirectorio().getText()));
                if (modelo.getDirectorioTrabajo().exists()) {
                    String contenido = Modelo.reemplazarCadenasEnArchivos(
                        modelo.getDirectorioTrabajo(),
                        vista.getTxtBuscarCoincidencias().getText(),
                        vista.getTxtTextoReemplazar().getText()
                    );
                    vista.getTextArea().setText(contenido);
                } else {
                    JOptionPane.showMessageDialog(vista.getContentPane(), "Este directorio no existe", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
