/*
 * @(#)UserIO.java 2.11.0 27/09/16
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
package net.algem.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import net.algem.contact.Person;
import net.algem.contact.PersonIO;
import net.algem.room.EstablishmentIO;
import net.algem.util.DataConnection;
import net.algem.util.model.Cacheable;
import net.algem.util.model.TableIO;
import org.apache.commons.codec.binary.Base64;

/**
 * IO methods for class {@link net.algem.security.User}.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">jean-marc gobat</a>
 * @version 2.11.0
 * @since 1.0a 07/07/1999
 */
public class UserIO
  extends TableIO
  implements Cacheable {

  static final String TABLE = "login";
  static final String T_MENU = "menu2";
  static final String T_PROFILE = "menuprofil";
  static final String T_ACCESS = "menuaccess";
  static final String T_RIGHTS = "droits";
  private DataConnection dc;

  public UserIO(DataConnection dc) {
    this.dc = dc;
  }

  public void insert(User u) throws SQLException {

    String query = "INSERT INTO " + TABLE + " (idper,login,profil,pass,clef) VALUES(?,?,?,?,?)";
    PreparedStatement statement = dc.prepareStatement(query);
    statement.setInt(1, u.getId());
    statement.setString(2, u.getLogin());
    statement.setInt(3, u.getProfile());
    String b64pass = Base64.encodeBase64String(u.getPassInfo().getPass());
    statement.setString(4, b64pass);
    String b64salt = Base64.encodeBase64String(u.getPassInfo().getKey());
    statement.setString(5, b64salt);
    statement.executeUpdate();
    statement.close();

  }

  public void update(User u) throws SQLException {
    String query = "UPDATE " + TABLE + " SET login = ?, profil = ?, pass = ?, clef = ? WHERE idper = " + u.getId();
    UserPass pass = u.getPassInfo();
    String b64pass = null;
    String b64salt = null;
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setString(1, u.getLogin());
      ps.setInt(2, u.getProfile());

      if (pass != null) {
        b64pass = Base64.encodeBase64String(pass.getPass());
        b64salt = Base64.encodeBase64String(pass.getKey());
      }

      ps.setString(3, pass == null ? null : b64pass);
      ps.setString(4, pass == null ? null : b64salt);
      ps.executeUpdate();
    }

  }

  public void delete(final int userId) throws UserException {
    try {
      dc.withTransaction(new DataConnection.SQLRunnable<Void>() {

        @Override
        public Void run(DataConnection conn) throws Exception {
          String query = "DELETE FROM " + TABLE + " WHERE idper = " + userId;
          dc.executeUpdate(query);
          query = "DELETE FROM " + T_ACCESS + " WHERE idper = " + userId;
          dc.executeUpdate(query);
          return null;
        }
      });
    } catch (Exception ex) {
      throw new UserException(ex.getMessage(), "SUPPRESSION");
    }

  }

  /**
   * Menus access initialization.
   *
   * @param u user
   * @throws java.sql.SQLException
   */
  public void initMenus(User u) throws SQLException {
    String query = "SELECT id, auth FROM " + T_MENU + ", " + T_PROFILE + " WHERE id = idmenu AND profil = " + u.getProfile();
    String batchQuery = "INSERT INTO " + T_ACCESS + " VALUES(?,?,?)";//idper,idmenu,auth

    try (PreparedStatement ps = dc.prepareStatement(batchQuery); ResultSet rs = dc.executeQuery(query)) {
      while (rs.next()) {
        int id = rs.getInt(1);
        boolean b = rs.getBoolean(2);
        ps.setInt(1, u.getId());
        ps.setInt(2, id);
        ps.setBoolean(3, b);
        ps.addBatch();
      }
      ps.executeBatch();
    } 
  }

  /**
   * Rights initialization.
   *
   * @param u user
   * @throws java.sql.SQLException
   */
  public void initRights(User u) throws SQLException {
    String query = "SELECT relname FROM pg_class WHERE relkind = 'r' AND relname !~ '^pg' AND relname !~ '^sql_' AND relname !~ '^Inv'";
    String batchQuery = "INSERT INTO " + T_RIGHTS + " VALUES(?,?,?,?,?,?)";

    try (PreparedStatement ps = dc.prepareStatement(batchQuery); ResultSet rs = dc.executeQuery(query)) {
      while (rs.next()) {
        String table = rs.getString(1);
        ps.setInt(1, u.getId());
        ps.setString(2, table);
        ps.setBoolean(3, true);
        ps.setBoolean(4, false);
        ps.setBoolean(5, false);
        ps.setBoolean(6, false);
        ps.addBatch();
      }
      ps.executeBatch();
    } 
  }
  
  /**
   * Establishment active status initialization.
   * @param idper user id
   * @throws SQLException 
   */
  public void initEstabStatus(int idper) throws SQLException {
    String query = "INSERT INTO " + EstablishmentIO.TABLE + " SELECT p.id," + idper + ",true FROM personne p WHERE p.ptype = " + Person.ESTABLISHMENT;
    dc.executeUpdate(query);
  }
  
  public User findId(int n) throws SQLException {
    String query = "WHERE idper = " + n;
    List<User> v = find(query);

    return v.isEmpty() ? null : v.get(0);
  }

  public User findLogin(String login) throws SQLException {
    String query = "WHERE login = '" + login + "'";
    List<User> v = find(query);
    if (v.size() > 0) {
      return v.get(0);
    }
    return null;
  }

  public List<User> find(String where) throws SQLException {
    List<User> v = new Vector<User>();
    String query = "SELECT p.id,p.ptype,p.nom,p.prenom,p.civilite,u.login,u.profil,u.pass,u.clef FROM " + PersonIO.TABLE + " p, " + TABLE + " u ";
    if (where != null) {
      query += where + " AND p.id = u.idper";
    } else {
      query += " WHERE p.id = u.idper";
    }
    query += " ORDER BY p.nom, p.prenom";
    ResultSet rs = dc.executeQuery(query);

    while (rs.next()) {
      User u = new User();
      u.setId(rs.getInt(1));
      u.setType(rs.getShort(2));
      u.setName(rs.getString(3).trim());
      u.setFirstName(rs.getString(4).trim());
      u.setGender(rs.getString(5).trim());
      u.setLogin(rs.getString(6).trim());
      u.setProfile(rs.getInt(7));

      byte[] b64pass = Base64.decodeBase64(rs.getString(8));
      byte[] b64salt = Base64.decodeBase64(rs.getString(9));

      u.setPassInfo(new UserPass(b64pass, b64salt));
      v.add(u);
    }
    rs.close();
    return v;
  }

  @Override
  public List<User> load() throws SQLException {
    return find(null);
  }
}
