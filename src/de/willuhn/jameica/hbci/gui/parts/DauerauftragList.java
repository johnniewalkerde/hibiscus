/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/src/de/willuhn/jameica/hbci/gui/parts/DauerauftragList.java,v $
 * $Revision: 1.1 $
 * $Date: 2005/05/02 23:56:45 $
 * $Author: web0 $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.gui.parts;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.TableItem;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.Dauerauftrag;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.I18N;

/**
 * Implementierung einer fix und fertig vorkonfigurierten Liste der Dauerauftraege.
 */
public class DauerauftragList extends TablePart implements Part
{
  private I18N i18n = null;

  /**
   * @param action
   * @throws RemoteException
   */
  public DauerauftragList(Action action) throws RemoteException
  {
    super(Settings.getDBService().createList(Dauerauftrag.class), action);
    this.i18n = Application.getPluginLoader().getPlugin(HBCI.class).getResources().getI18N();
    setFormatter(new TableFormatter()
    {
      public void format(TableItem item)
      {
        try
        {
          if (item == null || item.getData() == null)
            return;
          Dauerauftrag d = (Dauerauftrag) item.getData();
          if (d.getLetzteZahlung() != null && new Date().after(d.getLetzteZahlung()))
            item.setForeground(Color.COMMENT.getSWTColor());
        }
        catch (Exception e)
        {
          Logger.error("error while checking finish date",e);
          GUI.getStatusBar().setErrorText(i18n.tr("Fehler beim Pr�fen des Ablaufdatums eines Dauerauftrages"));
        }
      }
    });
    addColumn(i18n.tr("Konto"),"konto_id");
    addColumn(i18n.tr("Empf�ngername"),"empfaenger_name");
    addColumn(i18n.tr("Empf�ngerkonto"),"empfaenger_konto");
    addColumn(i18n.tr("Verwendungszweck"),"zweck");
    addColumn(i18n.tr("Betrag"),"betrag", new CurrencyFormatter("",HBCI.DECIMALFORMAT));
    addColumn(i18n.tr("Turnus"),"turnus_id");
    addColumn(i18n.tr("aktiv?"),"orderid",new Formatter()
    {
      public String format(Object o)
      {
        if (o == null)
          return "nein";
        String s = o.toString();
        if (s != null && s.length() > 0)
          return i18n.tr("ja");
        return i18n.tr("nein");
      }
    });
    setContextMenu(new de.willuhn.jameica.hbci.gui.menus.DauerauftragList());
  }

}


/**********************************************************************
 * $Log: DauerauftragList.java,v $
 * Revision 1.1  2005/05/02 23:56:45  web0
 * @B bug 66, 67
 * @C umsatzliste nach vorn verschoben
 * @C protokoll nach hinten verschoben
 *
 **********************************************************************/