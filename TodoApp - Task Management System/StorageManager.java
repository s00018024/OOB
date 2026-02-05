import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce il salvataggio e il caricamento dei dati.
 * Implementa il pattern Singleton per garantire un unico punto di accesso al file.
 */
public class StorageManager {
    private static StorageManager instance;
    private static final String FILE_NAME = "tasks.txt";
    
    // Logger configurato per registrare errori internamente senza esporli al terminale utente
    private static final Logger LOGGER = Logger.getLogger(StorageManager.class.getName());

    private StorageManager() {
        // Questo comando dice al Logger: 
        // "Mostra i messaggi solo se il programma sta per esplodere (SEVERE)"
        // Nascondendo i messaggi di "INFO" come quelli del salvataggio.
        LOGGER.setLevel(Level.SEVERE); 
    }

    public static StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    /**
     * Sanitizzazione robusta (Punto 3): impedisce al carattere '|' di corrompere il file
     * e rimuove ritorni a capo che spezzerebbero la logica di lettura per riga.
     */
    private String sanitize(String testo) {
        if (testo == null) return "";
        return testo.replace("|", " ")
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .trim();
    }

    /**
     * Salva i progetti e i relativi task sul file di testo.
     */
    public void saveTasks(List<Project> progetti) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Project p : progetti) {
                writer.println("PROGETTO|" + sanitize(p.getName()));
                for (TaskComponent t : p) {
                    String stato = t.isCompleted() ? "X" : "O";
                    writer.println("TASK|" + stato + "|" + sanitize(t.getName()));
                }
            }
            LOGGER.log(Level.INFO, "Salvataggio completato con successo.");
        } catch (IOException e) {
            // SICUREZZA (Punto 2): Logghiamo un messaggio fisso, non passiamo l'oggetto 'e'
            LOGGER.log(Level.SEVERE, "Errore IO durante il salvataggio sul disco.");
            
            // Messaggio amichevole per l'utente, senza dettagli tecnici
            System.out.println("⚠️ Errore: Impossibile salvare i dati. Controlla i permessi della cartella.");
        }
    }

    /**
     * Carica i dati dal file.
     * Implementa l'Exception Masking (Punto 4) per evitare leak di informazioni di sistema.
     */
    public List<Project> loadTasks() throws TaskStorageException {
        List<Project> listaCaricata = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return listaCaricata;
        }

        try (Scanner reader = new Scanner(file)) {
            Project progettoCorrente = null;

            while (reader.hasNextLine()) {
                String riga = reader.nextLine();
                String[] pezzi = riga.split("\\|");

                // Validazione lunghezza pezzi per evitare ArrayIndexOutOfBounds (Crash protection)
                if (pezzi.length >= 2 && pezzi[0].equals("PROGETTO")) {
                    progettoCorrente = new Project(pezzi[1]);
                    listaCaricata.add(progettoCorrente);
                } 
                else if (pezzi.length >= 3 && pezzi[0].equals("TASK") && progettoCorrente != null) {
                    boolean completato = pezzi[1].equals("X");
                    String nomeTask = pezzi[2];
                    
                    TaskComponent task = TaskFactory.createTask("SIMPLE", nomeTask);
                    
                    if (completato && task instanceof SimpleTask) {
                        ((SimpleTask) task).setCompletato(true);
                    }
                    
                    progettoCorrente.add(task);
                }
            }
        } catch (Exception e) {
            // LOGGING PROFESSIONALE (Punto 4):
            // Scriviamo l'errore tecnico nel log interno ma NON passiamo 'e'
            // al costruttore per non portarci dietro lo stack trace originale.
            LOGGER.log(Level.SEVERE, "Tentativo di caricamento file fallito: formato non valido o permessi negati.");
            
            // Lanciamo l'eccezione personalizzata con causa 'null' per troncare il leak
            throw new TaskStorageException("Il file di salvataggio è corrotto o illeggibile.", null);
        }
        return listaCaricata;
    }
}