# TP Integrador Banco - UTN

Este proyecto es una aplicación web dinámica en Java, desarrollada en Eclipse con Servlets, JSP y JDBC.

## ⚙️ Requisitos

Antes de empezar, asegurate de tener instalado:

- Java JDK 8 o superior
- Eclipse IDE for Enterprise Java Developers
- Apache Tomcat (versión recomendada: 9.x)
- MySQL Server (corriendo en localhost)
- Git

## 🚀 Pasos para ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/AlejoLg1/TPINT_GRUPO_10_LAB4.git
```


### 2. Importar el proyecto en Eclipse

1. Abrí Eclipse.
2. Ir a: `File` > `Import...` > `Existing Projects into Workspace`.
3. Seleccioná la carpeta del proyecto que acabás de clonar.
4. Asegurate de que Eclipse lo detecte como un proyecto *Dynamic Web Project*.

---

### 3. Configurar el archivo de conexión a la base de datos

1. Ir a:
   ```
   /src/main/java/utils/Conexion.java
   ```
2. Modificá este archivo con los datos correctos de tu MySQL local. Por ejemplo:

```java
// Para MySQL Connector 8.0+
Class.forName("com.mysql.cj.jdbc.Driver");

// Para MySQL Connector 5.1.x
// Class.forName("com.mysql.jdbc.Driver");

this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/segurosgroup","root","root");
```

---

### 4. Evitar conflictos con el archivo de conexión

Este archivo está **ignorado localmente** por Git, por lo que podés modificarlo sin preocuparte de subirlo por accidente.

> ⚠️ Solo tenés que correr este comando una vez:

```bash
git update-index --assume-unchanged src/main/java/utils/Conexion.java
```

---

### 5. Crear la base de datos

- En MySQL, crear la base de datos con el script .sql disponible

---

### 6. Ejecutar el proyecto

1. Agregá Apache Tomcat al workspace si no lo hiciste aún.
2. Hacé clic derecho sobre el proyecto → `Run As` → `Run on Server`.
3. Probá ingresar desde el navegador en:

```
http://localhost:8080/TPINT_GRUPO_10_LAB4/
```

---

## ✅ Buenas prácticas

- No vamos a usar ramas (`branches`) para evitar complicaciones.
- Hacer siempre `pull` antes de trabajar:
  ```bash
  git pull
  ```
- Hacer `commit` seguido para no perder avances:
  ```bash
  git add .
  git commit -m "Tu mensaje"
  git push
  ```

---

## 🧠 Notas útiles

- Si Eclipse no reconoce el proyecto como Web, revisá que tenga el *facet* "Dynamic Web Module" activado (`Project Properties > Project Facets`).
- Si tenés problemas con la conexión, revisá el conector de MySQL en tu ruta de build (`WEB-INF/lib`).

---

## 👨‍💻 Contacto

Para dudas técnicas o errores, consultá vía mensaje por el grupo de WhatsApp del TP.
