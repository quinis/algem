/*
 * @(#)MusicianExportDlg.java 2.15.8 22/03/18
 *
 * Copyright (c) 1999-2018 Musiques Tangentes. All Rights Reserved.
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

import javax.swing.JLabel;
import net.algem.config.InstrumentChoice;
import net.algem.util.BundleUtil;
import net.algem.util.MessageUtil;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.GemChoice;
import net.algem.util.ui.GridBagHelper;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.8
 * @since 2.6.a 07/11/2012
 */
public class MusicianExportDlg
  extends StudentExportDlg {

  private GemChoice instrument;

  public MusicianExportDlg(GemDesktop desktop) {
    super(desktop, BundleUtil.getLabel("Group.members.label"));
  }

  @Override
  protected void setPanel() {
    instrument = new InstrumentChoice(desktop.getDataCache().getInstruments());
    instrument.setToolTipText(MessageUtil.getMessage("export.musicians.tip", BundleUtil.getLabel("None.label")));

    gb.add(new JLabel(BundleUtil.getLabel("Instrument.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(instrument, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new JLabel(BundleUtil.getLabel("Date.From.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(dateRange, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(directToMail, 1, 2, 1, 1, GridBagHelper.WEST);

    //nextRow = 3;
  }

  @Override
  public String getRequest() {
    return service.getMusicianByInstrument(instrument.getKey(), dateRange.getStart(), dateRange.getEnd());
  }

  @Override
  protected String getFileName() {
    return BundleUtil.getLabel("Export.musician.file");
  }

}
