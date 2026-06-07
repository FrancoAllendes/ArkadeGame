# Videojuego (GM) - Arkade

**Asignatura:** INF2236-1 - Programación Avanzada  
**Periodo:** 2026-1  
**Institución:** Pontificia Universidad Católica de Valparaíso  

## Descripción del Proyecto
Arkade es un videojuego de supervivencia desarrollado en Java con el motor LibGDX, inspirado en el universo de ARK: Survival Evolved. El jugador controla a un superviviente que debe recolectar recursos (bayas, carne, metal) que caen del cielo mientras esquiva ataques de dinosaurios. El juego cuenta con un sistema de niveles progresivo, drops especiales de vida y escudo, un dinosaurio aliado con efecto imán, una zona secreta (Tek Cave) y un menú de pausa con control de volumen.

## Características Principales
* Sistema de niveles con dificultad progresiva.
* 6 tipos de drops: recurso, peligro, vida, escudo, supply drop y elemento.
* Dinosaurio aliado temporal que recolecta recursos automáticamente.
* Zona secreta Tek Cave al recolectar 3 supply drops.
* Menú de pausa con control de volumen.
* 3 patrones de movimiento para enemigos (normal, zigzag, rápido).
* 4 patrones de diseño: Singleton, Strategy, Template Method y Builder.

## Tecnologías Utilizadas
* **Lenguaje:** Java (JDK 8)
* **IDE:** Eclipse IDE for Java Developers
* **Motor:** LibGDX 1.12.1
* **Build System:** Gradle

## Controles
* **← →** Mover al superviviente.
* **ESC** Pausar / reanudar el juego.
* **↑ ↓ (en pausa)** Subir / bajar volumen.
* **ENTER** Iniciar juego / volver al menú.

## Instrucciones de Instalación y Ejecución

### Requisitos previos
* Tener instalado **Eclipse IDE for Java Developers**.
* Tener instalado **Oracle JDK 8** o superior.
* Tener instalado el plugin **Buildship Gradle Integration** en Eclipse (viene preinstalado en versiones recientes).

### Paso 1 — Descomprimir el proyecto
1. Descomprimir el archivo `.zip` del proyecto en una carpeta de fácil acceso (por ejemplo, el Escritorio).

### Paso 2 — Importar en Eclipse
1. Abrir Eclipse IDE.
2. Ir a `File` > `Import`.
3. Seleccionar `Gradle` > `Existing Gradle Project` y hacer clic en `Next`.
4. En `Project root directory`, hacer clic en `Browse` y seleccionar la carpeta descomprimida del proyecto.
5. Hacer clic en `Finish` y esperar a que Eclipse descargue las dependencias automáticamente (requiere conexión a internet, puede tardar unos minutos).

### Paso 3 — Ejecutar el juego
1. En el `Package Explorer`, expandir el módulo `lwjgl3`.
2. Navegar a `src/main/java` > `puppy.code.lwjgl3`.
3. Hacer clic derecho sobre la clase `Lwjgl3Launcher.java`.
4. Seleccionar `Run As` > `Java Application`.
5. Se abrirá la ventana del juego mostrando el menú principal.

### Solución de problemas comunes
* **Error de dependencias:** Hacer clic derecho sobre el proyecto padre > `Gradle` > `Refresh Gradle Project`.
* **Error de compilación:** Ir a `Project` > `Clean` > seleccionar todos los proyectos > `Clean`.
* **Error de assets no encontrados:** Verificar que la carpeta `assets` contenga todos los archivos de imagen y sonido necesarios.

## Estructura del Proyecto
* `core/src/main/java/puppy/code/` — Clases principales del juego (21 clases).
* `lwjgl3/` — Módulo de lanzamiento para escritorio.
* `assets/` — Imágenes, sonidos y música del juego.

## Autores
* Franco Allendes
* Francisco Ceballos
* Lucas Salamanca