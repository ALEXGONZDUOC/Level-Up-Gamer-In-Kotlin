from fastapi import FastAPI, HTTPException, Query, BackgroundTasks
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel
from typing import List, Optional
from pathlib import Path
import pymysql
import uvicorn
import os
import smtplib
import random
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from dotenv import load_dotenv

load_dotenv()

app = FastAPI()

# --- CONFIGURACIÓN ARCHIVOS ESTÁTICOS ---
BASE_DIR = Path(__file__).resolve().parent
static_path = BASE_DIR / "static"

if not static_path.exists():
    static_path.mkdir(parents=True, exist_ok=True)
    (static_path / "images").mkdir(parents=True, exist_ok=True)

app.mount("/static", StaticFiles(directory=static_path), name="static")

# --- CONFIGURACIÓN CORREO ---

def enviar_correo_confirmacion(email_destino: str, nombre_comprador: str, pedido_id: int, total: float, detalles: list):
    gmail_user = os.getenv("GMAIL_USER")
    gmail_pass = os.getenv("GMAIL_PASS")

    if not gmail_user or not gmail_pass or "tu_correo" in gmail_user:
        print("Error: Credenciales de Gmail no configuradas correctamente en .env")
        return

    dias_entrega = random.randint(1, 4)
    
    msg = MIMEMultipart()
    msg['From'] = f"Level Up Gamer <{gmail_user}>"
    msg['To'] = email_destino
    msg['Subject'] = f"Confirmación de Pedido #{pedido_id} - Level Up Gamer"

    filas_tabla = ""
    for d in detalles:
        filas_tabla += f"""
        <tr>
            <td style="padding: 10px; border-bottom: 1px solid #eee;">{d['nombre']}</td>
            <td style="padding: 10px; border-bottom: 1px solid #eee; text-align: center;">{d['cantidad']}</td>
            <td style="padding: 10px; border-bottom: 1px solid #eee; text-align: right;">${d['precio_unitario']:.2f}</td>
        </tr>
        """

    html = f"""
    <html>
    <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; line-height: 1.6;">
        <div style="max-width: 600px; margin: 20px auto; border: 1px solid #ddd; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
            <div style="background-color: #6200EE; color: white; padding: 20px; text-align: center;">
                <h1 style="margin: 0;">¡Compra Exitosa!</h1>
            </div>
            <div style="padding: 30px;">
                <p>Hola <strong>{nombre_comprador}</strong>,</p>
                <p>Gracias por elegir <strong>Level Up Gamer</strong>. Tu pedido ha sido recibido y está siendo procesado.</p>
                
                <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;">
                    <p style="margin: 0;"><strong>Número de Orden:</strong> #{pedido_id}</p>
                </div>

                <table style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr style="border-bottom: 2px solid #6200EE;">
                            <th style="padding: 10px; text-align: left;">Producto</th>
                            <th style="padding: 10px; text-align: center;">Cant.</th>
                            <th style="padding: 10px; text-align: right;">Precio</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filas_tabla}
                    </tbody>
                </table>

                <div style="text-align: right; margin-top: 20px; font-size: 1.2em;">
                    <strong>Total: ${total:.2f}</strong>
                </div>

                <div style="margin-top: 30px; padding: 15px; background-color: #E8F5E9; border-left: 5px solid #4CAF50;">
                    <p style="margin: 0; color: #2E7D32;">
                        <strong>🚚 Información de Envío:</strong><br>
                        Tu pedido llegará en un plazo estimado de <strong>{dias_entrega} {'día' if dias_entrega == 1 else 'días'}</strong> hábiles.
                    </p>
                </div>
            </div>
            <div style="background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 0.8em; color: #777;">
                Este es un correo automático, por favor no respondas a este mensaje.<br>
                &copy; 2026 Level Up Gamer Store.
            </div>
        </div>
    </body>
    </html>
    """
    
    msg.attach(MIMEText(html, 'html'))

    try:
        # Usamos SMTP con STARTTLS para mayor compatibilidad
        with smtplib.SMTP('smtp.gmail.com', 587) as server:
            server.starttls()
            server.login(gmail_user, gmail_pass)
            server.send_message(msg)
            print(f"DEBUG: Correo enviado a {email_destino}")
    except Exception as e:
        print(f"DEBUG ERROR CORREO: {e}")

# --- CONFIGURACIÓN DB ---
db_config = {
    "host": "localhost",
    "user": "gamer",
    "password": "password123",
    "database": "level_up_gamer",
    "cursorclass": pymysql.cursors.DictCursor
}

def get_db_connection():
    try:
        return pymysql.connect(**db_config)
    except Exception as e:
        print(f"Error DB: {e}")
        return None

# --- MODELOS ---

class UsuarioBase(BaseModel):
    nombre: str
    contrasena: str
    email: str
    tipo_usuario_id: int
    activo: bool
    fecha_creacion: str

class LoginRequest(BaseModel):
    nombre: str
    contrasena: str

