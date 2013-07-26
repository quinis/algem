/*
 * @(#)OrderLineTableView.java 2.8.j /07/13
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.RowFilter.Entry;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import net.algem.contact.Person;
import net.algem.contact.PersonIO;
import net.algem.planning.DateFr;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.menu.MenuPopupListener;
import net.algem.util.model.Model;

/**
 * Order line table view.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.j
 * @since 1.0a 07/07/1999
 *
 */
public class OrderLineTableView
        extends JPanel
        implements TableModelListener
{

  private OrderLineTableModel tableModel;
  private JTable table;
  private JScrollPane panel;
  /** Tooltips for columns header. */
  private String[] columnToolTips = {
    BundleUtil.getLabel("Payment.schedule.payer.tip"),
    BundleUtil.getLabel("Payment.schedule.member.tip"),
    BundleUtil.getLabel("Payment.schedule.date.tip"),
    BundleUtil.getLabel("Payment.schedule.label.tip"),
    BundleUtil.getLabel("Payment.schedule.mode.of.payment.tip"),
    BundleUtil.getLabel("Payment.schedule.amount.tip"),
    BundleUtil.getLabel("Payment.schedule.document.tip"),
    BundleUtil.getLabel("Payment.schedule.account.tip"),
    BundleUtil.getLabel("Payment.schedule.cost.account.tip"),
    BundleUtil.getLabel("Payment.schedule.cashing.tip"),
    BundleUtil.getLabel("Payment.schedule.transfer.tip"),
    BundleUtil.getLabel("Invoice.label")
  };
  private RowFilter<Object, Object> dateFilter;
  private RowFilter<Object, Object> memberShipFilter;
  private RowFilter<Object, Object> unpaidFilter;
  private final TableRowSorter<TableModel> sorter;
  private DateFr begin;
  private DateFr end;

  public OrderLineTableView(OrderLineTableModel _tableModel, ActionListener al) {

    tableModel = _tableModel;
    table = new JTable(tableModel)
    {
      //Implements table header tool tips.

      @Override
      protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel)
        {

          @Override
          public String getToolTipText(MouseEvent e) {
            //String tip = null;
            java.awt.Point p = e.getPoint();
            int index = columnModel.getColumnIndexAtX(p.x);
            int realIndex = columnModel.getColumn(index).getModelIndex();
            return columnToolTips[realIndex];
          }
        };
      }
    };

    sorter = new TableRowSorter<TableModel>(tableModel);
    table.setRowSorter(sorter);
