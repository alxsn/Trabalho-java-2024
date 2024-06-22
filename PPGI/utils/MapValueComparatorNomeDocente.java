package utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Map;

import classes.Docente;

public class MapValueComparatorNomeDocente implements Comparator<Long>{
    Map<Long, Docente> base;

    public MapValueComparatorNomeDocente(Map<Long, Docente> base){
        this.base = base;
    }

    @Override
    public int compare(Long a, Long b){
        Collator c = Collator.getInstance();
        return c.compare(base.get(a).getNome(), base.get(b).getNome());
    }
}
