package ETIQUETAS;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ETIQUETAS extends javax.swing.JFrame {

    FondoPanel fondo = new FondoPanel();
    
      
            //variables para el manejo de archivos
            static File input_Fileprn;
            static File input_Filebdd;
            static File default_ChooserStartTemplateDirectory;
            static File default_ChooserStartInputDirectory;
            FileNameExtensionFilter input_Filter = new FileNameExtensionFilter("Listado de etiquetas (CSV, TXT)", new String[] { "csv", "txt" });
            FileNameExtensionFilter template_Filter = new FileNameExtensionFilter("Plantilla DPL (DPL, TXT)", new String[] { "dpl", "txt" });
            
            
            
            
    
    
    public ETIQUETAS() 
    {
        
        this.setContentPane(fondo);
        
        initComponents();
        
        
        ActualizarImpresoras ();
        
        InhabilitarLblBdd();
        InhabilitarLblPrn();
        
        Inhabilitatxtcantidad();
        Inhabilitabtnproductos();
        
        
    }

  
    //manejo de campo lbl para visualizar nombre de los archivos 
         /* ---------********* metodo deshabilitar las cajas de texto *****------ */
   void InhabilitarLblBdd()
    {
        //deshabiltado de campos
        lbl_etiquetas_nombre_bdd.setVisible(false);
    }
    
    /* ---------********* metodo habilitar las cajas de texto *****------ */
    void HabilitarLblBdd()
    {  
         //deshabiltado de campos 
        lbl_etiquetas_nombre_bdd.setVisible(true);
    }
    
            /* ---------********* metodo deshabilitar las cajas de texto *****------ */
   void InhabilitarLblPrn()
    {
        //deshabiltado de campos
        lbl_etiquetas_nombre_prn.setVisible(false);
    }
    
    /* ---------********* metodo habilitar las cajas de texto *****------ */
    void HabilitarLblPrn()
    {  
         //deshabiltado de campos 
        lbl_etiquetas_nombre_prn.setVisible(true);
    }
    
    
    /*       HABILITA EL CAMPO DE TEXTO PARA INGRESAR LA CANTIDAD       */
    void habilitatxtcantidad()
    {
          //deshabiltado de campos 
        txt_etiquetas_cantidad_prodcutos.setEnabled(true);
        
    }
    
     void Inhabilitatxtcantidad()
    {
          //deshabiltado de campos 
        txt_etiquetas_cantidad_prodcutos.setEnabled(false);
        
    }
     
     
       void habilitabtnproductos()
    {
          //deshabiltado de campos 
        btn_etiquetas_productos.setEnabled(true);
        
    }
    
     void Inhabilitabtnproductos()
    {
          //deshabiltado de campos 
        btn_etiquetas_productos.setEnabled(false);
        
    }
    
    
    /*FUNCIONES PARA USO DE APLICACION*/
    
    //SELECCION Y INGRESO DE ARCHIVO DE TEXTO
    
    private File SeleccionarBuscarArchivo (FileNameExtensionFilter tipo_Archivo, File ubicacion_Archivo)
    {
            File Seleccionado = null;
            
            JFileChooser Busqueda_Chooser = new JFileChooser(); //muestra un cuadro de dialogo en el cual se selecciona el archivo
            Busqueda_Chooser.setFileFilter(tipo_Archivo);
            Busqueda_Chooser.setCurrentDirectory(ubicacion_Archivo);
            int returnVal = Busqueda_Chooser.showOpenDialog(this);
            if (returnVal == 0) {
              Seleccionado  = Busqueda_Chooser.getSelectedFile();
            }
            
            return Seleccionado ;
      
    }
    
    //funcion para actualizar impresoras de etiquetas disponibles
    private void ActualizarImpresoras ()
    {
                //CARGA LA IMPRESORA EN EL COMBO BOX
                this.cbo_etiquetas_listas_impresoras.setModel(new DefaultComboBoxModel(PrintServiceLookup.lookupPrintServices(null, null)));
                this.cbo_etiquetas_listas_impresoras.getModel().setSelectedItem(PrintServiceLookup.lookupDefaultPrintService());    
    }
    
     /*  funcion con servicio de impresion  */ 
    private void  ImpresionRawData (javax.print.PrintService printService, String rawData)
  {

    byte[] data = rawData.getBytes(StandardCharsets.US_ASCII);
    Doc doc = new SimpleDoc(data, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
    try
    {   
      printService.createPrintJob().print(doc, null);
    }
    catch (PrintException e)
    {
      JOptionPane.showMessageDialog(this, "Se produjo un error al imprimir\n\n" + e.getMessage());
      System.exit(0);
    }
  }
    
    
    /* FUNCION TIPO ARRAY LIST PARA RECORRER EL ARCHIVO */
 private List<String[]> csvFileToList(File file)
  {
    List<String[]> ret = new ArrayList();
    try
    {
      BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
      String tmpString ;
      int cant = 1;
      while ((tmpString = bis.readLine()) != null)
      { 
        String tmpString2 = tmpString.trim();
        if (tmpString2.length() > 0) 
        {
          ret.add(tmpString2.split("\\;"));
        }
        System.out.println(tmpString);
      }
    }
     catch (FileNotFoundException e)
    {
      JOptionPane.showMessageDialog(this, "No se encontro el archivo:\n" + file.getPath() + "\n\n" + e.getMessage(), "Error", 0);
      System.exit(0);
    }
    catch (IOException e)
    {
      JOptionPane.showMessageDialog(this, "Se produjo un error al procesar el archivo:\n" + file.getPath() + "\n\n" + e.getMessage(), "Error", 0);
      System.exit(0);
    }
    return ret;
  }
 
    /*funcion para imprimir */
private   void Imprimir ()
    {
    String codigo_prn_original="";
    
    String codigo_prn_temporal="";
          
          String ubicacion = txt_etiquetas_cantidad_prodcutos.getText();
          int ubicacion_int= Integer.parseInt(ubicacion);
                if ((input_Filebdd != null))
                {    
                    List<String[]> input = csvFileToList(input_Filebdd);
                        if (input_Fileprn == null)
                        {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo");
                        }
                        else
                        {
                            try
                            {
                                FileInputStream fis = null;
                                BufferedInputStream bis = null;
                                DataInputStream dis = null;
                                fis = new FileInputStream(input_Fileprn);
                                bis = new BufferedInputStream(fis);
                                dis = new DataInputStream(bis);
                                while (dis.available() != 0) //verificacion la fila leida del texto verifica que  alla contenido si no hay caracter es 0
                                {
                                    codigo_prn_original = codigo_prn_original + dis.readLine() + "\r\n";//Lectura del archivo linea linea y guardado en Lineaprn
                                }
                                fis.close();
                                bis.close();
                                dis.close();
                                // System.out.println(lineaprn);   //verificacion de que imprime en consola el texto del archivo prn
                            }//cierre de TRY
                            catch (IOException e)
                            {
                                System.out.println("no se ha encontrado el archivo");
                            }
                             // javax.print.PrintService servicio_impresora_Seleccionada = (javax.print.PrintService) this.cbo_etiquetas_listas_impresoras.getModel().getSelectedItem();
                             // ImpresionRawData(servicio_impresora_Seleccionada,codigo_Impresion);
                        if (input.size() > 0)
                        {
                            for (String[] contenido_bdd : input)
                            {   
                                
                                codigo_prn_temporal = codigo_prn_temporal + codigo_prn_original;
                                
                                if (contenido_bdd.length > 0) 
                                {
                                                              
                                try {
                                    
                                           int cant_etiquetas = Integer.parseInt(contenido_bdd[ubicacion_int]);
                                       
                                            if (cant_etiquetas >0)
                                            {                           
                                            for (int i = 0; i < contenido_bdd.length; i++) 
                                                {        
            
                                                        if (i == ubicacion_int) 
                                                        { 
                                                            if (cant_etiquetas < 9)
                                                            {
                                                                     codigo_prn_temporal = codigo_prn_temporal.replace("~" + i + "~", "000" + cant_etiquetas);
                                                            }
                                                            else if (cant_etiquetas > 9 && cant_etiquetas < 99)
                                                            {
                                                                     codigo_prn_temporal = codigo_prn_temporal.replace("~" + i + "~", "00" + cant_etiquetas);
                                                            }
                                                            else if (cant_etiquetas > 9 && cant_etiquetas > 99 && cant_etiquetas > 999)
                                                            {
                                                                    codigo_prn_temporal = codigo_prn_temporal.replace("~" + i + "~", "0" + cant_etiquetas);
                                                            } 
                                                        }  
                                                        else
                                                        {
                                                            codigo_prn_temporal = codigo_prn_temporal.replace("~" + i + "~",contenido_bdd[i]);
                                                            
                                                            
                                                        }            
                                                }                       
                                            }//cierre de if cantidad etiquetas
                                        } catch (Exception e) {

                                            //mensaje  
                                        System.out.print("error al reemplazar");
                                        JOptionPane.showMessageDialog(this, "error al reemplazar");

                                        }
                                    }//cierre de if longitud
                                  //codigo_Impresion="";
                                  
                                }//cierre de for  
                            
                                 javax.print.PrintService servicio_impresora_Seleccionada = (javax.print.PrintService) this.cbo_etiquetas_listas_impresoras.getModel().getSelectedItem();
                                 ImpresionRawData(servicio_impresora_Seleccionada,codigo_prn_temporal);
      
                            }//cierre de if tamaño de array list 
                            }//cierre de else recorrido de archivo PRN y inicio lectura arraylist para reemplazo 
                        }//cierre de if inputbdd 
    }//cierrer de funcion imprimir
    

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbl_etiquetas_productos = new javax.swing.JLabel();
        btn_etiquetas_productos = new javax.swing.JButton();
        lbl_etiquetas_prn = new javax.swing.JLabel();
        btn_etiquetas_prn = new javax.swing.JButton();
        btn_etiquetas_actualizar_impresoras = new javax.swing.JButton();
        lbl_etiquetas_nombre_prn = new javax.swing.JLabel();
        lbl_etiquetas_nombre_bdd = new javax.swing.JLabel();
        lbl_etiquetas_impresoras = new javax.swing.JLabel();
        cbo_etiquetas_listas_impresoras = new javax.swing.JComboBox<>();
        btn_etiquetas_imprimir = new javax.swing.JButton();
        btn_etiquetas_salir = new javax.swing.JButton();
        lbl_etiquetas_cantidad = new javax.swing.JLabel();
        txt_etiquetas_cantidad_prodcutos = new javax.swing.JTextField();
        lbl_etiquetas_version = new javax.swing.JLabel();
        btnconfiguracion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS A ETIQUETAR", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Britannic Bold", 0, 24))); // NOI18N
        jPanel1.setAutoscrolls(true);

        lbl_etiquetas_productos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_etiquetas_productos.setText("Subir Archivo Productos");

        btn_etiquetas_productos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_etiquetas_productos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenesaplicacion/pagos.png"))); // NOI18N
        btn_etiquetas_productos.setText("Subir");
        btn_etiquetas_productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_etiquetas_productosActionPerformed(evt);
            }
        });

        lbl_etiquetas_prn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_etiquetas_prn.setText("Subir Archivo Diseño Prn");

        btn_etiquetas_prn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_etiquetas_prn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenesaplicacion/Archivo.png"))); // NOI18N
        btn_etiquetas_prn.setText("Subir");
        btn_etiquetas_prn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_etiquetas_prnActionPerformed(evt);
            }
        });

        btn_etiquetas_actualizar_impresoras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_etiquetas_actualizar_impresoras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenesaplicacion/actualizar3.png"))); // NOI18N
        btn_etiquetas_actualizar_impresoras.setText("Actualizar Lista de Impresoras");
        btn_etiquetas_actualizar_impresoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_etiquetas_actualizar_impresorasActionPerformed(evt);
            }
        });

        lbl_etiquetas_nombre_prn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl_etiquetas_nombre_prn.setText("Nombre PRN");

        lbl_etiquetas_nombre_bdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl_etiquetas_nombre_bdd.setText("Nombre TXT ");

        lbl_etiquetas_impresoras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_etiquetas_impresoras.setText("Seleccionar Impresora");

        cbo_etiquetas_listas_impresoras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbo_etiquetas_listas_impresoras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_etiquetas_listas_impresoras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_etiquetas_listas_impresorasActionPerformed(evt);
            }
        });

        btn_etiquetas_imprimir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_etiquetas_imprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenesaplicacion/imprimir3.png"))); // NOI18N
        btn_etiquetas_imprimir.setText("Imprimir ");
        btn_etiquetas_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_etiquetas_imprimirActionPerformed(evt);
            }
        });

        btn_etiquetas_salir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_etiquetas_salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenesaplicacion/salir3.gif"))); // NOI18N
        btn_etiquetas_salir.setText("Salir");
        btn_etiquetas_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_etiquetas_salirActionPerformed(evt);
            }
        });

        lbl_etiquetas_cantidad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_etiquetas_cantidad.setText("Posición Cantidad");

        txt_etiquetas_cantidad_prodcutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_etiquetas_cantidad_prodcutosActionPerformed(evt);
            }
        });
        txt_etiquetas_cantidad_prodcutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_etiquetas_cantidad_prodcutosKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_etiquetas_cantidad_prodcutosKeyTyped(evt);
            }
        });

        lbl_etiquetas_version.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_etiquetas_version.setText("Version 1.1");

        btnconfiguracion.setText("Configuracion");
        btnconfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconfiguracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnconfiguracion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_etiquetas_version, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_etiquetas_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lbl_etiquetas_impresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(170, 170, 170)
                                            .addComponent(cbo_etiquetas_listas_impresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btn_etiquetas_imprimir)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lbl_etiquetas_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbl_etiquetas_prn, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbl_etiquetas_cantidad))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(btn_etiquetas_productos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btn_etiquetas_prn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txt_etiquetas_cantidad_prodcutos, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(58, 58, 58)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbl_etiquetas_nombre_prn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lbl_etiquetas_nombre_bdd, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                                .addComponent(btn_etiquetas_actualizar_impresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnconfiguracion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_etiquetas_listas_impresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_etiquetas_actualizar_impresoras)
                    .addComponent(lbl_etiquetas_impresoras, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_etiquetas_prn)
                            .addComponent(lbl_etiquetas_nombre_prn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_etiquetas_cantidad_prodcutos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_etiquetas_productos)
                            .addComponent(lbl_etiquetas_nombre_bdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_etiquetas_prn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_etiquetas_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_etiquetas_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_etiquetas_version, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_etiquetas_salir)
                        .addComponent(btn_etiquetas_imprimir)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(76, 76, 76))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_etiquetas_productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_etiquetas_productosActionPerformed

        input_Filebdd = SeleccionarBuscarArchivo(this.input_Filter, default_ChooserStartInputDirectory);
        
             if (input_Filebdd  == null)
            {
                JOptionPane.showMessageDialog(this, "Debe seleccionar el archivo");
            }
                    else //(input_Fileprn != null)
                    {
                        this.lbl_etiquetas_nombre_bdd.setText(input_Filebdd.getName());
                         HabilitarLblBdd();
                    } 
        
    }//GEN-LAST:event_btn_etiquetas_productosActionPerformed

    private void btn_etiquetas_prnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_etiquetas_prnActionPerformed
        // TODO add your handling code here:
        input_Fileprn = SeleccionarBuscarArchivo(this.template_Filter, default_ChooserStartInputDirectory);
        
        if (input_Fileprn == null)
            {
                JOptionPane.showMessageDialog(this, "Debe seleccionar el archivo");
            }
                    else //(input_Fileprn != null)
                    {
                        this.lbl_etiquetas_nombre_prn.setText(input_Fileprn.getName());
                        HabilitarLblPrn();      
                        habilitatxtcantidad();
                    }      
    }//GEN-LAST:event_btn_etiquetas_prnActionPerformed

    private void btn_etiquetas_actualizar_impresorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_etiquetas_actualizar_impresorasActionPerformed
        // TODO add your handling code here:
        ActualizarImpresoras ();
    }//GEN-LAST:event_btn_etiquetas_actualizar_impresorasActionPerformed

    private void btn_etiquetas_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_etiquetas_imprimirActionPerformed

        
            if((input_Fileprn == null) || (input_Filebdd == null))
            {
                JOptionPane.showMessageDialog(this, "Debe seleccionar los archivos para realizar la impresion de etiquetado");
            }
        
            else if ((input_Fileprn != null) || (input_Filebdd != null))
            {
                Imprimir();
            }
            
            else 
            {
                //JOptionPane.showMessageDialog(this, "Debe seleccionar los archivos para realizar la impresion de etiquetado"); 
                Imprimir();
            } 
           
            
    }//GEN-LAST:event_btn_etiquetas_imprimirActionPerformed

    private void btn_etiquetas_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_etiquetas_salirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_etiquetas_salirActionPerformed

    private void txt_etiquetas_cantidad_prodcutosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_etiquetas_cantidad_prodcutosKeyTyped
        // solo numeros en el campo de texto
        char numeros ;
        int num_ingresado=0;
        
        numeros =  evt.getKeyChar();
        if (numeros < '0' || numeros >'9')  evt.consume(); 
        
        if (  numeros >'0' && numeros <'9' )
            
        {
            //JOptionPane.showMessageDialog(this, "Solo admite valores entre 0-9 ");
            txt_etiquetas_cantidad_prodcutos.setText(null);
            
        }
            
    }//GEN-LAST:event_txt_etiquetas_cantidad_prodcutosKeyTyped

    private void txt_etiquetas_cantidad_prodcutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_etiquetas_cantidad_prodcutosKeyReleased
        
          if(txt_etiquetas_cantidad_prodcutos.getText().trim().isEmpty())
        {
              JOptionPane.showMessageDialog(this, "Llene todos los campos");
              Inhabilitabtnproductos();
        }
        else
        {
            habilitabtnproductos();
        } 
        
    }//GEN-LAST:event_txt_etiquetas_cantidad_prodcutosKeyReleased

    private void txt_etiquetas_cantidad_prodcutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_etiquetas_cantidad_prodcutosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_etiquetas_cantidad_prodcutosActionPerformed

    private void btnconfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconfiguracionActionPerformed
        // TODO add your handling code here:
     
        conf verconfiguracion = new conf();
        verconfiguracion.setVisible(true);
        
            this.setVisible(false);
        
    }//GEN-LAST:event_btnconfiguracionActionPerformed

    private void cbo_etiquetas_listas_impresorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_etiquetas_listas_impresorasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_etiquetas_listas_impresorasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ETIQUETAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ETIQUETAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ETIQUETAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ETIQUETAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ETIQUETAS().setVisible(true);
            }
        });
    }
    
    class FondoPanel extends JPanel
            {
               
                
                private Image imagen;
                
                
                @Override
                public void paint (Graphics g)
                {
                    imagen = new  ImageIcon(getClass().getResource("/Imagenesaplicacion/logomejorado.png")).getImage();
                    
                    g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                    
                    setOpaque(false);
                    super.paint(g);
                    
                }
            }    
                
               


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_etiquetas_actualizar_impresoras;
    private javax.swing.JButton btn_etiquetas_imprimir;
    private javax.swing.JButton btn_etiquetas_prn;
    private javax.swing.JButton btn_etiquetas_productos;
    private javax.swing.JButton btn_etiquetas_salir;
    private javax.swing.JButton btnconfiguracion;
    private javax.swing.JComboBox<String> cbo_etiquetas_listas_impresoras;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_etiquetas_cantidad;
    private javax.swing.JLabel lbl_etiquetas_impresoras;
    private javax.swing.JLabel lbl_etiquetas_nombre_bdd;
    private javax.swing.JLabel lbl_etiquetas_nombre_prn;
    private javax.swing.JLabel lbl_etiquetas_prn;
    private javax.swing.JLabel lbl_etiquetas_productos;
    private javax.swing.JLabel lbl_etiquetas_version;
    private javax.swing.JTextField txt_etiquetas_cantidad_prodcutos;
    // End of variables declaration//GEN-END:variables
}


