package upbringkingdom.screen;

import Model.YSortable;
import java.util.Comparator;

//classe che permette di comparare due oggetti per mostrare l'oggetto che è sotto, sopra quello che sta dietro di lui
public class WorldObjectYComparator implements Comparator<YSortable>{

    @Override
	public int compare(YSortable o1, YSortable o2) {
		if (o1.getWorldY() < o2.getWorldY()) {
			return -1;
		} else if (o1.getWorldY() > o2.getWorldY()) {
			return 1;
		}
		return 0;
	}
    
}
