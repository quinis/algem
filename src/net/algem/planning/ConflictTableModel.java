/*
 * @(#)ConflictTableModel.java	2.8.a 24/04/13
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
package net.algem.planning;

import javax.swing.ImageIcon;
import net.algem.util.BundleUtil;
import net.algem.util.ImageUtil;
import net.algem.util.ui.JTableModel;

/**
 * Conflict table model.
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.a
 */
public class ConflictTableModel
        extends JTableModel
{

  private ImageIcon iconOK;
  private ImageIcon iconERR;

  public ConflictTableModel() {
		
    header = new String[]{
      BundleUtil.getLabel("Date.label"), 
      BundleUtil.getLabel("Start.label"), 
      BundleUtil.getLabel("End.label"),
      BundleUtil.getLabel("Verification.label"), 
      BundleUtil.getLabel("Conflict.label")
    };
    iconOK = ImageUtil.createImageIcon(ImageUtil.NO_CONFLICT_ICON);
    iconERR = ImageUtil.createImageIcon(ImageUtil.CONFLICT_ICON);
  }

  @Override
  public int getIdFromIndex(int i) {
    return -1;
  }

  @Override
  public Class getColumnClass(int column) {
    switch (column) {
      case 0:
        return String.class;
      case 1:
      case 2:
        return String.class;
      case 3:
        return ImageIcon.class;
      case 4:
        return String.class;
      default:
        return Object.class;
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  @Override
  public Object getValueAt(int ligne, int colonne) {
    ScheduleTestConflict p = (ScheduleTestConflict) tuples.elementAt(ligne);
    switch (colonne) {
      case 0:
        return p.getDate().toString();
      case 1:
        return p.getStart().toString();
      case 2:
        return p.getEnd().toString();
      case 3:
        if (p.isConflict()) {
          return iconERR;
        } else {
          return iconOK;
        }
      case 4:
        StringBuilder lib = new StringBuilder();
        if (!p.isTeacherFree()) {
          lib.append("[ Prof occupé ]");
        }
        if (!p.isRoomFree()) {
          lib.append(" [ Salle occupée ]");//essai
        }
        if (!p.isMemberFree()) {
          lib.append(" [ Adhérent occupé ]");
        }
        if (p.getDetail() != null) {
          lib.append(p.getDetail());
        }
        return lib.toString();
    }
    return null;
  }

  @Override
  public void setValueAt(Object value, int ligne, int column) {
  }
}
