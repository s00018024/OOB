// Eccezione personalizzata per gli errori nel salvataggio dei task
public class TaskStorageException extends Exception {

    // Costruttore che prende il messaggio e la causa dell'errore
    public TaskStorageException(String messaggio, Throwable causa) {
        // Passo tutto alla classe Exception di Java
        super(messaggio, causa);
    }
}