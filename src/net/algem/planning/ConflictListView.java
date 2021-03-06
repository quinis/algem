/*
 * @(#)ConflictListView.java	2.12.0 08/03/17
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
package net.algem.planning;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import net.algem.util.BundleUtil;
import net.algem.util.ui.DateCellEditor;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;

/**
 * List of conflicts.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.12.0
 */
public class ConflictListView
        extends GemPanel
{

  private ConflictTableModel tableModel;
  private JTable table;
  private GemLabel status;

  public ConflictListView(ConflictTableModel tableModel) {
    this.tableModel = tableModel;
    this.table = new JTable(tableModel);
    //table.setAutoCreateRowSorter(true);

    TableColumnModel cm = table.getColumnModel();
    cm.getColumn(0).setPreferredWidth(80);
    cm.getColumn(1).setPreferredWidth(40);
    cm.getColumn(2).setPreferredWidth(40);
    cm.getColumn(3).setPreferredWidth(30);
    cm.getColumn(4).setPreferredWidth(30);
    cm.getColumn(5).setPreferredWidth(400);

    cm.getColumn(0).setCellEditor(new DateCellEditor());

    JScrollPane pm = new JScrollPane(table);

    status = new GemLabel();
    setLayout(new BorderLayout());
    add(pm, BorderLayout.CENTER);
    add(status, BorderLayout.SOUTH);
  }

  public void addUpdateConflictListener(UpdateConflictListener listener) {
    tableModel.addUpdateConflictListener(listener);
  }

  public void clear() {
    tableModel.clear();
    status.setText("");
  }

  public void addConflict(ScheduleTestConflict p) {
    tableModel.addItem(p);
  }

  public void setStatus(String s) {
    status.setText(s == null ? BundleUtil.getLabel("Conflicts.label").toLowerCase() : s);
  }

  public List<ScheduleTestConflict> getResolvedConflicts() {
    stopCellEditing();
    List<ScheduleTestConflict> resolved = new ArrayList<>();
    for(ScheduleTestConflict c : tableModel.getData()) {
      if (c.isActive()) {
        resolved.add(c);
      }
    }
    return resolved;
  }

  void stopCellEditing() {
    TableCellEditor tce = table.getCellEditor();
    if (tce != null) {
      tce.stopCellEditing();
    }
  }

}
