/*
 * @(#)PersonFileGroupListCtrl.java 2.6.a 18/09/12
 * 
 * Copyright (c) 1999-2012 Musiques Tangentes. All Rights Reserved.
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
package net.algem.contact;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.algem.group.Group;
import net.algem.group.Musician;
import net.algem.group.PersonFileMusicianTableModel;
import net.algem.util.ui.ListCtrl;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 */
public class PersonFileGroupListCtrl
        extends ListCtrl
{

  public PersonFileGroupListCtrl() {
    super(false);
    setLayout(new BorderLayout());
    tableModel = new PersonFileMusicianTableModel();

    table = new JTable(tableModel);
    table.setAutoCreateRowSorter(true);

    JScrollPane p = new JScrollPane(table);
    add(p, BorderLayout.NORTH);
  }

  /**
   * Gets the group corresponding to selection.
   * @return un groupe
   */
  public Group getGroup() {
    Musician m = ((PersonFileMusicianTableModel) tableModel).getMusician(table.getSelectedRow());
    return m.getGroup();
  }
}
