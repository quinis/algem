/*
 * @(#)RoomRateChoiceModel.java	2.8.t 16/04/14
 *
 * Copyright (c) 1999-2014 Musiques Tangentes. All Rights Reserved.
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
package net.algem.room;

import java.util.Vector;
import net.algem.util.model.GemList;
import net.algem.util.ui.GemChoiceModel;

/**
 * ComboxBox model for room rates.
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.t
 */
public class RoomRateChoiceModel
        extends GemChoiceModel {

  public RoomRateChoiceModel(GemList<RoomRate> list) {
    super(list);
  }

  public RoomRateChoiceModel(Vector<RoomRate> list) {
    this(new RoomRateList(list));
  }

  public RoomRate getRoomRate(int id) {
    return (RoomRate) list.getItem(id);
  }
}
