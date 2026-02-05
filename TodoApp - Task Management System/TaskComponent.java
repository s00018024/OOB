import java.util.Iterator;

// Interfaccia comune per Task e Progetti
public interface TaskComponent {
    
    // Metodo per stampare a video con l'indentazione (gli spazi a sinistra)
    void display(String spazi);
    
    // Restituisce vero se il task è finito
    boolean isCompleted();
    
    // Restituisce il nome del componente
    String getName();
    
    // Serve per scorrere i vari elementi (per il pattern Iterator)
    Iterator<TaskComponent> createIterator(); 
}