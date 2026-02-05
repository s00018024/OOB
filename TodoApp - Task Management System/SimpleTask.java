import java.util.Iterator;
import java.util.Collections;

// Questa è la "foglia" del pattern Composite
public class SimpleTask implements TaskComponent {
    private String nome;
    private boolean completato;

    // Quando crei un task, nasce sempre come "da fare" (false)
    public SimpleTask(String nome) {
        this.nome = nome;
        this.completato = false;
    }

    // NUOVO: Permette di cambiare lo stato del task (es. da [ ] a [X])
    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    @Override
    public void display(String spazi) {
        // Uso un operatore ternario per decidere se mettere la X
        String stato = completato ? "[X] " : "[ ] ";
        System.out.println(spazi + stato + nome);
    }

    @Override
    public boolean isCompleted() { 
        return completato; 
    }

    @Override
    public String getName() { 
        return nome; 
    }

    @Override
    public Iterator<TaskComponent> createIterator() {
        // Essendo un task singolo non ha figli, quindi restituisco un iterator vuoto
        return Collections.emptyIterator();
    }
}