/*
 * @(#)CourseCodeCtrl.java	2.14.0 08/06/17
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

import java.sql.SQLException;
import java.util.List;
import net.algem.course.CourseCode;
import net.algem.course.CourseCodeType;
import net.algem.course.ModuleIO;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.MessageUtil;
import net.algem.util.event.GemEvent;
import net.algem.util.model.Model;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.MessagePopup;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.14.0
 * @since 2.8.a 14/03/2013
 */
public class CourseCodeCtrl
  extends ParamTableCtrl
 {

  private CourseCodeIO ccIO;

  public CourseCodeCtrl(GemDesktop desktop) {
    super(desktop, BundleUtil.getLabel("Menu.course.codes.label"), true);
  }

  @Override
  public void setView(boolean activable) {
    table = new GemParamTableView(title, new GemParamTableModel<CourseCode>());
    table.setColumnModel();
    mask = new GemParamView(true, 6);
  }

  @Override
  protected boolean isKeyModif() {
    return false;
  }


  @Override
  public void load() {
    ccIO = new CourseCodeIO(dc);
    List<GemParam> codes = desktop.getDataCache().getList(Model.CourseCode).getData();
    for(GemParam p : codes) {
      table.addRow(p);
    }
  }

  @Override
  public void modification(Param current, Param p) throws SQLException, ParamException {
    if (p instanceof GemParam) {
      CourseCode cc = new CourseCode((GemParam) p);
      ccIO.update(cc);
      desktop.getDataCache().update(cc);
      desktop.postEvent(new GemEvent(this, GemEvent.MODIFICATION, GemEvent.COURSE_CODE, cc));

    }
  }

  @Override
  public void insertion(Param p) throws SQLException, ParamException {
    if (p instanceof GemParam) {
      CourseCode cc = new CourseCode((GemParam) p);
      ccIO.insert(cc);
      p.setId(cc.getId());// important ! (refresh id in table)
      desktop.getDataCache().add(cc);
      desktop.postEvent(new GemEvent(this, GemEvent.CREATION, GemEvent.COURSE_CODE, cc));
    }
  }

  @Override
  public void suppression(Param p) throws Exception {
    if (p instanceof GemParam) {
      CourseCode cc = new CourseCode((GemParam) p);
      if (CourseCodeType.INS.getId() == cc.getId()
        || CourseCodeType.ATL.getId() == cc.getId()
        || CourseCodeType.ATP.getId() == cc.getId()
        || CourseCodeType.STG.getId() == cc.getId()) {
        throw new ParamException(MessageUtil.getMessage("course.code.predefined.exception"));
      }
      int used = ((ModuleIO) DataCache.getDao(Model.Module)).haveCode(cc.getId());
      if (used > 0) {
        throw new ParamException(MessageUtil.getMessage("course.code.delete.exception", used));
      }
      if (MessagePopup.confirm(this, MessageUtil.getMessage("param.delete.confirmation"))) {
        ccIO.delete(cc);
        desktop.getDataCache().remove(cc);
        desktop.postEvent(new GemEvent(this, GemEvent.SUPPRESSION, GemEvent.COURSE_CODE, cc));
      } else {
        throw new ParamException();
      }
    }
  }

}
