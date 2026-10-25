# CHECKPOINT — Modernización MFRockola Java 21

Fecha: 2026-10-13
Responsable: colaborador técnico MFRockola Java 21
Rama: `collab/mfrockola-java21`
Worktree: `/home/fam/00-MultiPlataforma/worktrees/mfrockola-java21`
Base: `7c1e0f91bf038329e3f2ef63bd124a947f6dc784`
HEAD de código certificado antes de esta documentación: `169763357b7a988d866d2831418a20b95802a5dd`

## Regla de integración

Este trabajo es candidato aislado. No hacer merge/rebase/cherry-pick a la rama principal ni sustituir la instancia utilizada actualmente sin revisión/autorización del líder.

## Baseline Java 8 certificado

- Java: BellSoft/OpenJDK `1.8.0_504`.
- Ant: 1.10.15.
- Fuentes Java baseline: 24.
- LOC Java aproximadas: 5009.
- Tipos declarados aproximados: 31.
- Dependencias JAR manuales baseline: 7.
- Build: `ant clean jar` OK.
- Bytecode: major 52.
- JAR baseline SHA-256: `c1e3786ae3ab13c2068d3444ef62257735c6135cf70cb8407aec7d23c737bc83`.
- Baseline externo: `/home/fam/DocumentacionProyectos/MFRockola/baseline/20261013-java8-7c1e0f9/`.

## Estado Java 21 alcanzado

- JDK certificado: Temurin `21.0.12.1 LTS`.
- Build Ant usa **`--release 21`** de forma verificable en log verbose.
- Bytecode: major 65.
- Build completo: OK.
- `-Xlint:deprecation -Xlint:unchecked`: 0 warnings en el último build certificado.
- `jdeps`: `java.base,java.desktop,java.sql,jdk.unsupported`.
- Java 8 no se desinstaló ni se modificó globalmente; continúa como rollback.

## Cambios estructurales realizados

### Toolchain

- Plataforma NetBeans/Ant migrada de `JDK_1.8` a `JDK_21`.
- Compilación modernizada a `--release 21`.
- Eliminado import CORBA obsoleto/no usado que impedía Java 21.

### VLCJ / multimedia

- VLCJ 3.3.0 -> VLCJ 4.12.1.
- `vlcj-natives` 4.12.0 añadido.
- JNA 3.4.0 / platform 4.0.0 -> JNA JPMS 5.16.0, alineado con vlcj-natives.
- Se mantiene libVLC 3.0.24 del sistema; no se migra a libVLC 4.
- `MediaPlayer` encapsula VLCJ; `Interface` ya no depende directamente de la API VLCJ.
- Callbacks nativos se entregan al EDT antes de tocar estado/UI.
- Avance manual ya no depende del evento `stopped` asíncrono.
- Liberación explícita de reproductores y factory.
- Corrección de MP3 sin vídeos promocionales.
- Corrección de descarte de formato inválido para que el debounce no pueda atascar la cola.

### Teclado / foco

- Eliminado el patrón de registrar el mismo `KeyListener` en varios componentes.
- Un `KeyEventDispatcher` único recibe teclas cuando `Interface` es la ventana activa.
- No captura teclas cuando el usuario hace Alt+Tab a otra aplicación.
- Dispatcher se desregistra al cerrar.
- Pendiente validación física de Alt+Tab/retorno en GNOME Wayland/Xwayland.

### Créditos

- Introducido `CreditManager` como lógica pura independiente de Swing.
- Prueba determinista `CreditManagerSmoke` -> `MFR_CREDIT_MANAGER_OK`.
- Corregido modo libre con `lockScreen=true`: ahora puede navegar/seleccionar correctamente.
- Evitado consumo de saldo en modo libre.
- Blindadas configuraciones de promociones/premios con divisor 0.
- Créditos promocionales añadidos se persisten.

### Rendimiento / threading

Se encontró una causa real de CPU innecesario en `Splash`: un `while (moveMouse)` sin espera ejecutaba `Robot.mouseMove()` continuamente.

Corrección:
- eliminado `Runnable`/thread adicional del splash;
- eliminado `Robot`;
- eliminado busy-loop;
- espera de arranque con `javax.swing.Timer` one-shot;
- shortcut Q con key binding;
- creación Swing en EDT mediante `SwingUtilities.invokeLater`.

Queda por medir CPU durante reproducción real. La capa multimedia aún contiene `--avcodec-hw=none`, que fuerza decodificación por software. No se cambia hasta probar gráficamente porque existen antecedentes de artefactos verdes con vídeo/hardware acceleration.

### Recursos / shutdown

Al cerrar `Interface`:
- detiene timers de etiquetas/fullscreen/cortesía;
- desregistra teclado;
- detiene/cierra `Clip` promocional;
- libera VLCJ players/factory;
- `Clip` promocional también se cierra al terminar.

### Dependencias

