import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TaskTest {
    // Variabile globale per usarla in tutti i test
    private Project root;

    @BeforeEach
    void setUp() {
        // Creo un nuovo progetto vuoto prima di iniziare ogni test
        root = new Project("Test Root");
    }

    @Test
    void testFactoryCreation() {
        // Provo a creare un task usando la factory
        TaskComponent task = TaskFactory.createTask("SIMPLE", "Studiare Java");
        
        // Controllo se il task esiste davvero e se ha il nome giusto
        assertNotNull(task);
        assertEquals("Studiare Java", task.getName());
        
        // Controllo che sia effettivamente un SimpleTask
        assertTrue(task instanceof SimpleTask);
    }

    @Test
    void testCompositeAdd() {
        // Creo un progetto e lo aggiungo a quello principale
        TaskComponent subProject = TaskFactory.createTask("PROJECT", "SottoProgetto");
        root.add(subProject);
        
        // Controllo se dentro root c'è qualcosa usando l'iterator
        assertTrue(root.iterator().hasNext());
    }

    @Test
    void testSanitization() {
        // Verifica di sicurezza 
        assertThrows(IllegalArgumentException.class, () -> {
            TaskFactory.createTask("SIMPLE", "Task|Pericoloso");
        });
    }

    @Test
    void testEmptyNameException() {
        // Verifico che non sia possibile creare task con nomi vuoti
        assertThrows(IllegalArgumentException.class, () -> {
            TaskFactory.createTask("SIMPLE", "");
        });
    }
}
