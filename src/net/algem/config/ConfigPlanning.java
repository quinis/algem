/*
 * @(#)ConfigPlanning.java 2.12.0 03/03/17
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
package net.algem.config;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import net.algem.planning.DateFr;
import net.algem.planning.DateRangePanel;
import net.algem.planning.Hour;
import net.algem.planning.HourField;
import net.algem.util.BundleUtil;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemNumericField;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.12.0
 */
public class ConfigPlanning
        extends ConfigPanel
{

  private DateRangePanel yearPanel;
  private DateRangePanel periodPanel;
  private DateRangePanel preEnrolmentStart;
  private HourField offPeakTime;
  private GemNumericField minDelay;
  private GemNumericField maxDelay;
  private GemNumericField cancelDelay;
  private GemNumericField sessions;
  private HourField startTime;
  private JCheckBox rangeNames, memberShip;
  private JRadioButton normal, reverse;
  private Config c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14;

  public ConfigPlanning(Map<String, Config> confs) {
    super(BorderFactory.createEmptyBorder(), confs);
    init();
  }

  private void init() {
    c1 = confs.get(ConfigKey.BEGINNING_YEAR.getKey());
    c2 = confs.get(ConfigKey.END_YEAR.getKey());
    c3 = confs.get(ConfigKey.BEGINNING_PERIOD.getKey());
    c4 = confs.get(ConfigKey.END_PERIOD.getKey());
    c5 = confs.get(ConfigKey.OFFPEAK_TIME.getKey());
    c6 = confs.get(ConfigKey.START_TIME.getKey());
    c7 = confs.get(ConfigKey.SCHEDULE_RANGE_NAMES.getKey());
    c8 = confs.get(ConfigKey.PERSON_SORT_ORDER.getKey());
    c9 = confs.get(ConfigKey.BOOKING_MIN_DELAY.getKey());
    c10 = confs.get(ConfigKey.BOOKING_MAX_DELAY.getKey());
    c11 = confs.get(ConfigKey.BOOKING_CANCEL_DELAY.getKey());
    c12 = confs.get(ConfigKey.BOOKING_REQUIRED_MEMBERSHIP.getKey());
    c13 = confs.get(ConfigKey.PRE_ENROLMENT_START_DATE.getKey());
    c14 = confs.get(ConfigKey.DEFAULT_NUMBER_OF_SESSIONS.getKey());
    
    Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    yearPanel = new DateRangePanel(new DateFr(c1.getValue()), new DateFr(c2.getValue()), border);
    yearPanel.setToolTipText(ConfigKey.BEGINNING_YEAR.getLabel());
    periodPanel = new DateRangePanel(new DateFr(c3.getValue()), new DateFr(c4.getValue()), border);
    periodPanel.setToolTipText(ConfigKey.BEGINNING_PERIOD.getLabel());
    
    preEnrolmentStart = new DateRangePanel(new DateFr(c13.getValue()),border);

    offPeakTime = new HourField(c5.getValue());
    offPeakTime.setToolTipText(BundleUtil.getLabel("ConfEditor.offpeak.time.tip1"));
    startTime = new HourField(c6 == null ? "00:00" : c6.getValue());
    startTime.setToolTipText(BundleUtil.getLabel("ConfEditor.start.time.tip"));

    minDelay = new GemNumericField(2);
    minDelay.setToolTipText(BundleUtil.getLabel("Booking.min.delay.tip"));
    minDelay.setText(c9.getValue());
    maxDelay = new GemNumericField(2);
    maxDelay.setToolTipText(BundleUtil.getLabel("Booking.max.delay.tip"));
    maxDelay.setText(c10.getValue());
    cancelDelay = new GemNumericField(2);
    cancelDelay.setToolTipText(BundleUtil.getLabel("Booking.cancel.delay.tip"));
    cancelDelay.setText(c11.getValue());

    memberShip = new JCheckBox(ConfigKey.BOOKING_REQUIRED_MEMBERSHIP.getLabel());
    memberShip.setToolTipText(BundleUtil.getLabel("Booking.required.membership.tip"));
    memberShip.setBorder(null);
    memberShip.setSelected(c12.getValue().equalsIgnoreCase("t"));

    content = new GemPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

    GemPanel p1 = new GemPanel();
    p1.setBorder(BorderFactory.createTitledBorder(BundleUtil.getLabel("Period.label")));
    p1.setLayout(new GridBagLayout());
    p1.setMinimumSize(new Dimension(520,140));
    GridBagHelper gb = new GridBagHelper(p1);

    GemLabel yearLabel = new GemLabel(BundleUtil.getLabel("School.year.label"));
    yearLabel.setToolTipText(ConfigKey.BEGINNING_YEAR.getLabel());
    gb.add(yearLabel, 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(yearPanel, 1, 0, 1, 1, GridBagHelper.WEST);

    GemLabel periodLabel = new GemLabel(BundleUtil.getLabel("Period.label"));
    periodLabel.setToolTipText(ConfigKey.BEGINNING_PERIOD.getLabel());
    gb.add(periodLabel, 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(periodPanel, 1, 1, 1, 1, GridBagHelper.WEST);
    
    GemLabel preEnrolmentLabel = new GemLabel(BundleUtil.getLabel("ConfEditor.pre-enrolment.start.date.label"));
    preEnrolmentLabel.setToolTipText(BundleUtil.getLabel("ConfEditor.pre-enrolment.start.date.tip"));
    preEnrolmentStart.setToolTipText(BundleUtil.getLabel("ConfEditor.pre-enrolment.start.date.tip"));
    gb.add(preEnrolmentLabel, 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(preEnrolmentStart, 1, 2, 1, 1, GridBagHelper.WEST);
    
    GemPanel p2 = new GemPanel();
    p2.setBorder(BorderFactory.createTitledBorder(BundleUtil.getLabel("Times.label")));
    p2.setLayout(new GridBagLayout());
    p2.setMinimumSize(new Dimension(520,160));
    GridBagHelper gb2 = new GridBagHelper(p2);

    GemLabel hourLabel = new GemLabel(ConfigKey.OFFPEAK_TIME.getLabel());
    hourLabel.setToolTipText(BundleUtil.getLabel("ConfEditor.offpeak.time.tip2"));

    GemPanel opPanel = new GemPanel(new BorderLayout());
    opPanel.add(offPeakTime, BorderLayout.WEST);

    gb2.add(hourLabel, 0, 0, 1, 1, GridBagHelper.WEST);
    gb2.add(opPanel, 1, 0, 1, 1, GridBagHelper.WEST);

    GemPanel stPanel = new GemPanel(new BorderLayout());
    stPanel.add(startTime, BorderLayout.WEST);
    GemLabel startTimeLabel = new GemLabel(ConfigKey.START_TIME.getLabel());
    startTimeLabel.setToolTipText(BundleUtil.getLabel("ConfEditor.start.time.tip"));
    gb2.add(startTimeLabel, 0, 1, 1, 1, GridBagHelper.WEST);
    gb2.add(startTime, 1, 1, 1, 1, GridBagHelper.WEST);

    GemLabel minDelayLabel = new GemLabel(ConfigKey.BOOKING_MIN_DELAY.getLabel());
    minDelayLabel.setToolTipText(BundleUtil.getLabel("Booking.min.delay.tip"));
    GemLabel maxDelayLabel = new GemLabel(ConfigKey.BOOKING_MAX_DELAY.getLabel());
    maxDelayLabel.setToolTipText(BundleUtil.getLabel("Booking.max.delay.tip"));
    GemLabel cancelDelayLabel = new GemLabel(ConfigKey.BOOKING_CANCEL_DELAY.getLabel());
    cancelDelayLabel.setToolTipText(BundleUtil.getLabel("Booking.cancel.delay.tip"));
    gb2.add(minDelayLabel, 0, 2, 1, 1, GridBagHelper.WEST);
    gb2.add(minDelay, 1, 2, 1, 1, GridBagHelper.WEST);
    gb2.add(maxDelayLabel, 0, 3, 1, 1, GridBagHelper.WEST);
    gb2.add(maxDelay, 1, 3, 1, 1, GridBagHelper.WEST);
    gb2.add(cancelDelayLabel, 0, 4, 1, 1, GridBagHelper.WEST);
    gb2.add(cancelDelay, 1, 4, 1, 1, GridBagHelper.WEST);

    gb2.add(memberShip, 0, 5, 2, 1, GridBagHelper.WEST);

    GemPanel p3 = new GemPanel();
    p3.setBorder(BorderFactory.createTitledBorder(BundleUtil.getLabel("Menu.options.label")));
    p3.setLayout(new GridBagLayout());
    p3.setMinimumSize(new Dimension(520,110));
    GridBagHelper gb3 = new GridBagHelper(p3);
    rangeNames = new JCheckBox(ConfigKey.SCHEDULE_RANGE_NAMES.getLabel());
    rangeNames.setSelected(c7.getValue().equals("t"));
    gb3.insets = new Insets(10,0,0,0);
    gb3.add(rangeNames, 0, 0, 2, 1, GridBagHelper.WEST);

    ButtonGroup group = new ButtonGroup();
    normal = new JRadioButton(BundleUtil.getLabel("Name.label") + " " + BundleUtil.getLabel("First.name.label").toLowerCase());
    reverse = new JRadioButton(BundleUtil.getLabel("First.name.label") + " " + BundleUtil.getLabel("Name.label").toLowerCase());
    group.add(normal);
    group.add(reverse);
    if ("n".equals(c8.getValue())) {
      normal.setSelected(true);
    } else {
      reverse.setSelected(true);
    }

    GemPanel sortby = new GemPanel();
    sortby.add(normal);
    sortby.add(reverse);

    gb3.add(new GemLabel(BundleUtil.getLabel("Person.sort.order.label") + " :"), 0, 1, 2, 1, GridBagHelper.WEST);
    gb3.insets = new Insets(0,0,4,0);
    gb3.add(sortby, 0, 2, 2, 1, GridBagHelper.WEST);
    GemPanel sessionsPanel = new GemPanel(new FlowLayout());
    sessionsPanel.add(new GemLabel(BundleUtil.getLabel("Default.number.of.sessions")));
    sessions = new GemNumericField(2);
    sessions.setText(c14.getValue());
    sessionsPanel.add(sessions);
    gb3.add(sessionsPanel,0,4,2,1, GridBagHelper.WEST);
    content.add(p1);
    content.add(p2);
    content.add(p3);
    add(content);
  }

  @Override
  public List<Config> get() {
    List<Config> conf = new ArrayList<Config>();
    c1.setValue(yearPanel.getStartFr().toString());
    c2.setValue(yearPanel.getEndFr().toString());
    c3.setValue(periodPanel.getStartFr().toString());
    c4.setValue(periodPanel.getEndFr().toString());
    Hour h = offPeakTime.get();
    Hour limit = new Hour("23:00");
    c5.setValue(h.after(limit) ? limit.toString() : h.toString());
    h = startTime.get();
    c6.setValue(h.after(limit) ? limit.toString() : h.toString());

    c7.setValue(rangeNames.isSelected() ? "t" : "f");
    c8.setValue(normal.isSelected() ? "n" : "r");
    c9.setValue(minDelay.getText());
    c10.setValue(maxDelay.getText());
    c11.setValue(cancelDelay.getText());
    c12.setValue(memberShip.isSelected() ? "t" : "f");
    c13.setValue(preEnrolmentStart.getStartFr().toString());
    c14.setValue(sessions.getText());

    conf.add(c1);
    conf.add(c2);
    conf.add(c3);
    conf.add(c4);
    conf.add(c5);
    conf.add(c6);
    conf.add(c7);
    conf.add(c8);
    conf.add(c9);
    conf.add(c10);
    conf.add(c11);
    conf.add(c12);
    conf.add(c13);
    conf.add(c14);

    return conf;
  }

}
