package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen extends BaseScreen {

    private Texture fondoTexture;
    private GlyphLayout layout;
    private BitmapFont fontTitulo;

    public GameOverScreen(GameLluvia game) {
        super(game);
    }

    @Override
    public void show() {
        fondoTexture = new Texture(Gdx.files.internal("fondo.png"));
        layout = new GlyphLayout();
        fontTitulo = new BitmapFont();
        fontTitulo.getData().setScale(3f);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
    }

    @Override
    protected void update(float delta) {
    }

    @Override
    protected void draw(SpriteBatch batch) {
        batch.draw(fondoTexture, 0, 0, 800, 480);
    }

    @Override
    protected void drawUI(SpriteBatch batch) {
        GameManager gm = GameManager.getInstance();

        // titulo grande centrado
        String titulo = "GAME OVER";
        layout.setText(fontTitulo, titulo);
        fontTitulo.draw(batch, titulo, (800 - layout.width) / 2, 350);

        String msg = "Has sido devorado!";
        layout.setText(font, msg);
        font.draw(batch, msg, (800 - layout.width) / 2, 280);

        String puntos = "Puntaje final: " + gm.getPuntaje();
        layout.setText(font, puntos);
        font.draw(batch, puntos, (800 - layout.width) / 2, 245);

        String nivel = "Nivel alcanzado: " + gm.getNivel();
        layout.setText(font, nivel);
        font.draw(batch, nivel, (800 - layout.width) / 2, 210);

        String instruccion = "Presiona ENTER para volver al menu";
        layout.setText(font, instruccion);
        font.draw(batch, instruccion, (800 - layout.width) / 2, 130);
    }

    @Override
    public void dispose() {
        if (fondoTexture != null) fondoTexture.dispose();
        if (fontTitulo != null) fontTitulo.dispose();
    }
}