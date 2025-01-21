#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 10
#define RST_PIN 9

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Crear instancia del objeto MFRC522

void setup() {
  Serial.begin(9600);  // Iniciar comunicación serial
  SPI.begin();         // Iniciar bus SPI
  mfrc522.PCD_Init();  // Iniciar MFRC522
  Serial.println("Acerca una tarjeta al lector...");
  Serial.println("Menú:");
  Serial.println("1: Escribir UUID");
  Serial.println("2: Leer datos");
  Serial.println("Ingrese una opción:");
}

void loop() {
  if (Serial.available() > 0) {
    int option = Serial.parseInt();
    switch (option) {
      case 1:
        escribirUUID();
        break;
      case 2:
        leerDatos();
        break;
      default:
        Serial.println("Opción no válida. Ingrese 1 para escribir UUID o 2 para leer datos.");
    }
    Serial.println("Menú:");
    Serial.println("1: Escribir UUID");
    Serial.println("2: Leer datos");
    Serial.println("Ingrese una opción:");
  }
}

void escribirUUID() {
  // Esperar a que una tarjeta sea presentada
  while (!mfrc522.PICC_IsNewCardPresent() || !mfrc522.PICC_ReadCardSerial()) {
    delay(50);
  }

  // Mostrar UID de la tarjeta
  Serial.print("UID de la tarjeta: ");
  for (byte i = 0; i < mfrc522.uid.size; i++) {
    Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    Serial.print(mfrc522.uid.uidByte[i], HEX);
  }
  Serial.println();

  // Definir la clave personalizada (Key A)
  MFRC522::MIFARE_Key key;
  byte customKey[6] = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF }; // Tu clave personalizada
  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = customKey[i];
  }

  // Definir el bloque que se desea escribir
  byte block = 4; // Bloque a escribir

  // Generar UUID aleatorio
  byte uuid[16];
  for (byte i = 0; i < 16; i++) {
    uuid[i] = random(0, 256);
  }

  // Autenticar con la clave A para escribir
  Serial.println("Autenticando...");
  MFRC522::StatusCode status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Autenticación fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }

  // Escribir UUID en el bloque
  Serial.println("Escribiendo UUID...");
  status = mfrc522.MIFARE_Write(block, uuid, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Escritura fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
  } else {
    Serial.println("Escritura exitosa.");
    Serial.print("UUID escrito: ");
    for (byte i = 0; i < 16; i++) {
      Serial.print(uuid[i] < 0x10 ? " 0" : " ");
      Serial.print(uuid[i], HEX);
    }
    Serial.println();
  }

  // Detener la lectura de la tarjeta
  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1();
}

void leerDatos() {
  // Esperar a que una tarjeta sea presentada
  while (!mfrc522.PICC_IsNewCardPresent() || !mfrc522.PICC_ReadCardSerial()) {
    delay(50);
  }

  // Mostrar UID de la tarjeta
  Serial.print("UID de la tarjeta: ");
  for (byte i = 0; i < mfrc522.uid.size; i++) {
    Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    Serial.print(mfrc522.uid.uidByte[i], HEX);
  }
  Serial.println();

  // Definir la clave personalizada (Key A)
  MFRC522::MIFARE_Key key;
  byte customKey[6] = { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF }; // Tu clave personalizada
  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = customKey[i];
  }

  // Definir el bloque que se desea leer
  byte block = 4; // Bloque a leer
  byte len = 18; // Longitud del buffer
  byte buffer[18];

  // Autenticar con la clave A para leer
  Serial.println("Autenticando...");
  MFRC522::StatusCode status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Autenticación fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }

  // Leer datos del bloque
  Serial.println("Leyendo datos...");
  status = mfrc522.MIFARE_Read(block, buffer, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Lectura fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
  } else {
    Serial.print("Datos del bloque: ");
    for (byte i = 0; i < 16; i++) {
      Serial.print(buffer[i] < 0x10 ? " 0" : " ");
      Serial.print(buffer[i], HEX);
    }
    Serial.println();
  }

  // Detener la lectura de la tarjeta
  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1();
}
