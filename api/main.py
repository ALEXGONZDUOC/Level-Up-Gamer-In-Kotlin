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
import string
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from dotenv import load_dotenv
from datetime import date

load_dotenv()

app = FastAPI()

# --- CONFIGURACIÓN ARCHIVOS ESTÁTICOS ---
BASE_DIR = Path(__file__).resolve().parent
static_path = BASE_DIR / "static"

if not static_path.exists():
    static_path.mkdir(parents=True, exist_ok=True)
    (static_path / "images").mkdir(parents=True, exist_ok=True)

app.mount("/static", StaticFiles(directory=static_path), name="static")

# --- UTILIDADES ---

def generar_codigo_6_digitos():
    return ''.join(random.choices(string.digits, k=6))

# --- CONFIGURACIÓN CORREO ---

def enviar_email(email_destino: str, asunto: str, cuerpo_html: str):
    gmail_user = os.getenv("GMAIL_USER")
    gmail_pass = os.getenv("GMAIL_PASS")

    if not gmail_user or not gmail_pass or "tu_correo" in gmail_user:
        print(f"Error: Credenciales de Gmail no configuradas. No se pudo enviar '{asunto}' a {email_destino}")
        return

    msg = MIMEMultipart()
    msg['From'] = f"Level Up Gamer <{gmail_user}>"
    msg['To'] = email_destino
    msg['Subject'] = asunto
    msg.attach(MIMEText(cuerpo_html, 'html'))

    try:
        with smtplib.SMTP('smtp.gmail.com', 587) as server:
            server.starttls()
            server.login(gmail_user, gmail_pass)
            server.send_message(msg)
            print(f"DEBUG: Email '{asunto}' enviado a {email_destino}")
    except Exception as e:
        print(f"DEBUG ERROR CORREO: {e}")

def html_template(titulo: str, contenido: str):
    return f"""
    <html>
    <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; line-height: 1.6;">
        <div style="max-width: 600px; margin: 20px auto; border: 1px solid #ddd; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
            <div style="background-color: #6200EE; color: white; padding: 20px; text-align: center;">
                <h1 style="margin: 0;">{titulo}</h1>
            </div>
            <div style="padding: 30px;">
                {contenido}
            </div>
            <div style="background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 0.8em; color: #777;">
                &copy; 2026 Level Up Gamer Store. No respondas a este correo.
            </div>
        </div>
    </body>
    </html>
    """

def enviar_correo_bienvenida(email_destino: str, nombre: str, codigo: str):
    contenido = f"""
        <p>Hola <strong>{nombre}</strong>,</p>
        <p>¡Bienvenido a la comunidad de <strong>Level Up Gamer</strong>!</p>
        <p>Para activar tu cuenta, por favor ingresa el siguiente código de verificación en la aplicación:</p>
        <div style="background-color: #f4f4f9; padding: 20px; text-align: center; font-size: 2em; letter-spacing: 10px; font-weight: bold; color: #6200EE; border-radius: 5px; margin: 20px 0;">
            {codigo}
        </div>
        <p>Si no creaste esta cuenta, puedes ignorar este mensaje.</p>
    """
    enviar_email(email_destino, "Verifica tu cuenta - Level Up Gamer", html_template("¡Bienvenido!", contenido))

def enviar_correo_recuperacion(email_destino: str, codigo: str):
    contenido = f"""
        <p>Hemos recibido una solicitud para restablecer tu contraseña.</p>
        <p>Usa el siguiente código de seguridad para continuar con el proceso:</p>
        <div style="background-color: #FFF3E0; padding: 20px; text-align: center; font-size: 2em; letter-spacing: 10px; font-weight: bold; color: #E65100; border-radius: 5px; margin: 20px 0;">
            {codigo}
        </div>
        <p>Este código es temporal. Si no solicitaste esto, te recomendamos cambiar tu contraseña actual.</p>
    """
    enviar_email(email_destino, "Recuperar Contraseña - Level Up Gamer", html_template("Seguridad", contenido))

def enviar_correo_compra(email_destino: str, nombre: str, pedido_id: int, total: float, detalles: list):
    filas = "".join([f"<tr><td style='padding:10px;border-bottom:1px solid #eee;'>{d['nombre']}</td><td style='text-align:center;'>{d['cantidad']}</td><td style='text-align:right;'>${d['precio_unitario']:.2f}</td></tr>" for d in detalles])
    contenido = f"""
        <p>Hola {nombre}, tu pedido <strong>#{pedido_id}</strong> ha sido recibido.</p>
        <table style="width: 100%; border-collapse: collapse; margin: 20px 0;">
            <thead><tr style="border-bottom: 2px solid #6200EE;"><th style="text-align:left;">Ítem</th><th>Cant.</th><th style="text-align:right;">Precio</th></tr></thead>
            <tbody>{filas}</tbody>
        </table>
        <p style="text-align:right; font-size: 1.2em;"><strong>Total: ${total:.2f}</strong></p>
        <div style="padding: 15px; background-color: #E8F5E9; border-left: 5px solid #4CAF50; margin-top: 20px;">
            🚚 Tu pedido llegará en aprox. <strong>{random.randint(1,4)} días hábiles</strong>.
        </div>
    """
    enviar_email(email_destino, f"Confirmación de Pedido #{pedido_id}", html_template("¡Compra Exitosa!", contenido))

# --- CONFIGURACIÓN DB ---
db_config = {
    "host": "localhost",
    "user": "gamer",
    "password": "password123",
    "database": "level_up_gamer",
    "cursorclass": pymysql.cursors.DictCursor
}

def get_db_connection():
    try: return pymysql.connect(**db_config)
    except: return None

# --- MODELOS ---

