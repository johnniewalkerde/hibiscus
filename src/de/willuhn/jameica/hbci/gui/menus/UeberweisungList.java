/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/src/de/willuhn/jameica/hbci/gui/menus/UeberweisungList.java,v $
 * $Revision: 1.21 $
 * $Date: 2012/01/27 22:43:22 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.hbci.gui.menus;

import java.rmi.RemoteException;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.internal.action.Print;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.gui.action.DBObjectDelete;
import de.willuhn.jameica.hbci.gui.action.Duplicate;
import de.willuhn.jameica.hbci.gui.action.TerminableMarkExecuted;
import de.willuhn.jameica.hbci.gui.action.UeberweisungExecute;
import de.willuhn.jameica.hbci.gui.action.UeberweisungExport;
import de.willuhn.jameica.hbci.gui.action.UeberweisungImport;
import de.willuhn.jameica.hbci.gui.action.UeberweisungMerge;
import de.willuhn.jameica.hbci.gui.action.UeberweisungNew;
import de.willuhn.jameica.hbci.io.print.PrintSupportUeberweisungList;
import de.willuhn.jameica.hbci.rmi.Ueberweisung;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Kontext-Menu, welches an Listen mit Ueberweisungen gehangen werden kann.
 * Es ist fix und fertig vorkonfiguriert und mit Elementen gefuellt.
 */
public class UeberweisungList extends ContextMenu
{
	private final static I18N i18n = Application.getPluginLoader().getPlugin(HBCI.class).getResources().getI18N();

