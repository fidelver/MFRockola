# Inventario de dependencias — MFRockola Java 21

Fecha: 2026-10-13
Rama: `collab/mfrockola-java21`

## Criterio

La modernización conserva Ant/NetBeans y dependencias manuales por ahora. Se actualizaron dependencias sólo cuando existía una razón técnica concreta y se validó cada grupo con build/prueba. No se migró el proyecto a Maven/Gradle.

| Dependencia | Baseline Java 8 | Candidato Java 21 | Última/referencia revisada | Estado / decisión |
|---|---:|---:|---:|---|
| VLCJ | 3.3.0 | 4.12.1 | 4.12.1 estable para VLC 3; VLCJ 5 sigue ligado a VLC 4 en desarrollo | **Actualizar y conservar 4.x**. No migrar a VLCJ 5/libVLC 4 todavía. |
| vlcj-natives | integrado/implícito en VLCJ 3 | 4.12.0 | 4.12.0 rama VLCJ 4 | **Añadir**; dependencia requerida por VLCJ 4.12.1. |
| JNA core | 3.4.0 | jna-jpms 5.16.0 | JNA 5.19.1 disponible | **Pin 5.16.0** porque es la versión declarada por `vlcj-natives 4.12.0`; evitar desalinear manualmente la ABI/capa nativa sin necesidad. |
| JNA platform | 4.0.0 | jna-platform-jpms 5.16.0 | JNA 5.19.1 disponible | **Pin 5.16.0**, mismo motivo. |
| SQLite JDBC | 3.8.11.2 | 3.53.4.0 | 3.53.4.0 | **Actualizar**. Probado con Java 21 y copia de BBDD real (`PRAGMA integrity_check=ok`). |
| JSON-java | jar de 2011-04-05 | 20260814 | 20260814 | **Actualizar**. Se retiró la versión 2011 y se probó parse/write. |
| SLF4J | api/simple 1.7.25 | eliminado | n/a | **Eliminar**. No había imports en código y VLCJ 4.12.1 no lo requiere en el runtime resuelto. |
| libVLC del sistema | 3.0.23 conocido históricamente | 3.0.24 Fedora observado | VLC 3 estable | **Conservar inicialmente**. VLCJ 4 soporta VLC 3. |

## Hashes de artefactos incorporados

- `vlcj-4.12.1.jar`: `02335d28587f48b38b3a514f188469eaa019dd5bbe900b5b6828a3751f28a6dc`
- `vlcj-natives-4.12.0.jar`: `a40d0e22a86555de3d00da9fe3c440b23246644d9e2b0f30e3e8c7c96284b93d`
- `jna-jpms-5.16.0.jar`: `f4b68ffc6958c3ce3463fb908621595623db85c494bc44c7fb6f5e77f9a41804`
- `jna-platform-jpms-5.16.0.jar`: `49ad273625a7ce5dc106259cdb88fdb186f319b54d2cacf37f4fe0896a7a6c19`
- `json-20260814.jar`: `7dd2de1533e766e2b6c07d8194da85145ad313a167a4760050f5c9b6839604fe`
- `sqlite-jdbc-3.53.4.0.jar`: `bcb1f51e36f940867e83342f9efbf5968ac44a6bef4d397bb4af7b17b45cd2fb`

## Evidencia de compatibilidad

- Java 21 + VLCJ 4.12.1 + JNA 5.16.0 carga `libVLC 3.0.24` correctamente (`MFR_MEDIA_LIFECYCLE_OK`).
- SQLite JDBC 3.53.4.0 abre una copia de `canciones_populares.s3db`, pasa `PRAGMA integrity_check` y conserva `most_popular`.
- JSON-java 20260814 realiza parse/write correctamente.
- Build Ant completo con `--release 21` y bytecode major 65.

## Pendiente deliberado

No subir JNA a 5.19.1 de forma independiente mientras `vlcj-natives 4.12.0` declare 5.16.0, salvo que una prueba o vulnerabilidad concreta lo justifique. No migrar a VLCJ 5 ni libVLC 4 hasta que VLC 4 sea estable y exista una campaña de pruebas específica.
