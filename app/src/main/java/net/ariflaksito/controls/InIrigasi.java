package net.ariflaksito.controls;

import net.ariflaksito.models.Irigasi;

import java.util.List;

/**
 * Created by ariflaksito on 10/11/17.
 */

public interface InIrigasi {

    void add(Irigasi i);

    void update(String key, Irigasi i);

    void delete(String key);

    String get(String key);

    List<Irigasi> get();
}
