import java.util.Iterator;

// Interfaccia comune per Task e Progetti
public interface TaskComponent {
    
    void display(String spazi);
    
    // Restituisce vero se il task è finito
    boolean isCompleted();
    
    String getName();
    
    // Serve per scorrere i vari elementi (per il pattern Iterator)
    Iterator<TaskComponent> createIterator(); 
}
