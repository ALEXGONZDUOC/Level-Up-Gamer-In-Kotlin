from fastapi import FastAPI, HTTPException
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel
from typing import List, Optional
from pathlib import Path
import pymysql
import uvicorn

app = FastAPI()

BASE_DIR = Path(__file__).resolve().parent

app.mount(
    "/static",
    StaticFiles(directory=BASE_DIR / "static"),
    name="static"
)

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

class UsuarioResponse(BaseModel):
    id: int
    nombre: str
    contrasena: str
    email: str
    tipo_usuario_id: int
    activo: bool
    fecha_creacion: str

@app.get("/usuarios", response_model=List[UsuarioResponse])
def get_usuarios():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario")
            data = cursor.fetchall()
            for u in data:
                u["activo"] = bool(u["activo"])
                u["fecha_creacion"] = str(u["fecha_creacion"])
            return data
    finally: conn.close()

@app.post("/usuarios", response_model=UsuarioResponse)
def registrar_usuario(u: UsuarioBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "INSERT INTO usuario (nombre, contrasena, email, tipo_usuario_id, activo, fecha_creacion) VALUES (%s, %s, %s, %s, %s, %s)"
            cursor.execute(query, (u.nombre, u.contrasena, u.email, u.tipo_usuario_id, 1 if u.activo else 0, u.fecha_creacion))
            conn.commit()
            cursor.execute("SELECT * FROM usuario WHERE id=%s", (cursor.lastrowid,))
            user = cursor.fetchone()
            user["activo"] = bool(user["activo"])
            user["fecha_creacion"] = str(user["fecha_creacion"])
            return user
    finally: conn.close()

@app.put("/usuarios/{id}", response_model=UsuarioResponse)
def actualizar_usuario(id: int, u: UsuarioBase):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            query = "UPDATE usuario SET nombre=%s, email=%s, contrasena=%s, tipo_usuario_id=%s, activo=%s WHERE id=%s"
            cursor.execute(query, (u.nombre, u.email, u.contrasena, u.tipo_usuario_id, 1 if u.activo else 0, id))
            conn.commit()
            cursor.execute("SELECT * FROM usuario WHERE id=%s", (id,))
            user = cursor.fetchone()
            user["activo"] = bool(user["activo"])
            user["fecha_creacion"] = str(user["fecha_creacion"])
            return user
    finally: conn.close()

@app.post("/login", response_model=UsuarioResponse)
def login(req: LoginRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario WHERE nombre=%s AND contrasena=%s", (req.nombre, req.contrasena))
            user = cursor.fetchone()
            if not user: raise HTTPException(401, "Credenciales incorrectas")
            user["activo"] = bool(user["activo"])
            user["fecha_creacion"] = str(user["fecha_creacion"])
            return user
    finally: conn.close()

@app.get("/productos")
def get_productos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM producto")
            return cursor.fetchall()
    finally: conn.close()

class ItemCompra(BaseModel):
    id: int
    cantidad: int

class CompraRequest(BaseModel):
    productos: List[ItemCompra]

@app.post("/productos/comprar")
def comprar_productos(req: CompraRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            for item in req.productos:
                # Descuento de stock e incremento de ventas
                cursor.execute(
                    "UPDATE producto SET cantidad = cantidad - %s, total_vendido = total_vendido + %s WHERE id = %s",
                    (item.cantidad, item.cantidad, item.id)
                )
            conn.commit()
            return {"status": "success"}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally: conn.close()

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

            # Nota: Requiere tabla pedidos (usuario_id, total, fecha)
            cursor.execute(f"SELECT COALESCE(SUM(total), 0) as total FROM pedidos WHERE {condicion}")
            res = cursor.fetchone()
            return {"total": float(res["total"])}
    finally: conn.close()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=3000)