class RegistroRequest(BaseModel):
    nombre: str
    contrasena: str
    email: str

class LoginRequest(BaseModel):
    nombre: str
    contrasena: str

class VerificationRequest(BaseModel):
    email: str
    codigo: str

class RecoveryRequest(BaseModel):
    email: str

class ResetPasswordRequest(BaseModel):
    email: str
    codigo: str
    nueva_contrasena: str

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
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT id, nombre, email, tipo_usuario_id, activo, verificado, fecha_creacion FROM usuario")
            data = cursor.fetchall()
            for u in data: u["activo"] = bool(u["activo"]); u["verificado"] = bool(u["verificado"])
            return data
    finally: conn.close()

@app.post("/usuarios")
def registrar_usuario(req: RegistroRequest, background_tasks: BackgroundTasks):
    conn = get_db_connection()
    try:
        codigo = generar_codigo_6_digitos()
        with conn.cursor() as cursor:
            query = "INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id, activo, verificado, codigo_auth, fecha_creacion) VALUES (%s, %s, %s, 3, 1, 0, %s, %s)"
            cursor.execute(query, (req.nombre, req.contrasena, req.email, codigo, date.today()))
            conn.commit()
            background_tasks.add_task(enviar_correo_bienvenida, req.email, req.nombre, codigo)
            return {"mensaje": "Usuario registrado. Revisa tu correo para verificar la cuenta."}
    except Exception as e:
        if "Duplicate entry" in str(e): raise HTTPException(400, "El correo ya está registrado")
        raise HTTPException(500, str(e))
    finally: conn.close()

@app.post("/usuarios/verificar")
def verificar_usuario(req: VerificationRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT id FROM usuario WHERE email=%s AND codigo_auth=%s", (req.email, req.codigo))
            if not cursor.fetchone(): raise HTTPException(400, "Código de verificación incorrecto")
            cursor.execute("UPDATE usuario SET verificado=1, codigo_auth=NULL WHERE email=%s", (req.email,))
            conn.commit()
            return {"mensaje": "Cuenta verificada con éxito"}
    finally: conn.close()

@app.post("/usuarios/recuperar")
def solicitar_recuperacion(req: RecoveryRequest, background_tasks: BackgroundTasks):
    conn = get_db_connection()
    try:
        codigo = generar_codigo_6_digitos()
        with conn.cursor() as cursor:
            cursor.execute("SELECT id FROM usuario WHERE email=%s", (req.email,))
            if not cursor.fetchone(): return {"mensaje": "Si el correo existe, recibirás un código"}
            cursor.execute("UPDATE usuario SET codigo_auth=%s WHERE email=%s", (codigo, req.email))
            conn.commit()
            background_tasks.add_task(enviar_correo_recuperacion, req.email, codigo)
            return {"mensaje": "Código de recuperación enviado"}
    finally: conn.close()

@app.post("/usuarios/reset-password")
def reset_password(req: ResetPasswordRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT id FROM usuario WHERE email=%s AND codigo_auth=%s", (req.email, req.codigo))
            if not cursor.fetchone(): raise HTTPException(400, "Código incorrecto")
            cursor.execute("UPDATE usuario SET contrasena=%s, codigo_auth=NULL WHERE email=%s", (req.nueva_contrasena, req.email))
            conn.commit()
            return {"mensaje": "Contraseña actualizada correctamente"}
    finally: conn.close()

@app.post("/login")
def login(req: LoginRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario WHERE nombre=%s AND contrasena=%s", (req.nombre, req.contrasena))
            user = cursor.fetchone()
            if not user: raise HTTPException(401, "Credenciales incorrectas")
            if not user["verificado"]: raise HTTPException(403, "Cuenta no verificada. Revisa tu correo.")
            user["activo"] = bool(user["activo"])
            user["verificado"] = bool(user["verificado"])
            return user
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
            cursor.execute("SELECT nombre, email FROM usuario WHERE id=%s", (p.usuario_id,))
            user_info = cursor.fetchone()
            if not user_info: raise HTTPException(404, "Usuario no encontrado")

            total = 0
            detalles_email = []
            
            for d in p.detalles:
                cursor.execute("SELECT precio, cantidad, nombre FROM producto WHERE id=%s", (d.producto_id,))
                prod = cursor.fetchone()
                if not prod: raise HTTPException(404, f"Producto {d.producto_id} no existe")
                if prod["cantidad"] < d.cantidad: raise HTTPException(400, f"Sin stock para {prod['nombre']}")
                
                total += prod["precio"] * d.cantidad
                detalles_email.append({
                    "nombre": prod["nombre"],
                    "cantidad": d.cantidad,
                    "precio_unitario": prod["precio"]
                })

            cursor.execute("INSERT INTO pedidos (usuario_id, total, direccion) VALUES (%s, %s, %s)", (p.usuario_id, total, p.direccion))
            pedido_id = cursor.lastrowid

            for d in p.detalles:
                cursor.execute("INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (%s, %s, %s, (SELECT precio FROM producto WHERE id=%s))", 
                               (pedido_id, d.producto_id, d.cantidad, d.producto_id))
                cursor.execute("UPDATE producto SET cantidad = cantidad - %s, total_vendido = total_vendido + %s WHERE id = %s", 
                               (d.cantidad, d.cantidad, d.producto_id))

            conn.commit()
            background_tasks.add_task(enviar_correo_compra, user_info["email"], user_info["nombre"], pedido_id, total, detalles_email)
            return {"pedido_id": pedido_id, "total": total, "mensaje": "Pedido exitoso"}
    except Exception as e:
        if conn: conn.rollback()
        raise HTTPException(400, str(e))
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

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=3000)
