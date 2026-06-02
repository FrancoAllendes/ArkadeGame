package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa un Supply Drop (caja de suministros).
 * Extiende DropItem. Cae lento y otorga bonus especial.
 * Al recolectar 3, se activa la zona secreta Tek Cave.
 */
public class SupplyDrop extends DropItem {

    private Sound sonidoRecoleccion;
    private int puntosOtorgados;

    public SupplyDrop(Texture texture, float velocidadY, Sound sonidoRecoleccion, int puntosOtorgados) {
        super(texture, velocidadY);
        this.sonidoRecoleccion = sonidoRecoleccion;
        this.puntosOtorgados = puntosOtorgados;
        // cae mas lento que los demas
        setMovementStrategy(new NormalFall());
    }

    @Override
    public void onPlayerCollision(Survivor survivor) {
        survivor.sumarPuntos(puntosOtorgados);
        sonidoRecoleccion.play();
    }

    public Sound getSonidoRecoleccion() { return sonidoRecoleccion; }
    public void setSonidoRecoleccion(Sound sonidoRecoleccion) { this.sonidoRecoleccion = sonidoRecoleccion; }
    public int getPuntosOtorgados() { return puntosOtorgados; }
    public void setPuntosOtorgados(int puntosOtorgados) { this.puntosOtorgados = puntosOtorgados; }
}