/*
 * @(#)BasicEmployeeService.java 2.9.4.0 01/04/2015
 *
 * Copyright (c) 1999-2015 Musiques Tangentes. All Rights Reserved.
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

import java.math.BigDecimal;
import java.sql.SQLException;
import net.algem.config.ConfigKey;
import net.algem.util.DataConnection;
import net.algem.util.DocService;
import net.algem.util.FileUtil;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 * @since 2.8.m 03/09/13
 */
public class BasicEmployeeService
  implements EmployeeService, DocService
{

  private EmployeeIO employeeIO;
  private DataConnection dc;

  public BasicEmployeeService(DataConnection dc) {
    this.employeeIO = new EmployeeIO(dc);
    this.dc = dc;
  }

  @Override
  public Employee find(int idper) throws EmployeeException {
    try {
      return employeeIO.findId(idper);
    } catch (SQLException ex) {
      throw new EmployeeException(ex.getMessage());
    }
  }

  @Override
  public void create(final Employee e) throws EmployeeException {
     try {
      dc.withTransaction(new DataConnection.SQLRunnable<Void>() {
        @Override
        public Void run(DataConnection conn) throws Exception {
          employeeIO.insert(e);
          return null;
        }
      });
    } catch (Exception ex) {
      throw new EmployeeException(ex.getMessage());
    }
  }

  @Override
  public void update(final Employee e) throws EmployeeException {
    try {
      dc.withTransaction(new DataConnection.SQLRunnable<Void>() {
        @Override
        public Void run(DataConnection conn) throws Exception {
          employeeIO.update(e);
          return null;
        }
      });
    } catch (Exception ex) {
      throw new EmployeeException(ex.getMessage());
    }
  }

  @Override
  public void delete(Employee e) throws EmployeeException {
    try {
      employeeIO.delete(e.getIdPer());
    } catch (SQLException ex) {
      throw new EmployeeException(ex.getMessage());
    }
  }

  @Override
  public boolean checkNir(String insee) {
    if (insee == null || insee.length() < 15) {
      return false;
    }
    int key = Integer.parseInt(insee.substring(13));
    int cc = getKey(insee.substring(0,13));

    return cc == key;
  }

  /**
   * Calculates the key to a given insee number.
   * @param nir insee number (without key)
   * @return a 2-digits integer
   */
  int getKey(String nir) {

    StringBuilder nb = new StringBuilder(nir);
    String dept = nir.substring(5, 7).toUpperCase();
    if (dept.equals("2A")) {
      nb.replace(5, 7, "19");
    } else if (dept.equals("2B")) {
      nb.replace(5, 7, "18");
    }

    BigDecimal n = new BigDecimal(nb.toString());
    BigDecimal op = new BigDecimal(97);
//    (int) (97L - ( nir % 97L));
    return op.subtract(n.remainder(op)).intValue();
  }

  @Override
  public String getDocumentPath() {
    return FileUtil.getDocumentPath(ConfigKey.EMPLOYEES_PATH, dc);
  }

}
