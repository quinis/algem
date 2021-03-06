/*
 * @(#)StatsExportDlg.java	2.11.0 30/09/16
 *
 * Copyright (c) 1999-2016 Musiques Tangentes. All Rights Reserved.
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
 *
 */
package net.algem.edition;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.algem.accounting.AccountPrefIO;
import net.algem.config.ConfigUtil;
import net.algem.config.Preference;
import net.algem.planning.DateFr;
import net.algem.planning.DateRangePanel;
import net.algem.util.*;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.GemButton;
import net.algem.util.ui.GemField;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.MessagePopup;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.10.0
 * @since 2.6.a 11/10/2012
 */
public class StatsExportDlg
        extends JDialog
        implements ActionListener, PropertyChangeListener
{

  protected GemDesktop desktop;
  protected DataCache dataCache;
  protected GemField filePathField;
  protected GemButton btValidation;
  protected GemButton btCancel;
  protected JPanel buttons;
  protected JButton chooser;
  protected File file;
  protected DateRangePanel datePanel;
  private JProgressBar progressBar;
  private Statistics st;

  public StatsExportDlg(GemDesktop desktop) {
    super(desktop.getFrame(), BundleUtil.getLabel("Statistics.label"));
    this.desktop = desktop;
    this.dataCache = desktop.getDataCache();
    btValidation = new GemButton(GemCommand.VALIDATION_CMD);
    btValidation.addActionListener(this);
    btCancel = new GemButton(GemCommand.CANCEL_CMD);
    btCancel.addActionListener(this);

    buttons = new GemPanel();
    buttons.setLayout(new GridLayout(1, 1));
    buttons.add(btValidation);
    buttons.add(btCancel);

    filePathField = new GemField(ConfigUtil.getExportPath() + FileUtil.FILE_SEPARATOR + "stats.html", 30);
    GemPanel filePanel = new GemPanel();
    filePanel.add(new Label(BundleUtil.getLabel("Menu.file.label")));
    filePanel.add(filePathField);
    chooser = new JButton(GemCommand.BROWSE_CMD);
    chooser.addActionListener(this);
    filePanel.add(chooser);
    int year = dataCache.getStartOfYear().getYear();
    DateFr start = new DateFr("01-09-" + year);
    DateFr end = new DateFr("31-08-" + (year + 1));
    datePanel = new DateRangePanel(start, end);

    setLayout(new BorderLayout());
    add(filePanel, BorderLayout.NORTH);

    progressBar = new JProgressBar();
    GemPanel d = new GemPanel();
    d.add(new JLabel(BundleUtil.getLabel("Date.From.label")));
    d.add(datePanel);
    GemPanel pCenter = new GemPanel(new BorderLayout());
    pCenter.add(d, BorderLayout.NORTH);
    pCenter.add(progressBar, BorderLayout.SOUTH);
    add(pCenter, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);
    pack();
    setLocationRelativeTo(desktop.getFrame());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btCancel) {
      if (st == null || st.isDone()) {
        close();
      }
    } else if (e.getSource() == btValidation) {
      file = new File(filePathField.getText());
      if (!FileUtil.confirmOverWrite(this, file)) {
        return;
      }
      validation();
    } else if (e.getSource() == chooser) {
      JFileChooser fileChooser = ExportDlg.getFileChooser(filePathField.getText());
      int ret = fileChooser.showDialog(this, BundleUtil.getLabel("FileChooser.selection"));
      if (ret == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        if (FileUtil.confirmOverWrite(this, file)) {
          filePathField.setText(file.getPath());
        }
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("done")) {
      btValidation.setEnabled(true);
      btCancel.setEnabled(true);
      setCursor(Cursor.getDefaultCursor());
      close();
    }
  }

  private void validation() {
    try {
      st = StatisticsFactory.getInstance();
      if (st == null) {
        MessagePopup.warning(desktop.getFrame(), MessageUtil.getMessage("statistics.default.warning"));
        st = new StatisticsDefault();
      }
      st.setStats();
      List<StatElement> filtered = new ArrayList<>();
      StatsFilterDlg dlg = new StatsFilterDlg(desktop.getFrame(), true);
      dlg.createUI(st.getStats());
      if (!dlg.isValidation()) {
        st = null;
        return;
      } else {
        filtered = dlg.getSelected();
      }
      if (filtered.size() > 0) {
        st.setStats(filtered);
      }
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      btValidation.setEnabled(false);
      btCancel.setEnabled(false);

      st.init(dataCache);
      progressBar.setStringPainted(true);
      progressBar.setString(MessageUtil.getMessage("statistics.active.operation"));
      Preference [] prefs = {
        AccountPrefIO.find(AccountPrefIO.MEMBERSHIP, DataCache.getDataConnection()),
        AccountPrefIO.find(AccountPrefIO.PRO_MEMBERSHIP, DataCache.getDataConnection())
      };
      st.setConfig(
              filePathField.getText(),
              prefs,
              datePanel.getStartFr(),
              datePanel.getEndFr());
      st.addPropertyChangeListener(this);
      st.setMonitor(progressBar);
      progressBar.setIndeterminate(true);
      st.execute();
    } catch (IOException ex) {
      GemLogger.logException(ex);
      MessagePopup.warning(desktop.getFrame(), MessageUtil.getMessage("file.path.exception", filePathField.getText()));
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }
  }

  private void close() {
    if (st != null) {
      st.removePropertyChangeListener(this);
    }
    setVisible(false);
    dispose();
  }

}
