import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Properties;

public class Client {
    private static boolean enigmaOn = false; // booleana enigmaOn, false indica non attivo.
    private static boolean aesOn = false;
    private static boolean ceasarOn = false;
    private static int shift=0;; //numeri di salti per ceasar

    static String propsKey = getPSK("enigma.properties");// Inseriamo qui il nome del file da cui leggere la prop "key"
                                                         // con il
    // metodo getPSK



    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Client <server-ip> <port> <username>");
            System.exit(1);
        }
    
        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        try (Socket socket = new Socket(serverIp, port);
                Scanner userInput = new Scanner(System.in);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Invio dell'username al server subito dopo la connessione.
            out.println(username);
            System.out.println("Connected to server. Start typing messages (type 'exit' to quit).");

            
            

            // Thread per ascoltare e stampare i messaggi in arrivo dal server.
            Thread serverListener = new Thread(() -> {
                try (Scanner in = new Scanner(socket.getInputStream())) {
                    while (in.hasNextLine()) {
                        String message = in.nextLine();
                        if(enigmaOn == true){
                            EnigmaSimulator enigmaSimulator = new EnigmaSimulator();
                           System.out.println(enigmaSimulator.cifraDecifra(message, false));
                        }else if(aesOn){
                            System.out.println(CryptoUtils.decrypt(message, "experis"));
                        }else if(ceasarOn){
                            System.out.println(CifrarioDiCesare.decripta(message, shift));
                        }else if(enigmaOn == false && aesOn == false && ceasarOn == false){
                            System.out.println(in.nextLine());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Errore durante la lettura dal server: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverListener.start();




            // Ciclo principale per l'invio di messaggi.
            while (true) {
                String message = userInput.nextLine();

                // Esce dal ciclo se l'utente digita "exit".
                if (message.equalsIgnoreCase("exit")) {
                    out.println(username + " ha lasciato la chat.");
                    break;
                }
                // Attiva enigma
                if (message.equalsIgnoreCase("enigma_on")) { // Quando l'utente scrive il comando enigma_on in chat
                    System.out.println("ENIGMA encrypting ON");// Verrà stampato un messaggio di avviso
                    enigmaOn = true;// E la variabile diventerà true, attivando l'encrypting a riga 60
                    aesOn = false;
                    ceasarOn = false;
                    continue;
                }
                // Attiva eas encrypting
                if (message.equalsIgnoreCase("aes_on")) {
                    System.out.println("AES encrypting ON");
                    aesOn = true;
                    enigmaOn = false;
                    ceasarOn = false;
                    continue;
                }

                
                // Attiva caesar
                if (message.equalsIgnoreCase("caesar_on")) { // Quando l'utente scrive il comando ceasar_on in chat
                    System.out.println("CAESAR encrypting ON");// Verrà stampato un messaggio di avviso
                    ceasarOn = true; // E la variabile diventerà true, attivando l'encrypting a riga 77
                    enigmaOn = false;
                    aesOn = false;
                    System.out.println("Di quanto vuoi shiftare?");
                    Scanner scCesare = new Scanner(System.in);
                    shift = scCesare.nextInt();
                    continue;
                }

                // disattiva enigma
                if (message.equalsIgnoreCase("enigma_off")) {// Quando l'utente scrive il comando enigma_on in chat
                    System.out.println("ENIGMA encrypting OFF");// Verrà stampato un messaggio di avviso
                    enigmaOn = false;// E la variabile diventerà false, disattivando l'encrypting a riga 60
                    continue;
                }
                // disattiva aes
                if (message.equalsIgnoreCase("aes_off")) {// Quando l'utente scrive il comando enigma_on in chat
                    System.out.println("AES encrypting OFF");// Verrà stampato un messaggio di avviso
                    aesOn = false;// E la variabile diventerà false, disattivando l'encrypting a riga 60
                    continue;
                }

                // disattiva caesar
                if (message.equalsIgnoreCase("caesar_off")) {// Quando l'utente scrive il comando enigma_on in chat
                    System.out.println("CAESAR encrypting OFF");// Verrà stampato un messaggio di avviso
                    ceasarOn = false;// E la variabile diventerà false, disattivando l'encrypting a riga 60
                    continue;
                }

                


                // Cripta il messaggio se l'utente ha scritto il comando enigma_on
                if (enigmaOn == true) {
                    try { // Prova a criptare il messaggio utilizzando ENIGMA
                        EnigmaSimulator enigmaSimulator = new EnigmaSimulator();
                        message = enigmaSimulator.cifraDecifra(message, true); // Cripta il messaggio utilizzando ENIGMA
                    } catch (Exception e) { // Gestisce eventuali eccezioni
                        System.err.println("Errore nella crittografia del messaggio: " + e.getMessage()); // Stampa un
                                                                                                          // messaggio
                                                                                                          // di errore
                        continue; // Salta all'iterazione successiva del loop
                    }
                }
                // Cripta se l'utente ha il comando eas_on
                if (aesOn == true) { // Se è attivo AES
                    try { // Prova a criptare il messaggio utilizzando AES
                        message = CryptoUtils.encrypt(message, propsKey); // Cripta il messaggio utilizzando AES
                    } catch (Exception e) { // Gestisce eventuali eccezioni
                        System.err.println("Errore nella crittografia del messaggio: " +
                                e.getMessage()); // Stampa un di errore
                        continue; // Salta all'iterazione successiva del loop
                    }
                }

                if (ceasarOn == true) { // Se è attivo CAESAR
                    try { // Prova a criptare il messaggio utilizzando CAESAR
                        //CifrarioDiCesare cifrarioDiCesare = new CifrarioDiCesare();
                        message = CifrarioDiCesare.trasforma(message, shift); // Cripta il messaggio utilizzando CAESAR
                    } catch (Exception e) { // Gestisce eventuali eccezioni
                        System.err.println("Errore nella crittografia del messaggio: " +
                                e.getMessage()); // Stampa un di errore
                        continue; // Salta all'iterazione successiva del loop
                    }
                }
                out.println(username + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Si è verificato un errore di rete: " + e.getMessage());
        }
    }

    private static String getPSK(String filename) { // Metodo privato per ottenere la chiave condivisa dal file di
                                                    // configurazione
        Properties prop = new Properties(); // Crea un nuovo oggetto Properties per gestire le proprietà
        try (FileInputStream fis = new FileInputStream(filename)) { // Apre un file di input stream per leggere le
                                                                    // proprietà
            prop.load(fis); // Carica le proprietà dal file
            return prop.getProperty("key"); // Restituisce il valore della chiave condivisa dal file di
                                            // configurazione
        } catch (IOException e) { // Gestisce eventuali eccezioni di IO
            e.printStackTrace(); // Stampa lo stack trace dell'eccezione
            return null; // Restituisce null in caso di errore
        }
    }
}