class ProductoBase(BaseModel):
    codigo: float
    nombre: str
    categoria: str
    descripcion: str
    precio: float
    cantidad: int
    imagenUrl: Optional[str] = ""
    imagenLocal: Optional[str] = "product_placeholder"

class DireccionBase(BaseModel):
    usuario_id: int
    nombre_etiqueta: str
    calle: str
    ciudad: str
    referencias: Optional[str] = ""
    latitud: float
    longitud: float
    es_principal: bool = False

class DetallePedido(BaseModel):
    producto_id: int
    cantidad: int

class PedidoRequest(BaseModel):
    usuario_id: int
    direccion: str
    detalles: List[DetallePedido]

# --- USUARIOS ---

@app.get("/usuarios")
def get_usuarios():
    conn = get_db_connection()
    if not conn: raise HTTPException(500, "Error DB")
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario")
            data = cursor.fetchall()
            for u in data: u["activo"] = bool(u["activo"])
            return data
    finally: conn.close()

@app.post("/login")
def login(req: LoginRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario WHERE nombre=%s AND contrasena=%s", (req.nombre, req.contrasena))
            user = cursor.fetchone()
            if not user: raise HTTPException(401, "Credenciales incorrectas")
            user["activo"] = bool(user["activo"])
            return user
    finally: conn.close()

@app.put("/usuarios/{id}")
def actualizar_usuario(id: int, u: UsuarioBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "UPDATE usuario SET nombre=%s, email=%s, contrasena=%s, tipo_usuario_id=%s, activo=%s WHERE id=%s"
            cursor.execute(query, (u.nombre, u.email, u.contrasena, u.tipo_usuario_id, 1 if u.activo else 0, id))
            conn.commit()
            cursor.execute("SELECT * FROM usuario WHERE id=%s", (id,))
            user = cursor.fetchone()
            if user: user["activo"] = bool(user["activo"])
            return user
    finally: conn.close()

@app.delete("/usuarios/{id}")
def eliminar_usuario(id: int):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("DELETE FROM usuario WHERE id=%s", (id,))
            conn.commit()
            return {"mensaje": "Usuario eliminado correctamente"}
    finally: conn.close()

# --- PRODUCTOS ---

@app.get("/productos")
def get_productos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM producto")
            return cursor.fetchall()
    finally: conn.close()

@app.post("/productos")
def crear_producto(p: ProductoBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "INSERT INTO producto (codigo, nombre, categoria, descripcion, precio, cantidad, imagenUrl, imagenLocal) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(query, (p.codigo, p.nombre, p.categoria, p.descripcion, p.precio, p.cantidad, p.imagenUrl, p.imagenLocal))
            conn.commit()
            cursor.execute("SELECT * FROM producto WHERE id=%s", (cursor.lastrowid,))
            return cursor.fetchone()
    finally: conn.close()

@app.put("/productos/{id}")
def editar_producto(id: int, p: ProductoBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "UPDATE producto SET codigo=%s, nombre=%s, categoria=%s, descripcion=%s, precio=%s, cantidad=%s, imagenUrl=%s, imagenLocal=%s WHERE id=%s"
            cursor.execute(query, (p.codigo, p.nombre, p.categoria, p.descripcion, p.precio, p.cantidad, p.imagenUrl, p.imagenLocal, id))
            conn.commit()
            cursor.execute("SELECT * FROM producto WHERE id=%s", (id,))
            return cursor.fetchone()
    finally: conn.close()

@app.delete("/productos/{id}")
def eliminar_producto(id: int):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("DELETE FROM producto WHERE id=%s", (id,))
            conn.commit()
            return {"mensaje": "Eliminado correctamente"}
    finally: conn.close()

# --- DIRECCIONES ---

@app.get("/usuarios/{usuario_id}/direcciones")
def get_direcciones(usuario_id: int):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM direcciones WHERE usuario_id=%s", (usuario_id,))
            data = cursor.fetchall()
            for d in data: d["es_principal"] = bool(d["es_principal"])
            return data
    finally: conn.close()

@app.post("/direcciones")
def crear_direccion(d: DireccionBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            if d.es_principal:
                cursor.execute("UPDATE direcciones SET es_principal=0 WHERE usuario_id=%s", (d.usuario_id,))
            query = "INSERT INTO direcciones (usuario_id, nombre_etiqueta, calle, ciudad, referencias, latitud, longitud, es_principal) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(query, (d.usuario_id, d.nombre_etiqueta, d.calle, d.ciudad, d.referencias, d.latitud, d.longitud, d.es_principal))
            conn.commit()
            return {"id": cursor.lastrowid, **d.model_dump()}
    finally: conn.close()

@app.put("/direcciones/{id}/principal")
def marcar_principal(id: int, usuario_id: int = Query(...)):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("UPDATE direcciones SET es_principal=0 WHERE usuario_id=%s", (usuario_id,))
            cursor.execute("UPDATE direcciones SET es_principal=1 WHERE id=%s", (id,))
            conn.commit()
            return {"mensaje": "Dirección principal actualizada"}
    finally: conn.close()

# --- PEDIDOS ---

@app.post("/pedidos")
def crear_pedido(p: PedidoRequest, background_tasks: BackgroundTasks):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            # 0. Obtener info del usuario para el correo
            cursor.execute("SELECT nombre, email FROM usuario WHERE id=%s", (p.usuario_id,))
            user_info = cursor.fetchone()
            if not user_info: raise HTTPException(404, "Usuario no encontrado")

            total = 0
            detalles_email = []
            
            # 1. Validar y calcular
            for d in p.detalles:
                cursor.execute("SELECT precio, cantidad, nombre FROM producto WHERE id=%s", (d.producto_id,))
                prod = cursor.fetchone()
                if not prod: raise HTTPException(404, f"Producto {d.producto_id} no existe")
                if prod["cantidad"] < d.cantidad: raise HTTPException(400, f"Sin stock para {prod['nombre']}")
                
                total += prod["precio"] * d.cantidad
                
                # Guardamos info para el correo
                detalles_email.append({
                    "nombre": prod["nombre"],
                    "cantidad": d.cantidad,
                    "precio_unitario": prod["precio"]
                })

            # 2. Cabecera
            cursor.execute("INSERT INTO pedidos (usuario_id, total, direccion) VALUES (%s, %s, %s)", (p.usuario_id, total, p.direccion))
            pedido_id = cursor.lastrowid

            # 3. Detalles y ACTUALIZAR COLUMNAS DE PRODUCTO (Stock y Vendidos)
            for d in p.detalles:
                cursor.execute("SELECT precio FROM producto WHERE id=%s", (d.producto_id,))
                precio = cursor.fetchone()["precio"]
                
                cursor.execute("INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (%s, %s, %s, %s)", 
                               (pedido_id, d.producto_id, d.cantidad, precio))
                
                # 🔥 AQUÍ ESTÁ LA MAGIA: Actualiza stock y suma a total_vendido
                cursor.execute("UPDATE producto SET cantidad = cantidad - %s, total_vendido = total_vendido + %s WHERE id = %s", 
                               (d.cantidad, d.cantidad, d.producto_id))

            conn.commit()

            # 4. Enviar correo de confirmación (Background)
            background_tasks.add_task(
                enviar_correo_confirmacion, 
                user_info["email"], 
                user_info["nombre"], 
                pedido_id, 
                total, 
                detalles_email
            )

            return {"pedido_id": pedido_id, "total": total, "mensaje": "Pedido exitoso"}
    except Exception as e:
        if conn: conn.rollback()
        raise HTTPException(400, str(e))
    finally: conn.close()

@app.get("/pedidos")
def get_pedidos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM pedidos ORDER BY fecha DESC")
            return cursor.fetchall()
    finally: conn.close()

@app.get("/pedidos/{pedido_id}/detalles")
def get_detalles_pedido(pedido_id: int):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "SELECT dp.*, p.nombre as producto_nombre FROM detalle_pedido dp JOIN producto p ON dp.producto_id = p.id WHERE dp.pedido_id = %s"
            cursor.execute(query, (pedido_id,))
            return cursor.fetchall()
    finally: conn.close()

# --- ESTADÍSTICAS ---

@app.get("/estadisticas/top-productos")
def get_top_productos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM producto ORDER BY total_vendido DESC")
            return cursor.fetchall()
    finally: conn.close()

@app.get("/estadisticas/ventas-totales")
def get_ventas_totales(periodo: str):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            if periodo == "dia": condicion = "DATE(fecha) = CURDATE()"
            elif periodo == "semana": condicion = "YEARWEEK(fecha, 1) = YEARWEEK(CURDATE(), 1)"
            else: condicion = "MONTH(fecha) = MONTH(CURDATE()) AND YEAR(fecha) = YEAR(CURDATE())"
            
            cursor.execute(f"SELECT COALESCE(SUM(total), 0) as total FROM pedidos WHERE {condicion}")
            res = cursor.fetchone()
            return {"total": float(res["total"])}
    finally: conn.close()

@app.get("/productos/{id}/ventas-por-dia")
def get_ventas_producto_dia(id: int):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            # Consulta ultra-precisa: Solo suma la cantidad de detalle_pedido 
            # filtrando por el producto_id y uniendo con la fecha del pedido.
            query = """
                SELECT DATE(pe.fecha) as fecha_v, SUM(dp.cantidad) as total_dia
                FROM detalle_pedido dp
                INNER JOIN pedidos pe ON dp.pedido_id = pe.id
                WHERE dp.producto_id = %s
                GROUP BY DATE(pe.fecha)
                ORDER BY fecha_v DESC
            """
            cursor.execute(query, (id,))
            rows = cursor.fetchall()
            # Convertimos a string la fecha para el JSON
            return {str(r["fecha_v"]): int(r["total_dia"]) for r in rows}
    finally:
        conn.close()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=3000)
