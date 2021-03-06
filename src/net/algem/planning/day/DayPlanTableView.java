/*
 * @(#)DayPlanTableView.java	2.13.1 19/04/17
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
package net.algem.planning.day;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.algem.planning.DateDayBar;
import net.algem.planning.DateFr;
import net.algem.planning.DateFrField;
import net.algem.planning.ScheduleCanvas;
import net.algem.planning.ScheduleObject;
import net.algem.planning.ScheduleRangeObject;
import net.algem.util.BundleUtil;
import net.algem.util.ui.GemButton;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 * Abstract class for day views.
 * Main view {@literal DayPlanView} is composed of columns {@literal DayPlan}.
 * The field {@literal date} and button bar {@literal dayBar} permit to navigate in the dayPlanViewning.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">jean-marc gobat</a>
 * @version 2.13.1
 * @since 1.0a 07/07/1999
 */
public abstract class DayPlanTableView
        extends GemPanel
        implements AdjustmentListener, PropertyChangeListener, KeyListener
{

  protected DayPlanView dayPlanView;
  protected DateDayBar dayBar;
  protected DateFrField date;
  protected GemLabel dayLabel;
  protected GemButton btNow;
  protected GemButton btPrev;
  protected GemButton btNext;
  protected ActionListener mainActionListener;
  private JScrollBar sb;
  private JSlider hZoom;

  public DayPlanTableView(String label) {

    dayPlanView = new DayPlanView();
    date = new DateFrField();

    ActionListener prevNextListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        DateFr d = date.getDateFr();
        if (cmd.equals("DayPlanTableView.prevDay")) {
          d.decDay(1);
        } else if (cmd.equals("DayPlanTableView.nextDay")){
          d.incDay(1);
        }
        if (mainActionListener != null) {
          date.set(d);
          dayLabel.setText(date.getDayOfWeek());
          mainActionListener.actionPerformed(new ActionEvent(date, ActionEvent.ACTION_PERFORMED, "PrevNext"));
        }
      }
    };
    Insets prevNextInsets = new Insets(0, 2, 0, 2);
    dayLabel = new GemLabel();
    btPrev = new GemButton("<<");
    btPrev.setMargin(prevNextInsets);// IMPORTANT : call before setting preferredSize
    date.setMargin(prevNextInsets);
    date.setPreferredSize(new Dimension(date.getPreferredSize().width, btPrev.getPreferredSize().height));
    btPrev.setToolTipText(BundleUtil.getLabel("Day.previous.label"));
    btPrev.setActionCommand("DayPlanTableView.prevDay");
    btPrev.addActionListener(prevNextListener);
    btNext = new GemButton(">>");
    btNext.setMargin(prevNextInsets);
    btNext.setToolTipText(BundleUtil.getLabel("Day.next.label"));
    btNext.setActionCommand("DayPlanTableView.nextDay");
    btNext.addActionListener(prevNextListener);
    btNow = new GemButton(BundleUtil.getLabel("Action.today.label"));
    btNow.setMargin(prevNextInsets);
    hZoom = new JSlider(JSlider.HORIZONTAL,-40, 40, 0);
    hZoom.setToolTipText(BundleUtil.getLabel("Slider.horizontal.zoom.tip"));
    hZoom.setMajorTickSpacing(10);
    hZoom.setMinorTickSpacing(5);
    hZoom.setSnapToTicks(true);

    /*hZoom.setFont(new Font("SansSerif", Font.PLAIN, 8));
    hZoom.setPaintLabels(true);*/

    hZoom.setMinimumSize(new Dimension(80, hZoom.getPreferredSize().height));
    hZoom.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          hZoom.setValue(0);
          dayPlanView.setStepX(ScheduleCanvas.DAY_COL_WIDTH);
          if (mainActionListener != null) {
            mainActionListener.actionPerformed(new ActionEvent(date, ActionEvent.ACTION_PERFORMED, "ZOOM_H"));
          }
        }
      }
    });
    hZoom.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
          int z = (int) source.getValue();
          dayPlanView.setStepX(ScheduleCanvas.DAY_COL_WIDTH + z);
          if (mainActionListener != null) {
            mainActionListener.actionPerformed(new ActionEvent(date, ActionEvent.ACTION_PERFORMED, "ZOOM_H"));
          }
        }
      }
    });

    GemPanel p = new GemPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(new GemLabel(BundleUtil.getLabel("Day.schedule.prefix.label") + " " + label.toLowerCase() + " : "));
    p.add(dayLabel);
    p.add(date);
    p.add(btPrev);
    p.add(btNext);
    p.add(btNow);

    dayBar = new DateDayBar();

    sb = new JScrollBar(JScrollBar.HORIZONTAL);
    sb.addAdjustmentListener(this);

    this.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(this);

    gb.add(p, 0, 0, 1, 1,  GridBagHelper.WEST);
    gb.add(hZoom, 1, 0, 1, 1, GridBagHelper.EAST);
    gb.add(dayPlanView, 0, 1, 2, 1, GridBagHelper.BOTH, 1.0, 1.0);
    gb.add(sb, 0, 2, 2, 1, GridBagHelper.HORIZONTAL, 1.0, 0.0);
    gb.add(dayBar, 0, 3, 2, 1, GridBagHelper.HORIZONTAL, 1.0, 0.0);

    dayPlanView.addKeyListener(this);
  }

  /**
   * Gets the navigation bar.
   * @return a navigation component
   */
  public DateDayBar getNavigationBar() {
    return dayBar;
  }

  /**
   * Position the scrollbar.
   */
  public void setScrollBar() {
    Rectangle r = dayPlanView.computeScroll();
    sb.setValues(r.x, r.y, r.width, r.height);
  }

  /**
   * Position programmatically the scrollbar to 0.
   */
  public void setScrollBarToZero() {
    adjustmentValueChanged(new AdjustmentEvent(sb, AdjustmentEvent.ADJUSTMENT_LAST, AdjustmentEvent.TRACK, 0));
  }

  /**
   * The value adjusted depends on the of the first visible col.
   * @param e event
   * @see DayPlanView#setColOffset(int)
   */
  @Override
  public void adjustmentValueChanged(AdjustmentEvent e) {
    dayPlanView.setColOffset(e.getValue());
    setScrollBar();
    repaint();
  }

  public void addActionListener(ActionListener l) {
    this.mainActionListener = l;
    dayBar.addActionListener(l);
    dayPlanView.addActionListener(l);
    date.addActionListener(l);
    btNow.addActionListener(l);
  }

  public void removeActionListener(ActionListener l) {
    this.mainActionListener = null;
    dayBar.removeActionListener(l);
    dayPlanView.removeActionListener(l);
    date.removeActionListener(l);
    btNow.removeActionListener(l);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() instanceof DaySchedule) {
      DaySchedule model = (DaySchedule) evt.getSource();
      Date d = (Date) evt.getNewValue();
      load(d, model.getSchedules(), model.getRanges());
      dayBar.setDate(d);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    System.out.println("KEYTYPE:" + e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("KEYREL:" + e);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("KEYPRESS:" + e);
    if (e.isActionKey() && e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
      System.out.println("PGDOWN");
      e.consume();
    }
  }

  public DayPlanView getCanvas() {
    return dayPlanView;
  }

  public abstract void load(java.util.Date d, Vector<ScheduleObject> schedules, Vector<ScheduleRangeObject> ranges);

  public List<DayPlan> getCurrentPlanning() {
    return dayPlanView.getCurrentPlanning();
  }
}
