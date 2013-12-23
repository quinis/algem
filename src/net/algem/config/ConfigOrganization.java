/*
 * @(#)ConfigOrganization.java 2.8.p 12/11/13
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

package net.algem.config;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import net.algem.contact.Address;
import net.algem.contact.AddressView;
import net.algem.util.BundleUtil;
import net.algem.util.MessageUtil;
import net.algem.util.ui.GemField;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;

/**
 * Organization parameters and contact.
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.p
 * @since 2.2.p 23/01/12
 */
public class ConfigOrganization
        extends ConfigPanel
{

  private Config c1, c2, c3, c4, c5, c6, c7, c8, c9;
  
  /** Organization name. */
  private GemField name;
  
  private JFormattedTextField siret;
  private JFormattedTextField naf;

  /**
   * Code TVA intra-communautaire.
   * Pour la France, il est composé des lettres FR, complétées d'une clé de deux chiffres
   * ou lettres attribuée par le centre des impôts du lieu d'exercice de l'entreprise,
   * et du numéro SIREN à 9 chiffres. Par exemple pour l'entreprise déjà citée : FR 83 404 833 048.
   * La clé française suit la règle suivante :
   * Clé TVA = [ 12 + 3 * ( SIREN modulo 97 ) ] modulo 97
   */
  private JFormattedTextField tva;

  /**
   * Code Formation professionnelle.
   */
  private JFormattedTextField forPro;

  /** Address */
  private AddressView address;
  
  public ConfigOrganization(String title, Map<String, Config> cm) {
    super(title, cm);
    init();
  }

  private void init() {
    c1 = confs.get(ConfigKey.ORGANIZATION_NAME.getKey());
    c2 = confs.get(ConfigKey.ORGANIZATION_ADDRESS1.getKey());
    c3 = confs.get(ConfigKey.ORGANIZATION_ADDRESS2.getKey());
    c4 = confs.get(ConfigKey.ORGANIZATION_ZIPCODE.getKey());
    c5 = confs.get(ConfigKey.ORGANIZATION_CITY.getKey());
    c6 = confs.get(ConfigKey.SIRET_NUMBER.getKey());
    c7 = confs.get(ConfigKey.CODE_NAF.getKey());
    c8 = confs.get(ConfigKey.CODE_TVA.getKey());
    c9 = confs.get(ConfigKey.CODE_FP.getKey());

    content = new GemPanel(new BorderLayout());

    GemPanel pName = new GemPanel();
    pName.add(new GemLabel(BundleUtil.getLabel("Name.label")));
    name = new GemField(20);
    name.setText(c1.getValue());

    MaskFormatter siretMask = MessageUtil.createFormatter("### ### ### #####");
    siretMask.setValueContainsLiteralCharacters(false);
    siret = new JFormattedTextField(siretMask);
    siret.setColumns(14);
    siret.setValue(c6.getValue());

    naf = new JFormattedTextField(MessageUtil.createFormatter("AAAAA"));
    naf.setColumns(5);
    naf.setValue(c7.getValue());

    MaskFormatter tvaMask = MessageUtil.createFormatter("AA AA AAA AAA AAA AAAAA");
    tvaMask.setValueContainsLiteralCharacters(false);

    tva = new JFormattedTextField(tvaMask);
    tva.setColumns(18);
    tva.setValue(c8.getValue());
    
    MaskFormatter forProMask = MessageUtil.createFormatter("## ## ##### ##");
    forProMask.setValueContainsLiteralCharacters(false);

    forPro = new JFormattedTextField(forProMask);
    forPro.setColumns(11);
    forPro.setValue(c9.getValue());

    pName.add(name);

    address = new AddressView(null,false);

    Address a = new Address();
    a.setAdr1(c2.getValue());
    a.setAdr2(c3.getValue());
    a.setCdp(c4.getValue());
    a.setCity(c5.getValue());

    address.set(a);

    GemPanel pCodes = new GemPanel(new GridLayout(4,2));
    pCodes.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    pCodes.add(new GemLabel(ConfigKey.SIRET_NUMBER.getLabel()));
    pCodes.add(siret);
    pCodes.add(new GemLabel(ConfigKey.CODE_NAF.getLabel()));
    pCodes.add(naf);
    pCodes.add(new GemLabel(ConfigKey.CODE_TVA.getLabel()));
    pCodes.add(tva);
    pCodes.add(new GemLabel(ConfigKey.CODE_FP.getLabel()));
    pCodes.add(forPro);

    content.add(pName, BorderLayout.NORTH);
    content.add(address, BorderLayout.CENTER);
    content.add(pCodes, BorderLayout.SOUTH);

    add(content);
  }

  //TODO verify null values
  @Override
  public List<Config> get() {
    List<Config> conf = new ArrayList<Config>();
    Address b = address.get();
    c1.setValue(name.getText().trim());
    c2.setValue(b.getAdr1().trim());
    c3.setValue(b.getAdr2().trim());
    c4.setValue(b.getCdp().trim());
    c5.setValue(b.getCity().trim());
    c6.setValue((String)siret.getValue());
    c7.setValue(naf.getText());
    c8.setValue((String)tva.getValue());
    c9.setValue((String)forPro.getValue());

    conf.add(c1);
    conf.add(c2);
    conf.add(c3);
    conf.add(c4);
    conf.add(c5);
    conf.add(c6);
    conf.add(c7);
    conf.add(c8);
    conf.add(c9);

    return conf;
  }

}


