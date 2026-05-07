from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import pymysql
import uvicorn

app = FastAPI()

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

@app.post("/login", response_model=UsuarioResponse)
def login(req: LoginRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario WHERE nombre=%s AND contrasena=%s", (req.nombre, req.contrasena))
            user = cursor.fetchone()
            if not user: raise HTTPException(401, "Credenciales incorrectas")
            
            # Mapeo manual para asegurar tipos
            return UsuarioResponse(
                id=int(user['id']),
                nombre=str(user['nombre']),
                contrasena=str(user['contrasena']),
                email=str(user['email']),
                tipo_usuario_id=int(user['tipo_usuario_id']),
                activo=bool(user['activo']),
                fecha_creacion=str(user['fecha_creacion'])
            )
    finally: conn.close()

@app.get("/productos")
def get_productos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM producto")
            productos = cursor.fetchall()
            # No hay campos de fecha en producto según el script, pero por seguridad:
            for p in productos:
                if 'id' in p: p['id'] = int(p['id'])
            return productos
    finally: conn.close()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=3000)
