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

@app.post("/login")
def login(req: LoginRequest):
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario WHERE nombre=%s AND contrasena=%s", (req.nombre, req.contrasena))
            user = cursor.fetchone()
            if not user: raise HTTPException(401, "Credenciales incorrectas")
            return user
    finally: conn.close()

@app.get("/usuarios")
def get_usuarios():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM usuario")
            return cursor.fetchall()
    finally: conn.close()

@app.get("/productos")
def get_productos():
    conn = get_db_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM producto")
            return cursor.fetchall()
    finally: conn.close()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=3000)
