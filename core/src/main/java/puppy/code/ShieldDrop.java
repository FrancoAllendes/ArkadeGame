package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa un drop que otorga invencibilidad temporal al Survivor.
 * Extiende DropItem. Aparece con muy poca frecuencia.
 */
public class ShieldDrop extends DropItem {

    private Sound sonidoEscudo;
    private float duracionEscudo;

    /**
     * Constructor de ShieldDrop.
     * @param texture textura del escudo
     * @param velocidadY velocidad de caida
     * @param sonidoEscudo sonido al activar el escudo
     * @param duracionEscudo duracion de la invencibilidad en segundos
     */
    public ShieldDrop(Texture texture, float velocidadY, Sound sonidoEscudo, float duracionEscudo) {
        super(texture, velocidadY);
        this.sonidoEscudo = sonidoEscudo;
        this.duracionEscudo = duracionEscudo;
    }

    @Override
    public void onPlayerCollision(Survivor survivor) {
        survivor.activarEscudo(duracionEscudo);
        sonidoEscudo.play();
    }

    // ==================== Getters y Setters (GM-5) ====================

    public Sound getSonidoEscudo() {
        return sonidoEscudo;
    }

    public void setSonidoEscudo(Sound sonidoEscudo) {
        this.sonidoEscudo = sonidoEscudo;
    }

    public float getDuracionEscudo() {
        return duracionEscudo;
    }

    public void setDuracionEscudo(float duracionEscudo) {
        this.duracionEscudo = duracionEscudo;
    }
}