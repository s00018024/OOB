public class TaskFactory {
    
    // Metodo per pulire l'input da caratteri pericolosi
    private static String validaESanitizza(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto!");
        }
    
        // 1. Rimuoviamo tag HTML o script
        String pulito = nome.replaceAll("<[^>]*>", "");
    
        // 2. AGGIORNAMENTO REGEX: 
        // Abbiamo aggiunto àèìòù e le versioni maiuscole ÀÈÌÒÙ
        if (!pulito.matches("^[a-zA-Z0-9àèìòùÀÈÌÒÙ\\s.,!?\\-]*$")) {
            throw new IllegalArgumentException("Il nome contiene caratteri speciali non validi!");
        }
    
        // 3. Limite lunghezza
        if (pulito.length() > 50) {
            pulito = pulito.substring(0, 47) + "...";
        }
    
        return pulito.trim();
    }

    public static TaskComponent createTask(String tipo, String nome) {
        // Sanitizziamo il nome prima di creare l'oggetto
        String nomePulito = validaESanitizza(nome);

        if (tipo == null) {
            throw new IllegalArgumentException("Tipo non specificato");
        }

        if (tipo.equalsIgnoreCase("SIMPLE")) {
            return new SimpleTask(nomePulito);
        } else if (tipo.equalsIgnoreCase("PROJECT")) {
            return new Project(nomePulito);
        }
        
        throw new IllegalArgumentException("Tipo non valido: " + tipo);
    }
}