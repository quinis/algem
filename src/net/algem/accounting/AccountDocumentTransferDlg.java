/*
 * @(#)AccountDocumentTransferDlg.java	2.8.a 01/04/13
 * 
 * Copyright (c) 1999-2013 Musiques Tangentes. All Rights Reserved.
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

package net.algem.accounting;

import java.awt.*;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import net.algem.planning.DateFr;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;
import net.algem.util.ui.MessagePopup;

/**
 * Transfer from a document number.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.a
 * @since 1.0a 27/09/2000
 */
public class AccountDocumentTransferDlg
        extends AccountTransferDlg
{

  private AccountDocumentTransferView view;

  public AccountDocumentTransferDlg(Dialog _parent, DataCache dataCache) {
    super(_parent, dataCache);
  }

  public AccountDocumentTransferDlg(Frame _parent, DataCache dataCache) {
    super(_parent, dataCache);
  }
//
//  @Override
//  public void init(DataCache dataCache) {
//    super.init(dataCache);
//  }

  @Override
  public void setDisplay() {
    view = new AccountDocumentTransferView(dataCache);
    Container container = getContentPane();
    setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    GemPanel entete = new GemPanel();
    entete.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(entete);
    gb.insets = GridBagHelper.SMALL_INSETS;

    gb.add(new JLabel(BundleUtil.getLabel("Menu.file.label")), 0, 0, 1, 1, GridBagHelper.EAST);
    gb.add(filePath, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(chooser, 2, 0, 1, 1, GridBagHelper.WEST);

    container.add(entete);
    container.add(view);
    container.add(buttons);
    pack();
  }

  @Override
  void transfer() {
    DateFr start = view.getDateStart();
    DateFr end = view.getDateEnd();
    String payment = view.getModeOfPayment();
    String document = view.getDocument();
    int errors = 0;

    String query = "echeance >= '" + start + "' AND echeance <= '" + end + "' AND piece='" + document + "' AND reglement='" + payment + "' AND paye='t' AND transfert='f'";
    Vector<OrderLine> echeances = OrderLineIO.find(query, dbx);
    if (echeances.size() <= 0) {
      MessagePopup.information(this, MessageUtil.getMessage("payment.transfer.empty.collection"));
      return;
    }

    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    try {

      /*String codeJournal = "";
      String documentAccount = "";

      Compte c = getDocumentAccount(reglement);
      if (c != null) {
      codeJournal = getCodeJournal(c.getId());
      documentAccount = c.getNumber();
      }*/

      /*if ("ESP".equalsIgnoreCase(reglement)) {
      codeJournal = "CA";
      documentAccount = "5300000000";
      } else {
      codeJournal = "CC";
      documentAccount = "5120300000";
      }*/
      String codeJournal = "";
      documentAccount = getDocumentAccount(payment);
      if (documentAccount != null) {
        codeJournal = getCodeJournal(documentAccount.getId());
      }
      String path = filePath.getText();

      if (view.withCSV()) {
        path = path.replace(".txt", ".csv");
        exportCSV(path, echeances);
      } else {
        if (ModeOfPayment.FAC.toString().equalsIgnoreCase(payment)) {
          errors = tiersExport(path, echeances);
        } else {
          export(path, echeances, codeJournal);
        }
        updateTransfer(echeances);
      }
      MessagePopup.information(this, MessageUtil.getMessage("payment.transfer.info", new Object[]{echeances.size() - errors, path}));
    } catch (Exception ex) {
      GemLogger.logException(MessageUtil.getMessage("payment.transfer.exception"), ex, this);
    }
    setCursor(Cursor.getDefaultCursor());
  }
}
