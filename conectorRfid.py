import tkinter as tk
from tkinter import messagebox
import serial
import pyperclip
import pyautogui
import time
from serial.tools import list_ports
from pyaes import AESModeOfOperationCBC

# Clave AES y vector de inicialización (IV) usados en el Arduino
clave_aes = bytes([0xC9, 0x77, 0x39, 0x1A, 0xE8, 0x34, 0xF8, 0xCC, 0xD2, 0x57, 0xDA, 0x8A, 0x2B, 0x93, 0x98, 0xB4])
iv = bytes([0] * 16)  # Vector de inicialización

def leer_desde_arduino(puerto_serie, baudios):
    ser = serial.Serial(puerto_serie, baudios, timeout=1)
    try:
        while True:
            try:
                linea = ser.readline().decode('utf-8').strip()
                while not linea:
                    linea = ser.readline().decode('utf-8').strip()
                # Filtrar caracteres no hexadecimales          
                if "Autenticación fallida: Timeout in communication." in linea:
                    messagebox.showinfo("Autenticación fallida", "El carnet no está registrado en la base de datos.")
                else:
                    datos_hex = ''.join(c for c in linea if c in '0123456789ABCDEFabcdef')
                    # Decodificar los datos cifrados en hexadecimal
                    datos_cifrados = bytes.fromhex(datos_hex)
                    # Descifrar los datos usando AES en modo CBC
                    aes_cbc = AESModeOfOperationCBC(clave_aes, iv)
                    datos_descifrados = aes_cbc.decrypt(datos_cifrados)
                    # Formatear los datos descifrados en una cadena hexadecimal con ":" como separador
                    datos_hex = ':'.join('{:02X}'.format(byte) for byte in datos_descifrados)
                    # Copiar los datos descifrados al portapapeles
                    pyperclip.copy(datos_hex)
                    # Pegar los datos descifrados en la aplicación activa
                    pyautogui.hotkey('ctrl', 'v')
            except serial.SerialException:
                messagebox.showinfo("Error", "Error al comunicarse con el Arduino. Verifique la conexión.")
    except KeyboardInterrupt:
        print("Programa detenido por el usuario.")
    finally:
        ser.close()

def detectar_arduino():
    puertos_disponibles = [port.device for port in list_ports.comports()]
    for puerto in puertos_disponibles:
        try:
            ser = serial.Serial(puerto, 9600, timeout=1)
            ser.close()
            return puerto
        except serial.SerialException:
            continue
    return None

def iniciar_lectura():
    puerto = detectar_arduino()
    if puerto:
        try:
            ser = serial.Serial(puerto, 9600, timeout=1)
            ser.close()
            messagebox.showinfo("Estado de la conexión",
                                "Conectado correctamente al puerto " + puerto)
            time.sleep(2)  # Espera 2 segundos
            leer_desde_arduino(puerto, 9600)
        except Exception as e:
            messagebox.showinfo("Estado de la conexión",
                                "Error al conectarse al puerto " + puerto)
    else:
        messagebox.showinfo("Estado de la conexión",
                            "No se pudo detectar el Arduino.")

app = tk.Tk()
app.title("SIASA - MODULO LECTOR RFID")
app.iconbitmap("icon.ico")

# Centrar interfaz en cualquier monitor
# Definir el ancho y alto de la ventana
window_width = 350  # Ancho
window_height = 80  # Alto

# Obtener el ancho y alto de la pantalla para poder centrarla
screen_width = app.winfo_screenwidth()
screen_height = app.winfo_screenheight()

# Calcular las coordenadas x e y de la pantalla o monitor que se está usando para centrar la ventana
x = (screen_width // 2) - (window_width // 2)
y = (screen_height // 2) - (window_height // 2)

# Establecer la geometría de la ventana para centrarla
app.geometry("%dx%d+%d+%d" % (window_width, window_height, x, y))

boton_detectar = tk.Button(app, text="Detectar Arduino", command=iniciar_lectura)
boton_detectar.pack()

estado_label = tk.Label(app, text="")
estado_label.pack()

app.mainloop()