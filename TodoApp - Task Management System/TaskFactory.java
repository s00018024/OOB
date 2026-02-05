public class TaskFactory {
    
    // Pulisco l'input da caratteri pericolosi
    private static String validaESanitizza(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto!");
        }
    
        //Rimuovo possibili tag HTML o script
        String pulito = nome.replaceAll("<[^>]*>", "");
    
        // Regex per l'accettazione di caratteri
        if (!pulito.matches("^[a-zA-Z0-9àèìòùÀÈÌÒÙ\\s.,!?\\-]*$")) {
            throw new IllegalArgumentException("Il nome contiene caratteri speciali non validi!");
        }
    
        // Imposto un limite per la lunghezza
        if (pulito.length() > 50) {
            pulito = pulito.substring(0, 47) + "...";
        }
    
        return pulito.trim();
    }

    public static TaskComponent createTask(String tipo, String nome) {
        // Sanitizzo il nome prima di creare l'oggetto
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
