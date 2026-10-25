#!/usr/bin/env python3

from pathlib import Path
import shutil
import sys

archivo = Path("/home/fam/NetBeansProjects/MFRockola/src/com/mfrockola/classes/Interface.java")
backup = archivo.with_suffix(".java.backup-no-resize")

if not archivo.exists():
    print(f"[ERROR] No existe: {archivo}")
    sys.exit(1)

if not backup.exists():
    shutil.copy2(archivo, backup)
    print(f"[OK] Backup creado: {backup}")
else:
    print(f"[INFO] Backup ya existe: {backup}")

texto = archivo.read_text(encoding="utf-8")

objetivo = """        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(resolution);
"""

reemplazo = """        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(resolution);
"""

if objetivo not in texto:
    print("[ERROR] No se encontró exactamente el bloque esperado.")
    print("No se modificó Interface.java.")
    sys.exit(1)

if "setResizable(false);" in texto:
    print("[INFO] setResizable(false) ya existe.")
    sys.exit(0)

texto = texto.replace(objetivo, reemplazo, 1)

archivo.write_text(texto, encoding="utf-8")

print("[OK] Se agregó:")
print("     setResizable(false);")
print()
print("Ahora la ventana le indica explícitamente al sistema que")
print("no puede ser redimensionada.")
