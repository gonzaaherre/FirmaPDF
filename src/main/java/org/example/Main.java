package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class Main {
    //carpeta donde estan los pdf solos
    private static String rutaCarpeta = "C:\\prueba pdf\\arreglo";

    public static void main(String[] args) throws Exception {


        List<PDDocument> listaPDF = obtenerListaPDF(rutaCarpeta);//llamamos a la funcion
        AgregarFirma(listaPDF);


    }

    public static ArrayList<String> obtenerNombres() {//guardamos los nombres de los pdf siguiendo el metodo de obtenerLista
        ArrayList<String> nombres = new ArrayList<>();
        File carpeta = new File(rutaCarpeta);

        // verifica el directorio
        if (carpeta.isDirectory()) {
            // Obtener la lista de archivos en la carpeta
            File[] archivos = carpeta.listFiles();
            for (File archivo : archivos) {
                nombres.add(archivo.getName());
            }
            for (String nom : nombres) {
                System.out.println(nom);
            }
        }
        return nombres;
    }

    public static List<PDDocument> obtenerListaPDF(String rutaCarpeta) {
        List<PDDocument> listaPDF = new ArrayList<>();

        File carpeta = new File(rutaCarpeta);

        // verifica el directorio
        if (carpeta.isDirectory()) {
            // Obtener la lista de archivos en la carpeta
            File[] archivos = carpeta.listFiles();

            // Iterar sobre los archivos y cargar los PDF como documentos PDDocument
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".pdf")) {//solamente agarra .pdf
                    try {
                        //Pasamos de File a PDDocument
                        PDDocument document = PDDocument.load(archivo);
                        listaPDF.add(document);
                    } catch (IOException e) {
                        // Manejo de errores al cargar el documento PDF
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("La ruta no es un directorio válido.");
        }

        return listaPDF;
    }

    public static void AgregarFirma(List<PDDocument> listaPDF) {
        int i = 0;
        int numPaginas=0;
        //Arraylist de string para guardar los nombres de los pdf, ya que no me funciono el metodo getDocumentInformation
        ArrayList<String> nombres= obtenerNombres();
        String n="";
        for (PDDocument pdf : listaPDF) {
            try {
                //PDDocumentInformation documentInformation = pdf.getDocumentInformation();
                //String titulo = documentInformation.getTitle();
                //numero de pagina para trabajar con pdf de mas de 1 pag
                numPaginas=pdf.getNumberOfPages();
                System.out.println(numPaginas);
                n=nombres.get(i);
                //pdf=agregarImagen(pdf,numPaginas);
                while (numPaginas!=0){
                    //cargamos la imagen
                    PDImageXObject image = PDImageXObject.createFromFile("C:\\prueba pdf\\firmas\\firma.png", pdf);//imagen
                    PDPage page = pdf.getPage(numPaginas-1);//obtiene la pagina
                    PDPageContentStream stream = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
                    stream.drawImage(image, 265, 38, 100, 61);
                    stream.close();
                    numPaginas--;
                }
                pdf.save("C:\\prueba pdf\\PDFFIRMADOS\\"+n+"_signed.pdf");
                //pdf.save("C:\\prueba pdf\\PDFFIRMADOS\\"+titulo+".pdf");
                System.out.println("Documento firmado creado");

            } catch (IOException e) {
                // Manejar la excepción de entrada/salida (IOException)
                e.printStackTrace();
            } finally {
                try {
                    pdf.close();
                } catch (IOException e) {
                    // Manejar la excepción de entrada/salida (IOException) al cerrar el documento PDF
                    e.printStackTrace();
                }
            }
            i++;
        }
    }
}