//    tableVue.setAutoCreateRowSorter(true);
    dateFilter = new RowFilter<Object, Object>()
    {

      @Override
      public boolean include(Entry<? extends Object, ? extends Object> entry) {
        DateFr date = (DateFr) entry.getValue(2);
        return date.after(begin) && date.before(end);
      }
    };
    
    unpaidFilter = new RowFilter<Object, Object>()
    {

      @Override
      public boolean include(Entry<? extends Object, ? extends Object> entry) {
        boolean paid = (Boolean) entry.getValue(9);
        return !paid;
      }
    };
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    TableColumnModel cm = table.getColumnModel();
    cm.getColumn(0).setPreferredWidth(50);//payeur
    cm.getColumn(1).setPreferredWidth(50);//adherent
    cm.getColumn(2).setPreferredWidth(90);//date
    cm.getColumn(3).setPreferredWidth(160);//libelle
    cm.getColumn(4).setPreferredWidth(35);//reglement
    cm.getColumn(5).setPreferredWidth(70);//montant
    cm.getColumn(6).setPreferredWidth(70);//piece
    cm.getColumn(7).setPreferredWidth(160);//compte
    cm.getColumn(8).setPreferredWidth(140);//analytique
    cm.getColumn(9).setPreferredWidth(35);//encaissé
    cm.getColumn(10).setPreferredWidth(35);//transféré
    cm.getColumn(11).setPreferredWidth(35);//facture
    //cm.getColumn(11).setPreferredWidth(20);// suppression devise de la vue
    //cm.getColumn(12).setPreferredWidth(50);

    DefaultTableCellRenderer rd = new DefaultTableCellRenderer();
    rd.setHorizontalAlignment(SwingConstants.RIGHT);
    // alignement à droite de la colonne Montant
    table.getColumnModel().getColumn(5).setCellRenderer(rd);
    //tableVue.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // la sélection multiple permet le copier-coller d'un ensemble de lignes vers une autre application
    table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tableModel.addTableModelListener(this);

    // Display name in tooltip for member column
    table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer()
    {
      public Component getTableCellRendererComponent(
              JTable table, Object value,
              boolean isSelected, boolean hasFocus,
              int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String idper = (String) value;
        Person p = ((PersonIO) DataCache.getDao(Model.Person)).findId(Integer.parseInt(idper.trim()));
        c.setToolTipText(p != null ? p.getFirstnameName() : "");
        return c;
      }
    });
    
    panel = new JScrollPane(table);

    setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
  }

  /**
   * Adds right click listener for displaying popup menu.
   * At least one row must be selected and this row must be marked transfered.
   *
   * @param popup
   */
  void addPopupMenuListener(JPopupMenu popup) {
    table.addMouseListener(new MenuPopupListener(table, popup)
    {

      @Override
      public void maybeShowPopup(MouseEvent e) {
        int[] rows = table.getSelectedRows();
        boolean t = false;
        for (int i = 0; i < rows.length; i++) {
          if (getElementAt(rows[i]).isTransfered()) {
            t = true;
            break;
          }
        }
        if (t) {
          super.maybeShowPopup(e);
        }
      }
    });
  }

  @Override
  public void tableChanged(TableModelEvent evt) {
  }

  public void removeElementAt(int n) {
    tableModel.removeElementAt(table.convertRowIndexToModel(n));
  }

  public OrderLine getElementAt(int n) {
    return (OrderLine) tableModel.getElementAt(table.convertRowIndexToModel(n));
  }

  public void setElementAt(OrderLine e, int n) {
    tableModel.setElementAt(e, table.convertRowIndexToModel(n));
    //tableVue.repaint();
  }

  public int getSelectedRow() {
    int n = table.getSelectedRow();
    if (table.getSelectedRowCount() < 0 || n < 0 || tableModel.getRowCount() <= n) {
      return -1;
    }
    return n;
  }

  public int[] getSelectedRows() {
    int r[] = table.getSelectedRows();
    return r;
  }

  public void setMemberShipFilter(final String a) {
    memberShipFilter = new RowFilter<Object, Object>()
    {
      @Override
      public boolean include(Entry<? extends Object, ? extends Object> entry) {
        DateFr date = (DateFr) entry.getValue(2);
        String account = (String) entry.getValue(7);
        return account.equals(a) && date.after(begin) && date.before(end);
      }
    };
  }

  /**
   * Line selection by date.
   *
   * @param date start date
   */
  public void filterByDate(DateFr date) {

    if (date == null) {
      sorter.setRowFilter(null);
    } else {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date.getDate());
      cal.set(Calendar.MONTH, Calendar.SEPTEMBER);// beginning of year for accounting
      cal.set(Calendar.DAY_OF_MONTH, 1);
      begin = new DateFr(cal.getTime());
      end = new DateFr(begin);
      end.incYear(1);
      sorter.setRowFilter(dateFilter);
//      sorter.setRowFilter(RowFilter.regexFilter("^.*"+year+".*$", 2));
//      sorter.setRowFilter(RowFilter.begin(RowFilter.ComparisonType.AFTER, date, 2));
    }
  }

  public void filterByPeriod(DateFr begin, DateFr end) {
    this.begin = begin;
    this.end = end;
    sorter.setRowFilter(dateFilter);
  }

  public void filterByMemberShip(DateFr begin, DateFr end) {
    this.begin = begin;
    this.end = end;
    sorter.setRowFilter(memberShipFilter);
  }
  
   public void filterByUnpaid() {
    sorter.setRowFilter(unpaidFilter);
  }
  

  /**
   * Activates a listener for rows selection.
   *
   * @param tc text component to update
   */
  public void addListSelectionListener(JTextComponent tc) {
    table.getSelectionModel().addListSelectionListener(new OrderLineSelectionListener(this, tc));
  }

  /**
   * Retrieves the table view.
   *
   * @return une table
   */
  public JTable getTable() {
    return table;
  }
}
