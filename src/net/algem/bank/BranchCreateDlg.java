/*
 * @(#)BranchCreateDlg.java	2.15.2 27/09/17
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
 *
 */
package net.algem.bank;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import net.algem.contact.CodePostalCtrl;
import net.algem.util.DataConnection;
import net.algem.util.GemCommand;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.module.GemModule;
import net.algem.util.ui.*;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.2
 */
public class BranchCreateDlg
        implements ActionListener
{

  private DataConnection dc;
  private JDialog dlg;
  private boolean validation;
  private final GemButton btValidation;
  private final GemButton btCancel;
  private final BranchView branchView;
  private BankBranch branch;
  private BankBranchIO bankBranchIO;

  public BranchCreateDlg(Component c, String title, DataConnection dc) {
    this.dc = dc;
    bankBranchIO = new BankBranchIO(dc);
    dlg = new JDialog(PopupDlg.getTopFrame(c), true);
    validation = false;

    branchView = new BranchView();
    branchView.setPostalCodeCtrl(new CodePostalCtrl(this.dc));
    branchView.setBankCodeCtrl(new BankCodeCtrl(this.dc, bankBranchIO));

    btValidation = new GemButton(GemCommand.VALIDATE_CMD);
    btValidation.addActionListener(this);
    btCancel = new GemButton(GemCommand.CANCEL_CMD);
    btCancel.addActionListener(this);

    GemPanel buttons = new GemPanel();
    buttons.setLayout(new GridLayout(1, 2));
    buttons.add(btCancel);
    buttons.add(btValidation);

    dlg.setTitle(title);
    dlg.add(branchView, BorderLayout.CENTER);
    dlg.add(buttons, BorderLayout.SOUTH);
    dlg.setSize(GemModule.DEFAULT_SIZE);
    dlg.setLocation(100, 100);
  }

  public void setBankCode(String s) {
    branchView.setCodeBanque(s);
  }

  public void setBankName(String s) {
    branchView.setBankName(s);
  }

  public void setBranchCode(String s) {
    branchView.setBranchCode(s);
  }

  public void enter() {
    dlg.setVisible(true);
  }

  public boolean validate() {
    if (branchView.isNewBank()) {
      Bank b = branchView.getBank();
      if (b.isValid()) {
        try {
          if (BankIO.findCode(b.getCode(), dc) == null) {
            BankIO.insert(b, dc);
          }
        } catch (Exception e) {
          GemLogger.logException("insert banque", e, dlg);
          return false;
        }
      }
    }
    branch = branchView.getBankBranch();
    if (!branch.isValid()) {
      MessagePopup.error(dlg, MessageUtil.getMessage("incomplete.entry.error"));
      return false;
    }
    try {
      bankBranchIO.insert(branch);
    } catch (Exception e) {
      GemLogger.logException("insert guichet", e, dlg);
      return false;
    }
    return validation = true;
  }

  public boolean isValid() {
    return validation;
  }

  public void setBranch(BankBranch b) {
    branchView.setBankBranch(b);
  }

  public BankBranch getBranch() {
    return branch;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getActionCommand().equals(GemCommand.VALIDATE_CMD)) {
      if (!validate()) {
        return;
      }
    } else {
      validation = false;
    }
    dlg.setVisible(false);
    dlg.dispose();
  }
}