- JSON-java 2011 -> 20260814.
- SQLite JDBC 3.8.11.2 -> 3.53.4.0.
- SLF4J 1.7.25 no usado eliminado.
- Integración HTTP muerta que contenía credenciales/API embebidas retirada; el único consumidor creaba un `Thread` pero nunca lo iniciaba.

Ver `Documentacion/DEPENDENCIAS_JAVA21_20261013.md`.

## Pruebas automatizadas / de runtime pasadas

- Java 8 baseline build: OK.
- Java 21 build con `--release 21`: OK.
- Major class 65: OK.
- Lint de deprecación/unchecked: limpio.
- `MFR_CREDIT_MANAGER_OK`.
- `MFR_MEDIA_LIFECYCLE_OK`.
- VLCJ/JNA carga libVLC 3.0.24 en Java 21.
- `MFR_SQLITE_DB_OK sqlite=3.53.4` sobre **copia** de la BBDD y `PRAGMA integrity_check=ok`.
- `MFR_JSON_OK`.
- `jdeps`: `java.base,java.desktop,java.sql,jdk.unsupported`.

Último JAR de certificación temporal (HEAD `1697633`): SHA-256 `0f8704f6923026bb2426217e6ced435b5b9244d49433cbc5a232d8c6da13ff22`.

## Instancia LAB preparada para pruebas físicas

Ruta:
`/home/fam/DocumentacionProyectos/MFRockola/lab/java21-169763357b7a/`

Launcher:
`/home/fam/DocumentacionProyectos/MFRockola/lab/java21-169763357b7a/start-lab.sh`

JAR LAB SHA-256:
`d931f1cef77b486d79312124686d0ce7084e4439ec7bb2549939235d9df92e23`

Aislamiento:
- copia propia de `config.json`;
- copia propia de `canciones_populares.s3db`;
- `savedSongs=[]`, `savedCredits=0`;
- directorio promocional propio dentro del LAB;
- sólo la biblioteca de canciones existente se lee desde su ruta configurada;
- no sustituye ni modifica la instancia activa.

## Commits candidatos en orden

1. `7acaf29` — Preparar compilacion inicial con Java 21
2. `0519b9e` — Modernizar integracion VLCJ para Java 21
3. `43caa13` — Robustecer captura de teclado y foco
4. `2e46192` — Eliminar SLF4J heredado sin uso
5. `aa0bf2a` — Actualizar JSON-java y retirar version 2011
6. `bd79179` — Retirar integracion HTTP inactiva y credenciales heredadas
7. `862884e` — Compilar MFRockola con --release 21
8. `316902f` — Actualizar SQLite JDBC para Java 21
9. `ca7fd6e` — Eliminar warnings de tipos y API obsoleta
10. `e5ccff7` — Eliminar polling y Robot del arranque
11. `3c23c5b` — Robustecer transiciones de cola multimedia
12. `3af313b` — Separar logica de creditos y corregir modo libre
13. `1697633` — Cerrar recursos multimedia y timers limpiamente

## Pruebas físicas/gráficas pendientes y bloqueantes

Estas pruebas requieren usuario/escritorio/audio/vídeo real y por tanto son el siguiente gate antes de continuar con fullscreen/hardware acceleration/runtime final:

1. Arranque del LAB con Java 21.
2. MP3 con audio.
3. Vídeo con imagen + audio.
4. Playlist de al menos dos canciones.
5. Final natural MP3 y vídeo.
6. Error/archivo inválido y recuperación.
7. Secuencia **cortesía -> crédito -> canción -> fin**, verificando que no nazca otra cortesía inmediata indebida.
8. Crédito con foco en distintos componentes.
9. Alt+Tab fuera y regreso; verificar que MFRockola recupera teclado.
10. Fullscreen entrada/salida/retorno en GNOME Wayland/Xwayland.
11. Verificar que no aparece diálogo inesperado de escritorio remoto.
12. Medir CPU/memoria/threads en reposo, MP3 y vídeo.
13. Comparar vídeo con la actual política `--avcodec-hw=none`; sólo después decidir si habilitar VAAPI/hardware acceleration.

## Riesgos / pendientes técnicos

- Fullscreen actual es principalmente un modo de layout/video interno; aún no se ha refactorizado porque necesita validación gráfica para no reintroducir problemas Wayland/Xwayland.
- No crear `jlink` final hasta que MP3/vídeo/foco/fullscreen hayan pasado pruebas físicas. `jdeps` ya deja preparada la lista inicial de módulos.
- No habilitar aceleración por hardware sin prueba visual por antecedentes de artefactos verdes.
- No migrar a VLCJ 5/libVLC 4 mientras VLC 4 siga en desarrollo.

## Próximo paso recomendado

Ejecutar únicamente la instancia LAB y realizar la campaña física anterior. Con sus resultados, corregir fullscreen/hardware acceleration si procede; después certificar runtime autocontenido con `jlink` y campaña final de regresión.
