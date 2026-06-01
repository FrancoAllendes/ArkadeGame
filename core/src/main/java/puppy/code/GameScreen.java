package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Pantalla principal donde ocurre el juego.
 * Extiende BaseScreen aplicando Template Method (GM-8).
 */
public class GameScreen extends BaseScreen {

    private Survivor survivor;
    private Array<DropItem> drops;
    private long lastDropTime;

    // texturas
    private Texture survivorTexture;
    private Texture recursoTexture;
    private Texture carneTexture;
    private Texture metalTexture;
    private Texture peligroTexture;
    private Texture vidaTexture;
    private Texture escudoTexture;
    private Texture fondoTexture;

    // sonidos
    private Sound sonidoRecolectar;
    private Sound sonidoDanio;
    private Sound sonidoVida;
    private Sound sonidoEscudo;
    private Music musicaFondo;

    // pausa y volumen
    private boolean pausado;
    private float volumen;
    private GlyphLayout layout;

    public GameScreen(GameLluvia game) {
        super(game);
    }

    @Override
    public void show() {
        fondoTexture = new Texture(Gdx.files.internal("fondo.png"));
        survivorTexture = new Texture(Gdx.files.internal("survivor.png"));
        recursoTexture = new Texture(Gdx.files.internal("recurso.png"));
        carneTexture = new Texture(Gdx.files.internal("carne.png"));
        metalTexture = new Texture(Gdx.files.internal("metal.png"));
        peligroTexture = new Texture(Gdx.files.internal("peligro.png"));
        vidaTexture = new Texture(Gdx.files.internal("vida.png"));
        escudoTexture = new Texture(Gdx.files.internal("escudo.png"));

        sonidoRecolectar = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoDanio = Gdx.audio.newSound(Gdx.files.internal("danio.mp3"));
        sonidoVida = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoEscudo = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

        volumen = 0.5f;
        musicaFondo.setVolume(volumen);
        musicaFondo.setLooping(true);
        musicaFondo.play();

        pausado = false;
        layout = new GlyphLayout();

        survivor = new Survivor(survivorTexture, sonidoDanio);
        drops = new Array<>();
        spawnDrop();
    }

    private void spawnDrop() {
        GameManager gm = GameManager.getInstance();
        DropItem drop;
        int random = MathUtils.random(1, 100);

        if (random <= 5) {
            drop = DropBuilder.crearEscudo(escudoTexture, gm.getVelocidadBase(), sonidoEscudo);
        } else if (random <= 15) {
            drop = DropBuilder.crearVida(vidaTexture, gm.getVelocidadBase(), sonidoVida);
        } else if (random <= 40) {
            drop = DropBuilder.crearPeligro(peligroTexture, gm.getVelocidadBase());
        } else {
            Texture[] recursos = {recursoTexture, carneTexture, metalTexture};
            Texture recursoAleatorio = recursos[MathUtils.random(0, 2)];
            drop = DropBuilder.crearRecurso(recursoAleatorio, gm.getVelocidadBase(), sonidoRecolectar);
        }

        drops.add(drop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    protected void handleInput() {
        // ESC para pausar/reanudar
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pausado = !pausado;
            if (pausado) {
                musicaFondo.pause();
            } else {
                musicaFondo.play();
            }
        }

        // controles de volumen en pausa
        if (pausado) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                volumen = Math.min(volumen + 0.1f, 1.0f);
                musicaFondo.setVolume(volumen);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                volumen = Math.max(volumen - 0.1f, 0.0f);
                musicaFondo.setVolume(volumen);
            }
        }
    }

    @Override
    protected void update(float delta) {
        // no actualizar logica si esta pausado
        if (pausado) return;

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

        if (survivor.estaInvencible()) {
            String escudo = "ESCUDO ACTIVO!";
            layout.setText(font, escudo);
            font.draw(batch, escudo, (800 - layout.width) / 2, 450);
        }

        // menu de pausa
        if (pausado) {
            String pausa = "PAUSA";
            layout.setText(font, pausa);
            font.draw(batch, pausa, (800 - layout.width) / 2, 300);

            int porcentaje = Math.round(volumen * 100);
            String vol = "Volumen: " + porcentaje + "%";
            layout.setText(font, vol);
            font.draw(batch, vol, (800 - layout.width) / 2, 260);

            // barra de volumen visual
            String barra = "[";
            int bloques = Math.round(volumen * 10);
            for (int i = 0; i < 10; i++) {
                barra += (i < bloques) ? "=" : " ";
            }
            barra += "]";
            layout.setText(font, barra);
            font.draw(batch, barra, (800 - layout.width) / 2, 235);

            String controles = "Flechas UP/DOWN: volumen";
            layout.setText(font, controles);
            font.draw(batch, controles, (800 - layout.width) / 2, 200);

            String reanudar = "Presiona ESC para continuar";
            layout.setText(font, reanudar);
            font.draw(batch, reanudar, (800 - layout.width) / 2, 160);
        }
    }

    @Override
    public void dispose() {
        musicaFondo.dispose();
        sonidoRecolectar.dispose();
        sonidoDanio.dispose();
        sonidoVida.dispose();
        sonidoEscudo.dispose();
        survivorTexture.dispose();
        recursoTexture.dispose();
        carneTexture.dispose();
        metalTexture.dispose();
        peligroTexture.dispose();
        vidaTexture.dispose();
        escudoTexture.dispose();
        fondoTexture.dispose();
    }
}