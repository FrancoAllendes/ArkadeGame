package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa un drop que restaura una vida al Survivor.
 * Extiende DropItem. Aparece con poca frecuencia.
 */
public class HealDrop extends DropItem {

    private Sound sonidoCuracion;

    /**
     * Constructor de HealDrop.
     * @param texture textura del recurso curativo
     * @param velocidadY velocidad de caida
     * @param sonidoCuracion sonido al ser recolectado
     */
    public HealDrop(Texture texture, float velocidadY, Sound sonidoCuracion) {
        super(texture, velocidadY);
        this.sonidoCuracion = sonidoCuracion;
    }

    @Override
    public void onPlayerCollision(Survivor survivor) {
        survivor.curar();
        sonidoCuracion.play();
    }

    // ==================== Getters y Setters (GM-5) ====================

    public Sound getSonidoCuracion() {
        return sonidoCuracion;
    }

    public void setSonidoCuracion(Sound sonidoCuracion) {
        this.sonidoCuracion = sonidoCuracion;
    }
}