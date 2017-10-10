package net.ariflaksito.controls;

import net.ariflaksito.models.Data;
import net.ariflaksito.models.Irigasi;

import java.util.List;

/**
 * Created by ariflaksito on 10/11/17.
 */

public interface InData {
    void add(Data i);

    void update(String key, Data i);

    void delete(String key);

    String get(String key);

    List<Data> get();
}
