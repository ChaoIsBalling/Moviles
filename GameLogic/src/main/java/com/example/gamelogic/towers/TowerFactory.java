package com.example.gamelogic.towers;

import com.example.gamelogic.Tipos.TipoTorre;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.VisualElements.figure.Figure;

import java.util.HashMap;
import java.util.Map;

/**
 * Factoria para las torres (se usa en gameLogic a la hora de comprar una torre)
 * Cada vez que se quiera construir una, se llama a getTower
 */
public class TowerFactory {
    //Map que almacena los callbacks de contructoras de torres
    private final Map<TipoTorre, TowerCreator> creators = new HashMap<>();

    public TowerFactory() {
        // Registramos las "recetas" para cada tipo
        creators.put(TipoTorre.RAYO, (cx, cy, skin,figure) -> {
            if(skin != null)
                return new ThunderTower(cx,cy,skin);
            else
               return new ThunderTower(cx,cy,figure);
        });

        creators.put(TipoTorre.FUEGO, (cx, cy, skin,figure) ->
        {
            if(skin != null)
                return new FireTower(cx,cy,skin);
            else
                return new FireTower(cx,cy,figure);
        });

        creators.put(TipoTorre.HIELO, (cx, cy, skin,figure) -> {
            if(skin != null)
                return new IceTower(cx,cy,skin);
            else
                return new IceTower(cx,cy,figure);
        });

        creators.put(TipoTorre.MINI, (cx, cy, skin,figure) -> {
             return new MiniThunderTower(cx, cy, skin); // La mini siempre lleva skin
        });
    }

    /**
     * Crea la torre deseada pasandole sus parametros y el tipo
     * @param tipo Tipo de torre que se quiere crear
     * @param cx coordenada x
     * @param cy coordenada y
     * @param skin Skin de la torre (imagen)
     * @param figura Skin de la torre (figura)
     * @return
     */
    public Tower getTower(TipoTorre tipo, float cx, float cy, Image skin, Figure figura) {
        TowerCreator creator = creators.get(tipo);
        if (creator != null) {
            return creator.create(cx, cy, skin,figura);
        }
        return null; // O una torre por defecto
    }
}
