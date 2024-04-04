
import java.util.Scanner;

public class CifrarioDiCesare {
    // inserimento di lettere e numeri per per shiftare le lettere
    private static final String DIZIONARIO = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static void main(String[] args) {
        System.out.println(
                "inserisci una parola, poi il numero in cui vuoi che la lettera shifti e poi modalità (criptare o decriptare)");

        Scanner sc = new Scanner(System.in);

        // parola che vuole criptare o decriptare
        System.out.println("inserisci la parola");
        String messaggio = sc.nextLine();

        // quante posizioni vuole shiftare le lettere che compongono la parola
        System.out.println("di quante posizioni vuoi far shiftare le lettere?");
        int shift = sc.nextInt();

        // scelta dell'utente
        System.out.println("Desideri criptare il messaggio o decriptarlo?");
        String modalità = sc.next();

        if (modalità.equalsIgnoreCase("criptare")) {
            String risultato = cripta(messaggio, shift);
            // stampo la parola criptata o decriptata
            System.out.println(risultato);
        } else if (modalità.equalsIgnoreCase("decriptare")) {
            String risultato = decripta(messaggio, shift);
            // stampo la parola criptata o decriptata
            System.out.println(risultato);
        } else {
            System.out.println("modalità non valida. Scegli se vuoi criptare o decriptare");
        }

        sc.close();

    }

    // metodo per criptare, che prende come parametri la parola inserita dall'utente
    // ed il numero di shift
    public static String cripta(String testo, int shift) {
        return trasforma(testo, shift);
    }

    // metodo per decriptare, che prende come parametri la parola inserita
    // dall'utente ed il numero di shift
    public static String decripta(String testo, int shift) {
        return trasforma(testo, -shift);
    }

    // metodo che ha il compito di criptare o decriptare la parola inserita
    // dall'utente
    public static String trasforma(String testo, int shift) {
        StringBuilder risultato = new StringBuilder();

        // cicla tutti i caratteri che compongono la parola
        for (char carattere : testo.toCharArray()) {
            // se il carattere non viene trovato tra le posizioni di dizionario
            if (DIZIONARIO.indexOf(carattere) != -1) {
                // trova la posizione del carattere nella variabile DIZIONARIO
                int posizioneOriginale = DIZIONARIO.indexOf(carattere);
                // la nuova posizione risulterà dalla somma della posizione originaria del
                // carattere più lo shift preimpostato dall'utente,
                // il resto con dizionario.length
                // serve a non causare una index out of bounds exception
                int nuovaPosizione = (posizioneOriginale + shift) % DIZIONARIO.length();
                risultato.append(DIZIONARIO.charAt(nuovaPosizione));
            } else {
                risultato.append(carattere);
            }
        }
        return risultato.toString();
    }
}

