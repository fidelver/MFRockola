#!/usr/bin/env python3

from pathlib import Path
import shutil
import sys

BASE = Path("/home/fam/NetBeansProjects/MFRockola")
LISTMUSIC = BASE / "src/com/mfrockola/classes/ListMusic.java"
MEDIAPLAYER = BASE / "src/com/mfrockola/classes/MediaPlayer.java"


def backup(path):
    backup_path = path.with_suffix(path.suffix + ".backup-promovideos")
    if not backup_path.exists():
        shutil.copy2(path, backup_path)
        print(f"[OK] Backup: {backup_path}")
    else:
        print(f"[INFO] Backup ya existe: {backup_path}")


def replace_once(path, old, new, description):
    text = path.read_text(encoding="utf-8")

    count = text.count(old)

    if count == 0:
        print(f"[ERROR] No se encontró el bloque para: {description}")
        return False

    if count > 1:
        print(f"[ERROR] El bloque aparece {count} veces en {path}")
        return False

    text = text.replace(old, new, 1)
    path.write_text(text, encoding="utf-8")

    print(f"[OK] Modificado: {path}")
    print(f"     {description}")
    return True


def main():
    print("==============================================")
    print(" Corrección de videos promocionales MFRockola")
    print("==============================================")

    for path in (LISTMUSIC, MEDIAPLAYER):
        if not path.exists():
            print(f"[ERROR] No existe: {path}")
            sys.exit(1)

    backup(LISTMUSIC)
    backup(MEDIAPLAYER)

    # ------------------------------------------------------------
    # ListMusic.java
    # ------------------------------------------------------------

    old_listmusic = """    public String getPromVideo() {
        String promVideo = promVideos[random.nextInt(promVideos.length)];
        return promVideo;
    }
"""

    new_listmusic = """    public String getPromVideo() {
        if (promVideos == null || promVideos.length == 0) {
            return null;
        }

        return promVideos[random.nextInt(promVideos.length)];
    }
"""

    if not replace_once(
        LISTMUSIC,
        old_listmusic,
        new_listmusic,
        "getPromVideo() ahora soporta directorios vacíos"
    ):
        sys.exit(1)

    # ------------------------------------------------------------
    # MediaPlayer.java
    # ------------------------------------------------------------

    old_media_1 = """    void playAudio(String path, String pathPromotionalVideo) {
        embeddedMediaPlayerMp3.playMedia(path);
        embeddedMediaPlayer.playMedia(pathPromotionalVideo);
    }
"""

    new_media_1 = """    void playAudio(String path, String pathPromotionalVideo) {
        embeddedMediaPlayerMp3.playMedia(path);

        if (pathPromotionalVideo != null && !pathPromotionalVideo.isEmpty()) {
            File promotionalVideo = new File(pathPromotionalVideo);

            if (promotionalVideo.isFile()) {
                embeddedMediaPlayer.playMedia(promotionalVideo.getAbsolutePath());
            }
        }
    }
"""

    if not replace_once(
        MEDIAPLAYER,
        old_media_1,
        new_media_1,
        "playAudio(path, ...) ahora tolera ausencia de video promocional"
    ):
        sys.exit(1)

    old_media_2 = """    void playAudio(String gender, String singer, String songName, String pathPromotionalVideo) {
        embeddedMediaPlayerMp3.playMedia(new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());
        embeddedMediaPlayer.playMedia(pathPromotionalVideo);
    }
"""

    new_media_2 = """    void playAudio(String gender, String singer, String songName, String pathPromotionalVideo) {
        embeddedMediaPlayerMp3.playMedia(
                new File(pathSongs, gender + File.separator + singer + File.separator + songName).getPath());

        if (pathPromotionalVideo != null && !pathPromotionalVideo.isEmpty()) {
            File promotionalVideo = new File(pathPromotionalVideo);

            if (promotionalVideo.isFile()) {
                embeddedMediaPlayer.playMedia(promotionalVideo.getAbsolutePath());
            }
        }
    }
"""

    if not replace_once(
        MEDIAPLAYER,
        old_media_2,
        new_media_2,
        "playAudio(gender, ...) ahora tolera ausencia de video promocional"
    ):
        sys.exit(1)

    print()
    print("==============================================")
    print(" CORRECCIÓN APLICADA")
    print("==============================================")
    print()
    print("Ahora:")
    print("  - getPromVideo() no falla si no hay videos.")
    print("  - El audio puede reproducirse sin video promocional.")
    print("  - Si existe un video promocional válido, se sigue usando.")
    print()
    print("Backups creados:")
    print("  ListMusic.java.backup-promovideos")
    print("  MediaPlayer.java.backup-promovideos")
    print()


if __name__ == "__main__":
    main()
