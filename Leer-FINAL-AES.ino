#include <SPI.h>
#include <MFRC522.h>
#include <AES.h>

#define SS_PIN 10
#define RST_PIN 9

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Crear instancia del objeto MFRC522
AES aes;

void setup() {
  Serial.begin(9600);  // Iniciar comunicación serial
  SPI.begin();         // Iniciar bus SPI
  mfrc522.PCD_Init();  // Iniciar MFRC522
}

void loop() {
  // Esperar a que una tarjeta sea presentada
  while (!mfrc522.PICC_IsNewCardPresent() || !mfrc522.PICC_ReadCardSerial()) {
    delay(50);
  }

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
  MFRC522::StatusCode status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Autenticación fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
    delay(500);
    return;
  }

  // Leer datos del bloque
  status = mfrc522.MIFARE_Read(block, buffer, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print("Lectura fallida: ");
    Serial.println(mfrc522.GetStatusCodeName(status));
  } else {
    byte plaintext[16];
    for (byte i = 0; i < 16; i++) {
      plaintext[i] = buffer[i];
    }

    // Cifrar datos
    byte encrypted[16];
    byte aesKey[16] = { 0xC9, 0x77, 0x39, 0x1A, 0xE8, 0x34, 0xF8, 0xCC, 0xD2, 0x57, 0xDA, 0x8A, 0x2B, 0x93, 0x98, 0xB4 };
    byte iv[16] = {0}; // Vector de inicialización
    aes.do_aes_encrypt(plaintext, 16, encrypted, aesKey, 128, iv);

    // Mostrar datos cifrados en el monitor serie
    // Serial.print("Datos cifrados del bloque: ");
    for (byte i = 0; i < 16; i++) {
      if (encrypted[i] < 0x10) {
        Serial.print("0");
      }
      Serial.print(encrypted[i], HEX);
      Serial.print(" "); // Espacio para separar los bytes cifrados
    }
    Serial.println(); // Nueva línea al final de la impresión
  }

  // Detener la lectura de la tarjeta
  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1();

  // Esperar un tiempo antes de permitir otra lectura
  delay(500);
}
