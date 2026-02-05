import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApp {
    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);
        StorageManager storage = StorageManager.getInstance();
        
        List<Project> listaProgetti = new ArrayList<>();

        // 1. Caricamento dati
        try {
            listaProgetti = storage.loadTasks();
            if (listaProgetti.isEmpty()) {
                listaProgetti.add(new Project("Generali"));
            }
            System.out.println("✅ Dati caricati correttamente.");
        } catch (TaskStorageException e) {
            System.out.println("⚠️ Attenzione: " + e.getMessage());
            listaProgetti.add(new Project("Generali"));
        } catch (Exception e) {
            System.out.println("❌ Errore critico nel caricamento. Reset dell'applicazione.");
            listaProgetti.add(new Project("Generali"));
        }

        Project progettoCorrente = listaProgetti.get(0);

        boolean inEsecuzione = true;
        while (inEsecuzione) {
            System.out.println("\n--------------------------------------------");
            System.out.println(" PROGETTO ATTIVO: [" + progettoCorrente.getName().toUpperCase() + "]");
            System.out.println("--------------------------------------------");
            stampaMenu();
            
            String sceltaInput = tastiera.nextLine().trim();

            if (sceltaInput.isEmpty()) continue;

            switch (sceltaInput.toLowerCase()) {
                case "1": // Aggiungi Task
                    System.out.print("Nome del Task: ");
                    String nomeT = tastiera.nextLine().trim();
                    try {
                        progettoCorrente.add(TaskFactory.createTask("SIMPLE", nomeT));
                        System.out.println("✅ Task aggiunto.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ Errore: " + e.getMessage());
                    }
                    break;

                case "2": // Nuovo Progetto
                    System.out.print("Nome del nuovo Progetto: ");
                    String nomeP = tastiera.nextLine().trim();
                    try {
                        Project nuovoP = (Project) TaskFactory.createTask("PROJECT", nomeP);
                        listaProgetti.add(nuovoP);
                        progettoCorrente = nuovoP;
                        System.out.println("✅ Progetto creato e attivo.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ Errore: " + e.getMessage());
                    }
                    break;

                case "3": // Mostra Tutto
                    System.out.println("\n--- PANORAMICA COMPLETA ---");
                    for (Project p : listaProgetti) {
                        p.display("");
                    }
                    break;

                case "a": // Strategia Alfabetica
                    progettoCorrente.setStrategia(new OrdineAlfabetico());
                    storage.saveTasks(listaProgetti); // Persistenza immediata
                    System.out.println("🔤 Ordinamento ALFABETICO attivato e salvato.");
                    break;

                case "i": // Strategia Inserimento 
                    progettoCorrente.setStrategia(null);
                    storage.saveTasks(listaProgetti); 
                    System.out.println("🕙 Ordinamento INSERIMENTO attivato e salvato.");
                    break;

                case "z": // Strategia Alfabetica Reverse
                    progettoCorrente.setStrategia(new OrdineInverso());
                    storage.saveTasks(listaProgetti); 
                    System.out.println("Ordine Alfabetico inverso impostato.");
                    break;

                case "4": // Salvataggio manuale
                    storage.saveTasks(listaProgetti);
                    System.out.println("💾 Salvataggio completato.");
                    break;

                case "6": // Cambio Progetto
                    if (listaProgetti.size() <= 1) {
                        System.out.println("ℹ️ Esiste solo un progetto.");
                        break;
                    }
                    System.out.println("\n--- SELEZIONE PROGETTO ---");
                    for (int i = 0; i < listaProgetti.size(); i++) {
                        System.out.println(i + ". " + listaProgetti.get(i).getName());
                    }
                    System.out.print("Scegli l'indice: ");
                    try {
                        int idx = Integer.parseInt(tastiera.nextLine());
                        if (idx >= 0 && idx < listaProgetti.size()) {
                            progettoCorrente = listaProgetti.get(idx);
                            System.out.println(">>> Attivo: " + progettoCorrente.getName());
                        } else {
                            System.out.println("❌ Indice non valido.");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Inserisci un numero.");
                    }
                    break;

                case "7": // Reset
                    System.out.print("Svuotare '" + progettoCorrente.getName() + "'? (s/n): ");
                    if (tastiera.nextLine().trim().equalsIgnoreCase("s")) {
                        progettoCorrente.svuotaProgetto();
                        storage.saveTasks(listaProgetti);
                        System.out.println("🗑️ Progetto svuotato.");
                    }
                    break;

                case "8": // Segna come fatto
                    if (isSistemaVuoto(listaProgetti)) {
                        System.out.println("⚠️ Non c'è nessun task nel sistema. Aggiungine uno prima!");
                        break;
                    }
                    System.out.print("Nome del task completato: ");
                    String nomeCercato = tastiera.nextLine().trim();
                    if (!progettoCorrente.completaTask(nomeCercato)) {
                        String trovatoIn = cercaTaskInAltriProgetti(listaProgetti, nomeCercato);
                        if (trovatoIn != null) {
                            System.out.println("⚠️ Task trovato in: [" + trovatoIn + "]. Cambia progetto per completarlo.");
                        } else {
                            System.out.println("❌ Task non trovato.");
                        }
                    }
                    break;

                case "9": // Rimuovi completati 
                    if (isSistemaVuoto(listaProgetti)) {
                        System.out.println("⚠️ Non c'è nessun task nel sistema. Niente da rimuovere!");
                        break;
                    }

                    if (!haTaskCompletati(progettoCorrente)) {
                        System.out.println("ℹ️ Non ci sono task completati nel progetto attivo. Niente da pulire.");
                        break;
                    }

                    progettoCorrente.rimuoviCompletati();
                    storage.saveTasks(listaProgetti);
                    System.out.println("🧹 Pulizia effettuata: i task completati sono stati rimossi.");
                    break;

                case "5": // Esci
                    storage.saveTasks(listaProgetti);
                    inEsecuzione = false;
                    break;

                default:
                    System.out.println("⚠️ Opzione non valida. Seleziona un numero tra quelli proposti!");
            }
        }
        System.out.println("Arrivederci!");
        tastiera.close();
    }

    // --- Altri metodi (aggiunti qua per rendere il codice più pulito) ---

    private static boolean isSistemaVuoto(List<Project> lista) {
        for (Project p : lista) {
            if (p.iterator().hasNext()) return false;
        }
        return true;
    }

    private static boolean haTaskCompletati(Project p) {
        for (TaskComponent t : p) {
            if (t.isCompleted()) return true;
        }
        return false;
    }

    private static String cercaTaskInAltriProgetti(List<Project> lista, String nome) {
        for (Project p : lista) {
            for (TaskComponent t : p) {
                if (t.getName().equalsIgnoreCase(nome)) return p.getName();
            }
        }
        return null;
    }

    private static void stampaMenu() {
        System.out.println("1. Aggiungi Task      |  2. Nuovo Progetto");
        System.out.println("3. Mostra Tutto       |  4. Salva");
        System.out.println("6. Cambia Progetto    |  7. Reset Progetto");
        System.out.println("8. Segna come fatto   |  9. Rimuovi completati");
        System.out.println("A. Ordine Alfabetico  |  I. Ordine Inserimento |  Z. Ordine Inverso");
        System.out.println("5. Esci");
        System.out.print("Scelta: ");
    }
}
