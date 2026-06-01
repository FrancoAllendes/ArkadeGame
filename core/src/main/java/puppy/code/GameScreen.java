package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Pantalla principal donde ocurre el juego.
 * Extiende BaseScreen aplicando Template Method.
 */
public class GameScreen extends BaseScreen {

    private Survivor survivor;
    private Array<DropItem> drops;
    private long lastDropTime;

    // texturas
    private Texture survivorTexture;
    private Texture recursoTexture;
    private Texture peligroTexture;
    private Texture fondoTexture;

    // sonidos
    private Sound sonidoRecolectar;
    private Sound sonidoDanio;
    private Music musicaFondo;

    public GameScreen(GameLluvia game) {
        super(game);
    }

    @Override
    public void show() {
        // cargar texturas
        fondoTexture = new Texture(Gdx.files.internal("fondo.png"));
        survivorTexture = new Texture(Gdx.files.internal("survivor.png"));
        recursoTexture = new Texture(Gdx.files.internal("recurso.png"));
        peligroTexture = new Texture(Gdx.files.internal("peligro.png"));

        // cargar sonidos
        sonidoRecolectar = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoDanio = Gdx.audio.newSound(Gdx.files.internal("danio.mp3"));
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

        // iniciar musica de fondo
        musicaFondo.setLooping(true);
        musicaFondo.play();

        // crear survivor
        survivor = new Survivor(survivorTexture, sonidoDanio);

        // crear lista de drops
        drops = new Array<>();
        spawnDrop();
    }

    private void spawnDrop() {
        GameManager gm = GameManager.getInstance();
        DropItem drop;

        if (MathUtils.random(1, 10) < 3) {
            drop = DropBuilder.crearPeligro(peligroTexture, gm.getVelocidadBase());
        } else {
            drop = DropBuilder.crearRecurso(recursoTexture, gm.getVelocidadBase(), sonidoRecolectar);
        }

        drops.add(drop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    protected void handleInput() {
    }

    @Override
    protected void update(float delta) {
        GameManager gm = GameManager.getInstance();
        survivor.update();

        if (TimeUtils.nanoTime() - lastDropTime > gm.getIntervaloSpawn()) {
            spawnDrop();
        }

        for (int i = 0; i < drops.size; i++) {
            DropItem drop = drops.get(i);
            drop.update();

            if (drop.fueraDePantalla()) {
                drops.removeIndex(i);
                i--;
                continue;
            }

            if (drop.checkCollision(survivor)) {
                drop.onPlayerCollision(survivor);
                gm.setPuntaje(survivor.getPuntos());
                gm.setVidas(survivor.getVidas());
                drops.removeIndex(i);
                i--;
            }
        }

        if (!survivor.estaVivo()) {
            musicaFondo.stop();
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // dibujar fondo cubriendo toda la pantalla
        batch.draw(fondoTexture, 0, 0, 800, 480);

        survivor.draw(batch);

        for (DropItem drop : drops) {
            drop.draw(batch);
        }
    }

    @Override
    protected void drawUI(SpriteBatch batch) {
        GameManager gm = GameManager.getInstance();
        font.draw(batch, "Puntos: " + gm.getPuntaje(), 10, 470);
        font.draw(batch, "Vidas: " + gm.getVidas(), 710, 470);
        font.draw(batch, "Nivel: " + gm.getNivel(), 370, 470);
    }

    @Override
    public void dispose() {
        musicaFondo.dispose();
        sonidoRecolectar.dispose();
        sonidoDanio.dispose();
        survivorTexture.dispose();
        recursoTexture.dispose();
        peligroTexture.dispose();
        fondoTexture.dispose();
    }
}