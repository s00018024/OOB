import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Project implements TaskComponent, Iterable<TaskComponent> {
    private String nomeProgetto;
    private List<TaskComponent> componenti = new ArrayList<>();
    private OrdinamentoStrategy strategia;

    public Project(String nome) {
        this.nomeProgetto = nome;
    }

    public void setStrategia(OrdinamentoStrategy strategia) {
        this.strategia = strategia;
    }

    public void add(TaskComponent componente) {
        componenti.add(componente);
    }

    public void svuotaProgetto() {
        this.componenti.clear();
        this.strategia = null; 
    }

    // Eseguo verifiche quando si vuole segnare un task come "fatto"
    public boolean completaTask(String nome) {
        for (TaskComponent c : componenti) {
            // Verifico se è un task semplice e il nome coincide
            if (c instanceof SimpleTask && c.getName().equalsIgnoreCase(nome)) {
                ((SimpleTask) c).setCompletato(true);
                System.out.println("Task '" + nome + "' segnato come completato!");
                
                // Controllo e conferma
                if (this.isCompleted() && !componenti.isEmpty()) {
                    System.out.println("\n🌟 Complimenti! Tutti i task del progetto \"" + nomeProgetto + "\" sono stati completati; well done!");
                }
                return true; 
            }
            // Se è un sotto-progetto, cerca ricorsivamente
            else if (c instanceof Project) {
                if (((Project) c).completaTask(nome)) return true;
            }
        }
        return false; 
    }

    // Rimuove i task completati 
    public void rimuoviCompletati() {
        componenti.removeIf(TaskComponent::isCompleted);
    }

    @Override
    public void display(String spazi) {
        if (strategia != null) {
            strategia.ordina(componenti);
        }

        System.out.println(spazi + "+ Progetto: " + nomeProgetto);
        
        for (TaskComponent c : componenti) {
            c.display(spazi + "  ");
        }
    }

    @Override
    public boolean isCompleted() {
        if (componenti.isEmpty()) return false;
        return componenti.stream().allMatch(TaskComponent::isCompleted);
    }

    @Override
    public String getName() {
        return nomeProgetto;
    }

    @Override
    public Iterator<TaskComponent> createIterator() {
        return componenti.iterator();
    }

    @Override
    public Iterator<TaskComponent> iterator() {
        return componenti.iterator();
    }
}
