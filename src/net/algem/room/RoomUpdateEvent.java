/*
 * @(#)RoomUpdateEvent.java	2.6.a 24/09/12
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

package net.algem.room;

import java.util.Date;
import net.algem.util.event.GemEvent;

/**
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 2.1a
 */
public class RoomUpdateEvent
  extends GemEvent
{
  private Room room;
  private Date date;

  public RoomUpdateEvent(Object _source, Room _room)
	{
		super(_source, MODIFICATION, ROOM); // source, operation, type event
		room = _room;
	}

  public RoomUpdateEvent(Object _source, Room	_room, Date d) {
    this(_source, _room);
    this.date = d;
  }

	public Room getRoom()
	{
		return room;
	}

  @Override
	public String toString() {
		return "RoomUpdateEvent:"+type+","+operation+" "+room;
	}

  /**
   * @return the date
   */
  public Date getDate() {
    return date;
  }

}
