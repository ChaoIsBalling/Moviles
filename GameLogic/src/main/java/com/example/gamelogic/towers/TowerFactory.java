package com.example.gamelogic.towers;

import com.example.gamelogic.TipoTorre;
import com.example.gamelogic.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Factoria para las torres
 */
public class TowerFactory {
    private final Map<TipoTorre, TowerCreator> creators = new HashMap<>();

    public TowerFactory() {
        // Registramos las "recetas" para cada tipo
        creators.put(TipoTorre.RAYO, (cx, cy, skin) -> {
            if(skin != null)
                return new ThunderTower(cx,cy,skin);
            else
               return new ThunderTower(cx,cy);
        });

        creators.put(TipoTorre.FUEGO, (cx, cy, skin) ->
        {
            if(skin != null)
                return new FireTower(cx,cy,skin);
            else
                return new FireTower(cx,cy);
        });

        creators.put(TipoTorre.HIELO, (cx, cy, skin) -> {
            if(skin != null)
                return new IceTower(cx,cy,skin);
            else
                return new IceTower(cx,cy);
        });

        creators.put(TipoTorre.MINI, (cx, cy, skin) -> {
             return new MiniThunderTower(cx, cy, skin);
        }); // La mini siempre suele llevar skin
    }

    public Tower getTower(TipoTorre tipo, float cx, float cy, Image skin) {
        TowerCreator creator = creators.get(tipo);
        if (creator != null) {
            return creator.create(cx, cy, skin);
        }
        return null; // O una torre por defecto
    }
}
