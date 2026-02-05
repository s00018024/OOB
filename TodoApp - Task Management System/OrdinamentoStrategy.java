import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Interfaccia per decidere come ordinare i task
public interface OrdinamentoStrategy {
    void ordina(List<TaskComponent> componenti);
}

// Opzione 1: Ordine alfabetico
class OrdineAlfabetico implements OrdinamentoStrategy {
    @Override
    public void ordina(List<TaskComponent> componenti) {
        // Uso un Comparator per confrontare i nomi
        componenti.sort(Comparator.comparing(TaskComponent::getName));
    }
}

// Opzione 2: Ordine inverso (giusto per avere una scelta)
class OrdineInverso implements OrdinamentoStrategy {
    @Override
    public void ordina(List<TaskComponent> componenti) {
        componenti.sort((c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
    }
}