#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script para aplicar los fixes de SQLite en MFRockola:
  1. closeConnection() tolerante a cierres dobles (isClosed()).
  2. Eliminar closeConnection() redundante en Interface.updateDataBase().

Uso:
    python3 aplicar_fix_sqlite.py /ruta/al/proyecto
    python3 aplicar_fix_sqlite.py /ruta/al/proyecto --dry-run
"""

import argparse
import re
import shutil
import sys
from datetime import datetime
from pathlib import Path


# ---------- Utilidades ----------

def backup(archivo: Path, backups_dir: Path) -> Path:
    """Hace copia del archivo con timestamp antes de modificarlo."""
    backups_dir.mkdir(parents=True, exist_ok=True)
    ts = datetime.now().strftime("%Y%m%d_%H%M%S")
    destino = backups_dir / f"{archivo.name}.{ts}.bak"
    shutil.copy2(archivo, destino)
    return destino


def leer(p: Path) -> str:
    return p.read_text(encoding="utf-8")


def escribir(p: Path, contenido: str) -> None:
    p.write_text(contenido, encoding="utf-8")


# ---------- Fix 1: SQLiteConsultor.closeConnection() ----------

NUEVO_CLOSE_CONNECTION = """    public void closeConnection() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            // silencioso: el resultSet ya estaba cerrado
        }
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            // silencioso: el statement ya estaba cerrado
        }
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // silencioso: la conexión ya estaba cerrada
        }
    }"""


def fix_sqlite_consultor(ruta: Path, dry_run: bool) -> bool:
    """Reemplaza closeConnection() por la versión con isClosed()."""
    if not ruta.exists():
        print(f"[!] No existe: {ruta}")
        return False

    contenido = leer(ruta)

    # Comprobar si ya tiene el fix
    if "if (resultSet != null && !resultSet.isClosed())" in contenido:
        print(f"[=] {ruta.name}: ya tiene el fix de closeConnection()")
        return True

    # Regex: capturar el método closeConnection completo
    patron = re.compile(
        r"\s*public\s+void\s+closeConnection\s*\(\s*\)\s*\{(?:[^{}]|\{[^{}]*\})*\}\s*",
        re.DOTALL,
    )

    m = patron.search(contenido)
    if not m:
        print(f"[X] {ruta.name}: no se encontró closeConnection()")
        return False

    viejo = m.group(0)

    if dry_run:
        print(f"[·] {ruta.name}: se reemplazaría closeConnection() "
              f"({len(viejo)} chars) por la nueva versión ({len(NUEVO_CLOSE_CONNECTION)} chars)")
        return True

    contenido_nuevo = contenido[:m.start()] + "\n" + NUEVO_CLOSE_CONNECTION + "\n" + contenido[m.end():]
    escribir(ruta, contenido_nuevo)
    print(f"[OK] {ruta.name}: closeConnection() actualizado")
    return True


# ---------- Fix 2: Interface.updateDataBase() ----------

def fix_interface_update_data_base(ruta: Path, dry_run: bool) -> bool:
    """Elimina la llamada redundante a consultor.closeConnection() dentro de updateDataBase()."""
    if not ruta.exists():
        print(f"[!] No existe: {ruta}")
        return False

    contenido = leer(ruta)

    # Localizar el método updateDataBase
    patron_metodo = re.compile(
        r"(private\s+void\s+updateDataBase\s*\([^)]*\)\s*\{(?:[^{}]|\{[^{}]*\})*\}\s*)",
        re.DOTALL,
    )
    m = patron_metodo.search(contenido)
    if not m:
        print(f"[X] {ruta.name}: no se encontró updateDataBase()")
        return False

    metodo_original = m.group(1)

    # Buscar la línea "consultor.closeConnection();" dentro del método
    patron_llamada = re.compile(r"^\s*consultor\.closeConnection\s*\(\s*\)\s*;\s*$", re.MULTILINE)

    if not patron_llamada.search(metodo_original):
        if "consultor.closeConnection" not in metodo_original:
            print(f"[=] {ruta.name}: updateDataBase() ya no tiene closeConnection() redundante")
        else:
            print(f"[!] {ruta.name}: updateDataBase() menciona closeConnection() "
                  f"pero en formato inesperado. Revisar a mano.")
        return True

    if dry_run:
        print(f"[·] {ruta.name}: se eliminaría la línea 'consultor.closeConnection();' "
              f"de updateDataBase()")
        return True

    # Reemplazar: comentar la línea en lugar de borrarla (más seguro para revertir)
    metodo_nuevo = patron_llamada.sub(
        "        // consultor.closeConnection();  // FIX: redundante, insert() y update() ya cierran",
        metodo_original,
    )

    contenido_nuevo = contenido[:m.start()] + metodo_nuevo + contenido[m.end():]
    escribir(ruta, contenido_nuevo)
    print(f"[OK] {ruta.name}: closeConnection() redundante comentado en updateDataBase()")
    return True


# ---------- Main ----------

def main():
    parser = argparse.ArgumentParser(
        description="Aplica fixes de SQLite en MFRockola."
    )
    parser.add_argument(
        "proyecto",
        help="Ruta raíz del proyecto MFRockola (o directamente la carpeta 'classes')",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Solo muestra qué haría, sin modificar archivos",
    )
    args = parser.parse_args()

    raiz = Path(args.proyecto).resolve()

    # Detectar la carpeta de clases
    posibles = [
        raiz / "src" / "com" / "mfrockola" / "classes",
        raiz / "com" / "mfrockola" / "classes",
        raiz,
    ]
    classes_dir = next((p for p in posibles if (p / "SQLiteConsultor.java").exists()), None)

    if classes_dir is None:
        print("[X] No se encontró la carpeta com/mfrockola/classes.")
        print("    Pasa la ruta correcta o directamente la carpeta 'classes'.")
        sys.exit(1)

    print(f"Carpeta de clases: {classes_dir}")
    print(f"Modo: {'DRY-RUN (sin cambios)' if args.dry_run else 'APLICAR'}")
    print()

    backups_dir = raiz / "backup" / f"fix_sqlite_{datetime.now().strftime('%Y%m%d_%H%M%S')}"

    archivos = {
        "SQLiteConsultor": classes_dir / "SQLiteConsultor.java",
        "Interface":       classes_dir / "Interface.java",
    }

    # Backup
    if not args.dry_run:
        for nombre, ruta in archivos.items():
            if ruta.exists():
                b = backup(ruta, backups_dir)
                print(f"[BK] {ruta.name} → {b.relative_to(raiz)}")
        print()

    # Aplicar fixes
    ok1 = fix_sqlite_consultor(archivos["SQLiteConsultor"], args.dry_run)
    ok2 = fix_interface_update_data_base(archivos["Interface"], args.dry_run)

    print()
    if ok1 and ok2:
        print("✅ Fixes aplicados correctamente.")
        if not args.dry_run:
            print(f"   Backup en: {backups_dir}")
    else:
        print("⚠️  Algunos cambios no se pudieron aplicar. Revisa los mensajes de arriba.")
        sys.exit(2)


if __name__ == "__main__":
    main()
