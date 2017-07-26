/*
 * @(#) PdfHandler.java Algem 2.15.0 23/07/2017
 *
 * Copyright (c) 1999-2017 Musiques Tangentes. All Rights Reserved.
 *
 * This file is part of Algem.
 * Algem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Algem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 */
package net.algem.edition;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPrintPage;
import com.sun.pdfview.PagePanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.algem.config.PageTemplate;
import net.algem.config.PageTemplateIO;
import net.algem.util.BundleUtil;
import net.algem.util.GemCommand;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.ui.GemButton;
import net.algem.util.ui.GemPanel;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.0
 * @since 2.15.0 23/07/2017
 */
public class PdfHandler {

  private PageTemplateIO templateIO;

  public PdfHandler(PageTemplateIO templateIO) {
    this.templateIO = templateIO;
  }

  public void createPdf(String fileName, ByteArrayOutputStream out, short templateType) throws IOException, DocumentException {
    try {
      File tmpFile = File.createTempFile(fileName, ".pdf");
      final String target = tmpFile.getPath();
      PageTemplate pt = getTemplate(templateType);
      PdfReader reader = new com.lowagie.text.pdf.PdfReader(out.toByteArray());
      if (pt != null) {
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(target));
        PdfReader model = new com.lowagie.text.pdf.PdfReader(pt.getContent());
        PdfImportedPage importedPage = stamper.getImportedPage(model, 1);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
          PdfContentByte canvas = stamper.getUnderContent(i);
          canvas.addTemplate(importedPage, 0, 0);
        }

        stamper.getWriter().freeReader(model);
        model.close();
        stamper.close();
      } else {
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(target));
        stamper.close();
      }

      preview(target, null);
    } catch (SQLException ex) {
      Logger.getLogger(PdfHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public List<PageTemplate> getTemplates() throws SQLException {
    return templateIO.findAll();
  }

  public void saveTemplate(PageTemplate tp, Map<Short, PageTemplate> templates) throws SQLException {
    if (templates.get(tp.getType()) == null) {
      templateIO.insert(tp.getType(), tp.getContent());
      templates.put(tp.getType(), tp);
    } else {
      templateIO.update(tp.getType(), tp.getContent());
    }

  }

  public void resetTemplate(short type, Map<Short, PageTemplate> templates) throws SQLException {
    templateIO.delete(type);
    if (templates.get(type) != null) {
      templates.remove(type);
    }
  }

  private PageTemplate getTemplate(short type) throws SQLException {
    PageTemplate pt = templateIO.find(type);
    if (pt == null) {
      pt = templateIO.find(PageTemplate.DEFAULT_MODEL);
    }
    return pt;
  }

  public BufferedImage getThumbnail(PDFFile pdffile) throws IOException {
    PDFPage page = pdffile.getPage(1);
    //  create new image
    Rectangle rect = new Rectangle(0, 0, (int) (page.getBBox().getWidth()), (int) (page.getBBox().getHeight()));

    Image img = page.getImage(
      rect.width, rect.height, //width & height
      rect, // clip rect
      null, // null for the ImageObserver
      true, // fill background with white
      true // block until drawing is done
    );

    BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
    Graphics g = bufferedImage.createGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();

    return rescale(bufferedImage, rect.width / 4, rect.height / 4);
//    return null;
  }

  private void preview(String path, final Frame parent) throws IOException {
    final JDialog dlg = new JDialog(parent, BundleUtil.getLabel("Preview.pdf.label"));
    final File file = new File(path);
    final PDFFile pdfFile = new PDFFile(ByteBuffer.wrap(Files.readAllBytes(file.toPath())));

    PDFPage page = pdfFile.getPage(1);
    assert (page != null);
    com.sun.pdfview.PagePanel pp = new PagePanel();
    Dimension sz = new Dimension(425, 600);
    pp.setSize(sz);
    pp.setPreferredSize(sz);

    dlg.setLayout(new BorderLayout());
    GemPanel mainPanel = new GemPanel();
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    mainPanel.add(pp);
    dlg.add(mainPanel, BorderLayout.CENTER);

    GemPanel btPanel = new GemPanel(new GridLayout(1, 2));

    GemButton btPrint = new GemButton(GemCommand.PRINT_CMD);
    btPrint.setToolTipText(BundleUtil.getLabel("Action.print.pdf.tip"));
    btPrint.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        print(file);
      }
    });
    btPanel.add(btPrint);
    GemButton btSave = new GemButton(GemCommand.SAVE_CMD);
    btSave.setToolTipText(BundleUtil.getLabel("Action.save.pdf.tip"));
    btSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        copy(file, parent, true);
      }
    });
    btPanel.add(btSave);
    GemButton btClose = new GemButton(GemCommand.CLOSE_CMD);
    btClose.setToolTipText(BundleUtil.getLabel("Action.close.preview.tip"));
    btClose.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dlg.dispose();
      }
    });
    btPanel.add(btClose);

    dlg.add(btPanel, BorderLayout.SOUTH);
    pp.showPage(page);
    dlg.setSize(sz.width + 10, sz.height + 10);
    dlg.pack();
    dlg.setLocationRelativeTo(parent);
    dlg.setVisible(true);
  }

  private BufferedImage rescale(BufferedImage img, int nw, int nh) {
    Image imgSmall = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(nw, nh, img.getType());

    Graphics2D g = dimg.createGraphics();
    g.drawImage(imgSmall, 0, 0, null);
    g.dispose();
    return dimg;
  }

  private void print(File file) {
    try {
      final PDFFile pdfFile = new PDFFile(ByteBuffer.wrap(Files.readAllBytes(file.toPath())));
      PDFPrintPage pages = new PDFPrintPage(pdfFile);

      PrinterJob job = PrinterJob.getPrinterJob();
      if (job.printDialog()) {
        //Printing a PDF is also possible by sending the bytes directly to the printer, but
        //the printer would have to support it.
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
        /*int margin = ImageUtil.mmToPoints(10);
        paper.setImageableArea(margin, 0, (pdfFile.getPage(1).getWidth() * 2) - (2 * margin), pdfFile.getPage(1).getHeight()  - (2 * margin));*/
        Paper paper = new Paper();
        //This is to fix an error in PDF-Renderer
        //View http://juixe.com/techknow/index.php/2008/01/17/print-a-pdf-document-in-java/ for details
        paper.setImageableArea(0, 0, pdfFile.getPage(1).getWidth() * 2, pdfFile.getPage(1).getHeight());
        pf.setPaper(paper);
        Book book = new Book();
        book.append(pages, pf, pdfFile.getNumPages());
        job.setJobName(file.getName());
        job.setPageable(book);
        job.print();
      }
    } catch (PrinterException | IOException e) {
      GemLogger.logException(e);
    }

  }

  private void copy(File file, Frame parent, boolean deleteSource) {
    String name = file.getName().substring(0, file.getName().indexOf('_')) + ".pdf";
    FileNameExtensionFilter filter = new FileNameExtensionFilter(MessageUtil.getMessage("filechooser.pdf.filter.label"), "pdf");
    JFileChooser fileChooser = ExportDlg.getFileChooser(name, filter);
    int ret = fileChooser.showDialog(parent, BundleUtil.getLabel("FileChooser.selection"));
    if (ret == JFileChooser.APPROVE_OPTION) {
      File to = fileChooser.getSelectedFile();
      if (to != null) {
        try {
          Files.copy(file.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
          if (deleteSource) {
            Files.delete(file.toPath());
          }
        } catch (IOException ex) {
          GemLogger.logException(ex);
        }
      }
    }
  }

  private ByteBuffer getBufferFromFile(File file) throws IOException {
    RandomAccessFile raf = new RandomAccessFile(file, "r");
    //ReadableByteChannel ch = Channels.newChannel(new FileInputStream(file));
    FileChannel channel = raf.getChannel();
    return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
  }

  private byte[] getByteArrayFromFile(File file) throws IOException {
    return Files.readAllBytes(file.toPath());
  }

}
