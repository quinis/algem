/*
 * @(#)AgeRangeException.java 2.6.a 24/09/12
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

package net.algem.config;

/**
 *
 * @author <a href="mailto:nicolasnouet@free.fr">nicolasnouet@free.fr</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 2.3.a 21/02/12
 */
public class AgeRangeException extends Exception {

  /**
	* Creates a new instance of AgeRangeException
	*/
  public AgeRangeException() {
  }

  /**
	* Creates a new instance of AgeRangeException
	* @param message
	*/
  public AgeRangeException(String message) {
    super(message);
  }

  /**
	* Creates a new instance of AgeRangeException
	* @param cause
	*/
  public AgeRangeException(Throwable cause) {
		super(cause);
	}

  /**
	* Creates a new instance of AgeRangeException
	* @param message
	* @param cause
	*/
  public AgeRangeException(String message, Throwable cause) {
		super(message, cause);
	}

}