	  /**
	 * Erzeugt ein Kontext-Menu fuer eine Liste von Ueberweisungen.
	 */
	public UeberweisungList()
	{
		addItem(new SingleItem(i18n.tr("�ffnen"), new UeberweisungNew(),"document-open.png"));
    addItem(new ContextMenuItem(i18n.tr("Neue �berweisung..."), new UNeu(),"text-x-generic.png"));
    addItem(new CheckedContextMenuItem(i18n.tr("L�schen..."), new DBObjectDelete(),"user-trash-full.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new SingleItem(i18n.tr("Duplizieren..."), new Duplicate(),"edit-copy.png"));
    addItem(new NotActiveMultiMenuItem(i18n.tr("Zu Sammel�berweisung zusammenfassen..."), new UeberweisungMerge(),"stock_navigator-shift-right.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new NotActiveSingleMenuItem(i18n.tr("Jetzt ausf�hren..."), new UeberweisungExecute(),"emblem-important.png"));
    addItem(new NotActiveMultiMenuItem(i18n.tr("Als \"ausgef�hrt\" markieren..."), new TerminableMarkExecuted(),"emblem-default.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new CheckedContextMenuItem(i18n.tr("Drucken..."),new Action() {
      public void handleAction(Object context) throws ApplicationException
      {
        new Print().handleAction(new PrintSupportUeberweisungList(context));
      }
    },"document-print.png"));
    addItem(new CheckedContextMenuItem(i18n.tr("Exportieren..."),new UeberweisungExport(),"document-save.png"));
    addItem(new ContextMenuItem(i18n.tr("Importieren..."),new UeberweisungImport(),"document-open.png"));
		
	}

  /**
   * Ueberschrieben, um zu pruefen, ob ein Array oder ein einzelnes Element markiert ist.
   */
  private class SingleItem extends CheckedContextMenuItem
  {
    /**
     * @param text
     * @param action
     * @param optionale Angabe eines Icons.
     */
    private SingleItem(String text, Action action, String icon)
    {
      super(text,action,icon);
    }
    /**
     * @see de.willuhn.jameica.gui.parts.ContextMenuItem#isEnabledFor(java.lang.Object)
     */
    public boolean isEnabledFor(Object o)
    {
      if (o instanceof Ueberweisung[])
        return false;
      return super.isEnabledFor(o);
    }
  }

	/**
	 * Ueberschreiben wir, um <b>grundsaetzlich</b> eine neue Ueberweisung
	 * anzulegen - auch wenn der Focus auf einer existierenden liegt.
   */
  private class UNeu extends UeberweisungNew
	{
    /**
     * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
     */
    public void handleAction(Object context) throws ApplicationException
    {
    	super.handleAction(null);
    }
	} 
	
	/**
	 * Ueberschreiben wir, damit das Item nur dann aktiv ist, wenn die
	 * Ueberweisung noch nicht ausgefuehrt wurde.
   */
  private class NotActiveSingleMenuItem extends CheckedContextMenuItem
	{
		
    /**
     * ct.
     * @param text anzuzeigender Text.
     * @param a auszufuehrende Action.
     * @param icon optionales Icon.
     */
    public NotActiveSingleMenuItem(String text, Action a, String icon)
    {
      super(text, a, icon);
    }

	  /**
     * @see de.willuhn.jameica.gui.parts.ContextMenuItem#isEnabledFor(java.lang.Object)
     */
    public boolean isEnabledFor(Object o)
    {
      if (o == null || !(o instanceof Ueberweisung))
        return false;

      try
    	{
        if (o instanceof Ueberweisung[])
          return false;
        Ueberweisung u = (Ueberweisung) o;
        return !u.ausgefuehrt();
    	}
    	catch (Exception e)
    	{
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Pr�fen, ob Auftrag bereits ausgef�hrt wurde"),StatusBarMessage.TYPE_ERROR));
    		Logger.error("error while enable check in menu item",e);
    	}
    	return false;
    }
	}

  /**
   * Liefert nur dann true, wenn alle uebergebenen Ueberweisungen noch nicht
   * ausgefuehrt wurden.
   */
  private class NotActiveMultiMenuItem extends CheckedContextMenuItem
  {
    
    /**
     * ct.
     * @param text anzuzeigender Text.
     * @param a auszufuehrende Action.
     * @param icon optionales Icon.
     */
    public NotActiveMultiMenuItem(String text, Action a, String icon)
    {
      super(text, a, icon);
    }

    /**
     * @see de.willuhn.jameica.gui.parts.ContextMenuItem#isEnabledFor(java.lang.Object)
     */
    public boolean isEnabledFor(Object o)
    {
      if (o == null || (!(o instanceof Ueberweisung) && !(o instanceof Ueberweisung[])))
        return false;
      try
      {
        if (o instanceof Ueberweisung)
          return !((Ueberweisung)o).ausgefuehrt();

        Ueberweisung[] t = (Ueberweisung[]) o;
        for (int i=0;i<t.length;++i)
        {
          if (t[i].ausgefuehrt())
            return false;
        }
        return true;
      }
      catch (RemoteException e)
      {
        Logger.error("unable to check if terminable is allready executed",e);
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Pr�fen, ob Auftrag bereits ausgef�hrt wurde"),StatusBarMessage.TYPE_ERROR));
      }
      return false;
    }
  }

}


/**********************************************************************
 * $Log: UeberweisungList.java,v $
 * Revision 1.21  2012/01/27 22:43:22  willuhn
 * @N BUGZILLA 1181
 *
 * Revision 1.20  2011-04-11 11:28:08  willuhn
 * @N Drucken aus dem Contextmenu heraus
 *
 * Revision 1.19  2009/11/26 12:00:21  willuhn
 * @N Buchungen aus Sammelauftraegen in Einzelauftraege duplizieren
 *
 * Revision 1.18  2009/02/13 14:17:01  willuhn
 * @N BUGZILLA 700
 *
 * Revision 1.17  2008/12/19 01:12:09  willuhn
 * @N Icons in Contextmenus
 *
 * Revision 1.16  2007/12/06 23:53:35  willuhn
 * @C Menu-Eintraege uebersichtlicher angeordnet
 *
 * Revision 1.15  2007/10/25 15:47:21  willuhn
 * @N Einzelauftraege zu Sammel-Auftraegen zusammenfassen (BUGZILLA 402)
 **********************************************************************/